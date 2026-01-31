package com.fib.mall.config;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fib.autoconfigure.openapi.message.ApiResponse;
import com.fib.autoconfigure.openapi.message.ResponseHeader;
import com.fib.autoconfigure.openapi.util.EncryptUtils;
import com.fib.core.util.StatusCode;
import com.fib.core.web.ResultRsp;

import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import feign.Response;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;

/**
 * 自定义 Feign 解码器：调用后处理响应报文
 */
@Component
public class FeignCustomDecoder implements Decoder {

	private final JacksonDecoder delegate;
	private final ObjectMapper objectMapper;

	public FeignCustomDecoder(ObjectMapper objectMapper) {
		this.delegate = new JacksonDecoder(objectMapper);
		this.objectMapper = objectMapper;
	}

	@Override
	public Object decode(Response response, Type type) throws IOException {
		// 1. 先解析响应为公共响应体（ResponseWrapper）
		ApiResponse<?> apiResponse = (ApiResponse<?>) delegate.decode(response, ApiResponse.class);
		ResponseHeader responseHeader = apiResponse.getResponseHeader();
		// 2. 自定义处理：比如验签、解密、统一异常处理
		if (!StatusCode.SUCCESS.code().equals(responseHeader.getCode())) {
			throw new RuntimeException("Feign 调用失败：" + responseHeader.getMessage());
		}

		if (responseHeader.getAppId() == null || responseHeader.getTimestamp() == null
				|| responseHeader.getNonce() == null || responseHeader.getSign() == null) {
//			writeErrorResponse(httpResponse, "400", "缺少必要的安全参数");
//			return;
		}

		// ========== 步骤4：防重放校验（时间戳 + nonce） ==========
		long requestTime = responseHeader.getTimestamp();
		long currentTime = System.currentTimeMillis();

		/* 步骤6：解密业务报文 */
		String encryptBizData = apiResponse.getResponseBody().getEncryptedBody();
		// LOGGER.info("encryptBizData=[{}]", encryptBizData);

		String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJp4BnASzcnfBN58NFSjH61jKv+Np5aiXHmuY/LCTOyY0UjZBzcdBvgDefCpvAMTv7QsqCtX4B1V5HfHdW5z5/imJl6W6mwBtixcwfhqO+Fn6/wmG2AugmQBh6T0Se7peG6KJUTu1mEOExR7lGvY8hBNRngwdcJYSLx7bgcIfqu9AgMBAAECgYACITVY2RAwdZb2rf3htzBiCVvn00SULC+8DMEQ291y0KY9YSKl6kxKN0VjNvqM4fo9ah/fyHHNAxNn6bPZ3pa8PnyMknu/B8zU22/7DI9Kshm/6xxLsYs2TYdJfekcbPdnozWjtwoedCwASKwVz0ExHYzJ2ZfpST3zc2xVHUZ1AQJBANYwp72KTWcnYO6+HY07K/Dxv1VLe9VtiuanD/Mq14twRXICNcOXPth+ciVQUSR60vbVN6nTdlCjS1gZfo75NL0CQQC4nwbCPf/l42QPpVNVCEk4nbq39hPBJ/fY9QW1VCpBuYLBUQBiwLqrT7TSTSDdf8CWGQb/uZYxFb7xq2f7GEMBAkBRcb7Wu7gi+T5KidAC2/UhcUsny8QSq8ydV/kgpbHAO7isWVrIPMKQ38PXnGq+TFXbtcess9PRZcZIgak2BFyhAkBqIHg5Jny4gKtfVxD9G2ND2V+hKiKW8UvG+qqKXtRfra0dRVvsaI+ltI7kKRQQX8SsQ7zDOcK9epulvntqWrsBAkA5Nq7Ag4Yc7kF/+m3RRloaJ+AZXhHkEoLuLSi+Vu0I/IaeZwcocLWlGUUoTTynzC6BU8imcj8n+scH4RFnxm3w";
		String keyvStr = responseHeader.getEncryptedAesKey();
		String bizData = null;
		try {
			String[] ivStr = EncryptUtils.rsaDecryptAesKey(EncryptUtils.RSA_ALGORITHM, keyvStr, privateKey);
			bizData = EncryptUtils.aesDecrypt(encryptBizData, ivStr[0], ivStr[1]);
			// LOGGER.info("bizData=[{}]" + bizData);
		} catch (Exception e) {
//			writeErrorResponse(httpResponse, "500", "报文解密失败：" + e.getMessage());
//			return;
		}
		/* 步骤5：验签 */
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaeAZwEs3J3wTefDRUox+tYyr/jaeWolx5rmPywkzsmNFI2Qc3HQb4A3nwqbwDE7+0LKgrV+AdVeR3x3Vuc+f4piZelupsAbYsXMH4ajvhZ+v8JhtgLoJkAYek9Enu6XhuiiVE7tZhDhMUe5Rr2PIQTUZ4MHXCWEi8e24HCH6rvQIDAQAB";
		String signContent = responseHeader.getAppId() + responseHeader.getTimestamp() + responseHeader.getNonce()
				+ bizData;
		boolean signValid = false;
		try {
			String signSourceHash = DigestUtil.sha256Hex(signContent);
			signValid = EncryptUtils.verifySign(SignAlgorithm.SHA256withRSA.getValue(), signSourceHash,
					responseHeader.getSign(), publicKey);
		} catch (Exception e) {
//			writeErrorResponse(httpResponse, "500", "签名验证失败：" + e.getMessage());
//			return;
		}
//		LOGGER.info("message request signValid=[{}]", signValid);
//		if (!signValid) {
//			writeErrorResponse(httpResponse, "403", "签名无效");
//			return;
//		}
        IO.println(bizData);
		// 3. 提取业务数据并转换为目标类型（比如原接口期望的返回类型）
		return objectMapper.convertValue(objectMapper.readValue(bizData, objectMapper.constructType(type)), objectMapper.constructType(type));
	}
}
