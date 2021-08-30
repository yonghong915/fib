package com.fib.msgconverter.commgateway.dao.transformer.dao;

import java.math.BigDecimal;

public class Transformer{
	public Transformer() {

	}

	//id
	private String id;

	//源MessageID
	private String sourceMessageId;

	//映射ID
	private String beanMapping;

	//目的MessageID
	private String destMessageId;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setSourceMessageId(String newSourceMessageId) {
		this.sourceMessageId = newSourceMessageId;
	}

	public void setBeanMapping(String newBeanMapping) {
		this.beanMapping = newBeanMapping;
	}

	public void setDestMessageId(String newDestMessageId) {
		this.destMessageId = newDestMessageId;
	}

	public String getId() {
		return this.id;
	}

	public String getSourceMessageId() {
		return this.sourceMessageId;
	}

	public String getBeanMapping() {
		return this.beanMapping;
	}

	public String getDestMessageId() {
		return this.destMessageId;
	}

}