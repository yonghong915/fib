package com.fib.msgconverter.commgateway.dao.readerwriterparameterrelation.dao;

import java.math.BigDecimal;

public class ReaderWriterParameterRelation{
	public ReaderWriterParameterRelation() {

	}

	//读写器ID
	private String readerWriterId;

	//参数ID
	private String parameterId;

	public void setReaderWriterId(String newReaderWriterId) {
		this.readerWriterId = newReaderWriterId;
	}

	public void setParameterId(String newParameterId) {
		this.parameterId = newParameterId;
	}

	public String getReaderWriterId() {
		return this.readerWriterId;
	}

	public String getParameterId() {
		return this.parameterId;
	}

}