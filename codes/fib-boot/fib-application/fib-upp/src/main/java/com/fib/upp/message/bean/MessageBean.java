package com.fib.upp.message.bean;

import com.fib.upp.message.metadata.Message;

public abstract class MessageBean {

	public static final String NEW_LINE = System.getProperty("line.separator");
	private MessageBean parent;
	private Message metadata;
	private boolean shield;

	protected MessageBean() {
		shield = false;
	}

	public abstract void validate();

	public String toString() {
		return toString(false);
	}

	public String toString(boolean flag) {
		return null;
	}

	public String toString(boolean flag, boolean flag1) {
		return null;
	}

	public boolean isNull() {
		return true;
	}

	public Object getAttribute(String s) {
		return null;
	}

	public void setAttribute(String s, Object obj) {
	}

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

	public void shieldSensitiveFields() {
		shield = true;
	}

	public void unshieldSensitiveFields() {
		shield = false;
	}

	public boolean isShield() {
		return shield;
	}
}