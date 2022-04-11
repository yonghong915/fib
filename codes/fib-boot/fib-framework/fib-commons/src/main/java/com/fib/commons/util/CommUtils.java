package com.fib.commons.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import com.fib.commons.config.Configuration;
import com.fib.commons.config.parser.ConfigurationManager;
import com.fib.commons.exception.CommonException;

/**
 * 公共工具类
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-31
 */
public class CommUtils {
	private CommUtils() {
		// nothing to do
	}

	public static boolean validateLen(String msg, String encoding, int len) {
		try {
			return msg.getBytes(encoding).length > len;
		} catch (UnsupportedEncodingException e) {
			throw new CommonException("message.encoding.unsupport gbk");
		}
	}

	public static String getRandom(int len) {
		String source = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder rs = new StringBuilder();
		int sourceLen = source.length();
		for (int i = 0; i < len; i++) {
			int a = ThreadLocalRandom.current().nextInt(sourceLen);
			rs.append(source.charAt(a));
		}
		return rs.toString();
	}

	public static String getRandom(String source, int len) {
		StringBuilder rs = new StringBuilder();
		int sourceLen = source.length();
		for (int i = 0; i < len; i++) {
			int a = ThreadLocalRandom.current().nextInt(sourceLen);
			rs.append(source.charAt(a));
		}
		return rs.toString();
	}

	public static String getRealValue(String configName, String value) {
		return getRealValue(configName, value.trim(), null);
	}

	public static String getRealValue(String configName, String value, String categoryName) {
		if (null == value) {
			return value;
		}

		int startIndex = value.indexOf("${");
		if (-1 == startIndex) {
			return value;
		}

		startIndex += 2;
		int endIndex = value.indexOf("}", startIndex);
		if (-1 == endIndex) {
			return value;
		}

		value = value.substring(startIndex, endIndex);
		Configuration configuration = ConfigurationManager.getInstance().getConfiguration(configName);
		return configuration.getProperty(value, categoryName);
	}

	/**
	 * 是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String str) {
			return str.trim().isEmpty();
		} else if (obj instanceof Collection<?> col) {
			return col.isEmpty();
		} else if (obj instanceof Map<?, ?> map) {
			return map.isEmpty();
		}
		return false;
	}

	/**
	 * 是否不为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static void notEmpty(Object obj, String message, Object... args) {
		if (isEmpty(obj)) {
			throw new CommonException(I18nUtils.getMessage(message, args));
		}
	}

	public static String getCanonicalPath(String path) {
		try {
			path = new File(path).getCanonicalPath() + File.separatorChar;
			return path;
		} catch (IOException e) {
			throw new CommonException(I18nUtils.getMessage("CommGateway.loadChannel.getCanonicalPath.failed"), e);
		}
	}

	public static final class NullObject {
		public NullObject() {
			// do nothing
		}

		public String toString() {
			return "ObjectType.NullObject";
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof NullObject;
		}

		@Override
		public int hashCode() {
			return 32;
		}
	}

	/**
	 * 断言对象是否为{@code null} ，如果不为{@code null} ,就执行传入的操作
	 * 
	 * @param value
	 * @param errorSupplier
	 */
	public static void isNull(Object value, Consumer<Object> action) {
		if (null != value) {
			action.accept(value);
		}
	}
}
