
package com.fib.gateway.channel.http.config;


public class HTTPClientChannelConfig {
	/**
	 * 服务器的url
	 */
	private String url;

	/**
	 * url原始配置
	 */
	private String urlString;
	/**
	 * 传输数据的字符集
	 */
	private String contentCharset;

	/**
	 * 字符集原始配置
	 */
	private String contentCharsetString;

	/**
	 * 最大客户端处理器(线程)个数
	 */
	private int maxHandlerNumber = 10;

	private String maxHandlerNumberString;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlString() {
		return urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	/**
	 * @return the maxHandlerNumber
	 */
	public int getMaxHandlerNumber() {
		return maxHandlerNumber;
	}

	/**
	 * @param maxHandlerNumber
	 *            the maxHandlerNumber to set
	 */
	public void setMaxHandlerNumber(int maxHandlerNumber) {
		this.maxHandlerNumber = maxHandlerNumber;
	}

	public String getMaxHandlerNumberString() {
		return maxHandlerNumberString;
	}

	public void setMaxHandlerNumberString(String maxHandlerNumberString) {
		this.maxHandlerNumberString = maxHandlerNumberString;
	}

	public String getContentCharset() {
		return contentCharset;
	}

	public void setContentCharset(String contentCharset) {
		this.contentCharset = contentCharset;
	}

	public String getContentCharsetString() {
		return contentCharsetString;
	}

	public void setContentCharsetString(String contentCharsetString) {
		this.contentCharsetString = contentCharsetString;
	}

}
