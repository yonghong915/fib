package com.fib.commons.serializer.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fib.commons.exception.BaseException;
import com.fib.commons.serializer.Serializer;

/**
 * @author fangyh
 * @version 1.0.0
 * @date 2020-12-14
 * @since 1.0.0
 */
public class JsonSerializer implements Serializer {

	@Override
	public <T> byte[] serialize(T obj) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsBytes(obj);
		} catch (JsonProcessingException e) {
			throw new BaseException("Failed to serialize obj by json.", e);
		}
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(bytes, clazz);
		} catch (IOException e) {
			throw new BaseException("Failed to deserialize obj by json.", e);
		}
	}
}
