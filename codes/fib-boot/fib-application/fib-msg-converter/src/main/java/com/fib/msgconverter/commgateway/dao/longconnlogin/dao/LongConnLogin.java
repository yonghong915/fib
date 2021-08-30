package com.fib.msgconverter.commgateway.dao.longconnlogin.dao;

import java.math.BigDecimal;

public class LongConnLogin{
	public LongConnLogin() {

	}

	//ID
	private String id;

	//用户名
	private String name;

	//密码
	private String password;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public void setPassword(String newPassword) {
		this.password = newPassword;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getPassword() {
		return this.password;
	}

}