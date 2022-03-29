package com.fib.autoconfigure.ruleengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 规则服务
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-29 16:58:08
 */
public interface IRuleService {
	static final Logger LOGGER = LoggerFactory.getLogger(IRuleService.class);

	void execute();
}
