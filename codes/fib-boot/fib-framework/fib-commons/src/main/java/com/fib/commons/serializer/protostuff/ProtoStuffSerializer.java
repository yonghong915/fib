package com.fib.commons.serializer.protostuff;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.fib.commons.serializer.Serializer;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author fangyh
 * @version 1.0.0
 * @date 2020-12-14
 * @since 1.0.0
 */
public class ProtoStuffSerializer implements Serializer {

	private static Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

	@Override
	@SuppressWarnings("unchecked")
	public <T> byte[] serialize(T obj) {
		Class<T> clazz = (Class<T>) obj.getClass();
		Schema<T> schema = getSchema(clazz);
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		byte[] data;
		try {
			data = ProtobufIOUtil.toByteArray(obj, schema, buffer);
		} finally {
			buffer.clear();
		}
		return data;
	}

	@Override
	public <T> T deserialize(byte[] data, Class<T> clazz) {
		Schema<T> schema = getSchema(clazz);
		if (Objects.isNull(schema)) {
			return null;
		}
		T obj = schema.newMessage();
		ProtobufIOUtil.mergeFrom(data, obj, schema);
		return obj;
	}

	@SuppressWarnings("unchecked")
	private <T> Schema<T> getSchema(Class<T> clazz) {
		Schema<T> schema = (Schema<T>) schemaCache.get(clazz);
		if (Objects.isNull(schema)) {
			schema = RuntimeSchema.getSchema(clazz);
			if (Objects.nonNull(schema)) {
				schemaCache.put(clazz, schema);
			}
		}
		return schema;
	}
}