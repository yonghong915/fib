package com.fib.msgconverter.commgateway.dao.mqconnectorfilterrelation.dao;

import java.math.BigDecimal;

public class MqConnectorFilterRelation{
	public MqConnectorFilterRelation() {

	}

	//MQ连接器ID
	private String mqConnectorId;

	//过滤器ID
	private String filterId;

	//读取器过滤器或写入器过滤器
	private String readerOrWriterFilter;

	public void setMqConnectorId(String newMqConnectorId) {
		this.mqConnectorId = newMqConnectorId;
	}

	public void setFilterId(String newFilterId) {
		this.filterId = newFilterId;
	}

	public void setReaderOrWriterFilter(String newReaderOrWriterFilter) {
		this.readerOrWriterFilter = newReaderOrWriterFilter;
	}

	public String getMqConnectorId() {
		return this.mqConnectorId;
	}

	public String getFilterId() {
		return this.filterId;
	}

	public String getReaderOrWriterFilter() {
		return this.readerOrWriterFilter;
	}

}