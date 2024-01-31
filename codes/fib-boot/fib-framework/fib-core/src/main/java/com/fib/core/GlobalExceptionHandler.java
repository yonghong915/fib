package com.fib.core;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fib.core.exception.BusinessException;
import com.fib.core.util.StatusCode;
import com.fib.core.web.ResultRsp;
import com.fib.core.web.ResultUtil;

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

	@ExceptionHandler(BindException.class)
	public ResultRsp<T> bindExceptionHandler(BindException e) {
		logger.error("Restful Http请求发生异常.", e);
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		List<String> collect = fieldErrors.stream().map(o -> o.getDefaultMessage()).collect(Collectors.toList());
		return ResultUtil.message(StatusCode.PARAMS_CHECK_EXCEPTION, collect.toString());
	}

	@ExceptionHandler(BusinessException.class)
	public ResultRsp<T> handler(BusinessException e) {
		logger.error("Restful Http请求发生异常.", e);
		return ResultUtil.message(e.getCode(), e.getMsg());
	}

	@ExceptionHandler(Exception.class)
	public ResultRsp<T> handler(Exception e) {
		logger.error("Restful Http请求发生异常.", e);
		return ResultUtil.message(StatusCode.OTHER_EXCEPTION);
	}
}