package com.fib.msgconverter.commgateway.dao.cronrule.dao;

import java.math.BigDecimal;

public class CronRule{
	public CronRule() {

	}

	//Id
	private String id;

	//任务Id
	private String jobId;

	//状态
	private String state;

	//表达式
	private String expression;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setJobId(String newJobId) {
		this.jobId = newJobId;
	}

	public void setState(String newState) {
		this.state = newState;
	}

	public void setExpression(String newExpression) {
		this.expression = newExpression;
	}

	public String getId() {
		return this.id;
	}

	public String getJobId() {
		return this.jobId;
	}

	public String getState() {
		return this.state;
	}

	public String getExpression() {
		return this.expression;
	}

}