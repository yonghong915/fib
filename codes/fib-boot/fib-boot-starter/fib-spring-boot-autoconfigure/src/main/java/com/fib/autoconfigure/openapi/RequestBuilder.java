package com.fib.autoconfigure.openapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.openapi.config.NacosConfig;
import com.fib.autoconfigure.openapi.filter.SingletonObjectMapper;
import com.fib.autoconfigure.openapi.message.ApiRequest;
import com.fib.autoconfigure.openapi.message.RequestBody;
import com.fib.autoconfigure.openapi.message.RequestHeader;
import com.fib.autoconfigure.openapi.util.EncryptUtils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;

/**
 * 发送方请求构建工具
 */
@Component
public class RequestBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestBuilder.class);
	@Autowired
	private NacosConfig nacosConfig;

	public RequestBuilder() {
		// nothing
	}

	/**
	 * 构建加密请求报文
	 * 
	 * @param bizData   业务数据
	 * @param requestId 请求唯一ID
	 * @return 加密请求
	 */
	public ApiRequest<Object> buildMessageRequest(Object bizData) {
		String appId = nacosConfig.getAppId();
		Assert.notBlank(appId, () -> new RuntimeException("appId must be not null"));

		long requestId = IdUtil.getSnowflakeNextId();

		RequestBody<Object> requestBody = new RequestBody<>();
		requestBody.setBizData(bizData);

		// 1. 生成AES密钥和IV
		String[] aesKeyIv = EncryptUtils.generateAesKeyIv();
		String aesKey = aesKeyIv[0];
		String aesIv = aesKeyIv[1];

		// 2. AES加密业务数据
		
		String bizDataStr = SingletonObjectMapper.toSortedJson(bizData);
		LOGGER.info("bizDataStr=[{}]", bizDataStr);
		String encryedBizData = EncryptUtils.aesEncrypt(bizDataStr, aesKey, aesIv);
		IO.println(encryedBizData);

		// 3. RSA加密AES密钥+IV
		// String[] keyPair = EncryptUtils.generateKeyPair(EncryptUtils.RSA_ALGORITHM);
		String[] keyPair = {
				"MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJp4BnASzcnfBN58NFSjH61jKv+Np5aiXHmuY/LCTOyY0UjZBzcdBvgDefCpvAMTv7QsqCtX4B1V5HfHdW5z5/imJl6W6mwBtixcwfhqO+Fn6/wmG2AugmQBh6T0Se7peG6KJUTu1mEOExR7lGvY8hBNRngwdcJYSLx7bgcIfqu9AgMBAAECgYACITVY2RAwdZb2rf3htzBiCVvn00SULC+8DMEQ291y0KY9YSKl6kxKN0VjNvqM4fo9ah/fyHHNAxNn6bPZ3pa8PnyMknu/B8zU22/7DI9Kshm/6xxLsYs2TYdJfekcbPdnozWjtwoedCwASKwVz0ExHYzJ2ZfpST3zc2xVHUZ1AQJBANYwp72KTWcnYO6+HY07K/Dxv1VLe9VtiuanD/Mq14twRXICNcOXPth+ciVQUSR60vbVN6nTdlCjS1gZfo75NL0CQQC4nwbCPf/l42QPpVNVCEk4nbq39hPBJ/fY9QW1VCpBuYLBUQBiwLqrT7TSTSDdf8CWGQb/uZYxFb7xq2f7GEMBAkBRcb7Wu7gi+T5KidAC2/UhcUsny8QSq8ydV/kgpbHAO7isWVrIPMKQ38PXnGq+TFXbtcess9PRZcZIgak2BFyhAkBqIHg5Jny4gKtfVxD9G2ND2V+hKiKW8UvG+qqKXtRfra0dRVvsaI+ltI7kKRQQX8SsQ7zDOcK9epulvntqWrsBAkA5Nq7Ag4Yc7kF/+m3RRloaJ+AZXhHkEoLuLSi+Vu0I/IaeZwcocLWlGUUoTTynzC6BU8imcj8n+scH4RFnxm3w",
				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaeAZwEs3J3wTefDRUox+tYyr/jaeWolx5rmPywkzsmNFI2Qc3HQb4A3nwqbwDE7+0LKgrV+AdVeR3x3Vuc+f4piZelupsAbYsXMH4ajvhZ+v8JhtgLoJkAYek9Enu6XhuiiVE7tZhDhMUe5Rr2PIQTUZ4MHXCWEi8e24HCH6rvQIDAQAB" };
		String privateKeyStr = keyPair[0];
		String publicKeyStr = keyPair[1];

		LOGGER.info("privateKey=[{}]", privateKeyStr);
		LOGGER.info("publicKey=[{}]", publicKeyStr);

		String encrypedKeyData = EncryptUtils.rsaEncryptAesKey(EncryptUtils.RSA_ALGORITHM, aesKey, aesIv, publicKeyStr);
		LOGGER.info("encrypedKeyData=[{}]", encrypedKeyData);

		// 4. 构建请求头
		RequestHeader requestHeader = new RequestHeader();
		requestHeader.setAppId(nacosConfig.getAppId());
		requestHeader.setTimestamp(System.currentTimeMillis());
		requestHeader.setNonce(RandomUtil.randomString(16));
		requestHeader.setRequestId(nacosConfig.getAppId() + requestId);
		requestHeader.setEncryptedAesKey(encrypedKeyData);

		// 5. 生成签名
		// appId + timestamp + nonce + cipherText
		String signSource = requestHeader.getAppId() + requestHeader.getTimestamp() + requestHeader.getNonce() + bizDataStr;
		String signSourceHash = DigestUtil.sha256Hex(signSource);
		String sign = EncryptUtils.generateSign(SignAlgorithm.SHA256withRSA.getValue(), privateKeyStr, signSourceHash);
		LOGGER.info("sign=[{}]", sign);
		requestHeader.setSign(sign);

		requestBody.setEncryptedBody(encryedBizData);

		// 6. 组装请求
		ApiRequest<Object> apiRequest = new ApiRequest<>();
		apiRequest.setRequestHeader(requestHeader);
		apiRequest.setRequestBody(requestBody);

		return apiRequest;
	}
}