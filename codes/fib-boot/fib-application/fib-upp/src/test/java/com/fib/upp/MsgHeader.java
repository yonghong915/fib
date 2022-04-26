package com.fib.upp;

import java.util.List;

import com.fib.upp.message.metadata.Field;

import cn.hutool.core.collection.ListUtil;

public class MsgHeader {
	private List<Field> fields = ListUtil.list(false);

	public void add(Field field) {
		fields.add(field);
	}

	public List<Field> getFields() {
		return fields;
	}
}
