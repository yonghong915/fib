package com.fib.autoconfigure.ruleengine.rulebook;

import com.fib.autoconfigure.ruleengine.IRuleService;

public class RuleBookRuleService implements IRuleService {
	@Override
	public void execute() {
		LOGGER.info("RuleBookRuleService--->execute");
	}
}
