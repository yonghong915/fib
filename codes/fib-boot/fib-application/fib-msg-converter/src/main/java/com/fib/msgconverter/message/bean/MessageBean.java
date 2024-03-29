package com.fib.msgconverter.message.bean;

import com.fib.msgconverter.message.metadata.Message;

public abstract class MessageBean {
	public static final String NEW_LINE = System.getProperty("line.separator");
	private MessageBean parent;
	private Message metadata;
	private boolean shield;

	protected MessageBean() {
		shield = false;
	}

	/**
	 * 
	 */
	public abstract void validate();

	public String toString() {
		return toString(false);
	}

	public abstract String toString(boolean flag);

	public abstract String toString(boolean flag, boolean flag1);

	public boolean isNull() {
		return true;
	}

	public abstract Object getAttribute(String key);

	public abstract void setAttribute(String key, Object value);

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