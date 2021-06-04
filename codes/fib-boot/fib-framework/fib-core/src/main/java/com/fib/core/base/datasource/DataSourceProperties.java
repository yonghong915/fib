package com.fib.core.base.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {

	private String type;

	private String driverClassName;

	private String url;

	private String username;

	private String password;

	private int miniIdle;

	private int maxPoolSize;

	private long idleTimeout;

	private long connectionTimeout;
}
