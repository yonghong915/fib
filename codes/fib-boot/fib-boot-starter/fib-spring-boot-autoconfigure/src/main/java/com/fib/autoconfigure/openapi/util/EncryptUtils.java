package com.fib.autoconfigure.openapi.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.symmetric.AES;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 加密签名工具类
 */
public class EncryptUtils {
	// AES参数：256位密钥，CBC模式，PKCS5填充
	private static final int AES_KEY_SIZE = 32; // 256位
	private static final int AES_IV_SIZE = 16; // CBC模式IV长度固定16字节

	/** RSA默认加密算法 */
	public static final String RSA_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

	/**
	 * 生成随机AES密钥和IV
	 * 
	 * @return [0]密钥(Base64), [1]IV(Base64)
	 */
	public static String[] generateAesKeyIv() {
		String aesKey = Base64.encode(RandomUtil.randomBytes(AES_KEY_SIZE));
		String aesIv = Base64.encode(RandomUtil.randomBytes(AES_IV_SIZE));
		return new String[] { aesKey, aesIv };
	}

	/**
	 * AES加密业务数据
	 * 
	 * @param bizData      业务数据对象
	 * @param aesKeyBase64 Base64编码的AES密钥
	 * @param aesIvBase64  Base64编码的IV
	 * @return 加密后的数据(Base64)
	 */
	public static String aesEncrypt(String bizData, String aesKeyBase64, String aesIvBase64) {
		byte[] key = Base64.decode(aesKeyBase64);
		byte[] iv = Base64.decode(aesIvBase64);
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key, iv);
		return aes.encryptBase64(bizData, StandardCharsets.UTF_8);
	}

	/**
	 * AES解密业务数据
	 * 
	 * @param encryptedBodyBase64 加密后的业务数据(Base64)
	 * @param aesKeyBase64        Base64编码的AES密钥
	 * @param aesIvBase64         Base64编码的IV
	 * @param clazz               业务数据类型
	 * @return 解密后的业务对象
	 */
	public static String aesDecrypt(String encryptedBodyBase64, String aesKeyBase64, String aesIvBase64) {
		byte[] key = Base64.decode(aesKeyBase64);
		byte[] iv = Base64.decode(aesIvBase64);
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key, iv);
		String jsonStr = aes.decryptStr(encryptedBodyBase64, StandardCharsets.UTF_8);
		return jsonStr;
	}

	public static String[] generateKeyPair(String algorithm) {
		algorithm = StrUtil.isEmptyIfStr(algorithm) ? RSA_ALGORITHM : algorithm;

		// 生成RSA密钥对
		KeyPair keyPair = SecureUtil.generateKeyPair(algorithm);
		// 获取私钥和公钥
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		String privateKeyStr = Base64.encode(privateKey.getEncoded());
		String publicKeyStr = Base64.encode(publicKey.getEncoded());
		return new String[] { privateKeyStr, publicKeyStr };
	}

	/**
	 * RSA加密AES密钥+IV
	 * 
	 * @param aesKeyBase64 Base64编码的AES密钥
	 * @param aesIvBase64  Base64编码的IV
	 * @param publicKey    RSA公钥
	 * @return 加密后的密钥串(Base64)
	 */
	public static String rsaEncryptAesKey(String algorithm, String aesKeyBase64, String aesIvBase64, String publicKey) {
		algorithm = StrUtil.isEmptyIfStr(algorithm) ? RSA_ALGORITHM : algorithm;
		String keyIvStr = StrUtil.format("{}:{}", aesKeyBase64, aesIvBase64);
		RSA rsa = new RSA(algorithm, null, publicKey);
		return rsa.encryptBase64(keyIvStr, StandardCharsets.UTF_8, KeyType.PublicKey);
	}

	/**
	 * RSA解密AES密钥+IV
	 * 
	 * @param encryptedAesKeyBase64 RSA加密后的密钥串(Base64)
	 * @param privateKey            RSA私钥
	 * @return [0]AES密钥(Base64), [1]IV(Base64)
	 */
	public static String[] rsaDecryptAesKey(String algorithm, String encryptedAesKeyBase64, String privateKey) {
		algorithm = StrUtil.isEmptyIfStr(algorithm) ? RSA_ALGORITHM : algorithm;
		RSA rsa = new RSA(algorithm, privateKey, null);
		String keyIvStr = rsa.decryptStr(encryptedAesKeyBase64, KeyType.PrivateKey, StandardCharsets.UTF_8);
		return StrUtil.splitToArray(keyIvStr, ":");
	}

	/**
	 * 生成签名：对元数据+加密数据排序后签名
	 * 
	 * @param header        头信息（RequestHeader/ResponseHeader）
	 * @param encryptedBody 加密后的业务数据
	 * @param privateKey    RSA私钥
	 * @return 签名值(Base64)
	 */
	public static String generateSign(String signAlgorithm, String privateKey, String encryptedData) {
		signAlgorithm = StrUtil.isEmptyIfStr(signAlgorithm) ? SignAlgorithm.SHA256withRSA.getValue() : signAlgorithm;
		return Base64.encode(new Sign(SignAlgorithm.SHA256withRSA, privateKey, null).sign(encryptedData));
	}

	/**
	 * 验证签名
	 * 
	 * @param header        头信息
	 * @param encryptedBody 加密后的业务数据
	 * @param sign          签名值(Base64)
	 * @param publicKey     RSA公钥
	 * @return 是否验签通过
	 */
	public static boolean verifySign(String signAlgorithm, String signSource, String sign, String publicKey) {
		if (StrUtil.isEmptyIfStr(signSource) || StrUtil.isEmptyIfStr(sign) || StrUtil.isEmptyIfStr(publicKey)) {
			return false;
		}
		signAlgorithm = StrUtil.isEmptyIfStr(signAlgorithm) ? SignAlgorithm.SHA256withRSA.getValue() : signAlgorithm;
		return new Sign(SignAlgorithm.SHA256withRSA, null, publicKey).verify(signSource.getBytes(StandardCharsets.UTF_8),
				Base64.decode(sign));
	}

	/**
	 * 验证时间戳防重放（5分钟有效期）
	 */
	public static boolean verifyTimestamp(Long timestamp) {
		long now = System.currentTimeMillis();
		return Math.abs(now - timestamp) <= 5 * 60 * 1000L;
	}
}
