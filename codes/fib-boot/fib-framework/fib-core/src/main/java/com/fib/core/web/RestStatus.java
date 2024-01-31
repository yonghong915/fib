package com.fib.core.web;

public interface RestStatus {

	/**
	 * the status codes of per restful request.
	 * 
	 * @return 20xxx if succeed, 40xxx if client error, 50xxx if server side crash.
	 */
	String code();

	/**
	 * @return message summary
	 */
	String message();
}
