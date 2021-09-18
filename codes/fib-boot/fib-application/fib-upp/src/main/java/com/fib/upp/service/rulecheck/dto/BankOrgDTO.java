package com.fib.upp.service.rulecheck.dto;

import com.fib.core.base.dto.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class BankOrgDTO extends BaseDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8140052331727569347L;

	private String systemCode;

	private String transactionId;
}