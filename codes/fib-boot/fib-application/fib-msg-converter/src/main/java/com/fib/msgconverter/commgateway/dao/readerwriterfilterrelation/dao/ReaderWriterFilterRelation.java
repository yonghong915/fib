package com.fib.msgconverter.commgateway.dao.readerwriterfilterrelation.dao;

import java.math.BigDecimal;

public class ReaderWriterFilterRelation{
	public ReaderWriterFilterRelation() {

	}

	//读取器/写入器ID
	private String readerWriterId;

	//过滤器ID
	private String filterId;

	public void setReaderWriterId(String newReaderWriterId) {
		this.readerWriterId = newReaderWriterId;
	}

	public void setFilterId(String newFilterId) {
		this.filterId = newFilterId;
	}

	public String getReaderWriterId() {
		return this.readerWriterId;
	}

	public String getFilterId() {
		return this.filterId;
	}

}