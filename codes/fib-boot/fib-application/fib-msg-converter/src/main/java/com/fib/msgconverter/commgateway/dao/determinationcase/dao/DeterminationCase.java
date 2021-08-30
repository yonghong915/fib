package com.fib.msgconverter.commgateway.dao.determinationcase.dao;

import java.math.BigDecimal;

public class DeterminationCase{
	public DeterminationCase() {

	}

	//id
	private String id;

	//结果
	private String result;

	//对应通道Id
	private String channelId;

	//用于替换的处理器ID
	private String processorOverride;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setResult(String newResult) {
		this.result = newResult;
	}

	public void setChannelId(String newChannelId) {
		this.channelId = newChannelId;
	}

	public void setProcessorOverride(String newProcessorOverride) {
		this.processorOverride = newProcessorOverride;
	}

	public String getId() {
		return this.id;
	}

	public String getResult() {
		return this.result;
	}

	public String getChannelId() {
		return this.channelId;
	}

	public String getProcessorOverride() {
		return this.processorOverride;
	}

}