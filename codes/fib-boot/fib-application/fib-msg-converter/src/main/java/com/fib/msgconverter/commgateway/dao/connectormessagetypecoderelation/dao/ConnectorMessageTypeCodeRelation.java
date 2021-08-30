package com.fib.msgconverter.commgateway.dao.connectormessagetypecoderelation.dao;

import java.math.BigDecimal;

public class ConnectorMessageTypeCodeRelation{
	public ConnectorMessageTypeCodeRelation() {

	}

	//连接器ID
	private String connectorId;

	//报文类型码ID
	private String messageTypeCodeId;

	public void setConnectorId(String newConnectorId) {
		this.connectorId = newConnectorId;
	}

	public void setMessageTypeCodeId(String newMessageTypeCodeId) {
		this.messageTypeCodeId = newMessageTypeCodeId;
	}

	public String getConnectorId() {
		return this.connectorId;
	}

	public String getMessageTypeCodeId() {
		return this.messageTypeCodeId;
	}

}