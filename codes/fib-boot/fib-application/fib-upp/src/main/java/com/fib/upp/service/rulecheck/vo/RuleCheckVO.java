package com.fib.upp.service.rulecheck.vo;

import javax.validation.constraints.NotNull;

import com.fib.core.base.vo.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RuleCheckVO extends BaseVO {
	@NotNull(message = "systemCode must be not null")
	private String systemCode;

	@NotNull(message = "transactionId must be not null")
	private String transactionId;
}
