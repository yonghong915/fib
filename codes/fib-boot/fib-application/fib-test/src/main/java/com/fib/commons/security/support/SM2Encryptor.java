//package com.fib.commons.security.support;
//
//import com.fib.commons.security.AbstractSecurityEncryptor;
//
//import cn.hutool.crypto.SmUtil;
//import cn.hutool.crypto.asymmetric.KeyType;
//import cn.hutool.crypto.asymmetric.SM2;
//
///**
// * SM2国密非对称加密算法
// * <p>
// * 公钥加密，私钥解密
// * </p>
// * <p>
// * 私钥签名，公钥验签
// * </p>
// * 
// * @author fangyh
// * @version 1.0.0
// * @date 2021-10-19
// */
//public class SM2Encryptor extends AbstractSecurityEncryptor {
//	@Override
//	public byte[] encrypt(byte[] plainText, byte[] publicKey) {
//		SM2 sm2 = SmUtil.sm2(null, publicKey);
//		return sm2.encrypt(plainText, KeyType.PublicKey);
//	}
//
//	@Override
//	public byte[] decrypt(byte[] cipherText, byte[] privateKey) {
//		SM2 sm2 = new SM2(privateKey, null);
//		return sm2.decrypt(cipherText, KeyType.PrivateKey);
//	}
//
//	@Override
//	public byte[] sign(byte[] data, byte[] privateKey) {
//		SM2 sm2 = SmUtil.sm2(privateKey, null);
//		return sm2.sign(data);
//	}
//
//	@Override
//	public boolean verify(byte[] data, byte[] sign, byte[] publicKey) {
//		SM2 sm2 = SmUtil.sm2(null, publicKey);
//		return sm2.verify(data, sign);
//	}
//}
