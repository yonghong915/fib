package com.fib.gateway.channel.message.recognizer.impl;

import java.util.Map;
import java.util.Properties;

import com.fib.gateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.gateway.message.util.FieldDataLocator;

public class SeparatorRecognizer extends AbstractMessageRecognizer {
	private FieldDataLocator locator;

	public SeparatorRecognizer() {
		locator = new FieldDataLocator();
	}

	public String recognize(byte[] message) {
		return locator.locateAsString(message);
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);

		Properties prop = new Properties();
		prop.putAll(parameters);
		prop.setProperty(FieldDataLocator.LOCATE_METHOD_TEXT, FieldDataLocator.LOCATE_METHOD_SEPARATOR_TEXT);

		locator.setProperties(prop);

	}

	public Map getParameter() {
		return parameters;
	}

}
