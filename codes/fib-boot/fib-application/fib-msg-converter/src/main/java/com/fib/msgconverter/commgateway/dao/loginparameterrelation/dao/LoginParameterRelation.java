package com.fib.msgconverter.commgateway.dao.loginparameterrelation.dao;

import java.math.BigDecimal;

public class LoginParameterRelation{
	public LoginParameterRelation() {

	}

	//登录器ID
	private String loginId;

	//参数ID
	private String parameterId;

	public void setLoginId(String newLoginId) {
		this.loginId = newLoginId;
	}

	public void setParameterId(String newParameterId) {
		this.parameterId = newParameterId;
	}

	public String getLoginId() {
		return this.loginId;
	}

	public String getParameterId() {
		return this.parameterId;
	}

}