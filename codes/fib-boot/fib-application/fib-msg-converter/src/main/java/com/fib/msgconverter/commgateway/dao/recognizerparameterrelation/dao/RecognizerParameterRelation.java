package com.fib.msgconverter.commgateway.dao.recognizerparameterrelation.dao;

import java.math.BigDecimal;

public class RecognizerParameterRelation{
	public RecognizerParameterRelation() {

	}

	//识别器ID
	private String recognizerId;

	//参数ID
	private String parameterId;

	public void setRecognizerId(String newRecognizerId) {
		this.recognizerId = newRecognizerId;
	}

	public void setParameterId(String newParameterId) {
		this.parameterId = newParameterId;
	}

	public String getRecognizerId() {
		return this.recognizerId;
	}

	public String getParameterId() {
		return this.parameterId;
	}

}