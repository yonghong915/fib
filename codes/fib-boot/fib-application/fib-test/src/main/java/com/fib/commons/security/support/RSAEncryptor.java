package com.fib.commons.security.support;

import com.fib.commons.security.AbstractSecurityEncryptor;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;

public class RSAEncryptor extends AbstractSecurityEncryptor {

	@Override
	public byte[] encrypt(byte[] plainText, byte[] publicKey) {
		RSA rsa = SecureUtil.rsa(null, publicKey);
		return rsa.encrypt(plainText, KeyType.PublicKey);
	}

	@Override
	public byte[] decrypt(byte[] cipherText, byte[] privateKey) {
		RSA rsa = SecureUtil.rsa(privateKey, null);
		return rsa.decrypt(cipherText, KeyType.PrivateKey);
	}

	@Override
	public byte[] sign(byte[] data, byte[] privateKey) {
		Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA, privateKey, null);
		return sign.sign(data);
	}

	@Override
	public boolean verify(byte[] data, byte[] signedData, byte[] publicKey) {
		Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA, null, publicKey);
		return sign.verify(data, signedData);
	}
}