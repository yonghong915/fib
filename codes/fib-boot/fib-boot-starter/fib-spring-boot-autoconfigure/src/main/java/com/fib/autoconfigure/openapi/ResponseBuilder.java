package com.fib.autoconfigure.openapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.openapi.config.NacosConfig;
import com.fib.autoconfigure.openapi.message.ApiRequest;
import com.fib.autoconfigure.openapi.message.ApiResponse;
import com.fib.autoconfigure.openapi.message.ResponseBody;
import com.fib.autoconfigure.openapi.message.ResponseHeader;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;

@Component
public class ResponseBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseBuilder.class);
	@Autowired
	private NacosConfig nacosConfig;

	public ResponseBuilder() {
		// nothing
	}

	/**
	 * 构建加密请求报文
	 * 
	 * @param bizData   业务数据
	 * @param requestId 请求唯一ID
	 * @return 加密请求
	 */
	public ApiResponse<Object> buildMessageResponse(Object bizData) {
		
		ApiResponse<Object> apiResponse = new ApiResponse<>();
		
		ResponseHeader responseHeader = new ResponseHeader();
		responseHeader.setCode("000000");
		responseHeader.setMessage("success");
		responseHeader.setTimestamp(System.currentTimeMillis());
		
		ResponseBody<Object> responseBody =  new ResponseBody<>();
		responseBody.setBizData(bizData);
		
		apiResponse.setResponseHeader(responseHeader);
		apiResponse.setResponseBody(responseBody);
		return apiResponse;
	}
}
