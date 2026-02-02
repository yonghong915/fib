package com.fib.midbiz.common.api;

public class Result<T> {
	/** 响应码：200 成功，500 失败 */
	private int code;
	/** 响应消息 */
	private String msg;
	/** 响应数据 */
	private T data;

	// 成功响应
	public static <T> Result<T> success(T data) {
		Result<T> result = new Result<>();
		result.setCode(200);
		result.setMsg("操作成功");
		result.setData(data);
		return result;
	}

	// 失败响应
	public static <T> Result<T> fail(String msg) {
		Result<T> result = new Result<>();
		result.setCode(500);
		result.setMsg(msg);
		result.setData(null);
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
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
}