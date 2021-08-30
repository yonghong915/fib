package com.fib.msgconverter.commgateway.dao.filterparameterrelation.dao;

import java.math.BigDecimal;

public class FilterParameterRelation{
	public FilterParameterRelation() {

	}

	//过滤器ID
	private String filterId;

	//参数ID
	private String parameterId;

	public void setFilterId(String newFilterId) {
		this.filterId = newFilterId;
	}

	public void setParameterId(String newParameterId) {
		this.parameterId = newParameterId;
	}

	public String getFilterId() {
		return this.filterId;
	}

	public String getParameterId() {
		return this.parameterId;
	}

}