package com.fib.msgconverter.commgateway.channel.longconnection.config;

import java.util.HashSet;
import java.util.Set;

public class CodeRecognizerConfig {
	private String recognizerId;
	private Set<String> requestCodeSet = new HashSet<>();
	private Set<String> responseCodeSet = new HashSet<>();

	public Set<String> getResponseCodeSet() {
		return responseCodeSet;
	}

	public void setResponseCodeSet(Set<String> responseCodeSet) {
		this.responseCodeSet = responseCodeSet;
	}

	public String getRecognizerId() {
		return recognizerId;
	}

	public void setRecognizerId(String recognizerId) {
		this.recognizerId = recognizerId;
	}

	public Set<String> getRequestCodeSet() {
		return requestCodeSet;
	}

	public void setRequestCodeSet(Set<String> requestCodeSet) {
		this.requestCodeSet = requestCodeSet;
	}
}
