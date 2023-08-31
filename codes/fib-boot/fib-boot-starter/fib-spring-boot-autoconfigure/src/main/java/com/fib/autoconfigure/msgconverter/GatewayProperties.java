package com.fib.autoconfigure.msgconverter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fib.autoconfigure.util.PrefixUtil;

@ConfigurationProperties(PrefixUtil.GATEWAY_PREFIX)
public class GatewayProperties {
	/** Enable Gateway */
	private boolean enabled = false;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
