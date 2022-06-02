package com.fib.autoconfigure.msgconverter.channel.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 通道配置
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-19 16:16:09
 */
public class ChannelMainConfig {
	/** 通道id */
	private String id;

	/*** 通道名 */
	private String name;

	/** 是否启动 */
	private boolean startup;

	/** 部署目录名 */
	private String deploy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStartup() {
		return startup;
	}

	public void setStartup(boolean startup) {
		this.startup = startup;
	}

	public String getDeploy() {
		return deploy;
	}

	public void setDeploy(String deploy) {
		this.deploy = deploy;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
