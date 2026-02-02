package com.fib.midbiz.taxbank.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("bp_agreement")
public class BpAgreementEntity extends BaseEntity {
	private static final long serialVersionUID = 6461944434998706770L;

	/** 机构号 */
	private String orgCode;

	/** 协议号 */
	private String protocolNum;

	private String name;
	private String documentType;
	private String documentNum;
	private String protocolType;
	private Date signDate;
	private Date cancelDate;
	private Date toValidity;
	private String debitAcctNo;
	private String debitAcctName;
	private String creditAcctNo;
	private String creditAcctName;
	private String mobile;
	private String payType;
	private BigDecimal singleLimit;
	private BigDecimal dayLimit;
	private BigDecimal monthLimit;
	private String remark;
	private String signStatus;
	private String scCtrctEntityType;
	private String scCtrctEntityNum;
	private String custCtrctCodeFw;
	private String custCtrctCodeGw;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getProtocolNum() {
		return protocolNum;
	}

	public void setProtocolNum(String protocolNum) {
		this.protocolNum = protocolNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNum() {
		return documentNum;
	}

	public void setDocumentNum(String documentNum) {
		this.documentNum = documentNum;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Date getToValidity() {
		return toValidity;
	}

	public void setToValidity(Date toValidity) {
		this.toValidity = toValidity;
	}

	public String getDebitAcctNo() {
		return debitAcctNo;
	}

	public void setDebitAcctNo(String debitAcctNo) {
		this.debitAcctNo = debitAcctNo;
	}

	public String getDebitAcctName() {
		return debitAcctName;
	}

	public void setDebitAcctName(String debitAcctName) {
		this.debitAcctName = debitAcctName;
	}

	public String getCreditAcctNo() {
		return creditAcctNo;
	}

	public void setCreditAcctNo(String creditAcctNo) {
		this.creditAcctNo = creditAcctNo;
	}

	public String getCreditAcctName() {
		return creditAcctName;
	}

	public void setCreditAcctName(String creditAcctName) {
		this.creditAcctName = creditAcctName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public BigDecimal getSingleLimit() {
		return singleLimit;
	}

	public void setSingleLimit(BigDecimal singleLimit) {
		this.singleLimit = singleLimit;
	}

	public BigDecimal getDayLimit() {
		return dayLimit;
	}

	public void setDayLimit(BigDecimal dayLimit) {
		this.dayLimit = dayLimit;
	}

	public BigDecimal getMonthLimit() {
		return monthLimit;
	}

	public void setMonthLimit(BigDecimal monthLimit) {
		this.monthLimit = monthLimit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}

	public String getScCtrctEntityType() {
		return scCtrctEntityType;
	}

	public void setScCtrctEntityType(String scCtrctEntityType) {
		this.scCtrctEntityType = scCtrctEntityType;
	}

	public String getScCtrctEntityNum() {
		return scCtrctEntityNum;
	}

	public void setScCtrctEntityNum(String scCtrctEntityNum) {
		this.scCtrctEntityNum = scCtrctEntityNum;
	}

	public String getCustCtrctCodeFw() {
		return custCtrctCodeFw;
	}

	public void setCustCtrctCodeFw(String custCtrctCodeFw) {
		this.custCtrctCodeFw = custCtrctCodeFw;
	}

	public String getCustCtrctCodeGw() {
		return custCtrctCodeGw;
	}

	public void setCustCtrctCodeGw(String custCtrctCodeGw) {
		this.custCtrctCodeGw = custCtrctCodeGw;
	}
}
