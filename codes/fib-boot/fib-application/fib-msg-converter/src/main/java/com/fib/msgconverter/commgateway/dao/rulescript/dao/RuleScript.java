package com.fib.msgconverter.commgateway.dao.rulescript.dao;


public class RuleScript {
	public RuleScript() {

	}

	// id
	private String id;

	// 源属性名称
	private String attrFrom;

	// 脚本
	private byte[] script;

	public void setId(String newId) {
		id = newId;
	}

	public void setAttrFrom(String newAttrFrom) {
		attrFrom = newAttrFrom;
	}

	public void setScript(byte[] newScript) {
		script = newScript;
	}

	public String getId() {
		return id;
	}

	public String getAttrFrom() {
		return attrFrom;
	}

	public byte[] getScript() {
		return script;
	}

}