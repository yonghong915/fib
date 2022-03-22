package com.fib.upp.entity;

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
	private String payeeAccNo;
	private String payeeAccName;
	private String payeeAddress;
	private String payeeBankCode;
	private String payeeBankName;

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

	public String getPayeeAccNo() {
		return payeeAccNo;
	}

	public void setPayeeAccNo(String payeeAccNo) {
		this.payeeAccNo = payeeAccNo;
	}

	public String getPayeeAccName() {
		return payeeAccName;
	}

	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
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
}
