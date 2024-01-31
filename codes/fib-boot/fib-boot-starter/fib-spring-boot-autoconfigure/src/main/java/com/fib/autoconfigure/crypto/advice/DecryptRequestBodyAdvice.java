package com.fib.autoconfigure.crypto.advice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import com.fib.autoconfigure.crypto.annotation.Decrypt;
import com.fib.autoconfigure.crypto.config.CryptoProperties;
import com.fib.core.exception.BusinessException;
import com.fib.core.util.StatusCode;

/**
 * 通用来请求统一加密处理
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-10-19
 */
@ControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {
	private static final Logger LOGGER = LoggerFactory.getLogger(DecryptRequestBodyAdvice.class);

	private boolean encryptFlag;

	@Autowired
	private CryptoProperties cryptoProperties;

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		Method method = methodParameter.getMethod();
		if (null != method && method.isAnnotationPresent(Decrypt.class)) {
			encryptFlag = true;
		}
		return encryptFlag;
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		if (encryptFlag) {
			try {
				return new DecryptHttpInputMessage(inputMessage, cryptoProperties.getCharset());
			} catch (Exception e) {
				LOGGER.error("Decryption failed", e);
				throw new BusinessException(StatusCode.DECRYPT_FAIL, e);
			}
		}
		return inputMessage;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	/**
	 * 
	 * @author fangyh
	 * @version 1.0.0
	 * @date 2021-10-19
	 */
	private class DecryptHttpInputMessage implements HttpInputMessage {
		private HttpHeaders headers;
		private InputStream body;

		public DecryptHttpInputMessage(HttpInputMessage inputMessage, String charset) throws IOException {
			this.headers = inputMessage.getHeaders();
			this.body = inputMessage.getBody();
//			if (inputMessage.getBody().available() <= 0) {
//				return;
//			}
//			if (StrUtil.isEmptyIfStr(charset)) {
//				charset = CharsetUtil.CHARSET_UTF_8.name();
//			}
			//
//			/* 数据密文 */
//			String content = new BufferedReader(new InputStreamReader(inputMessage.getBody())).lines()
//					.collect(Collectors.joining(System.lineSeparator()));
//			if (StrUtil.isEmptyIfStr(content)) {
//				return;
//			}
			//
//			/* 原文数字摘要签名 */
//			String authentication = headers.getFirst("Authentication");
			//
//			/* 对称密钥的密文 */
//			String securityKey = headers.getFirst("SecurityKey");
			//
//			/* 系统编码 */
//			String systemCode = headers.getFirst("SystemCode");
			//
//			/* 时间戳 */
//			long timestamp = Long.parseLong(headers.getFirst("Timestamp"));
			//
//			/* 随机数 */
//			String nonce = headers.getFirst("Nonce");
//			if (StrUtil.isEmptyIfStr(authentication) || StrUtil.isEmptyIfStr(securityKey) || StrUtil.isEmptyIfStr(systemCode)
//					|| StrUtil.isEmptyIfStr(timestamp) || StrUtil.isEmptyIfStr(nonce)) {
//				//
//				throw new RuntimeException("empty.");
//			}
//			RequestHeader reqHeader = new RequestHeader();
//			reqHeader.setAuthentication(authentication);
//			reqHeader.setNonce(nonce);
//			reqHeader.setTimestamp(timestamp);
//			reqHeader.setSecurityKey(securityKey);
//			reqHeader.setSecurityKey(systemCode);
			//
//			com.fib.autoconfigure.crypto.RequestHeader requestHeader = SpringUtil.getBean("requestHeader");
//			
//			ISecurityService securityService = SpringUtil.getBean("securityService");
//			String ownPrivateKey = securityService.queryPrivateKey(ConstantUtil.UPP_SYSTEM_CODE);
//			if (StrUtil.isEmptyIfStr(ownPrivateKey)) {
//				//
//			}
			//
//			String otherpublicKey = securityService.queryPublicKey(systemCode);
//			if (StrUtil.isEmptyIfStr(otherpublicKey)) {
//				//
//			}
			//
//			/* 1.用自己私钥对非对称密钥解密-SM2 */
//			byte[] encryptedKey = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2")
//					.decrypt(HexUtil.decodeHex(securityKey), SecureUtil.decode(ownPrivateKey));
//			log.info("securityKey={}", StrUtil.str(encryptedKey, CharsetUtil.CHARSET_UTF_8));
			//
//			/* 2.用对称加密算法对报文内容解密-SM4 */
//			if (!JSONUtil.isTypeJSON(content)) {
//				//
//			}
			//
//			JSONObject jsonCxt = JSONUtil.parseObj(content);
//			JSONArray jsonBody = jsonCxt.getJSONArray("body");
//			String encryptedBody = (String) jsonBody.get(0);
//			log.info("encryptedBody={}", encryptedBody);
//			byte[] msgBody = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM4")
//					.decrypt(HexUtil.decodeHex(encryptedBody), encryptedKey);
			//
//			String bodySource = StrUtil.str(msgBody, CharsetUtil.CHARSET_UTF_8);
//			log.info("bodySource={}", bodySource);
			//
//			JSONObject messageBody = JSONUtil.parseObj(bodySource);
//			String rspCode = messageBody.getStr("rspCode");
//			if (!CharSequenceUtil.equals("000000", rspCode)) {
//				//
//			}
			//
//			Object data = messageBody.getObj("rspObj");
			//
//			/* 3.对原始数据取摘要-摘要加密算法SM3 */
//			String bodyHash = SmUtil.sm3(bodySource);
//			log.info("bodyHash={}", bodyHash);
			//
//			/* 4.用对方公钥对摘要验签-SM2 */
//			boolean verifyFlag = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2").verify(
//					CharSequenceUtil.bytes(bodyHash, CharsetUtil.CHARSET_UTF_8), HexUtil.decodeHex(authentication), SecureUtil.decode(otherpublicKey));
//			log.info("verifyFlag={}", verifyFlag);
//			if (!verifyFlag) {
//				//
//			}
			// this.body = new ByteArrayInputStream(JSONUtil.toJsonStr(data).getBytes());
		}

		@Override
		public InputStream getBody() {
			return body;
		}

		@Override
		public HttpHeaders getHeaders() {
			return headers;
		}
	}
}