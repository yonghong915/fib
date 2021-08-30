package com.fib.msgconverter.commgateway.dao.commlogmessage.dao;

import java.math.BigDecimal;
@SuppressWarnings("all")

public class CommLogMessage{
	public CommLogMessage() {

	}

	//日志ID
	private String logId;

	//报文种类
	private String messageType;

	//源通道请求报文
	private byte[] message;

	public void setLogId(String newLogId) {
		this.logId = newLogId;
	}

	public void setMessageType(String newMessageType) {
		this.messageType = newMessageType;
	}

	public void setMessage(byte[] newMessage) {
		this.message = newMessage;
	}

	public String getLogId() {
		return this.logId;
	}

	public String getMessageType() {
		return this.messageType;
	}

	public byte[] getMessage() {
		return this.message;
	}

}