package com.fib.upp.util.configuration;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fib.upp.service.gateway.IConfig;
import com.fib.upp.util.configuration.jconfig.Configuration;
import com.fib.upp.util.configuration.jconfig.ConfigurationManager;

@Service
@CacheConfig(cacheNames = "configCacheManager")
public class FibConfiguration implements IConfig {
	@Cacheable(key = "#fileName")
	@Override
	public Object load(String fileName, Object... params) {
		Configuration config = ConfigurationManager.getConfiguration(fileName);
		return config;
	}
}
