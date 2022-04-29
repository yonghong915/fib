package com.fib.upp.message.bean.impl;

import com.fib.upp.message.bean.MessageBean;

public class Ccms801Out extends MessageBean {
	private GrpHdr grpHdr = new GrpHdr();
	private String originalSystemDate;
	private String originalSystemStatus;
	private String currentSystemDate;
	private String currentSystemStatus;
	private String holidayFlag;
	private String nextSystemDate;
	private String bankChangeNumber;
	private String baseDataChangeNumber;
	private String cISChangeNumber;
	private String parameterChangeNumber;

	@Override
	public Object getAttribute(String name) {
		if ("GrpHdr".equals(name)) {
			return this.getGrpHdr();
		} else if ("OriginalSystemDate".equals(name)) {
			return this.getOriginalSystemDate();
		} else if ("OriginalSystemStatus".equals(name)) {
			return this.getOriginalSystemStatus();
		} else if ("CurrentSystemDate".equals(name)) {
			return this.getCurrentSystemDate();
		} else if ("CurrentSystemStatus".equals(name)) {
			return this.getCurrentSystemStatus();
		} else if ("HolidayFlag".equals(name)) {
			return this.getHolidayFlag();
		} else if ("NextSystemDate".equals(name)) {
			return this.getNextSystemDate();
		} else if ("BankChangeNumber".equals(name)) {
			return this.getBankChangeNumber();
		} else if ("BaseDataChangeNumber".equals(name)) {
			return this.getBaseDataChangeNumber();
		} else if ("CISChangeNumber".equals(name)) {
			return this.getCISChangeNumber();
		} else if ("ParameterChangeNumber".equals(name)) {
			return this.getParameterChangeNumber();
		}
		return null;
	}

	@Override
	public void setAttribute(String name, Object value) {
		if ("GrpHdr".equals(name)) {
			this.setGrpHdr((GrpHdr) value);
		} else if ("OriginalSystemDate".equals(name)) {
			this.setOriginalSystemDate((String) value);
		} else if ("OriginalSystemStatus".equals(name)) {
			this.setOriginalSystemStatus((String) value);
		} else if ("CurrentSystemDate".equals(name)) {
			this.setCurrentSystemDate((String) value);
		} else if ("CurrentSystemStatus".equals(name)) {
			this.setCurrentSystemStatus((String) value);
		} else if ("HolidayFlag".equals(name)) {
			this.setHolidayFlag((String) value);
		} else if ("NextSystemDate".equals(name)) {
			this.setNextSystemDate((String) value);
		} else if ("BankChangeNumber".equals(name)) {
			this.setBankChangeNumber((String) value);
		} else if ("BaseDataChangeNumber".equals(name)) {
			this.setBaseDataChangeNumber((String) value);
		} else if ("CISChangeNumber".equals(name)) {
			this.setCISChangeNumber((String) value);
		} else if ("ParameterChangeNumber".equals(name)) {
			this.setParameterChangeNumber((String) value);
		}
	}

	public GrpHdr getGrpHdr() {
		return grpHdr;
	}

	public void setGrpHdr(GrpHdr grpHdr) {
		this.grpHdr = grpHdr;
	}

	public String getOriginalSystemDate() {
		return originalSystemDate;
	}

	public void setOriginalSystemDate(String originalSystemDate) {
		this.originalSystemDate = originalSystemDate;
	}

	public String getOriginalSystemStatus() {
		return originalSystemStatus;
	}

	public void setOriginalSystemStatus(String originalSystemStatus) {
		this.originalSystemStatus = originalSystemStatus;
	}

	public String getCurrentSystemDate() {
		return currentSystemDate;
	}

	public void setCurrentSystemDate(String currentSystemDate) {
		this.currentSystemDate = currentSystemDate;
	}

	public String getCurrentSystemStatus() {
		return currentSystemStatus;
	}

	public void setCurrentSystemStatus(String currentSystemStatus) {
		this.currentSystemStatus = currentSystemStatus;
	}

	public String getHolidayFlag() {
		return holidayFlag;
	}

	public void setHolidayFlag(String holidayFlag) {
		this.holidayFlag = holidayFlag;
	}

	public String getNextSystemDate() {
		return nextSystemDate;
	}

	public void setNextSystemDate(String nextSystemDate) {
		this.nextSystemDate = nextSystemDate;
	}

	public String getBankChangeNumber() {
		return bankChangeNumber;
	}

	public void setBankChangeNumber(String bankChangeNumber) {
		this.bankChangeNumber = bankChangeNumber;
	}

	public String getBaseDataChangeNumber() {
		return baseDataChangeNumber;
	}

	public void setBaseDataChangeNumber(String baseDataChangeNumber) {
		this.baseDataChangeNumber = baseDataChangeNumber;
	}

	public String getCISChangeNumber() {
		return cISChangeNumber;
	}

	public void setCISChangeNumber(String cISChangeNumber) {
		this.cISChangeNumber = cISChangeNumber;
	}

	public String getParameterChangeNumber() {
		return parameterChangeNumber;
	}

	public void setParameterChangeNumber(String parameterChangeNumber) {
		this.parameterChangeNumber = parameterChangeNumber;
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
		String str = null;
		if (null != grpHdr) {
			str = grpHdr.toString(true);
			if (null != str) {
				buf.append("<a n=\"grpHdr\" t=\"bean\">");
				buf.append(str);
				buf.append("</a>");
			}
		}
		if (null != originalSystemDate) {
			buf.append("<a n=\"originalSystemDate\" t=\"datetime\">");
			buf.append(originalSystemDate);
			buf.append("</a>");
		}
		if (null != originalSystemStatus) {
			buf.append("<a n=\"originalSystemStatus\" t=\"str\">");
			buf.append(originalSystemStatus);
			buf.append("</a>");
		}
		if (null != currentSystemDate) {
			buf.append("<a n=\"currentSystemDate\" t=\"datetime\">");
			buf.append(currentSystemDate);
			buf.append("</a>");
		}
		if (null != currentSystemStatus) {
			buf.append("<a n=\"currentSystemStatus\" t=\"str\">");
			buf.append(currentSystemStatus);
			buf.append("</a>");
		}
		if (null != holidayFlag) {
			buf.append("<a n=\"holidayFlag\" t=\"str\">");
			buf.append(holidayFlag);
			buf.append("</a>");
		}
		if (null != nextSystemDate) {
			buf.append("<a n=\"nextSystemDate\" t=\"datetime\">");
			buf.append(nextSystemDate);
			buf.append("</a>");
		}
		if (null != bankChangeNumber) {
			buf.append("<a n=\"bankChangeNumber\" t=\"str\">");
			buf.append(bankChangeNumber);
			buf.append("</a>");
		}
		if (null != baseDataChangeNumber) {
			buf.append("<a n=\"baseDataChangeNumber\" t=\"str\">");
			buf.append(baseDataChangeNumber);
			buf.append("</a>");
		}
		if (null != cISChangeNumber) {
			buf.append("<a n=\"cISChangeNumber\" t=\"str\">");
			buf.append(cISChangeNumber);
			buf.append("</a>");
		}
		if (null != parameterChangeNumber) {
			buf.append("<a n=\"parameterChangeNumber\" t=\"str\">");
			buf.append(parameterChangeNumber);
			buf.append("</a>");
		}
		if (0 == buf.length()) {
			return null;
		} else {
			if (isTable) {
				buf.insert(0, "<b>");
			} else {
				buf.insert(0, "<b c=\"com.giantstone.cnaps2.messagebean.recv.req.CCMS_801_Out\">");
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
		if (null != grpHdr) {
			if (!grpHdr.isNull()) {
				return false;
			}
		}
		if (null != originalSystemDate) {
			return false;
		}
		if (null != originalSystemStatus) {
			return false;
		}
		if (null != currentSystemDate) {
			return false;
		}
		if (null != currentSystemStatus) {
			return false;
		}
		if (null != holidayFlag) {
			return false;
		}
		if (null != nextSystemDate) {
			return false;
		}
		if (null != bankChangeNumber) {
			return false;
		}
		if (null != baseDataChangeNumber) {
			return false;
		}
		if (null != cISChangeNumber) {
			return false;
		}
		if (null != parameterChangeNumber) {
			return false;
		}
		return true;
	}
}