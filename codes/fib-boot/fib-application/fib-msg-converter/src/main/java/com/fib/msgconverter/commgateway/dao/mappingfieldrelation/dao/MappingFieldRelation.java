package com.fib.msgconverter.commgateway.dao.mappingfieldrelation.dao;


public class MappingFieldRelation {
	public MappingFieldRelation() {

	}

	// 映射ID
	private String mappingId;

	// 映射ID
	private String fieldMappingRuleId;

	public void setMappingId(String newMappingId) {
		mappingId = newMappingId;
	}

	public void setFieldMappingRuleId(String newFieldMappingRuleId) {
		fieldMappingRuleId = newFieldMappingRuleId;
	}

	public String getMappingId() {
		return mappingId;
	}

	public String getFieldMappingRuleId() {
		return fieldMappingRuleId;
	}

}