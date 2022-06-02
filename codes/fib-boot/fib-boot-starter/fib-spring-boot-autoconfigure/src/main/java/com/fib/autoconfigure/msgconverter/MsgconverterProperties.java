package com.fib.autoconfigure.msgconverter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fib.autoconfigure.util.PrefixUtil;

/**
 * 配置属性
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-18 10:32:38
 */
@ConfigurationProperties(PrefixUtil.MSG_CONVERTER_PREFIX)
public class MsgconverterProperties {

	/** Enable. */
	private boolean enabled = false;

	private String libPath;

	private String configPath;

	private String deployPath;

	private String gatewayId;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getLibPath() {
		return libPath;
	}

	public void setLibPath(String libPath) {
		this.libPath = libPath;
	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public String getDeployPath() {
		return deployPath;
	}

	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}
}