package com.fib.gateway.config.base;

import java.util.Properties;

import com.fib.commons.exception.CommonException;

public abstract class TypedDynamicObjectConfig extends DynamicObjectConfig {
	public static final String USER_DEFINED = "USER_DEFINED";

	protected String type;

	/**
	 * 取得type与类名的映射关系。由子类实现。
	 * 
	 * @return
	 */
	protected abstract Properties getProperties();

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;

		if (type != null && !USER_DEFINED.equalsIgnoreCase(type)) {
			String className = getProperties().getProperty(type);
			if (null == className) {
				throw new CommonException("type.unsupport");
			}
			setClassName(className);
		}
	}
}