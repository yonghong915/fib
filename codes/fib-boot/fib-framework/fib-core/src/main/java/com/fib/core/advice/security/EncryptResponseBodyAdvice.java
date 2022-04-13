package com.fib.core.advice.security;

import java.lang.reflect.Method;
import java.util.Date;

import org.apache.dubbo.rpc.model.ScopeModelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fib.commons.exception.BusinessException;
import com.fib.commons.security.SecurityEncryptor;
import com.fib.commons.util.CommUtils;
import com.fib.core.annotation.security.Encrypt;
import com.fib.core.base.service.ISecurityService;
import com.fib.core.config.SecretKeyConfig;
import com.fib.core.util.ConstantUtil;
import com.fib.core.util.SpringContextUtils;
import com.fib.core.util.StatusCode;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.json.JSONUtil;

/**
 * 通用返回统一解密处理
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-10-19
 */
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private boolean encryptFlag = Boolean.FALSE.booleanValue();

	@Autowired
	private SecretKeyConfig secretKeyConfig;

	private static ThreadLocal<Boolean> encryptLocal = new ThreadLocal<>();

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		Assert.notNull(returnType, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
		Method method = returnType.getMethod();
		if (null != method && method.isAnnotationPresent(Encrypt.class) && secretKeyConfig.isOpen()) {
			encryptFlag = true;
		}
		return encryptFlag;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		Assert.notNull(returnType, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
		Boolean status = encryptLocal.get();
		if (null != status && !status) {
			encryptLocal.remove();
			return body;
		}
		if (!encryptFlag) {
			return body;
		}

		Method method = returnType.getMethod();
		if (null == method) {
			return body;
		}
		Encrypt en = method.getAnnotation(Encrypt.class);
		if (null == en) {
			return body;
		}

		String symmetricAlgorithm = en.symmetricAlgorithm();//
		String asymmetricAlgorithm = en.asymmetricAlgorithm();
		logger.info("symmetricAlgorithm={},asymmetricAlgorithm={}", symmetricAlgorithm, asymmetricAlgorithm);

		try {
			String content = JSONUtil.toJsonStr(body);

			/* 1.对原始数据取摘要-摘要加密算法SM3 */
			String contentHash = SmUtil.sm3(content);
			logger.info("content={},contentHash={}", content, contentHash);

			/* 2.用自己私钥对摘要签名-SM2 */
			ISecurityService securityService = SpringContextUtils.getBean("securityService");
			String ownPrivateKey = securityService.queryPrivateKey(ConstantUtil.UPP_SYSTEM_CODE);
			String otherPublicKey = securityService.queryPublicKey(ConstantUtil.OTHER_SYSTEM_CODE);
			if (StrUtil.isEmptyIfStr(ownPrivateKey) || StrUtil.isEmptyIfStr(otherPublicKey)) {
				//
			}

			byte[] signedContext = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2")
					.sign(CharSequenceUtil.bytes(contentHash, CharsetUtil.CHARSET_UTF_8),
							SecureUtil.decode(ownPrivateKey));
			logger.info("authentication={}", HexUtil.encodeHexStr(signedContext));

			/* 3.用对方公钥对非对称密钥加密-SM2 */
			String securityKeySource = CommUtils.getRandom(16);
			logger.info("securityKeySource={}", securityKeySource);

			byte[] cipherTxt = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2")
					.encrypt(CharSequenceUtil.bytes(securityKeySource, CharsetUtil.CHARSET_UTF_8),
							SecureUtil.decode(otherPublicKey));
			logger.info("securityKey={}", HexUtil.encodeHexStr(cipherTxt));

			/* 4.用对称加密算法对报文内容加密-SM4 */
			byte[] encryptedBodyContent = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null)
					.getExtension("SM4").encrypt(CharSequenceUtil.bytes(content, CharsetUtil.CHARSET_UTF_8),
							securityKeySource.getBytes(CharsetUtil.CHARSET_UTF_8));

			String bodyHash = HexUtil.encodeHexStr(encryptedBodyContent);
			logger.info("encryptedBodyContent={}", bodyHash);
			String dateTime = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN);
			logger.info("TimeStamp={}", dateTime);

			response.getHeaders().add("authentication", HexUtil.encodeHexStr(signedContext));
			response.getHeaders().add("securityKey", HexUtil.encodeHexStr(cipherTxt));
			response.getHeaders().add("timeStamp", dateTime);
			response.getHeaders().add("systemCode", ConstantUtil.UPP_SYSTEM_CODE);
			return bodyHash;
		} catch (Exception e) {
			logger.error("Encrypted data exception", e);
		}
		return body;
	}
}
