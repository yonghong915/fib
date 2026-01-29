package com.fib.autoconfigure.openapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fib.autoconfigure.util.PrefixUtil;

@Configuration
@ConditionalOnClass({ JsonRequestWrapper.class })
@ComponentScan("com.fib.autoconfigure.openapi")
@ConditionalOnProperty(prefix = PrefixUtil.OPENAPI_PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ OpenapiProperties.class })
public class OpenapiAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenapiAutoConfiguration.class);
}
