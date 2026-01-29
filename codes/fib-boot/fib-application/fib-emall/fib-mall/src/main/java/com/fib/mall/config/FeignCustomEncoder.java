package com.fib.mall.config;

import java.lang.reflect.Type;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fib.autoconfigure.openapi.RequestBuilder;
import com.fib.autoconfigure.openapi.message.ApiRequest;

import feign.RequestTemplate;
import feign.codec.Encoder;
import feign.jackson.JacksonEncoder;

/**
 * 自定义 Feign 编码器：序列化前封装请求报文
 */
@Component
public class FeignCustomEncoder implements Encoder {

	// 复用Feign 默认的 Jackson 编码器
	private final JacksonEncoder delegate;

	private RequestBuilder requestBuilder;

	public FeignCustomEncoder(ObjectMapper objectMapper, RequestBuilder requestBuilder) {
		this.delegate = new JacksonEncoder(objectMapper);
		this.requestBuilder = requestBuilder;
	}

	@Override
	public void encode(Object object, Type bodyType, RequestTemplate template) {

		/* 封装报文数据 */
		ApiRequest<Object> apiRequest = requestBuilder.buildMessageRequest(object);

		// 用默认编码器序列化封装后的对象
		delegate.encode(apiRequest, ApiRequest.class, template);
	}
}