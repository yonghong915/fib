package com.fib.gateway.channel.config.processor;

public class ErrorMappingConfig {
	private String soureMessageId;
	private String mappingRuleId;

	public String getSoureMessageId() {
		return soureMessageId;
	}

	public void setSoureMessageId(String soureMessageId) {
		this.soureMessageId = soureMessageId;
	}

	public String getMappingRuleId() {
		return mappingRuleId;
	}

	public void setMappingRuleId(String mappingRuleId) {
		this.mappingRuleId = mappingRuleId;
	}
}
