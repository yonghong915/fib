package com.fib.gateway.module;

import java.util.Map;

public abstract class Module {
	private Map parameters;

	/**
	 * 初始化
	 */
	public abstract void initialize();

	/**
	 * 停止
	 */
	public abstract void shutdown();

	public Map getParameters() {
		return parameters;
	}

	public void setParameter(Map parameters) {
		this.parameters = parameters;
	}
}
