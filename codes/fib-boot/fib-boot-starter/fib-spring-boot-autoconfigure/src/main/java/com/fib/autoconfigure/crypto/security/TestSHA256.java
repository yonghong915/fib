package com.fib.autoconfigure.crypto.security;

import org.apache.dubbo.rpc.model.ScopeModelUtil;

import com.fib.autoconfigure.crypto.security.support.sha.SHA256;
import com.fib.autoconfigure.crypto.security.support.sha.SHA256Encryptor;
import com.fib.autoconfigure.crypto.security.support.sm.SM3Encryptor;
import com.fib.commons.util.CommUtils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.HexUtil;

public class TestSHA256 {

	public static void main(String[] args) {
		AbstractSecurityEncryptor encryptor = new SHA256Encryptor();
		String plainText = "123456";
		byte[] encryptedTxt = encryptor.encrypt(plainText.getBytes(), CommUtils.randomBytes(8));
		System.out.println("encryptedTxt=" + HexUtil.encodeHexStr(encryptedTxt));

		System.out.println(validatePwd(plainText, HexUtil.encodeHexStr(encryptedTxt)));

		SecurityEncryptor encryptor1 = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM3");
		encryptedTxt = encryptor1.encrypt(plainText.getBytes(), CommUtils.randomBytes(8));
		System.out.println("encryptedTxt=" + HexUtil.encodeHexStr(encryptedTxt));

		System.out.println(SecurityEncryptor.class.isAssignableFrom(SM3Encryptor.class));
	}

	public static boolean validatePwd(String plainPwd, String pwd) {
		Assert.notBlank(plainPwd);
		Assert.notBlank(pwd);
		String salt = pwd.substring(0, 16);
		SHA256 sha256 = new SHA256(HexUtil.decodeHex(salt));
		String rtn = sha256.digestHex(plainPwd);
		return pwd.equals(salt + rtn);
	}
}