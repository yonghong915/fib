/**
 * 北京长信通信息技术有限公司
 * 2008-8-26 下午03:33:06
 */
package com.fib.msgconverter.commgateway.channel.nio.config;

import com.fib.msgconverter.commgateway.channel.config.base.ConnectionConfig;

/**
 * Socke通道配置
 * @author 刘恭亮
 *
 */
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
