package com.fib.msgconverter.commgateway.dao.gateway.dao;

import java.math.BigDecimal;

public class Gateway{
	public Gateway() {

	}

	//网关Id
	private String id;

	//网关名称
	private String name;

	//启动时监听命令端口
	private int monitorPort;

	//最大事件处理器数
	private int maxHandlerNumber;

	//会话超时时间
	private int sessionTimeout;

	//日志记录器名称
	private String loggerName;

	//记录入数据库的日志级别
	private String logLevelIntoDb;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public void setMonitorPort(int newMonitorPort) {
		this.monitorPort = newMonitorPort;
	}

	public void setMaxHandlerNumber(int newMaxHandlerNumber) {
		this.maxHandlerNumber = newMaxHandlerNumber;
	}

	public void setSessionTimeout(int newSessionTimeout) {
		this.sessionTimeout = newSessionTimeout;
	}

	public void setLoggerName(String newLoggerName) {
		this.loggerName = newLoggerName;
	}

	public void setLogLevelIntoDb(String newLogLevelIntoDb) {
		this.logLevelIntoDb = newLogLevelIntoDb;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public int getMonitorPort() {
		return this.monitorPort;
	}

	public int getMaxHandlerNumber() {
		return this.maxHandlerNumber;
	}

	public int getSessionTimeout() {
		return this.sessionTimeout;
	}

	public String getLoggerName() {
		return this.loggerName;
	}

	public String getLogLevelIntoDb() {
		return this.logLevelIntoDb;
	}

}