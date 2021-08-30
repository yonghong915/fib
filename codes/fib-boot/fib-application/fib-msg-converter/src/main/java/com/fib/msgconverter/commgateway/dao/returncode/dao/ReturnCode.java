package com.fib.msgconverter.commgateway.dao.returncode.dao;

import java.math.BigDecimal;

public class ReturnCode{
	public ReturnCode() {

	}

	//id
	private String id;

	//返回码
	private String returnCode;

	//是否为成功返回码
	private String success;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setReturnCode(String newReturnCode) {
		this.returnCode = newReturnCode;
	}

	public void setSuccess(String newSuccess) {
		this.success = newSuccess;
	}

	public String getId() {
		return this.id;
	}

	public String getReturnCode() {
		return this.returnCode;
	}

	public String getSuccess() {
		return this.success;
	}

}