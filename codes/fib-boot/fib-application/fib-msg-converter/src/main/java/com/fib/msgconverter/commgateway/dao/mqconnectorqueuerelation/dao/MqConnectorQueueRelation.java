package com.fib.msgconverter.commgateway.dao.mqconnectorqueuerelation.dao;

import java.math.BigDecimal;

public class MqConnectorQueueRelation{
	public MqConnectorQueueRelation() {

	}

	//连接器ID
	private String mqConnectorId;

	//队列ID
	private String queueId;

	public void setMqConnectorId(String newMqConnectorId) {
		this.mqConnectorId = newMqConnectorId;
	}

	public void setQueueId(String newQueueId) {
		this.queueId = newQueueId;
	}

	public String getMqConnectorId() {
		return this.mqConnectorId;
	}

	public String getQueueId() {
		return this.queueId;
	}

}