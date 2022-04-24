package com.fib.upp.modules.payment.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fib.core.base.entity.BaseEntity;

/**
 * 报文登记实体
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-24 16:25:22
 */
public class MsgRegisterInfo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4412407560542242790L;
	@TableId
	private Long pkId;

	private String paySerialNo;

	private String sysCode;

	private String msgTypeCode;

	private String msgId;

	private String msgDirection;

	private String sysDate;

	private String nettingDate;

	private String nettingRound;

	private String processStatus;

	private BigDecimal transAmt;

	private Integer transNum;

	private BigDecimal succAmt;

	private Integer succNum;

	private String sndBankCode;

	private String rcvBankCode;

	private Long msgContentId;

	public Long getPkId() {
		return pkId;
	}

	public void setPkId(Long pkId) {
		this.pkId = pkId;
	}

	public String getPaySerialNo() {
		return paySerialNo;
	}

	public void setPaySerialNo(String paySerialNo) {
		this.paySerialNo = paySerialNo;
	}

	public String getMsgTypeCode() {
		return msgTypeCode;
	}

	public void setMsgTypeCode(String msgTypeCode) {
		this.msgTypeCode = msgTypeCode;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgDirection() {
		return msgDirection;
	}

	public void setMsgDirection(String msgDirection) {
		this.msgDirection = msgDirection;
	}

	public String getSysDate() {
		return sysDate;
	}

	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}

	public String getNettingDate() {
		return nettingDate;
	}

	public void setNettingDate(String nettingDate) {
		this.nettingDate = nettingDate;
	}

	public String getNettingRound() {
		return nettingRound;
	}

	public void setNettingRound(String nettingRound) {
		this.nettingRound = nettingRound;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public BigDecimal getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}

	public Integer getTransNum() {
		return transNum;
	}

	public void setTransNum(Integer transNum) {
		this.transNum = transNum;
	}

	public BigDecimal getSuccAmt() {
		return succAmt;
	}

	public void setSuccAmt(BigDecimal succAmt) {
		this.succAmt = succAmt;
	}

	public Integer getSuccNum() {
		return succNum;
	}

	public void setSuccNum(Integer succNum) {
		this.succNum = succNum;
	}

	public String getSndBankCode() {
		return sndBankCode;
	}

	public void setSndBankCode(String sndBankCode) {
		this.sndBankCode = sndBankCode;
	}

	public String getRcvBankCode() {
		return rcvBankCode;
	}

	public void setRcvBankCode(String rcvBankCode) {
		this.rcvBankCode = rcvBankCode;
	}

	public Long getMsgContentId() {
		return msgContentId;
	}

	public void setMsgContentId(Long msgContentId) {
		this.msgContentId = msgContentId;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
}