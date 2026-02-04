package com.fib.cmms;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;

public class Test {

	public static void main(String[] args) {
		String data = "123456";

		String rtn = digest(data);
		System.out.println("rtn1=" + rtn);

		rtn = digest(data);
		System.out.println("rtn2=" + rtn);

		System.out.println(validatePwd(data, rtn));
	}

	public static boolean validatePwd(String plainPwd, String pwd) {
		String salt = pwd.substring(0, 16);
		SHA256 sha256 = new SHA256(HexUtil.decodeHex(salt));
		String rtn = sha256.digestHex(plainPwd);
		return pwd.equals(salt + rtn);
	}

	public static String digest(String data) {
		byte[] salt = randomBytes(8);
		SHA256 sha256 = new SHA256(salt);
		String rtn = sha256.digestHex(data);
		String saltStr = HexUtil.encodeHexStr(salt);
		return saltStr + rtn;
	}

	public static byte[] randomBytes(final int length) {
		final byte[] bytes = new byte[length];
		RandomUtil.getRandom(true).nextBytes(bytes);
		return bytes;
	}
}
