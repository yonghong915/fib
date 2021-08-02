package com.fib.upp.util.configuration.jconfig;

import java.util.Properties;

public class DefaultCategory implements Category {
	private String name;
	private Properties properties;
	private String configurationName;

	public DefaultCategory(String categoryName) {
		properties = new Properties();
		// listenerList = new EventListenerList();
		name = categoryName;
	}

	@Override
	public Category setProperty(String propertyName, String propertyValue) {
		properties.setProperty(propertyName, propertyValue);
		return this;
	}

	@Override
	public String getProperty(String propertyName) {
		String value = properties.getProperty(propertyName);
		return value;
	}

	@Override
	public String getProperty(String propertyName, String defaultValue) {
		String temp = properties.getProperty(propertyName, defaultValue);
		return temp;
	}

	@Override
	public String getCategoryName() {
		return name;
	}

	public String getConfigurationName() {
		return configurationName;
	}

	@Override
	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}
}
