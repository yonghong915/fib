package com.fib.core.base.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public abstract class BaseDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6060535681706403250L;
	protected String channelSerialNo;
	protected Date channelDate;
	protected String channel;

	protected String returnCode;
	protected String returnMsg;
	protected String returnType = "S";

	public String getChannelSerialNo() {
		return channelSerialNo;
	}

	public void setChannelSerialNo(String channelSerialNo) {
		this.channelSerialNo = channelSerialNo;
	}

	public Date getChannelDate() {
		return channelDate;
	}

	public void setChannelDate(Date channelDate) {
		this.channelDate = channelDate;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
}