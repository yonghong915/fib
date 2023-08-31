package com.fib.commons.security;

/**
 * 安全加密器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-10-20
 */
public interface SecurityEncryptor {

	/**
	 * 加密-公钥
	 * 
	 * @param plainText   明文
	 * @param securityKey 密钥(非对称为公钥)
	 * @return 密文
	 */
	byte[] encrypt(byte[] plainText, byte[] securityKey);

	/**
	 * 解密-私钥
	 * 
	 * @param cipherText  密文
	 * @param securityKey 密钥(非对称为私钥)
	 * @return 明文
	 */
	byte[] decrypt(byte[] cipherText, byte[] securityKey);

	/**
	 * 签名-私钥
	 * 
	 * @param plainText  需签名的数据
	 * @param privateKey 私钥
	 * @return 签名
	 */
	byte[] sign(byte[] data, byte[] privateKey);

	/**
	 * 验签-公钥
	 * 
	 * @param dataHex   签名的数据
	 * @param signHex   签名
	 * @param publicKey 公钥
	 * @return 是否验证通过
	 */
	boolean verify(byte[] data, byte[] sign, byte[] publicKey);
}
