package com.fib.uqcp.api.msg;

public class RequestHeader {
	/**
	 * 签名
	 */
	private String authentication;

	/**
	 * 时间戳
	 */
	private Long timestamp;

	/**
	 * 随机数-hash(ip+时间+随机数)
	 */
	private String nonce;

	/**
	 * 对称加密密钥
	 */
	private String securityKey;

	/**
	 * 发起系统编码
	 */
	private String systemCode;

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getSecurityKey() {
		return securityKey;
	}

	public void setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
}