package com.fib.core.util;

import java.util.HashMap;
import java.util.Map;

public class MessageContext {
	private Map<String, Object> properties = new HashMap<>();

	/**
	 * 得到属性
	 */
	public Object getProperty(String name) {
		if (properties.get(name) == null) {
			return "";
		}
		return properties.get(name);
	}

	/**
	 * 设置属性
	 */
	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}
}