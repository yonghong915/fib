package com.fib.autoconfigure.openapi;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.openapi.config.NacosConfig;
import com.fib.autoconfigure.openapi.filter.SingletonObjectMapper;
import com.fib.autoconfigure.openapi.message.ApiResponse;
import com.fib.autoconfigure.openapi.message.ResponseBody;
import com.fib.autoconfigure.openapi.message.ResponseHeader;
import com.fib.autoconfigure.openapi.util.EncryptUtils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;

@Component
public class ResponseBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseBuilder.class);
	@Autowired
	private NacosConfig nacosConfig;

	public ResponseBuilder() {
		// nothing
	}

	/**
	 * 构建加密请求报文
	 * 
	 * @param bizData   业务数据
	 * @param requestId 请求唯一ID
	 * @return 加密请求
	 */
	public ApiResponse<Object> buildMessageResponse(Object bizData) {
		String appId = nacosConfig.getAppId();
		Assert.notBlank(appId, () -> new RuntimeException("appId must be not null"));
		ApiResponse<Object> apiResponse = new ApiResponse<>();

		ResponseHeader responseHeader = new ResponseHeader();
		responseHeader.setAppId(appId);
		responseHeader.setCode("000000");
		responseHeader.setMessage("success");
		responseHeader.setTimestamp(System.currentTimeMillis());
		responseHeader.setNonce(RandomUtil.randomString(16));

		ResponseBody<Object> responseBody = new ResponseBody<>();
		responseBody.setBizData(bizData);

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

		responseHeader.setEncryptedAesKey(encrypedKeyData);

		// 5. 生成签名
		// appId + timestamp + nonce + cipherText
		String signSource = responseHeader.getAppId() + responseHeader.getTimestamp() + responseHeader.getNonce()
				+ bizDataStr;
		String signSourceHash = DigestUtil.sha256Hex(signSource);
		String sign = EncryptUtils.generateSign(SignAlgorithm.SHA256withRSA.getValue(), privateKeyStr, signSourceHash);
		LOGGER.info("sign=[{}]", sign);
		responseHeader.setSign(sign);
		
		responseBody.setEncryptedBody(encryedBizData);

		apiResponse.setResponseHeader(responseHeader);
		apiResponse.setResponseBody(responseBody);
		return apiResponse;
	}
}
