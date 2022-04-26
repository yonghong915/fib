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
}