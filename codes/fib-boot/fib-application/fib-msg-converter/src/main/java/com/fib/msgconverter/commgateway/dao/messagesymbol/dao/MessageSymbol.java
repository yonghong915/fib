package com.fib.msgconverter.commgateway.dao.messagesymbol.dao;

import java.math.BigDecimal;

public class MessageSymbol{
	public MessageSymbol() {

	}

	//id
	private String id;

	//报文符号
	private String messageSymbolId;

	//报文符号类型
	private String symbolType;

	//数据类型
	private String dataType;

	//值或脚本
	private byte[] valueOrScript;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setMessageSymbolId(String newMessageSymbolId) {
		this.messageSymbolId = newMessageSymbolId;
	}

	public void setSymbolType(String newSymbolType) {
		this.symbolType = newSymbolType;
	}

	public void setDataType(String newDataType) {
		this.dataType = newDataType;
	}

	public void setValueOrScript(byte[] newValueOrScript) {
		this.valueOrScript = newValueOrScript;
	}

	public String getId() {
		return this.id;
	}

	public String getMessageSymbolId() {
		return this.messageSymbolId;
	}

	public String getSymbolType() {
		return this.symbolType;
	}

	public String getDataType() {
		return this.dataType;
	}

	public byte[] getValueOrScript() {
		return this.valueOrScript;
	}

}