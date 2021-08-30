package com.fib.msgconverter.commgateway.dao.login.dao;

import java.math.BigDecimal;

public class Login{
	public Login() {

	}

	//id
	private String id;

	//登录器类名
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