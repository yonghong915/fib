package com.fib.upp.message.metadata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Message {
	private String id;
	private int type;
	private String shortText;
	private String className;
	private String msgCharset;
	private Map<String, Field> fields;

	public Message() {
		this.type = 1000;
		this.fields = new LinkedHashMap<>(32);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMsgCharset() {
		return msgCharset;
	}

	public void setMsgCharset(String msgCharset) {
		this.msgCharset = msgCharset;
	}

	public Field getField(String fieldName) {
		return fields.get(fieldName);
	}

	public void setField(String fieldName, Field field) {
		fields.put(fieldName, field);
	}

	public List<Field> getFieldList() {
		List<Field> arraylist = new ArrayList<>(fields.size());
		arraylist.addAll(fields.values());
		return arraylist;
	}

	public Map<String, Field> getFields() {
		return fields;
	}

	public void setFields(Map<String, Field> sorthashmap) {
		fields = sorthashmap;
	}
}