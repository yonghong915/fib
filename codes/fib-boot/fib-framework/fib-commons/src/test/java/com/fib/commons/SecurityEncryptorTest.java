package com.fib.commons;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.security.SecurityEncryptor;
import com.fib.commons.web.ResultUtil;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;

public class SecurityEncryptorTest {
	private static final Logger logger = LoggerFactory.getLogger(SecurityEncryptorTest.class);

	/**
	 * 对称加密算法
	 */
	@Test
	public void testEncryptAndDecrypt4SM4() {
		String body = "明天好，继续努力1233bddg!@#*&^%$#$*())__++";
		String content = JSONUtil.toJsonStr(ResultUtil.error("000000", "sucess", body));
		logger.info("before enrypt data ：{}", content);

		String securityKey = "3pzISVLZvPXuFg7m";
		byte[] cipherTxt = ExtensionLoader.getExtensionLoader(SecurityEncryptor.class).getExtension("SM4").encrypt(
				StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), securityKey.getBytes(CharsetUtil.CHARSET_UTF_8));
		logger.info("cipherTxt={}", HexUtil.encodeHexStr(cipherTxt));

		byte[] plainTxt = ExtensionLoader.getExtensionLoader(SecurityEncryptor.class).getExtension("SM4")
				.decrypt(cipherTxt, securityKey.getBytes(CharsetUtil.CHARSET_UTF_8));
		logger.info("plainTxt={}", StrUtil.str(plainTxt, CharsetUtil.CHARSET_UTF_8));
	}

	/**
	 * 非对称加密算法
	 */
	@Test
	public void testEncryptAndDecrypt4SM2() {
		String body = "明天好，继续努力1233bddg!@#*&^%$#$*())__++";
		String content = JSONUtil.toJsonStr(ResultUtil.error("000000", "sucess", body));
		logger.info("before enrypt data ：{}", content);

		String otherpublicKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEsb6Eodd/rSsBdIAmSQulFG2ddlO75Ye/uZfk1zgaTUiSy0cBVSFOgY2AQxTL+SmGLV24oKaG6IjiJdSfcciffg==";
		byte[] cipherTxt = ExtensionLoader.getExtensionLoader(SecurityEncryptor.class).getExtension("SM2")
				.encrypt(StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), SecureUtil.decode(otherpublicKey));
		logger.info("cipherTxt={}", HexUtil.encodeHexStr(cipherTxt));

		String ownPrivateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgaoYFkmCx0POJqdrTzXkPqHVsgJvcTceM64+z3tKL4iygCgYIKoEcz1UBgi2hRANCAASxvoSh13+tKwF0gCZJC6UUbZ12U7vlh7+5l+TXOBpNSJLLRwFVIU6BjYBDFMv5KYYtXbigpoboiOIl1J9xyJ9+";
		byte[] plainTxt = ExtensionLoader.getExtensionLoader(SecurityEncryptor.class).getExtension("SM2")
				.decrypt(cipherTxt, SecureUtil.decode(ownPrivateKey));
		logger.info("plainTxt={}", StrUtil.str(plainTxt, CharsetUtil.CHARSET_UTF_8));
	}

	/**
	 * 非对称加密算法
	 */
	@Test
	public void testSignAndVerify4SM2() {
		String body = "明天好，继续努力1233bddg!@#*&^%$#$*())__++";
		String content = JSONUtil.toJsonStr(ResultUtil.error("000000", "sucess", body));
		logger.info("before enrypt data ：{}", content);

		String ownPrivateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgMHuFx4Ww2QToOSWNeG2XFLJtoqh10sRRaZnYp+cO1T2gCgYIKoEcz1UBgi2hRANCAATuzT9+2dtL/FztfhBHgvtinUI50M8RZBEvN8+y8nQSfjJ5yKd0OUcvj1tbL5C6bFsc8ak6PpsJbPYcpnk42Qj+";
		byte[] cipherTxt = ExtensionLoader.getExtensionLoader(SecurityEncryptor.class).getExtension("SM2")
				.sign(StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), SecureUtil.decode(ownPrivateKey));
		logger.info("cipherTxt={}", HexUtil.encodeHexStr(cipherTxt));

		String otherpublicKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE7s0/ftnbS/xc7X4QR4L7Yp1COdDPEWQRLzfPsvJ0En4yecindDlHL49bWy+QumxbHPGpOj6bCWz2HKZ5ONkI/g==";
		boolean verifyFlag = ExtensionLoader.getExtensionLoader(SecurityEncryptor.class).getExtension("SM2").verify(
				StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), cipherTxt, SecureUtil.decode((otherpublicKey)));
		logger.info("verifyFlag={}", verifyFlag);
	}

}
