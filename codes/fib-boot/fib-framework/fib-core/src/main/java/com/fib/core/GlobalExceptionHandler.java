package com.fib.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fib.commons.web.ResultRsp;
import com.fib.commons.web.ResultUtil;
import com.fib.core.exception.BusinessException;
import com.fib.core.util.StatusCode;

/**
 * Global Exception Handler
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
		logger.error("Restful Http请求发生异常.", e);
		if (e instanceof BusinessException) {
			BusinessException bizExp = (BusinessException) e;
			return ResultUtil.error(bizExp.getCode(), bizExp.getMsg());
		} else {
			return ResultUtil.message(StatusCode.OTHER_EXCEPTION);
		}
	}
}