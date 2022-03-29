package com.fib.autoconfigure.ruleengine.drools;

import com.fib.autoconfigure.ruleengine.IRuleService;

public class DroolsRuleService implements IRuleService {

	@Override
	public void execute() {
		LOGGER.info("DroolsRuleService--->execute");
	}
}
