package com.fib.upp.service.rulecheck.dto;

import com.fib.core.base.dto.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RuleCheckDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7635520964772752781L;

	private String sysCode;
	private String transactionId;

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
