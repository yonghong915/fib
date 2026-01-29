package com.fib.autoconfigure.openapi;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.MediaType;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 自定义HttpServletRequestWrapper，适配JSON报文的读取、缓存和重新封装
 */
public class JsonRequestWrapper extends HttpServletRequestWrapper {

	// 缓存请求体数据（字节数组，支持多次读取）
	private byte[] requestBodyBytes;

	/**
	 * 构造方法：读取原始请求数据并缓存
	 * 
	 * @param request 原始HttpServletRequest
	 */
	public JsonRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		// 仅处理JSON类型的请求（避免处理表单、文件上传等其他类型）
		if (isJsonRequest(request)) {
			// 读取原始请求体并缓存到字节数组
			this.requestBodyBytes = IOUtils.toByteArray(request.getInputStream());
		} else {
			this.requestBodyBytes = new byte[0];
		}
	}

	/**
	 * 重新封装JSON请求数据（核心：改写请求体）
	 * 
	 * @param newJsonStr 新的JSON字符串（如解密后的报文、补充字段后的JSON）
	 */
	public void resetJsonRequestBody(String newJsonStr) {
		if (newJsonStr == null) {
			this.requestBodyBytes = new byte[0];
			return;
		}
		// 将新的JSON字符串转为字节数组，更新缓存
		this.requestBodyBytes = newJsonStr.getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * 重写getInputStream()：返回缓存的字节数组输入流（支持多次读取）
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBodyBytes);
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				return byteArrayInputStream.available() == 0;
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				// 无需实现（同步读取场景）
			}
		};
	}

	/**
	 * 重写getReader()：基于缓存的字节数组创建BufferedReader（支持多次读取）
	 */
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
	}

	/**
	 * 获取当前缓存的请求体JSON字符串
	 * 
	 * @return JSON字符串
	 */
	public String getRequestBodyJson() {
		if (requestBodyBytes == null || requestBodyBytes.length == 0) {
			return "{}";
		}
		return new String(requestBodyBytes, StandardCharsets.UTF_8);
	}

	/**
	 * 判断是否为JSON类型请求
	 * 
	 * @param request 原始请求
	 * @return true=JSON请求，false=其他类型
	 */
	private boolean isJsonRequest(HttpServletRequest request) {
		String contentType = request.getContentType();
		if (contentType == null) {
			return false;
		}
		// 匹配application/json及相关变种（如application/json;charset=utf-8）
		return contentType.toLowerCase().startsWith(MediaType.APPLICATION_JSON_VALUE.toLowerCase());
	}
}