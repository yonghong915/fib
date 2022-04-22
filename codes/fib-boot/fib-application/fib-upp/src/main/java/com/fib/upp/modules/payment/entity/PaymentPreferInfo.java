package com.fib.upp.modules.payment.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fib.core.base.entity.BaseEntity;

/**
 * 支付相关信息
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-22 14:53:17
 */
public class PaymentPreferInfo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private Long paymentPreferId;
	private BigInteger orderId;
	private String paymentMethodTypeId;
	private String paymentMethodId;
	private BigDecimal transAmt;
	private String paymentStatus;
	private String acctNo;
	private String acctName;
	private String acctType;
	private String sysCode;
	private String bankCode;
	private String bankName;
	private String tradeOrgId;
	private String dcFlag;
	private String routeId;
	/** 每批次序号 */
	private String itemSeqId;
	/** 客户附言 */
	private String custPostscript;

	/** 银行附言 */
	private String bankPostscript;

	public Long getPaymentPreferId() {
		return paymentPreferId;
	}

	public void setPaymentPreferId(Long paymentPreferId) {
		this.paymentPreferId = paymentPreferId;
	}

	public BigInteger getOrderId() {
		return orderId;
	}

	public void setOrderId(BigInteger orderId) {
		this.orderId = orderId;
	}

	public String getPaymentMethodTypeId() {
		return paymentMethodTypeId;
	}

	public void setPaymentMethodTypeId(String paymentMethodTypeId) {
		this.paymentMethodTypeId = paymentMethodTypeId;
	}

	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public BigDecimal getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getAcctName() {
		return acctName;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getTradeOrgId() {
		return tradeOrgId;
	}

	public void setTradeOrgId(String tradeOrgId) {
		this.tradeOrgId = tradeOrgId;
	}

	public String getDcFlag() {
		return dcFlag;
	}

	public void setDcFlag(String dcFlag) {
		this.dcFlag = dcFlag;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getItemSeqId() {
		return itemSeqId;
	}

	public void setItemSeqId(String itemSeqId) {
		this.itemSeqId = itemSeqId;
	}

	public String getCustPostscript() {
		return custPostscript;
	}

	public void setCustPostscript(String custPostscript) {
		this.custPostscript = custPostscript;
	}

	public String getBankPostscript() {
		return bankPostscript;
	}

	public void setBankPostscript(String bankPostscript) {
		this.bankPostscript = bankPostscript;
	}
}