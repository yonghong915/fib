package com.fib.msgconverter.commgateway.dao.channelreturncoderelation.dao;

import java.math.BigDecimal;

public class ChannelReturnCodeRelation{
	public ChannelReturnCodeRelation() {

	}

	//通道ID
	private String channelId;

	//返回码ID
	private String returnCodeId;

	public void setChannelId(String newChannelId) {
		this.channelId = newChannelId;
	}

	public void setReturnCodeId(String newReturnCodeId) {
		this.returnCodeId = newReturnCodeId;
	}

	public String getChannelId() {
		return this.channelId;
	}

	public String getReturnCodeId() {
		return this.returnCodeId;
	}

}