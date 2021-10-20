package com.fib.commons.security.support.sm;

import static cn.hutool.crypto.Mode.CBC;

import com.fib.commons.security.AbstractSecurityEncryptor;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * SM4国密对称加密算法
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-10-20
 */
public class SM4Encryptor extends AbstractSecurityEncryptor {
	private static final String IV = "iviviviviviviviv";

	@Override
	public byte[] encrypt(byte[] plainText, byte[] securityKey) {
		SymmetricCrypto sm4 = new SM4(CBC, Padding.PKCS5Padding, securityKey, IV.getBytes(CharsetUtil.CHARSET_UTF_8));
		return sm4.encrypt(plainText);
	}

	@Override
	public byte[] decrypt(byte[] cipherText, byte[] securityKey) {
		SymmetricCrypto sm4 = new SM4(CBC, Padding.PKCS5Padding, securityKey, IV.getBytes(CharsetUtil.CHARSET_UTF_8));
		return sm4.decrypt(cipherText);
	}
}