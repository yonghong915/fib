package com.fib.commons.config.parser;

import java.util.HashMap;
import java.util.Map;

import com.fib.commons.config.Configuration;
import com.fib.commons.config.DefaultConfiguration;
import com.fib.commons.config.exception.ConfigurationManagerException;
import com.fib.commons.config.handler.ConfigurationHandler;
import com.fib.commons.config.handler.InputStreamHandler;

/**
 * 配置管理器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-12-01
 */
public class ConfigurationManager {
	private Map<String, Configuration> configurations = null;
	private Map<String, ConfigurationHandler> handlerMapping;

	private ConfigurationManager() {
		configurations = new HashMap<>();
		handlerMapping = new HashMap<>();
	}

	public Configuration getConfiguration() {
		return getConfiguration("default");
	}

	/**
	 * uppcfg_config.xml
	 * 
	 * @param configName uppcfg
	 * @return
	 */
	public Configuration getConfiguration(String configName) {
		if (null == configName) {
			return new DefaultConfiguration(configName);
		}
		if (configurations.containsKey(configName)) {
			return configurations.get(configName);
		}

		String fileName = "config.xml";
		if (!configName.equals("default")) {
			fileName = configName + "_config.xml";
		}
		ConfigurationHandler ish = new InputStreamHandler(fileName, configName);
		Configuration config = ish.load();
		if (config != null) {
			configurations.put(configName, config);
			return config;
		}
		return new DefaultConfiguration(configName);
	}

	public void reload(String name) throws ConfigurationManagerException {
		if (configurations.containsKey(name)) {
			ConfigurationHandler handler = getConfigurationHandler(name);
			if (handler == null) {
				throw new ConfigurationManagerException("There is no handler associated for this configuration");
			}
			Configuration cfg = handler.load();
			configurations.put(name, cfg);
		} else {
			throw new ConfigurationManagerException("There is no configuration with this name");
		}
	}

	private ConfigurationHandler getConfigurationHandler(String name) {
		return handlerMapping.get(name);
	}

	public static ConfigurationManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		public static final ConfigurationManager INSTANCE = new ConfigurationManager();
	}
}
