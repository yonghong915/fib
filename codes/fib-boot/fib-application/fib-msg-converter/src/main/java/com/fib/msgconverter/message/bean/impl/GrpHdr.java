package com.fib.msgconverter.message.bean.impl;

import com.fib.msgconverter.message.bean.MessageBean;

public class GrpHdr extends MessageBean {
	private String messageIdentification;
	private String creationDateTime;
	private String instructingDirectParty;
	private String instructingParty;
	private String instructedDirectParty;
	private String instructedParty;
	private String systemCode;
	private String remark;

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString(boolean flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString(boolean flag, boolean flag1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttribute(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String key, Object value) {
		// TODO Auto-generated method stub

	}

	public String getMessageIdentification() {
		return messageIdentification;
	}

	public void setMessageIdentification(String messageIdentification) {
		this.messageIdentification = messageIdentification;
	}

	public String getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(String creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getInstructingDirectParty() {
		return instructingDirectParty;
	}

	public void setInstructingDirectParty(String instructingDirectParty) {
		this.instructingDirectParty = instructingDirectParty;
	}

	public String getInstructingParty() {
		return instructingParty;
	}

	public void setInstructingParty(String instructingParty) {
		this.instructingParty = instructingParty;
	}

	public String getInstructedDirectParty() {
		return instructedDirectParty;
	}

	public void setInstructedDirectParty(String instructedDirectParty) {
		this.instructedDirectParty = instructedDirectParty;
	}

	public String getInstructedParty() {
		return instructedParty;
	}

	public void setInstructedParty(String instructedParty) {
		this.instructedParty = instructedParty;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
