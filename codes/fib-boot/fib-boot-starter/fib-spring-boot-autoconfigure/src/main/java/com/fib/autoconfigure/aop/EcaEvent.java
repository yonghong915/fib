package com.fib.autoconfigure.aop;

public enum EcaEvent {
	UNKNOWN("unknown"), IN_VALIDATE("inValidate"), OUT_VALIDATE("outValidate"), RETURN("return");

	private String value;

	EcaEvent(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
