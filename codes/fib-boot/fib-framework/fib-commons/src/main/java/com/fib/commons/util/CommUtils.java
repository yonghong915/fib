package com.fib.commons.util;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

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
		if (obj instanceof String) {
			return ((String) obj).isEmpty();
		} else if (obj instanceof Collection) {
			return ((Collection<?>) obj).isEmpty();
		} else if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty();
		}
		return false;
	}

	public static boolean validateLen(String msg, String encoding, int len) {
		try {
			return msg.getBytes(encoding).length > len;
		} catch (UnsupportedEncodingException e) {
			throw new CommonException("message.encoding.unsupport gbk");
		}
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
}
