package com.fib.msgconverter.commgateway.dao.mqconnector.dao;

import java.math.BigDecimal;

public class MqConnector{
	public MqConnector() {

	}

	//id
	private String id;

	//超时时间
	private int timeout;

	//传输用编码格式
	private int ccsid;

	//报文类型码识别器ID
	private String codeRecognizerId;

	//MQ通道类型
	private String mqType;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setTimeout(int newTimeout) {
		this.timeout = newTimeout;
	}

	public void setCcsid(int newCcsid) {
		this.ccsid = newCcsid;
	}

	public void setCodeRecognizerId(String newCodeRecognizerId) {
		this.codeRecognizerId = newCodeRecognizerId;
	}

	public void setMqType(String newMqType) {
		this.mqType = newMqType;
	}

	public String getId() {
		return this.id;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public int getCcsid() {
		return this.ccsid;
	}

	public String getCodeRecognizerId() {
		return this.codeRecognizerId;
	}

	public String getMqType() {
		return this.mqType;
	}

}