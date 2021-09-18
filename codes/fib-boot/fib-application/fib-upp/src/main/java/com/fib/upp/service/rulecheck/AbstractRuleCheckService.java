package com.fib.upp.service.rulecheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.core.base.dto.BaseDTO;

public abstract class AbstractRuleCheckService<T extends BaseDTO> implements IRuleCheckService<T> {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
}
