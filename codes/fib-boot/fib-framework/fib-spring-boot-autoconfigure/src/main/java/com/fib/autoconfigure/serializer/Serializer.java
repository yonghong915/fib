package com.fib.autoconfigure.serializer;

/**
 * @author fangyh
 * @version 1.0.0
 * @date 2020-12-14
 * @since 1.0.0
 */
public interface Serializer {
	/**
	 * 序列化方法，把指定对象序列化成字节数组
	 */
	<T> byte[] serialize(T obj);

	/**
	 * 反序列化方法，将字节数组反序列化成指定Class类型
	 */
	<T> T deserialize(byte[] bytes, Class<T> clazz);
}