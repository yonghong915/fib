package com.fib.upp.modules.payment.entity;

import java.math.BigDecimal;

import com.fib.core.base.entity.BaseEntity;

public class AcctTransRegister extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8736842481262069138L;

	private Long pkId;

	private String chanlSerialNo;

	private String drAcctNo;

	private String drAcctName;

	private String crAcctNo;

	private String crAcctName;

	private String currencyCode;

	private BigDecimal tranAmt;

	private Long orderId;

	private Long opId;

	private String sysCode;

	private String transOrgCode;

	private String billingDate;

	private String coreSerialNo;

	private String acctRegType;

	private String statusId;

	private String remark;

	public Long getPkId() {
		return pkId;
	}

	public void setPkId(Long pkId) {
		this.pkId = pkId;
	}

	public String getChanlSerialNo() {
		return chanlSerialNo;
	}

	public void setChanlSerialNo(String chanlSerialNo) {
		this.chanlSerialNo = chanlSerialNo;
	}

	public String getDrAcctNo() {
		return drAcctNo;
	}

	public void setDrAcctNo(String drAcctNo) {
		this.drAcctNo = drAcctNo;
	}

	public String getDrAcctName() {
		return drAcctName;
	}

	public void setDrAcctName(String drAcctName) {
		this.drAcctName = drAcctName;
	}

	public String getCrAcctNo() {
		return crAcctNo;
	}

	public void setCrAcctNo(String crAcctNo) {
		this.crAcctNo = crAcctNo;
	}

	public String getCrAcctName() {
		return crAcctName;
	}

	public void setCrAcctName(String crAcctName) {
		this.crAcctName = crAcctName;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public BigDecimal getTranAmt() {
		return tranAmt;
	}

	public void setTranAmt(BigDecimal tranAmt) {
		this.tranAmt = tranAmt;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOpId() {
		return opId;
	}

	public void setOpId(Long opId) {
		this.opId = opId;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getTransOrgCode() {
		return transOrgCode;
	}

	public void setTransOrgCode(String transOrgCode) {
		this.transOrgCode = transOrgCode;
	}

	public String getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(String billingDate) {
		this.billingDate = billingDate;
	}

	public String getCoreSerialNo() {
		return coreSerialNo;
	}

	public void setCoreSerialNo(String coreSerialNo) {
		this.coreSerialNo = coreSerialNo;
	}

	public String getAcctRegType() {
		return acctRegType;
	}

	public void setAcctRegType(String acctRegType) {
		this.acctRegType = acctRegType;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}