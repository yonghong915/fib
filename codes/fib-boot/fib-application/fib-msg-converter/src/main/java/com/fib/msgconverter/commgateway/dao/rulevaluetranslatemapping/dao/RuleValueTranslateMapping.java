package com.fib.msgconverter.commgateway.dao.rulevaluetranslatemapping.dao;


public class RuleValueTranslateMapping {
	public RuleValueTranslateMapping() {

	}

	// id
	private String id;

	// 源属性名称
	private String attrFrom;

	// 值转换映射规则组ID
	private String valueTranslateRuleGroupId;

	public void setId(String newId) {
		id = newId;
	}

	public void setAttrFrom(String newAttrFrom) {
		attrFrom = newAttrFrom;
	}

	public void setValueTranslateRuleGroupId(String newValueTranslateRuleGroupId) {
		valueTranslateRuleGroupId = newValueTranslateRuleGroupId;
	}

	public String getId() {
		return id;
	}

	public String getAttrFrom() {
		return attrFrom;
	}

	public String getValueTranslateRuleGroupId() {
		return valueTranslateRuleGroupId;
	}

}