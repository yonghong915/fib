package com.fib.autoconfigure.openapi.message;

import java.io.Serializable;

public class RequestHeader implements Serializable {
	private static final long serialVersionUID = 1L;

	// 接口版本
	private String apiVersion = "1.0.0";
	// 调用方应用ID
	private String appId;
	// 请求时间戳（毫秒）
	private Long timestamp;
	// 随机字符串
	private String nonce;
	// 请求唯一标识
	private String requestId;
	// 签名算法类型
	private String signType = "SHA256WithRSA";
	private String encryptType = "AES-256-CBC";
	private String encryptedAesKey; // RSA加密后的AES密钥+IV
	// 签名值
	private String sign;

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getEncryptType() {
		return encryptType;
	}

	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}

	public String getEncryptedAesKey() {
		return encryptedAesKey;
	}

	public void setEncryptedAesKey(String encryptedAesKey) {
		this.encryptedAesKey = encryptedAesKey;
	}
}