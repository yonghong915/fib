package com.fib.commons.exception;

/**
 * 通用异常
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-15
 */
public class CommonException extends BaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonException(String message) {
		super(message);
	}

}
