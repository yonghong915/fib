package com.fib.msgconverter.commgateway.dao.recognizerrelation.dao;

import java.math.BigDecimal;

public class RecognizerRelation{
	public RecognizerRelation() {

	}

	//组合识别器ID
	private String compositeRecognizerId;

	//子识别器ID
	private String subRecognizerId;

	//子识别器在组合识别器中的顺序
	private int subIndex;

	public void setCompositeRecognizerId(String newCompositeRecognizerId) {
		this.compositeRecognizerId = newCompositeRecognizerId;
	}

	public void setSubRecognizerId(String newSubRecognizerId) {
		this.subRecognizerId = newSubRecognizerId;
	}

	public void setSubIndex(int newSubIndex) {
		this.subIndex = newSubIndex;
	}

	public String getCompositeRecognizerId() {
		return this.compositeRecognizerId;
	}

	public String getSubRecognizerId() {
		return this.subRecognizerId;
	}

	public int getSubIndex() {
		return this.subIndex;
	}

}