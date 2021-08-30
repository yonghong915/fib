package com.fib.msgconverter.commgateway.dao.heartbeat.dao;

import java.math.BigDecimal;

public class Heartbeat{
	public Heartbeat() {

	}

	//id
	private String id;

	//所属连接ID
	private String connectionId;

	//心跳包方向
	private String direction;

	//心跳包发送间隔
	private int sendInterval;

	//心跳包报文符号ID
	private String messageSymbolId;

	//是否在原路返回心跳包应答
	private String responseTurnBack;

	//应答心跳包符号ID
	private String responseMessageSymbolId;

	//应答连接ID
	private String responseConnectionId;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setConnectionId(String newConnectionId) {
		this.connectionId = newConnectionId;
	}

	public void setDirection(String newDirection) {
		this.direction = newDirection;
	}

	public void setSendInterval(int newSendInterval) {
		this.sendInterval = newSendInterval;
	}

	public void setMessageSymbolId(String newMessageSymbolId) {
		this.messageSymbolId = newMessageSymbolId;
	}

	public void setResponseTurnBack(String newResponseTurnBack) {
		this.responseTurnBack = newResponseTurnBack;
	}

	public void setResponseMessageSymbolId(String newResponseMessageSymbolId) {
		this.responseMessageSymbolId = newResponseMessageSymbolId;
	}

	public void setResponseConnectionId(String newResponseConnectionId) {
		this.responseConnectionId = newResponseConnectionId;
	}

	public String getId() {
		return this.id;
	}

	public String getConnectionId() {
		return this.connectionId;
	}

	public String getDirection() {
		return this.direction;
	}

	public int getSendInterval() {
		return this.sendInterval;
	}

	public String getMessageSymbolId() {
		return this.messageSymbolId;
	}

	public String getResponseTurnBack() {
		return this.responseTurnBack;
	}

	public String getResponseMessageSymbolId() {
		return this.responseMessageSymbolId;
	}

	public String getResponseConnectionId() {
		return this.responseConnectionId;
	}

}