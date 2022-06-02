package com.fib.autoconfigure.msgconverter.channel.message.recognizer.impl;

import java.util.Map;
import java.util.Properties;

import com.fib.autoconfigure.msgconverter.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.autoconfigure.msgconverter.channel.message.util.FieldDataLocator;

public class IndexRecognizer extends AbstractMessageRecognizer {
	private FieldDataLocator locator;

	public IndexRecognizer() {
		locator = new FieldDataLocator();
		// locator.setLocateMethod(FieldDataLocator.LOCATE_METHOD_INDEX);
	}

	@Override
	public String recognize(byte[] message) {
		return locator.locateAsString(message);
	}

	@Override
	public void setParameters(Map<String, String> parameters) {
		super.setParameters(parameters);

		Properties prop = new Properties();
		prop.putAll(parameters);
		prop.setProperty(FieldDataLocator.LOCATE_METHOD_TEXT, FieldDataLocator.LOCATE_METHOD_INDEX_TEXT);

		locator.setProperties(prop);

		// parseParameters();
	}
}
