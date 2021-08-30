package com.fib.msgconverter.commgateway.dao.longconnection.dao;

import java.math.BigDecimal;

public class LongConnection{
	public LongConnection() {

	}

	//id
	private String id;

	//连接ID
	private String connectionId;

	//传输方向
	private String direction;

	//服务器地址
	private String serverAddress;

	//端口
	private int port;

	//本地绑定地址
	private String localServerAddress;

	//本地绑定端口
	private int localPort;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setConnectionId(String newConnectionId) {
		this.connectionId = newConnectionId;
	}

	public void setDirection(String newDirection) {
		this.direction = newDirection;
	}

	public void setServerAddress(String newServerAddress) {
		this.serverAddress = newServerAddress;
	}

	public void setPort(int newPort) {
		this.port = newPort;
	}

	public void setLocalServerAddress(String newLocalServerAddress) {
		this.localServerAddress = newLocalServerAddress;
	}

	public void setLocalPort(int newLocalPort) {
		this.localPort = newLocalPort;
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

	public String getServerAddress() {
		return this.serverAddress;
	}

	public int getPort() {
		return this.port;
	}

	public String getLocalServerAddress() {
		return this.localServerAddress;
	}

	public int getLocalPort() {
		return this.localPort;
	}

}