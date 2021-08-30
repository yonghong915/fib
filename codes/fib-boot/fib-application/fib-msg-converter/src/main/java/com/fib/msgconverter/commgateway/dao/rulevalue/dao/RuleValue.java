package com.fib.msgconverter.commgateway.dao.rulevalue.dao;


public class RuleValue {
	public RuleValue() {

	}

	// id
	private String id;

	// 值
	private String value;

	public void setId(String newId) {
		id = newId;
	}

	public void setValue(String newValue) {
		value = newValue;
	}

	public String getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

}