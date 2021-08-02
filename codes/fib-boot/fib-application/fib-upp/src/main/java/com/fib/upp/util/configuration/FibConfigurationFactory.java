package com.fib.upp.util.configuration;

import org.springframework.beans.factory.annotation.Autowired;

import com.fib.upp.service.gateway.IConfig;
import com.fib.upp.util.configuration.jconfig.Configuration;

public class FibConfigurationFactory {

	@Autowired
	IConfig fibConfiguration;

	public Configuration getConfig() {
		return getConfig("syspara");
	}

	public Configuration getConfig(String configName) {
		Object obj = fibConfiguration.load(configName);
		if (null != obj && obj instanceof Configuration) {
			return (Configuration) obj;
		}
		return null;
	}
}
