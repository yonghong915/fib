package com.fib.msgconverter.commgateway.dao.sessiondigest.dao;

import java.math.BigDecimal;

public class SessionDigest{
	public SessionDigest() {

	}

	//ID
	private String id;

	//所属网关ID
	private String gatewayId;

	//外部流水号
	private String externalSerialNum;

	//开始日期
	private String startDate;

	//开始时间
	private String startTime;

	//结束日期
	private String endDate;

	//结束时间
	private String endTime;

	//请求发送日期
	private String requestSendDate;

	//请求发送时间
	private String requestSendTime;

	//响应接收日期
	private String responseArrivedDate;

	//响应接收时间
	private String responseArrivedTime;

	//状态
	private String state;

	//类型
	private String type;

	//处理器Id
	private String processorId;

	//源通道ID
	private String sourceChannelId;

	//源通道名称
	private String sourceChannelName;

	//目的通道ID
	private String destChannelId;

	//目的通道名称
	private String destChannelName;

	//错误类型
	private String errorType;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setGatewayId(String newGatewayId) {
		this.gatewayId = newGatewayId;
	}

	public void setExternalSerialNum(String newExternalSerialNum) {
		this.externalSerialNum = newExternalSerialNum;
	}

	public void setStartDate(String newStartDate) {
		this.startDate = newStartDate;
	}

	public void setStartTime(String newStartTime) {
		this.startTime = newStartTime;
	}

	public void setEndDate(String newEndDate) {
		this.endDate = newEndDate;
	}

	public void setEndTime(String newEndTime) {
		this.endTime = newEndTime;
	}

	public void setRequestSendDate(String newRequestSendDate) {
		this.requestSendDate = newRequestSendDate;
	}

	public void setRequestSendTime(String newRequestSendTime) {
		this.requestSendTime = newRequestSendTime;
	}

	public void setResponseArrivedDate(String newResponseArrivedDate) {
		this.responseArrivedDate = newResponseArrivedDate;
	}

	public void setResponseArrivedTime(String newResponseArrivedTime) {
		this.responseArrivedTime = newResponseArrivedTime;
	}

	public void setState(String newState) {
		this.state = newState;
	}

	public void setType(String newType) {
		this.type = newType;
	}

	public void setProcessorId(String newProcessorId) {
		this.processorId = newProcessorId;
	}

	public void setSourceChannelId(String newSourceChannelId) {
		this.sourceChannelId = newSourceChannelId;
	}

	public void setSourceChannelName(String newSourceChannelName) {
		this.sourceChannelName = newSourceChannelName;
	}

	public void setDestChannelId(String newDestChannelId) {
		this.destChannelId = newDestChannelId;
	}

	public void setDestChannelName(String newDestChannelName) {
		this.destChannelName = newDestChannelName;
	}

	public void setErrorType(String newErrorType) {
		this.errorType = newErrorType;
	}

	public String getId() {
		return this.id;
	}

	public String getGatewayId() {
		return this.gatewayId;
	}

	public String getExternalSerialNum() {
		return this.externalSerialNum;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public String getRequestSendDate() {
		return this.requestSendDate;
	}

	public String getRequestSendTime() {
		return this.requestSendTime;
	}

	public String getResponseArrivedDate() {
		return this.responseArrivedDate;
	}

	public String getResponseArrivedTime() {
		return this.responseArrivedTime;
	}

	public String getState() {
		return this.state;
	}

	public String getType() {
		return this.type;
	}

	public String getProcessorId() {
		return this.processorId;
	}

	public String getSourceChannelId() {
		return this.sourceChannelId;
	}

	public String getSourceChannelName() {
		return this.sourceChannelName;
	}

	public String getDestChannelId() {
		return this.destChannelId;
	}

	public String getDestChannelName() {
		return this.destChannelName;
	}

	public String getErrorType() {
		return this.errorType;
	}

}