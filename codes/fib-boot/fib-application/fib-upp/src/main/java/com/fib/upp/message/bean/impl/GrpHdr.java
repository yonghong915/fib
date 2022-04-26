package com.fib.upp.message.bean.impl;

import com.fib.upp.message.bean.MessageBean;

public class GrpHdr extends MessageBean {
	private String messageIdentification;
	private String creationDateTime;
	private String instructingDirectParty;
	private String instructingParty;
	private String instructedDirectParty;
	private String instructedParty;
	private String systemCode;
	private String remark;

	public Object getAttribute(String name) {
		if ("MessageIdentification".equals(name)) {
			return this.getMessageIdentification();
		} else if ("CreationDateTime".equals(name)) {
			return this.getCreationDateTime();
		} else if ("InstructingDirectParty".equals(name)) {
			return this.getInstructingDirectParty();
		} else if ("InstructingParty".equals(name)) {
			return this.getInstructingParty();
		} else if ("InstructedDirectParty".equals(name)) {
			return this.getInstructedDirectParty();
		} else if ("InstructedParty".equals(name)) {
			return this.getInstructedParty();
		} else if ("SystemCode".equals(name)) {
			return this.getSystemCode();
		} else if ("Remark".equals(name)) {
			return this.getRemark();
		}
		return null;
	}

	public void setAttribute(String name, Object value) {
		if ("MessageIdentification".equals(name)) {
			this.setMessageIdentification((String) value);
		} else if ("CreationDateTime".equals(name)) {
			this.setCreationDateTime((String) value);
		} else if ("InstructingDirectParty".equals(name)) {
			this.setInstructingDirectParty((String) value);
		} else if ("InstructingParty".equals(name)) {
			this.setInstructingParty((String) value);
		} else if ("InstructedDirectParty".equals(name)) {
			this.setInstructedDirectParty((String) value);
		} else if ("InstructedParty".equals(name)) {
			this.setInstructedParty((String) value);
		} else if ("SystemCode".equals(name)) {
			this.setSystemCode((String) value);
		} else if ("Remark".equals(name)) {
			this.setRemark((String) value);
		}
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
