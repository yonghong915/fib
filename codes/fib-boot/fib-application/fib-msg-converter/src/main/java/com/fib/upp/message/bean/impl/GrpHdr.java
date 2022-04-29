package com.fib.upp.message.bean.impl;

import com.giantstone.message.bean.MessageBean;

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

	@Override
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

	@Override
	public void validate() {
		//
	}

	@Override
	public String toString() {
		return toString(false);
	}

	@Override
	public String toString(boolean isWrap) {
		return toString(isWrap, false);
	}

	@Override
	public String toString(boolean isWrap, boolean isTable) {
		StringBuilder buf = new StringBuilder(10240);
		if (null != messageIdentification) {
			buf.append("<a n=\"messageIdentification\" t=\"str\">");
			buf.append(messageIdentification);
			buf.append("</a>");
		}
		if (null != creationDateTime) {
			buf.append("<a n=\"creationDateTime\" t=\"datetime\">");
			buf.append(creationDateTime);
			buf.append("</a>");
		}
		if (null != instructingDirectParty) {
			buf.append("<a n=\"instructingDirectParty\" t=\"str\">");
			buf.append(instructingDirectParty);
			buf.append("</a>");
		}
		if (null != instructingParty) {
			buf.append("<a n=\"instructingParty\" t=\"str\">");
			buf.append(instructingParty);
			buf.append("</a>");
		}
		if (null != instructedDirectParty) {
			buf.append("<a n=\"instructedDirectParty\" t=\"str\">");
			buf.append(instructedDirectParty);
			buf.append("</a>");
		}
		if (null != instructedParty) {
			buf.append("<a n=\"instructedParty\" t=\"str\">");
			buf.append(instructedParty);
			buf.append("</a>");
		}
		if (null != systemCode) {
			buf.append("<a n=\"systemCode\" t=\"str\">");
			buf.append(systemCode);
			buf.append("</a>");
		}
		if (null != remark) {
			buf.append("<a n=\"remark\" t=\"str\">");
			buf.append(remark);
			buf.append("</a>");
		}
		if (0 == buf.length()) {
			return null;
		} else {
			if (isTable) {
				buf.insert(0, "<b>");
			} else {
				buf.insert(0, "<b c=\"com.giantstone.cnaps2.messagebean.recv.req.GrpHdr\">");
			}
			buf.append("</b>");
			if (!isWrap) {
				buf = new StringBuilder(buf.toString());
				buf.insert(0, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				return buf.toString();
			} else {
				return buf.toString();
			}
		}
	}

	@Override
	public boolean isNull() {
		if (null != messageIdentification) {
			return false;
		}
		if (null != creationDateTime) {
			return false;
		}
		if (null != instructingDirectParty) {
			return false;
		}
		if (null != instructingParty) {
			return false;
		}
		if (null != instructedDirectParty) {
			return false;
		}
		if (null != instructedParty) {
			return false;
		}
		if (null != systemCode) {
			return false;
		}
		if (null != remark) {
			return false;
		}
		return true;
	}
}
