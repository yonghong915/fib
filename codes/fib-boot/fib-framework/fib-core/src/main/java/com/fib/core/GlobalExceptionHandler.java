package com.fib.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fib.commons.web.ResultRsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handle
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-15
 */
@RestControllerAdvice
public class GlobalExceptionHandler<T> {
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler
	public ResultRsp<T> handler(HttpServletRequest req, HttpServletResponse res, Exception e) {
		logger.info("Restful Http请求发生异常...");

		return null;
	}
}
