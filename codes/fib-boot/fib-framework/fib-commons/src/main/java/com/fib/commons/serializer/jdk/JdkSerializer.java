package com.fib.commons.serializer.jdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import com.fib.commons.exception.BaseException;
import com.fib.commons.serializer.Serializer;

/**
 * @author fangyh
 * @version 1.0.0
 * @date 2020-12-14
 * @since 1.0.0
 */
public class JdkSerializer implements Serializer {

	@Override
	public <T> byte[] serialize(T obj) {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream)) {
			objectOutput.writeObject(obj);
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			throw new BaseException("Failed to serialize obj by jdk.", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) {
		try (ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
			Object obj = objectInput.readObject();
			return (T) obj;
		} catch (IOException | ClassNotFoundException e) {
			throw new BaseException("Failed to deserialize obj by jdk.", e);
		}
	}
}
