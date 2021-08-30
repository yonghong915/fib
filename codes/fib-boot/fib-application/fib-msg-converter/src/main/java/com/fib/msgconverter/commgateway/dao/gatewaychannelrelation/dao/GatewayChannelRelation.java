package com.fib.msgconverter.commgateway.dao.gatewaychannelrelation.dao;

import java.math.BigDecimal;

public class GatewayChannelRelation{
	public GatewayChannelRelation() {

	}

	//网关ID
	private String gatewayId;

	//通道ID
	private String channelId;

	public void setGatewayId(String newGatewayId) {
		this.gatewayId = newGatewayId;
	}

	public void setChannelId(String newChannelId) {
		this.channelId = newChannelId;
	}

	public String getGatewayId() {
		return this.gatewayId;
	}

	public String getChannelId() {
		return this.channelId;
	}

}