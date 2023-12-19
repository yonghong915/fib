package com.fib.event;

import java.io.Serializable;

public class CustomEntity implements Serializable {
	private String serviceName;
	private Object data;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
