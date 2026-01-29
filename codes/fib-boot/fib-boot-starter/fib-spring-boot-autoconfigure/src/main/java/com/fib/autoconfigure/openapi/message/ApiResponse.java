package com.fib.autoconfigure.openapi.message;

import java.io.Serializable;

public class ApiResponse<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private ResponseHeader responseHeader;
	private ResponseBody<T> responseBody;

	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

	public ResponseBody<T> getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(ResponseBody<T> responseBody) {
		this.responseBody = responseBody;
	}
}