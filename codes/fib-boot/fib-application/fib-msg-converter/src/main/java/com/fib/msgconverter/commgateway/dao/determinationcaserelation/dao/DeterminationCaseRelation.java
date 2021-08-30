package com.fib.msgconverter.commgateway.dao.determinationcaserelation.dao;

import java.math.BigDecimal;

public class DeterminationCaseRelation{
	public DeterminationCaseRelation() {

	}

	//动态路由集合ID
	private String determinationId;

	//动态路由ID
	private String caseId;

	public void setDeterminationId(String newDeterminationId) {
		this.determinationId = newDeterminationId;
	}

	public void setCaseId(String newCaseId) {
		this.caseId = newCaseId;
	}

	public String getDeterminationId() {
		return this.determinationId;
	}

	public String getCaseId() {
		return this.caseId;
	}

}