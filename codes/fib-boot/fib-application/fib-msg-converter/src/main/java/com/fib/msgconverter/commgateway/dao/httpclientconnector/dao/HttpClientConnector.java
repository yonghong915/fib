package com.fib.msgconverter.commgateway.dao.httpclientconnector.dao;

import java.math.BigDecimal;

public class HttpClientConnector{
	public HttpClientConnector() {

	}

	//id
	private String id;

	//服务器url
	private String url;

	//传输内容编码格式
	private String contentCharset;

	//最大处理线程数量
	private int maxHandlerNumber;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setUrl(String newUrl) {
		this.url = newUrl;
	}

	public void setContentCharset(String newContentCharset) {
		this.contentCharset = newContentCharset;
	}

	public void setMaxHandlerNumber(int newMaxHandlerNumber) {
		this.maxHandlerNumber = newMaxHandlerNumber;
	}

	public String getId() {
		return this.id;
	}

	public String getUrl() {
		return this.url;
	}

	public String getContentCharset() {
		return this.contentCharset;
	}

	public int getMaxHandlerNumber() {
		return this.maxHandlerNumber;
	}

}