package com.fib.gateway.message.bean;

import com.fib.gateway.message.metadata.Message;

import lombok.Data;

/**
 * MessageBean基类
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
@Data
public abstract class MessageBean {

	public static final String COMMON_MESSAGEBEAN_CLASS = CommonMessageBean.class.getName();
	public static final String NEW_LINE = System.getProperty("line.separator");
	private MessageBean parent;
	private Message metadata;
	private boolean shield = false;

	public abstract void validate();

	public String toString() {
		return this.toString(false);
	}

	public String toString(boolean isWrap) {
		return toString(isWrap, false);
	}

	public abstract String toString(boolean isWrap, boolean isTable);

	public boolean isNull() {
		return true;
	}

	public Object getAttribute(String name) {
		return name;
	}

	public void setAttribute(String name, Object value) {
	}

	public void cover(MessageBean bean) {
	}
	
	public void shieldSensitiveFields() {
		this.shield = true;
	}

	public void unshieldSensitiveFields() {
		this.shield = false;
	}
}
