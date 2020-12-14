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

	public void loadSerializerInstance(String className) {
		if (null != serializer) {
			return;
		}
		try {
			Class<?> clazz = Class.forName(className);
			Object obj = clazz.getDeclaredConstructor().newInstance();
			if (obj instanceof Serializer) {
				serializer = (Serializer) obj;
			}
		} catch (ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | InstantiationException | IllegalAccessException e) {
			throw new BaseException("Failed to obtain serial instance.", e);
		}
	}

	public <T> byte[] serialize(T obj) {
		return serializer.serialize(obj);
	}

	public <T> T deserialize(byte[] data, Class<T> clazz) {
		return serializer.deserialize(data, clazz);
	}
}
