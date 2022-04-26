package com.fib.upp.message.parser;

import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.metadata.Message;

public abstract class AbstractMessageParser {
	protected Message message;
	protected byte[] messageData;
	protected MessageBean messageBean;
	protected MessageBean parentBean;
	private String fileEncoding;

	protected AbstractMessageParser() {
		fileEncoding = System.getProperty("file.encoding");
	}

	public abstract MessageBean parse();

	public String getDefaultCharset() {
		return this.fileEncoding;
	}

	public void setDefaultCharset(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public byte[] getMessageData() {
		return messageData;
	}

	public void setMessageData(byte[] messageData) {
		this.messageData = messageData;
	}

	public MessageBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(MessageBean messageBean) {
		this.messageBean = messageBean;
	}

	public MessageBean getParentBean() {
		return parentBean;
	}

	public void setParentBean(MessageBean parentBean) {
		this.parentBean = parentBean;
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

	public int parse(int idx) {
		return 0;
	}
}