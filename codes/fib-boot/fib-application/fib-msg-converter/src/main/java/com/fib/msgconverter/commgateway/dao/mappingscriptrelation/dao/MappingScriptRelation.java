package com.fib.msgconverter.commgateway.dao.mappingscriptrelation.dao;


public class MappingScriptRelation {
	public MappingScriptRelation() {

	}

	// 映射规则ID
	private String mappingId;

	// 脚本ID
	private String scriptId;

	// 脚本执行顺序索引
	private int scriptIndex;

	public void setMappingId(String newMappingId) {
		mappingId = newMappingId;
	}

	public void setScriptId(String newScriptId) {
		scriptId = newScriptId;
	}

	public void setScriptIndex(int newScriptIndex) {
		scriptIndex = newScriptIndex;
	}

	public String getMappingId() {
		return mappingId;
	}

	public String getScriptId() {
		return scriptId;
	}

	public int getScriptIndex() {
		return scriptIndex;
	}

}