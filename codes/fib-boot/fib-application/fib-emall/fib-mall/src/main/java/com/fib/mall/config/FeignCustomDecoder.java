package com.fib.mall.config;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fib.autoconfigure.openapi.ResponseBuilder;
import com.fib.autoconfigure.openapi.message.ApiResponse;
import feign.Response;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;

/**
 * 自定义 Feign 解码器：调用后处理响应报文
 */
@Component
public class FeignCustomDecoder implements Decoder {

	private final JacksonDecoder delegate;
	private final ObjectMapper objectMapper;

	private ResponseBuilder responseBuilder;

	public FeignCustomDecoder(ObjectMapper objectMapper, ResponseBuilder responseBuilder) {
		this.delegate = new JacksonDecoder(objectMapper);
		this.objectMapper = objectMapper;
		this.responseBuilder = responseBuilder;
	}

	@Override
	public Object decode(Response response, Type type) throws IOException {
		// 1. 先解析响应为公共响应体（ResponseWrapper）
		ApiResponse<?> apiResponse = (ApiResponse<?>) delegate.decode(response, ApiResponse.class);
		Object respBizObj = responseBuilder.parseMessageResponse(apiResponse, objectMapper.constructType(type).getRawClass());
		return objectMapper.convertValue(respBizObj, objectMapper.constructType(type));
	}
}