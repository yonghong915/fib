package com.fib.autoconfigure.disruptor.exception;

public class EventHandleException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3667648352873635903L;

	public EventHandleException(Exception e) {
		super(e.getMessage(), null);
	}

	public EventHandleException(String errorMessage) {
		super(errorMessage, null);
	}

	public EventHandleException(String errorMessage, Throwable cause) {
		super(errorMessage, cause);
	}
}