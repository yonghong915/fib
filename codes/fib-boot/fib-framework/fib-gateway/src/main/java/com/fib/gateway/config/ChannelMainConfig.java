package com.fib.gateway.config;

import lombok.Data;

@Data
public class ChannelMainConfig {
	/**
	 * 数据库专用：通道唯一索引
	 */
	private String databaseChannelId;
	/**
	 * 通道id
	 */
	private String id;

	/**
	 * 通道名
	 */
	private String name;

	/**
	 * 是否启动
	 */
	private boolean startup;

	/**
	 * 部署目录名
	 */
	private String deploy;

}
