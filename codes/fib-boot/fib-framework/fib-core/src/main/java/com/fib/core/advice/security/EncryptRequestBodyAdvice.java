package com.fib.core.advice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import java.lang.reflect.Type;
import com.fib.core.config.SecretKeyConfig;
import com.fib.core.annotation.security.*;

/**
 * 通用来请求统一加密处理
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-10-19
 */
@ControllerAdvice
public class EncryptRequestBodyAdvice implements RequestBodyAdvice {
	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptRequestBodyAdvice.class);

	private boolean encryptFlag;

	@Autowired
	private SecretKeyConfig secretKeyConfig;

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		if (methodParameter.getMethod().isAnnotationPresent(Decrypt.class) && secretKeyConfig.isOpen()) {
			encryptFlag = true;
		}
		return encryptFlag;
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		if (encryptFlag) {
			try {
				return new DecryptHttpInputMessage(inputMessage, secretKeyConfig.getCharset(),
						secretKeyConfig.isShowLog());
			} catch (Exception e) {
				LOGGER.error("Decryption failed", e);
			}
		}
		return inputMessage;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}
}
