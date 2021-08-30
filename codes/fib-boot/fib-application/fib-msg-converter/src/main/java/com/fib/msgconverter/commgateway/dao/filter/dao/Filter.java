package com.fib.msgconverter.commgateway.dao.filter.dao;

import java.math.BigDecimal;

public class Filter{
	public Filter() {

	}

	//id
	private String id;

	//过滤器类名
	private String className;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setClassName(String newClassName) {
		this.className = newClassName;
	}

	public String getId() {
		return this.id;
	}

	public String getClassName() {
		return this.className;
	}

}