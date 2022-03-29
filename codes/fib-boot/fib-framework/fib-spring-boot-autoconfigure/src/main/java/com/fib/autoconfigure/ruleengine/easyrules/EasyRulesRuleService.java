package com.fib.autoconfigure.ruleengine.easyrules;

import com.fib.autoconfigure.ruleengine.IRuleService;

public class EasyRulesRuleService implements IRuleService {

	@Override
	public void execute() {
		LOGGER.info("EasyRulesRuleService--->execute");
	}

}
