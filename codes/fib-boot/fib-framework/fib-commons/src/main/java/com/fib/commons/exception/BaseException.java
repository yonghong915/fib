package com.fib.commons.exception;

/**
 * Base exception class of all exception classes
 * 
 * @author fangyh
 * @date 2020-12-14
 * @version 1.0.0
 */
public class BaseException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1799221519428280390L;

	public BaseException() {
		super();
	}

	public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}
}
