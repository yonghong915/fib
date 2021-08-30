package com.fib.msgconverter.commgateway.dao.queue.dao;

import java.math.BigDecimal;

public class Queue{
	public Queue() {

	}

	//id
	private String id;

	//队列名
	private String name;

	//队列方向
	private String direction;

	//队列服务器地址
	private String serverAddress;

	//队列服务器端口
	private int port;

	//队列管理器名
	private String queueManager;

	//队列通道名
	private String mqChannelName;

	//报文标识码识别器ID
	private String messageKeyRecognizerId;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setName(String newName) {
		this.name = newName;
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

	public void setQueueManager(String newQueueManager) {
		this.queueManager = newQueueManager;
	}

	public void setMqChannelName(String newMqChannelName) {
		this.mqChannelName = newMqChannelName;
	}

	public void setMessageKeyRecognizerId(String newMessageKeyRecognizerId) {
		this.messageKeyRecognizerId = newMessageKeyRecognizerId;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
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

	public String getQueueManager() {
		return this.queueManager;
	}

	public String getMqChannelName() {
		return this.mqChannelName;
	}

	public String getMessageKeyRecognizerId() {
		return this.messageKeyRecognizerId;
	}

}