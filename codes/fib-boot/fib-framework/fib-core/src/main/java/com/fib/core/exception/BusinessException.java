package com.fib.core.exception;

import com.fib.core.web.RestStatus;

import cn.hutool.core.text.StrFormatter;

/**
 * Business exception of all business exception
 * 
 * @author fangyh
 * @date 2020-12-14
 * @version 1.0.0
 */
public class BusinessException extends BaseException {
	private static final long serialVersionUID = -2314396437259053562L;

	private final String code;

	private final String msg;

	public BusinessException(String errCode, String errMsg, Object... args) {
		super(errMsg);
		this.code = errCode;
		this.msg = StrFormatter.format(errMsg, args);
	}

	public BusinessException(RestStatus restStatus, Object... args) {
		super(restStatus.message());
		this.code = restStatus.code();
		String errMsg = restStatus.message();
		this.msg = StrFormatter.format(errMsg, args);
	}

	public BusinessException(RestStatus restStatus, Throwable cause, Object... args) {
		super(restStatus.message(), cause);
		this.code = restStatus.code();
		String errMsg = restStatus.message();
		this.msg = StrFormatter.format(errMsg, args);
	}

	@Override
	public String toString() {
		return "{rspCode:" + code + ",rspMsg:" + msg + "}";
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}