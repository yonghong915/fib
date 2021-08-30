package com.fib.msgconverter.commgateway.config.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态对象配置：动态对象含类名属性和一组参数，类名用于实例化对象，参数传递给实例化后的对象使用
 * 
 * @author 刘恭亮
 * 
 */
public abstract class DynamicObjectConfig {
	protected String className;
	protected Map parameters;

	protected String classNameString;

	public String getParameter(String name) {
		if (parameters != null) {
			return (String) parameters.get(name);
		}
		return null;
	}

	public void setParameter(String name, String value) {
		if (parameters == null) {
			parameters = new HashMap();
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
	public Map getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

}
