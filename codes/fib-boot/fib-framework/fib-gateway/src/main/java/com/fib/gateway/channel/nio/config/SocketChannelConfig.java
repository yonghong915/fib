package com.fib.gateway.channel.nio.config;

import com.fib.gateway.channel.config.base.ConnectionConfig;

public class SocketChannelConfig {
	/**
	 * 连接配置：监听端口配置
	 */
	protected ConnectionConfig connectionConfig;
	
	/**
	 * 消息接收器配置
	 */
	protected ReaderConfig readerConfig;
	
	/**
	 * 消息发送器配置
	 */
	protected WriterConfig writerConfig;

	/**
	 * @return the connection
	 */
	public ConnectionConfig getConnectionConfig() {
		return connectionConfig;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnectionConfig(ConnectionConfig connection) {
		this.connectionConfig = connection;
	}

	/**
	 * @return the readerConfig
	 */
	public ReaderConfig getReaderConfig() {
		return readerConfig;
	}

	/**
	 * @param readerConfig the readerConfig to set
	 */
	public void setReaderConfig(ReaderConfig readerConfig) {
		this.readerConfig = readerConfig;
	}

	/**
	 * @return the writerConfig
	 */
	public WriterConfig getWriterConfig() {
		return writerConfig;
	}

	/**
	 * @param writerConfig the writerConfig to set
	 */
	public void setWriterConfig(WriterConfig writerConfig) {
		this.writerConfig = writerConfig;
	}
}
