package com.fib.upp.message.bean.impl;

import com.fib.upp.message.bean.MessageBean;

public class MsgHeader extends MessageBean {
	private String beginFlag = "{H:";
	private String versionID;
	private String origSender;
	private String origSenderSID;
	private String origReceiver;
	private String origReceiverSID;
	private String origSendDate;
	private String origSendTime;
	private String structType;
	private String mesgType;
	private String mesgID;
	private String mesgRefID;
	private String mesgPriority;
	private String mesgDirection;
	private String reserve;
	private String endFlag = "}";

	public Object getAttribute(String name) {
		if ("BeginFlag".equals(name)) {
			return this.getBeginFlag();
		} else if ("VersionID".equals(name)) {
			return this.getVersionID();
		} else if ("OrigSender".equals(name)) {
			return this.getOrigSender();
		} else if ("OrigSenderSID".equals(name)) {
			return this.getOrigSenderSID();
		} else if ("OrigReceiver".equals(name)) {
			return this.getOrigReceiver();
		} else if ("OrigReceiverSID".equals(name)) {
			return this.getOrigReceiverSID();
		} else if ("OrigSendDate".equals(name)) {
			return this.getOrigSendDate();
		} else if ("OrigSendTime".equals(name)) {
			return this.getOrigSendTime();
		} else if ("StructType".equals(name)) {
			return this.getStructType();
		} else if ("MesgType".equals(name)) {
			return this.getMesgType();
		} else if ("MesgID".equals(name)) {
			return this.getMesgID();
		} else if ("MesgRefID".equals(name)) {
			return this.getMesgRefID();
		} else if ("MesgPriority".equals(name)) {
			return this.getMesgPriority();
		} else if ("MesgDirection".equals(name)) {
			return this.getMesgDirection();
		} else if ("Reserve".equals(name)) {
			return this.getReserve();
		} else if ("EndFlag".equals(name)) {
			return this.getEndFlag();
		}
		return null;
	}

	public void setAttribute(String name, Object value) {
		if ("VersionID".equals(name)) {
			this.setVersionID((String) value);
		} else if ("OrigSender".equals(name)) {
			this.setOrigSender((String) value);
		} else if ("OrigSenderSID".equals(name)) {
			this.setOrigSenderSID((String) value);
		} else if ("OrigReceiver".equals(name)) {
			this.setOrigReceiver((String) value);
		} else if ("OrigReceiverSID".equals(name)) {
			this.setOrigReceiverSID((String) value);
		} else if ("OrigSendDate".equals(name)) {
			this.setOrigSendDate((String) value);
		} else if ("OrigSendTime".equals(name)) {
			this.setOrigSendTime((String) value);
		} else if ("StructType".equals(name)) {
			this.setStructType((String) value);
		} else if ("MesgType".equals(name)) {
			this.setMesgType((String) value);
		} else if ("MesgID".equals(name)) {
			this.setMesgID((String) value);
		} else if ("MesgRefID".equals(name)) {
			this.setMesgRefID((String) value);
		} else if ("MesgPriority".equals(name)) {
			this.setMesgPriority((String) value);
		} else if ("MesgDirection".equals(name)) {
			this.setMesgDirection((String) value);
		} else if ("Reserve".equals(name)) {
			this.setReserve((String) value);
		} else if ("EndFlag".equals(name)) {
			this.setEndFlag((String) value);
		}
	}

	public String getBeginFlag() {
		return beginFlag;
	}

	public void setBeginFlag(String beginFlag) {
		this.beginFlag = beginFlag;
	}

	public String getVersionID() {
		return versionID;
	}

	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}

	public String getOrigSender() {
		return origSender;
	}

	public void setOrigSender(String origSender) {
		this.origSender = origSender;
	}

	public String getOrigSenderSID() {
		return origSenderSID;
	}

	public void setOrigSenderSID(String origSenderSID) {
		this.origSenderSID = origSenderSID;
	}

	public String getOrigReceiver() {
		return origReceiver;
	}

	public void setOrigReceiver(String origReceiver) {
		this.origReceiver = origReceiver;
	}

	public String getOrigReceiverSID() {
		return origReceiverSID;
	}

	public void setOrigReceiverSID(String origReceiverSID) {
		this.origReceiverSID = origReceiverSID;
	}

	public String getOrigSendDate() {
		return origSendDate;
	}

	public void setOrigSendDate(String origSendDate) {
		this.origSendDate = origSendDate;
	}

	public String getOrigSendTime() {
		return origSendTime;
	}

	public void setOrigSendTime(String origSendTime) {
		this.origSendTime = origSendTime;
	}

	public String getStructType() {
		return structType;
	}

	public void setStructType(String structType) {
		this.structType = structType;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public String getMesgID() {
		return mesgID;
	}

	public void setMesgID(String mesgID) {
		this.mesgID = mesgID;
	}

	public String getMesgRefID() {
		return mesgRefID;
	}

	public void setMesgRefID(String mesgRefID) {
		this.mesgRefID = mesgRefID;
	}

	public String getMesgPriority() {
		return mesgPriority;
	}

	public void setMesgPriority(String mesgPriority) {
		this.mesgPriority = mesgPriority;
	}

	public String getMesgDirection() {
		return mesgDirection;
	}

	public void setMesgDirection(String mesgDirection) {
		this.mesgDirection = mesgDirection;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public String getEndFlag() {
		return endFlag;
	}

	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}
}