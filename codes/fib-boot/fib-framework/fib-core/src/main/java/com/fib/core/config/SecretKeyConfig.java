package com.fib.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource("classpath:config/common/custom_security.properties")
@ConfigurationProperties(prefix = "custom.security")
@Data
public class SecretKeyConfig {
	private String charset = "UTF-8";

	private boolean open = true;

	private boolean showLog = false;
}
