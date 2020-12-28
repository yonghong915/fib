package com.fib.gateway.message.util;

import com.fib.commons.exception.CommonException;

/**
 * 异常工具类
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
public class ExceptionUtil {
	private ExceptionUtil() {
	}

	public static void requireNotEmpty(Object obj, String message) {
		if (obj == null) {
			throw new CommonException(message);
		}
		if (obj instanceof String) {
			String ex = (String) obj;
			if (ex.isEmpty() || ex.trim().isEmpty()) {
				throw new CommonException(message);
			}
		}
	}
}
