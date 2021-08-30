package com.fib.msgconverter.commgateway.dao.longsocketconnector.dao;

import java.math.BigDecimal;

public class LongSocketConnector{
	public LongSocketConnector() {

	}

	//id
	private String id;

	//登录器ID
	private String loginId;

	//请求报文流水号识别器ID
	private String requestSerialNumberRecognizerId;

	//应答报文流水号识别器ID
	private String responseSerialNumberRecognizerId;

	//报文码识别器ID
	private String codeRecognizerId;

	//读取器ID
	private String readerId;

	//写入器ID
	private String writerId;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setLoginId(String newLoginId) {
		this.loginId = newLoginId;
	}

	public void setRequestSerialNumberRecognizerId(String newRequestSerialNumberRecognizerId) {
		this.requestSerialNumberRecognizerId = newRequestSerialNumberRecognizerId;
	}

	public void setResponseSerialNumberRecognizerId(String newResponseSerialNumberRecognizerId) {
		this.responseSerialNumberRecognizerId = newResponseSerialNumberRecognizerId;
	}

	public void setCodeRecognizerId(String newCodeRecognizerId) {
		this.codeRecognizerId = newCodeRecognizerId;
	}

	public void setReaderId(String newReaderId) {
		this.readerId = newReaderId;
	}

	public void setWriterId(String newWriterId) {
		this.writerId = newWriterId;
	}

	public String getId() {
		return this.id;
	}

	public String getLoginId() {
		return this.loginId;
	}

	public String getRequestSerialNumberRecognizerId() {
		return this.requestSerialNumberRecognizerId;
	}

	public String getResponseSerialNumberRecognizerId() {
		return this.responseSerialNumberRecognizerId;
	}

	public String getCodeRecognizerId() {
		return this.codeRecognizerId;
	}

	public String getReaderId() {
		return this.readerId;
	}

	public String getWriterId() {
		return this.writerId;
	}

}