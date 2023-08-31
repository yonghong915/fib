package com.fib.commons.web;

import java.io.Serializable;

/**
 * 
 * @author fangyh
 * @since 2020-12-14
 * @version 1.0
 */
public class ResultUtil<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ResultUtil() {
		// do nothing
	}

	public static <T> ResultRsp<T> message(String code, String msg) {
		return message(code, msg, null);
	}

	public static <T> ResultRsp<T> message(RestStatus statusCode) {
		return message(statusCode.code(), statusCode.message(), null);
	}

	public static <T> ResultRsp<T> message(RestStatus statusCode, Object data) {
		return message(statusCode.code(), statusCode.message(), data);
	}

	@SuppressWarnings("unchecked")
	public static <T> ResultRsp<T> message(String code, String msg, Object data) {
		ResultRsp<Object> ret = new ResultRsp<>();
		ret.setRsp(code, msg, data);
		return (ResultRsp<T>) ret;
	}
}
