package com.fib.msgconverter.commgateway.dao.messagecodeprocessormapping.dao;

import java.math.BigDecimal;

public class MessageCodeProcessorMapping{
	public MessageCodeProcessorMapping() {

	}

	//通道ID
	private String channelId;

	//报文吗
	private String messageCode;

	//处理器ID
	private String processorId;

	public void setChannelId(String newChannelId) {
		this.channelId = newChannelId;
	}

	public void setMessageCode(String newMessageCode) {
		this.messageCode = newMessageCode;
	}

	public void setProcessorId(String newProcessorId) {
		this.processorId = newProcessorId;
	}

	public String getChannelId() {
		return this.channelId;
	}

	public String getMessageCode() {
		return this.messageCode;
	}

	public String getProcessorId() {
		return this.processorId;
	}

}