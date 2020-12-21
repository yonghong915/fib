package com.fib.commons.web;

/**
 * 
 * @author fangyh
 * @since 2020-12-14
 * @version 1.0
 */
public class ResultUtil {
	private ResultUtil() {
		// do nothing
	}

	public static <T> ResultRsp<T> error(String code, String msg) {
		return message(code, msg, null);
	}

	public static <T> ResultRsp<T> error(String code, String msg, Object data) {
		return message(code, msg, data);
	}

	public static <T> ResultRsp<T> message(RestStatus statusCode) {
		return message(statusCode.code(), statusCode.message(), null);
	}

	public static <T> ResultRsp<T> message(RestStatus statusCode, Object data) {
		return message(statusCode.code(), statusCode.message(), data);
	}

	@SuppressWarnings("unchecked")
	private static <T> ResultRsp<T> message(String code, String msg, Object data) {
		ResultRsp<Object> ret = new ResultRsp<>();
		ret.setRsp(code, msg, data);
		return (ResultRsp<T>) ret;
	}
}
