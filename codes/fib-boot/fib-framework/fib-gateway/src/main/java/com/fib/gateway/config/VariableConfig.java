package com.fib.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource(value = { "classpath:config/variable.properties" }, ignoreResourceNotFound = false, encoding = "UTF-8")
@ConfigurationProperties(prefix = "cnaps2.upp.cnaps2")
@Data
public class VariableConfig {
	private String recvPort;

	private String sendUrl;

	private int monitorPort;
}
