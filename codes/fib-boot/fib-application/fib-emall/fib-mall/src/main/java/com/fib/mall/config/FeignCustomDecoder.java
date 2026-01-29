package com.fib.mall.config;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fib.autoconfigure.openapi.message.ApiResponse;
import com.fib.autoconfigure.openapi.message.ResponseHeader;
import com.fib.core.util.StatusCode;
import com.fib.core.web.ResultRsp;

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

	public FeignCustomDecoder(ObjectMapper objectMapper) {
		this.delegate = new JacksonDecoder(objectMapper);
		this.objectMapper = objectMapper;
	}

	@Override
	public Object decode(Response response, Type type) throws IOException {
		// 1. 先解析响应为公共响应体（ResponseWrapper）
		ApiResponse<?> apiResponse = (ApiResponse<?>) delegate.decode(response, ApiResponse.class);
		ResponseHeader responseHeader = apiResponse.getResponseHeader();
		// 2. 自定义处理：比如验签、解密、统一异常处理
		if (!StatusCode.SUCCESS.code().equals(responseHeader.getCode())) {
			throw new RuntimeException("Feign 调用失败：" + responseHeader.getMessage());
		}

		// 3. 提取业务数据并转换为目标类型（比如原接口期望的返回类型）
		return objectMapper.convertValue(responseHeader, ResultRsp.class);
	}
}
