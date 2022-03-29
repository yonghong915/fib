package com.fib.autoconfigure.ruleengine;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.fib.autoconfigure.ruleengine.drools.DroolsAutoConfiguration;
import com.fib.autoconfigure.ruleengine.easyrules.EasyRulesAutoConfiguration;
import com.fib.autoconfigure.ruleengine.rulebook.RuleBookAutoConfiguration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ DataSource.class })
@EnableConfigurationProperties(RuleEngineProperties.class)
@AutoConfigureAfter({ DroolsAutoConfiguration.class, EasyRulesAutoConfiguration.class,
		RuleBookAutoConfiguration.class, })
public class RuleEngineAutoConfiguration {

}
