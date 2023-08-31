package com.fib.upp;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MoneySerializer extends JsonSerializer<Long> {
	public static final BigDecimal MULTIPLE = BigDecimal.valueOf(10000);

	@Override
	public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (null == value) {
			return;
		}
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumFractionDigits(2);
		gen.writeString(numberFormat.format(this.reduce(value)));

	}

	private Double reduce(Long value) {
		return reduce(value, 2);
	}

	private Double reduce(Long value, int scale) {
		return null == value ? null : BigDecimal.valueOf(value).divide(MULTIPLE, scale, RoundingMode.UP).doubleValue();
	}
}