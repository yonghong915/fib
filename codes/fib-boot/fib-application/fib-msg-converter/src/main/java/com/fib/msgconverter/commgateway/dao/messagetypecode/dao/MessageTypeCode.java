package com.fib.msgconverter.commgateway.dao.messagetypecode.dao;

import java.math.BigDecimal;

public class MessageTypeCode{
	public MessageTypeCode() {

	}

	//id
	private String id;

	//报文类型码
	private String code;

	//报文类型
	private String messageType;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setCode(String newCode) {
		this.code = newCode;
	}

	public void setMessageType(String newMessageType) {
		this.messageType = newMessageType;
	}

	public String getId() {
		return this.id;
	}

	public String getCode() {
		return this.code;
	}

	public String getMessageType() {
		return this.messageType;
	}

}