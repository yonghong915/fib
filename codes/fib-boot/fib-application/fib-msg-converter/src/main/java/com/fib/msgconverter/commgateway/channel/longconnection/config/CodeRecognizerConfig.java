package com.fib.msgconverter.commgateway.channel.longconnection.config;

import java.util.HashSet;
import java.util.Set;

public class CodeRecognizerConfig {
	private String recognizerId;
	private Set requestCodeSet = new HashSet();
	private Set responseCodeSet = new HashSet();

	public Set getResponseCodeSet() {
		return responseCodeSet;
	}

	public void setResponseCodeSet(Set responseCodeSet) {
		this.responseCodeSet = responseCodeSet;
	}

	public String getRecognizerId() {
		return recognizerId;
	}

	public void setRecognizerId(String recognizerId) {
		this.recognizerId = recognizerId;
	}

	public Set getRequestCodeSet() {
		return requestCodeSet;
	}

	public void setRequestCodeSet(Set requestCodeSet) {
		this.requestCodeSet = requestCodeSet;
	}
}
