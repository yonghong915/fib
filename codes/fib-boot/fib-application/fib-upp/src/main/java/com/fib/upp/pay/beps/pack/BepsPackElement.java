package com.fib.upp.pay.beps.pack;

import lombok.Data;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-25
 */
@Data
public class BepsPackElement {
	/*
	 * 业务类型
	 */
	private String transactionType;
	/*
	 * 发起清算行
	 */
	private String sendClearingBank;
	/*
	 * 接收清算行
	 */
	private String receiveClearingBank;
	/*
	 * 相同的回执期
	 */
	private String returnLimited;
	/*
	 * 原报文标识号
	 */
	private String originalMessageId;
	/*
	 * 批次号
	 */
	private String batchId;

	/*
	 * 包序号
	 */
	private int packNO = 0;

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getSendClearingBank() {
		return sendClearingBank;
	}

	public void setSendClearingBank(String sendClearingBank) {
		this.sendClearingBank = sendClearingBank;
	}

	public String getReceiveClearingBank() {
		return receiveClearingBank;
	}

	public void setReceiveClearingBank(String receiveClearingBank) {
		this.receiveClearingBank = receiveClearingBank;
	}

	public String getReturnLimited() {
		return returnLimited;
	}

	public void setReturnLimited(String returnLimited) {
		this.returnLimited = returnLimited;
	}

	public String getOriginalMessageId() {
		return originalMessageId;
	}

	public void setOriginalMessageId(String originalMessageId) {
		this.originalMessageId = originalMessageId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public int getPackNO() {
		return packNO;
	}

	public void setPackNO(int packNO) {
		this.packNO = packNO;
	}
}
