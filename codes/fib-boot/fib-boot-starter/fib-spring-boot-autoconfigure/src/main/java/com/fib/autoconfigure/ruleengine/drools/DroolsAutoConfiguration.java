package com.fib.autoconfigure.ruleengine.drools;

import org.drools.model.Rule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fib.autoconfigure.ruleengine.IRuleService;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Rule.class)
public class DroolsAutoConfiguration {

	@Bean("ruleService")
	@ConditionalOnMissingBean(IRuleService.class)
	IRuleService getRuleService() {
		return new DroolsRuleService();
	}
}
