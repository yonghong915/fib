package com.fib.mall.config;

public class RequestWrapper<T> {
	/** 请求流水号 */
	private String requestId;

	/** 时间戳 */
	private Long timestamp;

	/** 业务数据 */
	private T data;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
