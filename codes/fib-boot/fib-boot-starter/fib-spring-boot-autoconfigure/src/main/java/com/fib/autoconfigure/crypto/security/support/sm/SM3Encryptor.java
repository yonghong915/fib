package com.fib.autoconfigure.crypto.security.support.sm;

import com.fib.autoconfigure.crypto.security.AbstractSecurityEncryptor;

import cn.hutool.crypto.SmUtil;

/**
 * SM3
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-31 17:06:39
 */
public class SM3Encryptor extends AbstractSecurityEncryptor {
	@Override
	public byte[] encrypt(byte[] plainText, byte[] securityKey) {
		return SmUtil.sm3WithSalt(securityKey).digest(plainText);
	}

	@Override
	public byte[] decrypt(byte[] cipherText, byte[] securityKey) {
		throw new RuntimeException("SM3 can not decrypt method.");
	}
}