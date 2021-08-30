package com.fib.msgconverter.commgateway.dao.serialnumber.dao;

import java.math.BigDecimal;
@SuppressWarnings("all")

public class SerialNumber{
	public SerialNumber() {

	}

	//流水号标识符
	private String snId;

	//当前号码
	private String curNum;

	//增量
	private int increment;

	//最大号码
	private String maxNum;

	//每批获取的号码数量
	private int batchSize;

	//备注
	private String memo;

	public void setSnId(String newSnId) {
		this.snId = newSnId;
	}

	public void setCurNum(String newCurNum) {
		this.curNum = newCurNum;
	}

	public void setIncrement(int newIncrement) {
		this.increment = newIncrement;
	}

	public void setMaxNum(String newMaxNum) {
		this.maxNum = newMaxNum;
	}

	public void setBatchSize(int newBatchSize) {
		this.batchSize = newBatchSize;
	}

	public void setMemo(String newMemo) {
		this.memo = newMemo;
	}

	public String getSnId() {
		return this.snId;
	}

	public String getCurNum() {
		return this.curNum;
	}

	public int getIncrement() {
		return this.increment;
	}

	public String getMaxNum() {
		return this.maxNum;
	}

	public int getBatchSize() {
		return this.batchSize;
	}

	public String getMemo() {
		return this.memo;
	}

}