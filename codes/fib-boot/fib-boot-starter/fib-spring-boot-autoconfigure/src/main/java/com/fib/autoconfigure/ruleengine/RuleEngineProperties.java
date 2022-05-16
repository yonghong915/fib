package com.fib.autoconfigure.ruleengine;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.fib.autoconfigure.util.PrefixUtil;

@Configuration
@ConfigurationProperties(prefix = PrefixUtil.RULE_PREFIX)
@ConditionalOnProperty(prefix = PrefixUtil.RULE_PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class RuleEngineProperties {
	private RuleEngineType type;

	private Drools drools;

	private EasyRules easyRules;

	private RuleBook ruleBook;

	public RuleEngineType getType() {
		return type;
	}

	public void setType(RuleEngineType type) {
		this.type = type;
	}

	public static class Drools {
		private String ruleName;

		public String getRuleName() {
			return ruleName;
		}

		public void setRuleName(String ruleName) {
			this.ruleName = ruleName;
		}

	}

	public static class EasyRules {
		private String ruleName;

		public String getRuleName() {
			return ruleName;
		}

		public void setRuleName(String ruleName) {
			this.ruleName = ruleName;
		}
	}

	public static class RuleBook {
		private String ruleName;

		public String getRuleName() {
			return ruleName;
		}

		public void setRuleName(String ruleName) {
			this.ruleName = ruleName;
		}
	}

	public Drools getDrools() {
		return drools;
	}

	public void setDrools(Drools drools) {
		this.drools = drools;
	}

	public EasyRules getEasyRules() {
		return easyRules;
	}

	public void setEasyRules(EasyRules easyRules) {
		this.easyRules = easyRules;
	}

	public RuleBook getRuleBook() {
		return ruleBook;
	}

	public void setRuleBook(RuleBook ruleBook) {
		this.ruleBook = ruleBook;
	}
}
