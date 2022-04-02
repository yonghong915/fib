package com.fib.autoconfigure.aop;

public enum EcaService {
	UNKNOWN("", EcaEvent.UNKNOWN);

	EcaService(String serviceName, EcaEvent event) {
		this.serviceName = serviceName;
		this.event = event;
	}

	private String serviceName;
	private EcaEvent event;

	public EcaService setService(String serviceName, EcaEvent event) {
		this.serviceName = serviceName;
		this.event = event;
		return this;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public EcaEvent getEvent() {
		return event;
	}

	public void setEvent(EcaEvent event) {
		this.event = event;
	}
}
