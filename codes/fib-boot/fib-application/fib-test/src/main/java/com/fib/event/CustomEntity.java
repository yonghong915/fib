package com.fib.event;

import java.io.Serializable;

public class CustomEntity implements Serializable {
	private String serviceName;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
