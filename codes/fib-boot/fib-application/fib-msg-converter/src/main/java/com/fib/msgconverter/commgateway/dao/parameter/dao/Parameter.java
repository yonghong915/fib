package com.fib.msgconverter.commgateway.dao.parameter.dao;

import java.math.BigDecimal;

public class Parameter{
	public Parameter() {

	}

	//id
	private String id;

	//参数名称
	private String parameterName;

	//参数值
	private String parameterValue;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setParameterName(String newParameterName) {
		this.parameterName = newParameterName;
	}

	public void setParameterValue(String newParameterValue) {
		this.parameterValue = newParameterValue;
	}

	public String getId() {
		return this.id;
	}

	public String getParameterName() {
		return this.parameterName;
	}

	public String getParameterValue() {
		return this.parameterValue;
	}

}