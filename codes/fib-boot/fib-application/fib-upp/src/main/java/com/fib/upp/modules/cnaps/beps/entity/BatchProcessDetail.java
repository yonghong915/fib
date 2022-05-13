package com.fib.upp.modules.cnaps.beps.entity;

import java.math.BigDecimal;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-02-24
 */
public class BatchProcessDetail {
	private String batchId;
	private String batchSeqId;

	private String payeeAcctNo;
	private String payeeAcctName;
	private String payeeAcctType;
	private String payeeAddress;
	private String payeeBankCode;
	private String payeeBankName;
	private String payeeClearBankCode;
	private String payeeClearBankName;

	private String draweeAcctNo;
	private String draweeAcctName;
	private String draweeAcctType;
	private String draweeAddress;
	private String draweeBankCode;
	private String draweeBankName;
	private String draweeClearBankCode;
	private String draweeClearBankName;

	private String currencyType;

	private BigDecimal transAmt;
	private String sysCode;
	private String bizType;
	private String bizClass;

	private String orderId;
	private String oppId;
	private String rspCode;
	private String rspMsg;
	private String custPostscript;

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getBatchSeqId() {
		return batchSeqId;
	}

	public void setBatchSeqId(String batchSeqId) {
		this.batchSeqId = batchSeqId;
	}

	public String getPayeeAcctNo() {
		return payeeAcctNo;
	}

	public void setPayeeAcctNo(String payeeAcctNo) {
		this.payeeAcctNo = payeeAcctNo;
	}

	public String getPayeeAcctName() {
		return payeeAcctName;
	}

	public void setPayeeAcctName(String payeeAcctName) {
		this.payeeAcctName = payeeAcctName;
	}

	public String getPayeeAcctType() {
		return payeeAcctType;
	}

	public void setPayeeAcctType(String payeeAcctType) {
		this.payeeAcctType = payeeAcctType;
	}

	public String getPayeeAddress() {
		return payeeAddress;
	}

	public void setPayeeAddress(String payeeAddress) {
		this.payeeAddress = payeeAddress;
	}

	public String getPayeeBankCode() {
		return payeeBankCode;
	}

	public void setPayeeBankCode(String payeeBankCode) {
		this.payeeBankCode = payeeBankCode;
	}

	public String getPayeeBankName() {
		return payeeBankName;
	}

	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}

	public String getPayeeClearBankCode() {
		return payeeClearBankCode;
	}

	public void setPayeeClearBankCode(String payeeClearBankCode) {
		this.payeeClearBankCode = payeeClearBankCode;
	}

	public String getPayeeClearBankName() {
		return payeeClearBankName;
	}

	public void setPayeeClearBankName(String payeeClearBankName) {
		this.payeeClearBankName = payeeClearBankName;
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

	public String getDraweeAcctType() {
		return draweeAcctType;
	}

	public void setDraweeAcctType(String draweeAcctType) {
		this.draweeAcctType = draweeAcctType;
	}

	public String getDraweeAddress() {
		return draweeAddress;
	}

	public void setDraweeAddress(String draweeAddress) {
		this.draweeAddress = draweeAddress;
	}

	public String getDraweeBankCode() {
		return draweeBankCode;
	}

	public void setDraweeBankCode(String draweeBankCode) {
		this.draweeBankCode = draweeBankCode;
	}

	public String getDraweeBankName() {
		return draweeBankName;
	}

	public void setDraweeBankName(String draweeBankName) {
		this.draweeBankName = draweeBankName;
	}

	public String getDraweeClearBankCode() {
		return draweeClearBankCode;
	}

	public void setDraweeClearBankCode(String draweeClearBankCode) {
		this.draweeClearBankCode = draweeClearBankCode;
	}

	public String getDraweeClearBankName() {
		return draweeClearBankName;
	}

	public void setDraweeClearBankName(String draweeClearBankName) {
		this.draweeClearBankName = draweeClearBankName;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getBizClass() {
		return bizClass;
	}

	public void setBizClass(String bizClass) {
		this.bizClass = bizClass;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOppId() {
		return oppId;
	}

	public void setOppId(String oppId) {
		this.oppId = oppId;
	}

	public String getRspCode() {
		return rspCode;
	}

	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}

	public String getCustPostscript() {
		return custPostscript;
	}

	public void setCustPostscript(String custPostscript) {
		this.custPostscript = custPostscript;
	}
}
