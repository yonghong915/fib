package com.fib.commons.exception;

import com.fib.commons.web.RestStatus;

import lombok.Getter;

/**
 * Business exception of all business exception
 * 
 * @author fangyh
 * @date 2020-12-14
 * @version 1.0.0
 */
public class BusinessException extends BaseException {
	private static final long serialVersionUID = -2314396437259053562L;

	@Getter
	private final String code;

	@Getter
	private final String msg;

	public BusinessException(String errCode, String message) {
		super(message);
		this.code = errCode;
		this.msg = message;
	}

	public BusinessException(RestStatus restStatus) {
		super(restStatus.message());
		this.code = restStatus.code();
		this.msg = restStatus.message();
	}

	public BusinessException(RestStatus restStatus, Throwable cause) {
		super(restStatus.message(), cause);
		this.code = restStatus.code();
		this.msg = restStatus.message();
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