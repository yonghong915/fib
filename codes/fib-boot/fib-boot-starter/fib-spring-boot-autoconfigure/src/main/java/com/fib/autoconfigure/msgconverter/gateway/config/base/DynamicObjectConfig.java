package com.fib.autoconfigure.msgconverter.gateway.config.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态对象配置：动态对象含类名属性和一组参数，类名用于实例化对象，参数传递给实例化后的对象使用
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-23 14:48:51
 */
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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public String getClassNameString() {
		return classNameString;
	}

	public void setClassNameString(String classNameString) {
		this.classNameString = classNameString;
	}

	@Override
	public String toString() {
		return "DynamicObjectConfig [className=" + className + ", parameters=" + parameters + ", classNameString=" + classNameString + "]";
	}
}