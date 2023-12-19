package com.fib.core.base.validate;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 校验器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-15 11:24:23
 */
public interface BaseValidator {
	boolean check(HttpServletRequest request);
}
