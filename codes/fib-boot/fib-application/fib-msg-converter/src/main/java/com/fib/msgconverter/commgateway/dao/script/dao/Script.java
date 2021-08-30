package com.fib.msgconverter.commgateway.dao.script.dao;


public class Script {
	public Script() {

	}

	// id
	private String id;

	// 脚本
	private byte[] script;

	public void setId(String newId) {
		id = newId;
	}

	public void setScript(byte[] newScript) {
		script = newScript;
	}

	public String getId() {
		return id;
	}

	public byte[] getScript() {
		return script;
	}

}