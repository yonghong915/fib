package com.fib.msgconverter.commgateway.dao.fieldmappingrule.dao;


public class FieldMappingRule {
	public FieldMappingRule() {

	}

	// id
	private String id;

	// 类型
	private String mappingType;

	// 目的属性名称
	private String attrTo;

	// 是否强制类型转换
	private String forceTypeConversion;

	// 目标字段数据类型
	private String targetDataType;

	public void setId(String newId) {
		id = newId;
	}

	public void setMappingType(String newMappingType) {
		mappingType = newMappingType;
	}

	public void setAttrTo(String newAttrTo) {
		attrTo = newAttrTo;
	}

	public void setForceTypeConversion(String newForceTypeConversion) {
		forceTypeConversion = newForceTypeConversion;
	}

	public void setTargetDataType(String newTargetDataType) {
		targetDataType = newTargetDataType;
	}

	public String getId() {
		return id;
	}

	public String getMappingType() {
		return mappingType;
	}

	public String getAttrTo() {
		return attrTo;
	}

	public String getForceTypeConversion() {
		return forceTypeConversion;
	}

	public String getTargetDataType() {
		return targetDataType;
	}

}