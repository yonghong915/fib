package com.fib.commons.util.serialize;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fib.commons.exception.CommonException;

import cn.hutool.core.util.StrUtil;

/**
 * 时间类型JSON序列化
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-15
 */
public class DateJsonDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(com.fasterxml.jackson.core.JsonParser jsonParser,
			DeserializationContext deserializationContext) throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (jsonParser != null && StrUtil.isNotEmpty(jsonParser.getText())) {
				return format.parse(jsonParser.getText());
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new CommonException("", e);
		}
	}
}
