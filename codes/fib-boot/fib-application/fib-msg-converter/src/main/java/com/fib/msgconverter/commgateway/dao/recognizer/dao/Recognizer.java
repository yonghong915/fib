package com.fib.msgconverter.commgateway.dao.recognizer.dao;

import java.math.BigDecimal;

public class Recognizer{
	public Recognizer() {

	}

	//id
	private String id;

	//识别器类型
	private String recognizerType;

	//识别器类名
	private String className;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setRecognizerType(String newRecognizerType) {
		this.recognizerType = newRecognizerType;
	}

	public void setClassName(String newClassName) {
		this.className = newClassName;
	}

	public String getId() {
		return this.id;
	}

	public String getRecognizerType() {
		return this.recognizerType;
	}

	public String getClassName() {
		return this.className;
	}

}