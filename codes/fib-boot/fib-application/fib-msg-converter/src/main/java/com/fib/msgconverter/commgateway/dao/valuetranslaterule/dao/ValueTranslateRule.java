package com.fib.msgconverter.commgateway.dao.valuetranslaterule.dao;


public class ValueTranslateRule {
	public ValueTranslateRule() {

	}

	// 值转换映射规则ID
	private String id;

	// 源值
	private String valueFrom;

	// 映射后的值
	private String valueTo;

	public void setId(String newId) {
		id = newId;
	}

	public void setValueFrom(String newValueFrom) {
		valueFrom = newValueFrom;
	}

	public void setValueTo(String newValueTo) {
		valueTo = newValueTo;
	}

	public String getId() {
		return id;
	}

	public String getValueFrom() {
		return valueFrom;
	}

	public String getValueTo() {
		return valueTo;
	}

}