package com.fib.upp.message.bean;

import com.fib.upp.message.metadata.Message;

public abstract class MessageBean {
	public static final String NEW_LINE = System.getProperty("line.separator");
	private MessageBean parent;
	private Message metadata;

	public MessageBean getParent() {
		return parent;
	}

	public void setParent(MessageBean messagebean) {
		parent = messagebean;
	}

	public Message getMetadata() {
		return metadata;
	}

	public void setMetadata(Message message) {
		metadata = message;
	}
}
