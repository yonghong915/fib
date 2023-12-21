package com.fib.autoconfigure.ruleengine.rulebook;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.deliveredtechnologies.rulebook.annotation.Rule;
import com.fib.autoconfigure.ruleengine.IRuleService;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Rule.class)
public class RuleBookAutoConfiguration {

    @Bean("ruleService")
    @ConditionalOnMissingBean(IRuleService.class)
    IRuleService getRuleService() {
		return new RuleBookRuleService();
	}
}
