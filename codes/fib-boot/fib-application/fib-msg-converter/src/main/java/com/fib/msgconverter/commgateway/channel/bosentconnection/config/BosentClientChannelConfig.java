package com.fib.msgconverter.commgateway.channel.bosentconnection.config;

public class BosentClientChannelConfig {
	/**
	 * 最大客户端处理器(线程)个数
	 */
	private int maxHandlerNumber = 10;
	/**
	 * 服务器的url
	 */
	private String url;

	/**
	 * 传输数据的字符集
	 */
	private String contentCharset;

	private String userName;
	private String password;
	/**
	 * 调用Bosent服务名
	 */
	private String serverId;

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContentCharset() {
		return contentCharset;
	}

	public void setContentCharset(String contentCharset) {
		this.contentCharset = contentCharset;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getMaxHandlerNumber() {
		return maxHandlerNumber;
	}

	public void setMaxHandlerNumber(int maxHandlerNumber) {
		this.maxHandlerNumber = maxHandlerNumber;
	}
}
