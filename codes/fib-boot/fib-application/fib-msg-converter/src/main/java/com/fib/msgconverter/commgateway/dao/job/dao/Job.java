package com.fib.msgconverter.commgateway.dao.job.dao;

import java.math.BigDecimal;
@SuppressWarnings("all")

public class Job{
	public Job() {

	}

	//任务Id
	private String id;

	//关联日志Id
	private String logId;

	//任务类型
	private String type;

	//状态
	private String state;

	//任务添加日期
	private String startDate;

	//任务添加时间
	private String startTime;

	//分行代码
	private String branchCode;

	//应用系统分类代码
	private String appCategory;

	//调度类型
	private String scheduleType;

	//当前执行次数
	private int currentTimes;

	//时间间隔
	private long jobInterval;

	//最大重复次数
	private int maxTimes;

	//任务实现类名
	private String jobClassName;

	//处理器id
	private String processId;

	//请求报文来源
	private int requestMessageFrom;

	//调度结束条件
	private String scheduleEndFlag;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setLogId(String newLogId) {
		this.logId = newLogId;
	}

	public void setType(String newType) {
		this.type = newType;
	}

	public void setState(String newState) {
		this.state = newState;
	}

	public void setStartDate(String newStartDate) {
		this.startDate = newStartDate;
	}

	public void setStartTime(String newStartTime) {
		this.startTime = newStartTime;
	}

	public void setBranchCode(String newBranchCode) {
		this.branchCode = newBranchCode;
	}

	public void setAppCategory(String newAppCategory) {
		this.appCategory = newAppCategory;
	}

	public void setScheduleType(String newScheduleType) {
		this.scheduleType = newScheduleType;
	}

	public void setCurrentTimes(int newCurrentTimes) {
		this.currentTimes = newCurrentTimes;
	}

	public void setJobInterval(long newJobInterval) {
		this.jobInterval = newJobInterval;
	}

	public void setMaxTimes(int newMaxTimes) {
		this.maxTimes = newMaxTimes;
	}

	public void setJobClassName(String newJobClassName) {
		this.jobClassName = newJobClassName;
	}

	public void setProcessId(String newProcessId) {
		this.processId = newProcessId;
	}

	public void setRequestMessageFrom(int newRequestMessageFrom) {
		this.requestMessageFrom = newRequestMessageFrom;
	}

	public void setScheduleEndFlag(String newScheduleEndFlag) {
		this.scheduleEndFlag = newScheduleEndFlag;
	}

	public String getId() {
		return this.id;
	}

	public String getLogId() {
		return this.logId;
	}

	public String getType() {
		return this.type;
	}

	public String getState() {
		return this.state;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public String getBranchCode() {
		return this.branchCode;
	}

	public String getAppCategory() {
		return this.appCategory;
	}

	public String getScheduleType() {
		return this.scheduleType;
	}

	public int getCurrentTimes() {
		return this.currentTimes;
	}

	public long getJobInterval() {
		return this.jobInterval;
	}

	public int getMaxTimes() {
		return this.maxTimes;
	}

	public String getJobClassName() {
		return this.jobClassName;
	}

	public String getProcessId() {
		return this.processId;
	}

	public int getRequestMessageFrom() {
		return this.requestMessageFrom;
	}

	public String getScheduleEndFlag() {
		return this.scheduleEndFlag;
	}

}