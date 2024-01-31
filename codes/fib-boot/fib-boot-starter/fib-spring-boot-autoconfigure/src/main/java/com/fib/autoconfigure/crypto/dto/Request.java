package com.fib.autoconfigure.crypto.dto;

/**
 * 请求信息
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-31 14:45:57
 */
public class Request {
	/**
	 * 请求头
	 */
	private RequestHeader requestHeader;

	/**
	 * 报文体，JSON格式
	 */
	private String body;

	public RequestHeader getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(RequestHeader requestHeader) {
		this.requestHeader = requestHeader;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}