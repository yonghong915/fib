package com.fib.upp.util.configuration.jconfig.parser;

import cn.hutool.core.util.ReflectUtil;

public class ConfigurationParserFactory {
	private ConfigurationParserFactory() {
		//
	}

	public static ConfigurationParser getParser(String configName) {
		String configParser;
		configParser = getParserClassName(configName);
		if (null == configParser) {
			return getDefaultParser();
		}
		return ReflectUtil.newInstance(configParser);
	}

	private static String getParserClassName(String configName) {
		return null;
	}

	private static ConfigurationParser getDefaultParser() {
		return new DefaultConfigParser();
	}
}
