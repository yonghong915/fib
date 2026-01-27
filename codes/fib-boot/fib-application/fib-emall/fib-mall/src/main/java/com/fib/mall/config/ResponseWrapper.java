package com.fib.mall.config;

public class ResponseWrapper<T> {

	/** 原请求id */
	private String orgiRequestId;

	/** 返回码 */
	private String code;

	/** 返回信息 */
	private String msg;

	/** 业务响应体 */
	private T data;

	/** 时间戳 */
	private Long timestamp;

	public String getOrgiRequestId() {
		return orgiRequestId;
	}

	public void setOrgiRequestId(String orgiRequestId) {
		this.orgiRequestId = orgiRequestId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}