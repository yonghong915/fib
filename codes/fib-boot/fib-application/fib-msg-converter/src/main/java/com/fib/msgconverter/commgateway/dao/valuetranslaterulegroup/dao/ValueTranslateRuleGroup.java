package com.fib.msgconverter.commgateway.dao.valuetranslaterulegroup.dao;


public class ValueTranslateRuleGroup {
	public ValueTranslateRuleGroup() {

	}

	// id
	private String id;

	// 描述
	private String description;

	// 值转换映射组默认值
	private byte[] defaultValue;

	public void setId(String newId) {
		id = newId;
	}

	public void setDescription(String newDescription) {
		description = newDescription;
	}

	public void setDefaultValue(byte[] newDefaultValue) {
		defaultValue = newDefaultValue;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public byte[] getDefaultValue() {
		return defaultValue;
	}

}