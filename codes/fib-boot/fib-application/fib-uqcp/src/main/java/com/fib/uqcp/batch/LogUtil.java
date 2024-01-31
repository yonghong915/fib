package com.fib.uqcp.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
	private static Logger LOGGER = null;

	public void debug(String msg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(msg);
		}
	}

	public void info(String format, Object arg) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(format, arg);
		}
	}

	public void error(String format, Object arg) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(format, arg);
		}
	}

	public static Logger getLogger(Class<?> clazz) {
		return LOGGER = LoggerFactory.getLogger(clazz);
	}
}