package com.fib.autoconfigure.crypto.security;

/**
 * 安全加密器抽象类
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-10-19
 */
public abstract class AbstractSecurityEncryptor implements SecurityEncryptor {

	@Override
	public byte[] sign(byte[] data, byte[] privateKey) {
		return null;
	}

	@Override
	public boolean verify(byte[] data, byte[] sign, byte[] publicKey) {
		return false;
	}
}