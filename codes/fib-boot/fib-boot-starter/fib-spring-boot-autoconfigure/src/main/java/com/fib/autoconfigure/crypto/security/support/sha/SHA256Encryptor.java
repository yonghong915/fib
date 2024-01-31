package com.fib.autoconfigure.crypto.security.support.sha;

import com.fib.autoconfigure.crypto.security.AbstractSecurityEncryptor;

import cn.hutool.core.util.HexUtil;

public class SHA256Encryptor extends AbstractSecurityEncryptor {

	@Override
	public byte[] encrypt(byte[] plainText, byte[] securityKey) {
		// securityKey is same to salt.
		SHA256 sha256 = new SHA256(securityKey);
		String encrypedTxt = sha256.digestHex(plainText);
		return HexUtil.decodeHex(HexUtil.encodeHexStr(securityKey) + encrypedTxt);
	}

	@Override
	public byte[] decrypt(byte[] cipherText, byte[] securityKey) {
		throw new RuntimeException("SHA256 can not decrypt method.");
	}
}
