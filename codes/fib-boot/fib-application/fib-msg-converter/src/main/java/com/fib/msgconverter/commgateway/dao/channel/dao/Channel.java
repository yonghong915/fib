package com.fib.msgconverter.commgateway.dao.channel.dao;

import java.math.BigDecimal;

public class Channel{
	public Channel() {

	}

	//id
	private String id;

	//名称
	private String name;

	//通道ID
	private String channelId;

	//是否启动
	private String startup;

	//通道类名
	private String className;

	//通道模式
	private String mode;

	//通道描述
	private String description;

	//报文码识别器ID
	private String messageCodeRecognizerId;

	//返回码识别器ID
	private String returnCodeRecognizerId;

	//MessageBean和Mapping组ID
	private String mbMappingGroup;

	//连接器Id
	private String connectorId;

	//通道类型
	private String channelType;

	//通道部署目录
	private String deployPath;

	//事件处理器类名
	private String eventHandlerClassName;

	//默认处理器ID
	private String defaultProcessorId;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public void setChannelId(String newChannelId) {
		this.channelId = newChannelId;
	}

	public void setStartup(String newStartup) {
		this.startup = newStartup;
	}

	public void setClassName(String newClassName) {
		this.className = newClassName;
	}

	public void setMode(String newMode) {
		this.mode = newMode;
	}

	public void setDescription(String newDescription) {
		this.description = newDescription;
	}

	public void setMessageCodeRecognizerId(String newMessageCodeRecognizerId) {
		this.messageCodeRecognizerId = newMessageCodeRecognizerId;
	}

	public void setReturnCodeRecognizerId(String newReturnCodeRecognizerId) {
		this.returnCodeRecognizerId = newReturnCodeRecognizerId;
	}

	public void setMbMappingGroup(String newMbMappingGroup) {
		this.mbMappingGroup = newMbMappingGroup;
	}

	public void setConnectorId(String newConnectorId) {
		this.connectorId = newConnectorId;
	}

	public void setChannelType(String newChannelType) {
		this.channelType = newChannelType;
	}

	public void setDeployPath(String newDeployPath) {
		this.deployPath = newDeployPath;
	}

	public void setEventHandlerClassName(String newEventHandlerClassName) {
		this.eventHandlerClassName = newEventHandlerClassName;
	}

	public void setDefaultProcessorId(String newDefaultProcessorId) {
		this.defaultProcessorId = newDefaultProcessorId;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getChannelId() {
		return this.channelId;
	}

	public String getStartup() {
		return this.startup;
	}

	public String getClassName() {
		return this.className;
	}

	public String getMode() {
		return this.mode;
	}

	public String getDescription() {
		return this.description;
	}

	public String getMessageCodeRecognizerId() {
		return this.messageCodeRecognizerId;
	}

	public String getReturnCodeRecognizerId() {
		return this.returnCodeRecognizerId;
	}

	public String getMbMappingGroup() {
		return this.mbMappingGroup;
	}

	public String getConnectorId() {
		return this.connectorId;
	}

	public String getChannelType() {
		return this.channelType;
	}

	public String getDeployPath() {
		return this.deployPath;
	}

	public String getEventHandlerClassName() {
		return this.eventHandlerClassName;
	}

	public String getDefaultProcessorId() {
		return this.defaultProcessorId;
	}

}