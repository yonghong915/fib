package com.fib.autoconfigure.openapi;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fib.autoconfigure.util.PrefixUtil;

/**
 * Openapi配置属性
 * 
 * @author fangyh
 * @version 1.0
 * @date 2026-01-29 10:32:38
 */
@ConfigurationProperties(PrefixUtil.OPENAPI_PREFIX)
public class OpenapiProperties {

	/** Enable Openapi */
	private boolean enabled = false;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}