package com.fib.autoconfigure.crypto.advice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fib.autoconfigure.crypto.annotation.AlgorithmType.AsymmetricAlgorithm;
import com.fib.autoconfigure.crypto.annotation.AlgorithmType.DigestAlgorithm;
import com.fib.autoconfigure.crypto.annotation.AlgorithmType.SymmetricAlgorithm;
import com.fib.autoconfigure.crypto.annotation.Encrypt;
import com.fib.autoconfigure.crypto.dto.Request;
import com.fib.autoconfigure.crypto.dto.RequestHeader;
import com.fib.autoconfigure.crypto.service.ISecurityService;
import com.fib.core.exception.BusinessException;
import com.fib.core.util.ConstantUtil;
import com.fib.core.util.StatusCode;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.spring.SpringUtil;

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

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		Assert.notNull(returnType, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
		Method method = returnType.getMethod();
		if (null != method && method.isAnnotationPresent(Encrypt.class)) {
			encryptFlag = true;
		}
		return encryptFlag;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		Assert.notNull(returnType, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
		if (!encryptFlag) {
			return body;
		}

		Method method = returnType.getMethod();
		if (null == method) {
			return body;
		}
		Encrypt encrypt = method.getAnnotation(Encrypt.class);
		if (null == encrypt) {
			return body;
		}

		AsymmetricAlgorithm asymmetricAlgorithm = encrypt.asymmetricAlgorithm();
		SymmetricAlgorithm symmetricAlgorithm = encrypt.symmetricAlgorithm();
		DigestAlgorithm digestAlgorithm = encrypt.digestAlgorithm();
		logger.info("symmetricAlgorithm={},asymmetricAlgorithm={},digestAlgorithm={}", symmetricAlgorithm, asymmetricAlgorithm, digestAlgorithm);

		try {
			ISecurityService securityService = SpringUtil.getBean("securityService");
			Request rea = securityService.buildReq(body,digestAlgorithm);
			RequestHeader reqHeader = rea.getRequestHeader();

			String bodyHash = securityService.getEncrypBody(body, reqHeader);

			String securityKey = securityService.getSecurityKey(reqHeader);
//			String content = JSONUtil.toJsonStr(body);
//
//			/* 1.对原始数据取摘要-摘要加密算法SM3 */
//			String contentHash = SmUtil.sm3(content);
//			logger.info("content={},contentHash={}", content, contentHash);
//
//			/* 2.用自己私钥对摘要签名-SM2 */
//			ISecurityService securityService = SpringContextUtils.getBean("securityService");
//			String ownPrivateKey = securityService.queryPrivateKey(ConstantUtil.UPP_SYSTEM_CODE);
//			String otherPublicKey = securityService.queryPublicKey(ConstantUtil.OTHER_SYSTEM_CODE);
//			if (StrUtil.isEmptyIfStr(ownPrivateKey) || StrUtil.isEmptyIfStr(otherPublicKey)) {
//				//
//			}
//
//			byte[] signedContext = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2")
//					.sign(CharSequenceUtil.bytes(contentHash, CharsetUtil.CHARSET_UTF_8), SecureUtil.decode(ownPrivateKey));
//			logger.info("authentication={}", HexUtil.encodeHexStr(signedContext));
//
//			/* 3.用对方公钥对对称密钥加密-SM2 */
//			String securityKeySource = CommUtils.getRandom(16);
//			logger.info("securityKeySource={}", securityKeySource);
//
//			byte[] cipherTxt = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2")
//					.encrypt(CharSequenceUtil.bytes(securityKeySource, CharsetUtil.CHARSET_UTF_8), SecureUtil.decode(otherPublicKey));
//			logger.info("securityKey={}", HexUtil.encodeHexStr(cipherTxt));
//
//			/* 4.用对称加密算法对报文内容加密-SM4 */
//			byte[] encryptedBodyContent = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM4")
//					.encrypt(CharSequenceUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), securityKeySource.getBytes(CharsetUtil.CHARSET_UTF_8));
//
			// String bodyHash = "ok";// HexUtil.encodeHexStr(encryptedBodyContent);
//			logger.info("encryptedBodyContent={}", bodyHash);
//			String dateTime = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN);
//			logger.info("TimeStamp={}", dateTime);

			response.getHeaders().add("Authentication", reqHeader.getAuthentication());
			response.getHeaders().add("SecurityKey", securityKey);
			response.getHeaders().add("TimeStamp", String.valueOf(reqHeader.getTimestamp()));
			response.getHeaders().add("SystemCode", ConstantUtil.UPP_SYSTEM_CODE);
			response.getHeaders().add("Nonce", reqHeader.getNonce());
			Map<String, Object> map = new HashMap<>();
			map.put("data", bodyHash);
			return map;
		} catch (Exception e) {
			logger.error("Encrypted data exception", e);
		}
		return body;
	}
}
