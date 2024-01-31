package com.fib.autoconfigure.crypto.security.support.aes;

import static cn.hutool.crypto.Mode.CBC;

import com.fib.autoconfigure.crypto.security.AbstractSecurityEncryptor;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * AES对称加密算法
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-10-20
 */
public class AESEncryptor extends AbstractSecurityEncryptor {
	private static final String IV = "iviviviviviviviv";

	@Override
	public byte[] encrypt(byte[] plainText, byte[] securityKey) {
		SymmetricCrypto aes = new AES(CBC, Padding.PKCS5Padding, securityKey, IV.getBytes(CharsetUtil.CHARSET_UTF_8));
		return aes.encrypt(plainText);
	}

	@Override
	public byte[] decrypt(byte[] cipherText, byte[] securityKey) {
		SymmetricCrypto aes = new AES(CBC, Padding.PKCS5Padding, securityKey, IV.getBytes(CharsetUtil.CHARSET_UTF_8));
		return aes.decrypt(cipherText);
	}
}
