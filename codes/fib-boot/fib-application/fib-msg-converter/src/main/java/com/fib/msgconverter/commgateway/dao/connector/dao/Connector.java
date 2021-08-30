package com.fib.msgconverter.commgateway.dao.connector.dao;

import java.math.BigDecimal;

public class Connector{
	public Connector() {

	}

	//id
	private String id;

	//连接器类型
	private String connectorType;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setConnectorType(String newConnectorType) {
		this.connectorType = newConnectorType;
	}

	public String getId() {
		return this.id;
	}

	public String getConnectorType() {
		return this.connectorType;
	}

}