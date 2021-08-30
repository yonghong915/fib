package com.fib.msgconverter.commgateway.dao.valuetranslaterulerelation.dao;


public class ValueTranslateRuleRelation {
	public ValueTranslateRuleRelation() {

	}

	// 值转换映射规则组ID
	private String groupId;

	// 值转换映射规则ID
	private String ruleId;

	public void setGroupId(String newGroupId) {
		groupId = newGroupId;
	}

	public void setRuleId(String newRuleId) {
		ruleId = newRuleId;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getRuleId() {
		return ruleId;
	}

}