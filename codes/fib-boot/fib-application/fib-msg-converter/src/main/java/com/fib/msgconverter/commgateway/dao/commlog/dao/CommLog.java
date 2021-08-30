package com.fib.msgconverter.commgateway.dao.commlog.dao;

import java.math.BigDecimal;

public class CommLog {
	public CommLog() {

	}

	// ID
	private String id;

	// 所属网关ID
	private String gatewayId;

	// 外部流水号
	// private String externalSerialNum;

	// 开始日期
	private String startDate;

	// 开始时间
	private String startTime;

	// 结束日期
	private String endDate;

	// 结束时间
	private String endTime;

	// 状态
	private String state;

	// 类型
	private String type;

	// 分行代码
	private String branchCode;

	// 应用分类代码
	private String appCategory;

	// 处理器Id
	private String processorId;

	// 源通道ID
	private String sourceChannelId;

	// 源通道名称
	private String sourceChannelName;

	// 目的通道ID
	private String destChannelId;

	// 目的通道名称
	private String destChannelName;

	// 错误类型
	private String errorType;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setGatewayId(String newGatewayId) {
		this.gatewayId = newGatewayId;
	}

	// public void setExternalSerialNum(String newExternalSerialNum) {
	// this.externalSerialNum = newExternalSerialNum;
	// }

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

	public void setState(String newState) {
		this.state = newState;
	}

	public void setType(String newType) {
		this.type = newType;
	}

	public void setBranchCode(String newBranchCode) {
		this.branchCode = newBranchCode;
	}

	public void setAppCategory(String newAppCategory) {
		this.appCategory = newAppCategory;
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

	// public String getExternalSerialNum() {
	// return this.externalSerialNum;
	// }

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

	public String getState() {
		return this.state;
	}

	public String getType() {
		return this.type;
	}

	public String getBranchCode() {
		return this.branchCode;
	}

	public String getAppCategory() {
		return this.appCategory;
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