package com.fib.core.base.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@RefreshScope
@Data
public class DataSourceProperties {

	@Value("${type}")
	private String type;

	@Value("${driverClassName}")
	private String driverClassName;

	@Value("${url}")
	private String url;

	@Value("${username}")
	private String username;

	@Value("${password}")
	private String password;

	@Value("${miniIdle}")
	private int miniIdle;

	@Value("${maxPoolSize}")
	private int maxPoolSize;

	@Value("${idleTimeout}")
	private long idleTimeout;

	@Value("${connectionTimeout}")
	private long connectionTimeout;

}
