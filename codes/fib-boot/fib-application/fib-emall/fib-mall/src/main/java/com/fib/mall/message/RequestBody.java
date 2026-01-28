package com.fib.mall.message;

import java.io.Serializable;

public class RequestBody<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	// 业务数据
	private transient T bizData;
	private String encryptedBody;

	public T getBizData() {
		return bizData;
	}

	public void setBizData(T bizData) {
		this.bizData = bizData;
	}

	public String getEncryptedBody() {
		return encryptedBody;
	}

	public void setEncryptedBody(String encryptedBody) {
		this.encryptedBody = encryptedBody;
	}
}