package com.fib.msgconverter.commgateway.dao.joblog.dao;

import java.math.BigDecimal;
@SuppressWarnings("all")

public class JobLog{
	public JobLog() {

	}

	//任务Id
	private String jobId;

	//Id
	private String id;

	//执行日期
	private String executeDate;

	//执行时间
	private String executeTime;

	//执行状态
	private String state;

	//日志ID
	private String logId;

	public void setJobId(String newJobId) {
		this.jobId = newJobId;
	}

	public void setId(String newId) {
		this.id = newId;
	}

	public void setExecuteDate(String newExecuteDate) {
		this.executeDate = newExecuteDate;
	}

	public void setExecuteTime(String newExecuteTime) {
		this.executeTime = newExecuteTime;
	}

	public void setState(String newState) {
		this.state = newState;
	}

	public void setLogId(String newLogId) {
		this.logId = newLogId;
	}

	public String getJobId() {
		return this.jobId;
	}

	public String getId() {
		return this.id;
	}

	public String getExecuteDate() {
		return this.executeDate;
	}

	public String getExecuteTime() {
		return this.executeTime;
	}

	public String getState() {
		return this.state;
	}

	public String getLogId() {
		return this.logId;
	}

}