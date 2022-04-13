package com.fib.commons.util.security;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

import cn.hutool.core.util.StrUtil;

/**
 * Jasypt加密解密
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-23
 */
public class JasyptUtil {

	private static final String ALGORITHM = "PBEWithMD5AndDES";
	private static final String DEFAULT_SECRET_KEY = "EWRREWRERWECCCXC";

	private JasyptUtil() {
	}

	public static String encrypt(String plainText, String securityKey) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
		encryptor.setAlgorithm(ALGORITHM);
		encryptor.setPassword(StrUtil.isEmptyIfStr(securityKey) ? DEFAULT_SECRET_KEY : securityKey);
		encryptor.setConfig(config);
		return encryptor.encrypt(plainText);
	}

	public static String decrypt(String cipherText, String securityKey) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
		encryptor.setAlgorithm(ALGORITHM);
		encryptor.setPassword(StrUtil.isEmptyIfStr(securityKey) ? DEFAULT_SECRET_KEY : securityKey);
		encryptor.setConfig(config);
		return encryptor.decrypt(cipherText);
	}
}
