package com.fib.upp.modules.cnaps.beps.entity;

import java.math.BigDecimal;

public class BatchProcessGroup {
	private String batchGroupId;
	private Integer transNum;
	private BigDecimal transAmt;
	private String draweeAcctNo;
	private String draweeAcctName;
	private String bizType;
	private String batchType;
	private String partyId;
	private String tellerId;
	private String transId;

	public String getBatchGroupId() {
		return batchGroupId;
	}

	public void setBatchGroupId(String batchGroupId) {
		this.batchGroupId = batchGroupId;
	}

	public Integer getTransNum() {
		return transNum;
	}

	public void setTransNum(Integer transNum) {
		this.transNum = transNum;
	}

	public BigDecimal getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}

	public String getDraweeAcctNo() {
		return draweeAcctNo;
	}

	public void setDraweeAcctNo(String draweeAcctNo) {
		this.draweeAcctNo = draweeAcctNo;
	}

	public String getDraweeAcctName() {
		return draweeAcctName;
	}

	public void setDraweeAcctName(String draweeAcctName) {
		this.draweeAcctName = draweeAcctName;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getBatchType() {
		return batchType;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getTellerId() {
		return tellerId;
	}

	public void setTellerId(String tellerId) {
		this.tellerId = tellerId;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}
}