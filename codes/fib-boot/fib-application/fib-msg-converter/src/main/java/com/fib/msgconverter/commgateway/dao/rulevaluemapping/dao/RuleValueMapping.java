package com.fib.msgconverter.commgateway.dao.rulevaluemapping.dao;


public class RuleValueMapping {
	public RuleValueMapping() {

	}

	// id
	private String id;

	// 源属性名称
	private String attrFrom;

	public void setId(String newId) {
		id = newId;
	}

	public void setAttrFrom(String newAttrFrom) {
		attrFrom = newAttrFrom;
	}

	public String getId() {
		return id;
	}

	public String getAttrFrom() {
		return attrFrom;
	}

}