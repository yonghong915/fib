package com.fib.msgconverter.commgateway.dao.longconnectorconnectionrelation.dao;

import java.math.BigDecimal;

public class LongConnectorConnectionRelation{
	public LongConnectorConnectionRelation() {

	}

	//长连接器ID
	private String longConnectorId;

	//连接ID
	private String connectionId;

	public void setLongConnectorId(String newLongConnectorId) {
		this.longConnectorId = newLongConnectorId;
	}

	public void setConnectionId(String newConnectionId) {
		this.connectionId = newConnectionId;
	}

	public String getLongConnectorId() {
		return this.longConnectorId;
	}

	public String getConnectionId() {
		return this.connectionId;
	}

}