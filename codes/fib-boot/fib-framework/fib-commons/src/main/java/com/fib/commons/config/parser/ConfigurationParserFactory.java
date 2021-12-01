package com.fib.commons.config.parser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import cn.hutool.core.util.ClassUtil;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-12-01
 */
public class ConfigurationParserFactory {
	private ConfigurationParserFactory() {
		// nothing to do
	}

	public static ConfigurationParser getParser(String configName) {
		String configParser;
		configParser = getParserClassName(configName);
		if (configParser == null) {
			return getDefaultParser();
		}
		Class<ConfigurationParser> clazz;
		try {
			clazz = ClassUtil.loadClass(configParser);
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// nothing to do
		}
		return getDefaultParser();
	}

	private static String getParserClassName(String configName) {
		InputStream is = ClassUtil.getClassLoader().getResourceAsStream("jconfig.properties");
		if (is == null) {
			return null;
		}
		Properties props;
		String value;
		props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String testName = "parser." + configName;// parser.uppcfg
		value = props.getProperty(testName);
		if (value != null) {
			return value;
		}
		value = props.getProperty("parser");
		if (value != null) {
			return value;
		}

		String configParser = System.getProperty("jconfig.parser");
		if (configParser != null) {
			return configParser;
		}
		return null;
	}

	private static ConfigurationParser getDefaultParser() {
		return new DefaultConfigParser();
	}
}
