package com.fib.autoconfigure.openapi.message;

import java.io.Serializable;

public class ResponseHeader implements Serializable {
	private static final long serialVersionUID = 1L;
	// 调用方应用ID
	private String appId;
	// 响应码 000000成功
	private String code = "000000";
	// 响应信息
	private String message = "success";
	// 对应请求ID
	private String requestId;
	// 随机字符串
	private String nonce;
	// 响应时间戳
	private Long timestamp;
	// 签名算法类型
	private String signType = "SHA256WithRSA";
	private String encryptType = "AES-256-CBC";
	private String encryptedAesKey;
	// 签名值
	private String sign;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
}