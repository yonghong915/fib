package com.fib.msgconverter.commgateway.messagehandler;

import org.apache.log4j.Logger;

public abstract class AbstractMessageHandler {
	public static final String SUCCESS = "S";
	public static final String FAILED = "F";

	private byte[] messageData;
	private Object source;
	private String messageType;
	protected Logger logger;

	public abstract ResultAfterHandler doProcess();

	public byte[] getMessageData() {
		return messageData;
	}

	public void setMessageData(byte[] messageData) {
		this.messageData = messageData;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
