package com.fib.core.util;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;

@Component
public class ErrorCodeUtil {
	private static final Logger logger = LoggerFactory.getLogger(ErrorCodeUtil.class);
	private static final Locale DEFAULT_LOCALE = Locale.getDefault();

	private static RedisUtil redisUtil;

	@Autowired
	private RedisUtil redisUtilTmp;

	private ErrorCodeUtil() {
	}

	@PostConstruct
	public void init() {
		redisUtil = this.redisUtilTmp;
	}

	public static String getDescription(String errorCode, String... args) {
		if (StrUtil.isEmpty(errorCode)) {
			logger.error("input param errorCode is null.");
			return errorCode;
		}
		return getDescription(errorCode, DEFAULT_LOCALE, args);
	}
	
	public static String getDescription(String errorCode, Locale locale, String... args) {
		if (StrUtil.isEmpty(errorCode)) {
			logger.error("input param errorCode is null.");
			return errorCode;
		}
		Object obj = redisUtil.get("errorCode~" + locale.toString() + "~" + errorCode);
		if (null == obj) {
			return "";
		}
		return obj.toString();
	}
}
