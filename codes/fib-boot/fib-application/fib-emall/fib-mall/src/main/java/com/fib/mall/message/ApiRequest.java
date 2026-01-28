package com.fib.mall.message;

import java.io.Serializable;

public class ApiRequest<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private RequestHeader requestHeader;
	private RequestBody<T> requestBody;

	public RequestHeader getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(RequestHeader requestHeader) {
		this.requestHeader = requestHeader;
	}

	public RequestBody<T> getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(RequestBody<T> requestBody) {
		this.requestBody = requestBody;
	}
}