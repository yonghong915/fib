package com.fib.upp.util.configuration.jconfig;

import com.fib.upp.util.configuration.jconfig.handler.InputStreamHandler;

public class ConfigurationManager {
	public static Configuration getConfiguration() {
		return getConfiguration("default");
	}

	public static Configuration getConfiguration(String name) {
		if (name == null) {
			return new DefaultConfiguration(name);
		}

		String fileName = "config.xml";
		if (!name.equals("default")) {
			fileName = name + "_config.xml";
		}

		InputStreamHandler ish = new InputStreamHandler(fileName);
		Configuration config = ish.load(name);
		if (null == config) {
			return new DefaultConfiguration(fileName);
		}
		return config;
	}
}
