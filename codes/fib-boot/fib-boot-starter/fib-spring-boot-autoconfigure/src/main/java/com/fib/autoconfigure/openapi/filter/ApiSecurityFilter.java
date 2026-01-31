package com.fib.autoconfigure.openapi.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.openapi.JsonRequestWrapper;
import com.fib.autoconfigure.openapi.RequestBuilder;
import com.fib.autoconfigure.openapi.ResponseBuilder;
import com.fib.autoconfigure.openapi.message.ApiRequest;
import com.fib.autoconfigure.openapi.message.ApiResponse;
import com.fib.autoconfigure.openapi.message.ResponseHeader;
import com.fib.core.exception.BusinessException;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.WriteListener;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

/**
 * 接口安全过滤器：统一处理加解密、签名验签、防重放
 */
@Component
@WebFilter(urlPatterns = "/*") // 仅拦截/api开头的接口
@Order(0)
public class ApiSecurityFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiSecurityFilter.class);

	@Autowired
	private RequestBuilder requestBuilder;

	@Autowired
	private ResponseBuilder responseBuilder;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("application/json;charset=UTF-8");

		// ========== 步骤1：包装请求，读取加密请求体 ==========
		JsonRequestWrapper wrappedRequest = new JsonRequestWrapper(httpRequest);
		String requestBody = wrappedRequest.getRequestBodyJson();
		if ("{}".equals(requestBody)) {
			chain.doFilter(request, response);
			return;
		}
		ApiRequest<?> apiRequest;
		try {
			apiRequest = SingletonObjectMapper.fromJson(requestBody, ApiRequest.class);
		} catch (Exception e) {
			LOGGER.error("请求格式错误", e);
			writeErrorResponse(httpResponse, "400", "请求格式错误：" + e.getMessage());
			return;
		}
		try {
			Object reqBizData = requestBuilder.parseMessageRequest(apiRequest);
			wrappedRequest.getInputStream().close(); // 关闭原流
			wrappedRequest.resetJsonRequestBody(SingletonObjectMapper.toSortedJson(reqBizData));
		} catch (BusinessException e) {
			LOGGER.error("Failed to ", e);
			writeErrorResponse(httpResponse, e.getCode(), "请求格式错误：" + e.getMsg());
			return;
		}

		// ==========自定义响应包装，捕获Controller响应并加密 ==========
		CustomResponseWrapper customResponse = new CustomResponseWrapper(httpResponse);
		chain.doFilter(wrappedRequest, customResponse);

		// ========== 步骤9：加密响应报文并返回 ==========
		try {
			// 读取Controller的原始响应
			String originalResponse = new String(customResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
			Object respBizObject = originalResponse;
			if (JSONUtil.isTypeJSON(originalResponse)) {
				respBizObject = SingletonObjectMapper.fromJson(originalResponse, Object.class);
			}
			ApiResponse<Object> apiResponse = responseBuilder.buildMessageResponse(respBizObject);
			apiResponse.getResponseHeader().setRequestId(apiRequest.getRequestHeader().getRequestId());
			httpResponse.getWriter().write(SingletonObjectMapper.toSortedJson(apiResponse));
		} catch (Exception e) {
			LOGGER.error("Failed to ", e);
			writeErrorResponse(httpResponse, "500", "响应加密失败：" + e.getMessage());
		}
	}

	/**
	 * 写入错误响应
	 */
	private void writeErrorResponse(HttpServletResponse response, String code, String msg) throws IOException {
		ApiResponse<Object> apiResponse = new ApiResponse<>();
		ResponseHeader responseHeader = new ResponseHeader();
		responseHeader.setCode(code);
		responseHeader.setMessage(msg);
		apiResponse.setResponseHeader(responseHeader);
		response.getWriter().write(SingletonObjectMapper.toSortedJson(apiResponse));
	}

	/**
	 * 自定义响应包装类：捕获响应内容
	 */
	private static class CustomResponseWrapper extends HttpServletResponseWrapper {
		private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		private final PrintWriter writer = new PrintWriter(outputStream);

		public CustomResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			return new ServletOutputStream() {
				@Override
				public void write(int b) throws IOException {
					outputStream.write(b);
				}

				@Override
				public boolean isReady() {
					return true;
				}

				@Override
				public void setWriteListener(WriteListener writeListener) {
					//
				}
			};
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			return writer;
		}

		/**
		 * 获取响应内容字节数组
		 */
		public byte[] getContentAsByteArray() {
			writer.flush();
			return outputStream.toByteArray();
		}
	}
}