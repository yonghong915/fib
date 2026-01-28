package com.fib.mall.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

/**
 * 接口安全工具类：AES加解密、HMAC-SHA256签名
 */
@Component
public class SecurityUtils {

	// AES算法
	private static final String AES_ALGORITHM = "AES";
	// 签名算法
	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

	/**
	 * AES加密（ECB模式，PKCS5Padding填充）
	 */
	public String aesEncrypt(String content, String aesKey) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM + "/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	/**
	 * AES解密
	 */
	public String aesDecrypt(String cipherText, String aesKey) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM + "/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
		return new String(decryptedBytes, StandardCharsets.UTF_8);
	}

	/**
	 * HMAC-SHA256签名
	 */
	public String hmacSign(String content, String signKey) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(signKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
		mac.init(secretKey);
		byte[] signBytes = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
		return bytesToHex(signBytes);
	}

	/**
	 * 验证签名
	 */
	public boolean verifySign(String content, String signKey, String sign) throws Exception {
		String calculatedSign = hmacSign(content, signKey);
		return calculatedSign.equalsIgnoreCase(sign);
	}

	/**
	 * 生成随机nonce（32位）
	 */
	public String generateNonce() {
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}

	/**
	 * 字节数组转16进制字符串
	 */
	private String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
