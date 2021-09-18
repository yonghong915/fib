package com.fib.msgconverter.commgateway.dao.businesssimulateresponsedata.dao;


public class BusinessSimulateResponseData{
	public BusinessSimulateResponseData() {

	}

	//id
	private String id;

	//报文数据
	private byte[] messageData;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setMessageData(byte[] newMessageData) {
		this.messageData = newMessageData;
	}

	public String getId() {
		return this.id;
	}

	public byte[] getMessageData() {
		return this.messageData;
	}

}