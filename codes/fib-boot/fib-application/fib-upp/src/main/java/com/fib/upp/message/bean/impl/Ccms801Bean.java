package com.fib.upp.message.bean.impl;

import com.fib.upp.message.bean.MessageBean;

public class Ccms801Bean extends MessageBean {
	private MsgHeader msgHeader = new MsgHeader();
	private Ccms801Out ccms801Out = new Ccms801Out();

	public Object getAttribute(String name) {
		if ("MsgHeader".equals(name)) {
			return this.getMsgHeader();
		} else if ("CCMS_801_Out".equals(name)) {
			return this.getCcms801Out();
		}
		return null;
	}

	public void setAttribute(String name, Object value) {
		if ("MsgHeader".equals(name)) {
			this.setMsgHeader((MsgHeader) value);
		} else if ("CCMS_801_Out".equals(name)) {
			this.setCcms801Out((Ccms801Out) value);
		}
	}

	public MsgHeader getMsgHeader() {
		return msgHeader;
	}

	public void setMsgHeader(MsgHeader msgHeader) {
		this.msgHeader = msgHeader;
	}

	public Ccms801Out getCcms801Out() {
		return ccms801Out;
	}

	public void setCcms801Out(Ccms801Out ccms801Out) {
		this.ccms801Out = ccms801Out;
	}
}