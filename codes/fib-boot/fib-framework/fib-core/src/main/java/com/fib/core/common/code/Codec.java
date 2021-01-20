package com.fib.core.common.code;

public interface Codec {
	public <T> byte[] serialize(T obj);

	public <T> T deserialize(byte[] bytes, Class<T> clazz);
}
