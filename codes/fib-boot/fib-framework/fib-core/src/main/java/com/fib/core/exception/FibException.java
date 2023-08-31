package com.fib.core.exception;

import org.springframework.core.NestedCheckedException;

public class FibException extends NestedCheckedException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4029279259668262577L;
	
	public FibException(String msg) {
		super(msg);
	}

}
