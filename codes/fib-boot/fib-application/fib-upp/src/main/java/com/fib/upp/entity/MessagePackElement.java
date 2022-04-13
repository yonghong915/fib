package com.fib.upp.entity;

public class MessagePackElement {
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
		if (sendClearingBank != null)
			this.sendClearingBank = sendClearingBank;
		else
			this.sendClearingBank = "SendBank_";
	}

	public String getReceiveClearingBank() {
		return receiveClearingBank;
	}

	public void setReceiveClearingBank(String receiveClearingBank) {
		if (receiveClearingBank != null)
			this.receiveClearingBank = receiveClearingBank;
		else
			this.receiveClearingBank = "ReceBank_";
	}

	public String getReturnLimited() {
		return returnLimited;
	}

	public void setReturnLimited(String returnLimited) {
		if (returnLimited != null)
			this.returnLimited = returnLimited;
		else
			this.returnLimited = "RetLimit_";
	}

	public String getOriginalMessageId() {
		return originalMessageId;
	}

	public void setOriginalMessageId(String originalMessageId) {
		if (originalMessageId != null)
			this.originalMessageId = originalMessageId;
		else
			this.originalMessageId = "OrigiMsg_";
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
