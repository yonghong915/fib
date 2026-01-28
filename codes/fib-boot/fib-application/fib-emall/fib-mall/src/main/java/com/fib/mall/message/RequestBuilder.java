package com.fib.mall.message;

import com.fib.mall.config.EncryptRequest;

import cn.hutool.core.util.RandomUtil;

/**
 * 发送方请求构建工具
 */
public class RequestBuilder {
	// 发送方私钥（用于签名）
	private final String senderPrivateKey;
	// 接收方公钥（用于加密AES密钥）
	private final String receiverPublicKey;
	// 应用ID
	private final String appId;

	public RequestBuilder(String appId, String senderPrivateKey, String receiverPublicKey) {
		this.appId = appId;
		this.senderPrivateKey = senderPrivateKey;
		this.receiverPublicKey = receiverPublicKey;
	}

	/**
	 * 构建加密请求报文
	 * 
	 * @param bizData   业务数据
	 * @param requestId 请求唯一ID
	 * @return 加密请求
	 */
	public EncryptRequest buildEncryptRequest(Object bizData, String requestId) {
		// 1. 生成AES密钥和IV
		String[] aesKeyIv = EncryptUtils.generateAesKeyIv();
		String aesKey = aesKeyIv[0];
		String aesIv = aesKeyIv[1];

		// 2. AES加密业务数据
		String encryptedBody = "";//EncryptSignUtils.aesEncrypt(bizData, aesKey, aesIv);

		// 3. RSA加密AES密钥+IV
		String encryptedAesKey = EncryptUtils.rsaEncryptAesKey(aesKey, aesIv, receiverPublicKey);

		// 4. 构建请求头
		RequestHeader header = new RequestHeader();
		header.setAppId(appId);
		header.setTimestamp(System.currentTimeMillis());
		header.setNonce(RandomUtil.randomString(16));
		header.setRequestId(requestId);
		header.setEncryptedAesKey(encryptedAesKey);

		// 5. 生成签名
//		String sign = EncryptSignUtils.generateSign(header, encryptedBody, senderPrivateKey);
//		header.setSign(sign);

		// 6. 组装请求
		EncryptRequest request = new EncryptRequest();
//		request.setRequestHeader(header);
//		request.setEncryptedBody(encryptedBody);
		return request;
	}
}