package com.fib.commons.serializer;

import java.lang.reflect.InvocationTargetException;

import com.fib.commons.exception.BaseException;

/**
 * @author fangyh
 * @version 1.0.0
 * @date 2020-12-14
 * @since 1.0.0
 */
public class SerializationUtils {
	private Serializer serializer = null;

	private SerializationUtils() {
		// do nothing
	}

	private static class SingletonHolder {
		private static SerializationUtils instance = new SerializationUtils();
	}

	public static SerializationUtils getInstance() {
		return SingletonHolder.instance;
	}

	public Serializer loadSerializerInstance(Class<?> clazz) {
		if (null != serializer) {
			return serializer;
		}
		Object obj;
		try {
			obj = clazz.getDeclaredConstructor().newInstance();
			if (obj instanceof Serializer) {
				serializer = (Serializer) obj;
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new BaseException("Failed to obtain serial instance.", e);
		}
		return serializer;
	}
}
