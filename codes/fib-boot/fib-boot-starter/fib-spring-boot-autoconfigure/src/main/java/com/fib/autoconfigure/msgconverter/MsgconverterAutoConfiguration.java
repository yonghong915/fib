package com.fib.autoconfigure.msgconverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fib.autoconfigure.util.PrefixUtil;
import com.fib.msgconverter.commgateway.startup.Bootstrap;

@Configuration(proxyBeanMethods = false)
@ComponentScan("com.fib.autoconfigure.msgconverter")
@ConditionalOnClass({ Bootstrap.class })
@ConditionalOnProperty(prefix = PrefixUtil.GATEWAY_PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ GatewayProperties.class })
public class MsgconverterAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(MsgconverterAutoConfiguration.class);

	@Bean
	@ConditionalOnMissingBean
	public Bootstrap bootstrap() {
		LOGGER.info("Build Bootstrap Bean");
		return new Bootstrap();
	}
}
