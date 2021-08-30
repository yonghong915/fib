package com.fib.msgconverter.commgateway.dao.longconnectorheartbeatrelation.dao;

import java.math.BigDecimal;

public class LongConnectorHeartbeatRelation{
	public LongConnectorHeartbeatRelation() {

	}

	//长连接器ID
	private String longConnectorId;

	//心跳包ID
	private String heartbeatId;

	public void setLongConnectorId(String newLongConnectorId) {
		this.longConnectorId = newLongConnectorId;
	}

	public void setHeartbeatId(String newHeartbeatId) {
		this.heartbeatId = newHeartbeatId;
	}

	public String getLongConnectorId() {
		return this.longConnectorId;
	}

	public String getHeartbeatId() {
		return this.heartbeatId;
	}

}