package com.fib.autoconfigure.msgconverter.channel.config;

import com.fib.autoconfigure.msgconverter.message.metadata.ConstantMB;

/**
 * 通道连接器配置
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-23 16:06:30
 */
public class ConnectorConfig {
	/** 配置文件名 */
	private String config;

	private ConstantMB.ConnectorMode type;

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public ConstantMB.ConnectorMode getType() {
		return type;
	}

	public void setType(ConstantMB.ConnectorMode type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ConnectorConfig [config=" + config + ", type=" + type + "]";
	}
}