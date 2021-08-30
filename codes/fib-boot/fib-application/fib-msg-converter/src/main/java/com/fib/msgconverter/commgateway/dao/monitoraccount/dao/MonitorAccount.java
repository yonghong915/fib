package com.fib.msgconverter.commgateway.dao.monitoraccount.dao;

import java.math.BigDecimal;

public class MonitorAccount{
	public MonitorAccount() {

	}

	//账户名
	private String accountName;

	//账户密码
	private String accountPassword;

	public void setAccountName(String newAccountName) {
		this.accountName = newAccountName;
	}

	public void setAccountPassword(String newAccountPassword) {
		this.accountPassword = newAccountPassword;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public String getAccountPassword() {
		return this.accountPassword;
	}

}