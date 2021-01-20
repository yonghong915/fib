package com.fib.commons.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

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

	public CommonException(String message) {
		super(message);
	}

	public CommonException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public CommonException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public CommonException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CommonException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
