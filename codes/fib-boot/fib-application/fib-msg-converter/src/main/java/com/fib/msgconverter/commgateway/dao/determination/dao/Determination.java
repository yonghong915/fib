package com.fib.msgconverter.commgateway.dao.determination.dao;

import java.math.BigDecimal;

public class Determination{
	public Determination() {

	}

	//id
	private String id;

	//默认的通道ID
	private String defaultChannelId;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setDefaultChannelId(String newDefaultChannelId) {
		this.defaultChannelId = newDefaultChannelId;
	}

	public String getId() {
		return this.id;
	}

	public String getDefaultChannelId() {
		return this.defaultChannelId;
	}

}