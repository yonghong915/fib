package com.fib.gateway.config.base;

import java.util.HashMap;
import java.util.Map;

public abstract class DynamicObjectConfig {
	protected String className;
	protected Map<String, String> parameters;

	protected String classNameString;

	public String getParameter(String name) {
		if (parameters != null) {
			return parameters.get(name);
		}
		return null;
	}

	public void setParameter(String name, String value) {
		if (parameters == null) {
			parameters = new HashMap<>();
		}
		parameters.put(name, value);
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassNameString() {
		return classNameString;
	}

	public void setClassNameString(String classNameString) {
		this.classNameString = classNameString;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
}
