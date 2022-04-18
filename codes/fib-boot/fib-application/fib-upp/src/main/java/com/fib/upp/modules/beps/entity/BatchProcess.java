package com.fib.upp.modules.beps.entity;

import java.math.BigDecimal;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-02-24
 */
public class BatchProcess {
	private String batchGroupId;
	private String batchId;
	private String batchType;
	private String processStatus;

	private String endDateTime;

	private Integer transNum;
	private BigDecimal transAmt;

	private Integer succNum;
	private BigDecimal succAmt;

	private Integer failNum;
	private BigDecimal failAmt;

	private String chanlSerialNo;

	private String bizType;

	private String sndClearBankCode;

	private String rcvClearBankCode;

	public String getBatchGroupId() {
		return batchGroupId;
	}

	public void setBatchGroupId(String batchGroupId) {
		this.batchGroupId = batchGroupId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getBatchType() {
		return batchType;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
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

	public Integer getSuccNum() {
		return succNum;
	}

	public void setSuccNum(Integer succNum) {
		this.succNum = succNum;
	}

	public BigDecimal getSuccAmt() {
		return succAmt;
	}

	public void setSuccAmt(BigDecimal succAmt) {
		this.succAmt = succAmt;
	}

	public Integer getFailNum() {
		return failNum;
	}

	public void setFailNum(Integer failNum) {
		this.failNum = failNum;
	}

	public BigDecimal getFailAmt() {
		return failAmt;
	}

	public void setFailAmt(BigDecimal failAmt) {
		this.failAmt = failAmt;
	}

	public String getChanlSerialNo() {
		return chanlSerialNo;
	}

	public void setChanlSerialNo(String chanlSerialNo) {
		this.chanlSerialNo = chanlSerialNo;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getSndClearBankCode() {
		return sndClearBankCode;
	}

	public void setSndClearBankCode(String sndClearBankCode) {
		this.sndClearBankCode = sndClearBankCode;
	}

	public String getRcvClearBankCode() {
		return rcvClearBankCode;
	}

	public void setRcvClearBankCode(String rcvClearBankCode) {
		this.rcvClearBankCode = rcvClearBankCode;
	}
}