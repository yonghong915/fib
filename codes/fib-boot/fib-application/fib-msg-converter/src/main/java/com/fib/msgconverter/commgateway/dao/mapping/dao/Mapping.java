package com.fib.msgconverter.commgateway.dao.mapping.dao;


public class Mapping {
	public Mapping() {

	}

	// id
	private String id;

	// 映射ID
	private String mappingId;

	// 映射组ID
	private String mappingGroupId;

	// 目的数据类型
	private String targetType;

	// 目的数据对象类
	private String targetBeanClass;

	// 是否存在手动脚本映射
	private String hasScript;

	public void setId(String newId) {
		id = newId;
	}

	public void setMappingId(String newMappingId) {
		mappingId = newMappingId;
	}

	public void setMappingGroupId(String newMappingGroupId) {
		mappingGroupId = newMappingGroupId;
	}

	public void setTargetType(String newTargetType) {
		targetType = newTargetType;
	}

	public void setTargetBeanClass(String newTargetBeanClass) {
		targetBeanClass = newTargetBeanClass;
	}

	public void setHasScript(String newHasScript) {
		hasScript = newHasScript;
	}

	public String getId() {
		return id;
	}

	public String getMappingId() {
		return mappingId;
	}

	public String getMappingGroupId() {
		return mappingGroupId;
	}

	public String getTargetType() {
		return targetType;
	}

	public String getTargetBeanClass() {
		return targetBeanClass;
	}

	public String getHasScript() {
		return hasScript;
	}

}