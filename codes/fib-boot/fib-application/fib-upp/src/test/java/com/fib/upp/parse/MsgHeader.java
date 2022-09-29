package com.fib.upp.parse;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class MsgHeader {
	private String beginFlag = "{H:";
	private String version;
	private String origSender;
	private String origSenderSid;
	private String origReceiver;
	private String origReceiverSid;
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

	public String getOrigSendTime() {
		return origSendTime;
	}

	public void setOrigSendTime(String origSendTime) {
		this.origSendTime = origSendTime;
	}

	public String getOrigSendDate() {
		return origSendDate;
	}

	public void setOrigSendDate(String origSendDate) {
		this.origSendDate = origSendDate;
	}

	public String getOrigReceiver() {
		return origReceiver;
	}

	public void setOrigReceiver(String origReceiver) {
		this.origReceiver = origReceiver;
	}

	public String getOrigReceiverSid() {
		return origReceiverSid;
	}

	public void setOrigReceiverSid(String origReceiverSid) {
		this.origReceiverSid = origReceiverSid;
	}

	public String getOrigSenderSid() {
		return origSenderSid;
	}

	public void setOrigSenderSid(String origSenderSid) {
		this.origSenderSid = origSenderSid;
	}

	public String getOrigSender() {
		return origSender;
	}

	public void setOrigSender(String origSender) {
		this.origSender = origSender;
	}

	public String getBeginFlag() {
		return beginFlag;
	}

	public void setBeginFlag(String beginFlag) {
		this.beginFlag = beginFlag;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
