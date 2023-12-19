package com.fib;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MoneySerializer extends JsonSerializer<Long> {

	@Override
	public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		if (Objects.nonNull(value)) {
			String format = getString(value);
			jsonGenerator.writeString(format);
		} else {// 这个分支不要忘记了，否则将不输出这个属性的值
			value = 0L;
			String format = getString(value);
			jsonGenerator.writeString(format);
		}
	}

	private String getString(Long aLong) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(aLong.doubleValue() / 100);
	}
}
