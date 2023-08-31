package com.fib.msgconverter.message.bean.impl;

import com.fib.msgconverter.message.bean.MessageBean;

public class Ccms801Out extends MessageBean {
	private GrpHdr grpHdr;
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
