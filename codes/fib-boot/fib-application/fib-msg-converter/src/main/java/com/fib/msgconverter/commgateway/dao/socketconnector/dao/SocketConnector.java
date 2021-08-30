package com.fib.msgconverter.commgateway.dao.socketconnector.dao;

import java.math.BigDecimal;

public class SocketConnector{
	public SocketConnector() {

	}

	//id
	private String id;

	//连接器类型
	private String connectorType;

	//服务器地址
	private String serverAddress;

	//端口
	private int port;

	//本地绑定地址
	private String localServerAddress;

	//本地绑定端口
	private int localPort;

	//最大客户端接入数量
	private int backlog;

	//缓冲区最大容量
	private int commBufferSize;

	//读取器ID
	private String readerId;

	//写入器ID
	private String writerId;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setConnectorType(String newConnectorType) {
		this.connectorType = newConnectorType;
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

	public void setBacklog(int newBacklog) {
		this.backlog = newBacklog;
	}

	public void setCommBufferSize(int newCommBufferSize) {
		this.commBufferSize = newCommBufferSize;
	}

	public void setReaderId(String newReaderId) {
		this.readerId = newReaderId;
	}

	public void setWriterId(String newWriterId) {
		this.writerId = newWriterId;
	}

	public String getId() {
		return this.id;
	}

	public String getConnectorType() {
		return this.connectorType;
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

	public int getBacklog() {
		return this.backlog;
	}

	public int getCommBufferSize() {
		return this.commBufferSize;
	}

	public String getReaderId() {
		return this.readerId;
	}

	public String getWriterId() {
		return this.writerId;
	}

}