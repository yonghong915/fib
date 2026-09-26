package com.fib;

import java.security.SecureRandom;
import java.util.HexFormat;

public class SecureRandomUtil {
    private static final ThreadLocal<SecureRandom> RANDOM_TL =
            ThreadLocal.withInitial(SecureRandom::new);

    private SecureRandomUtil() {}

    private static SecureRandom getRandom() {
        return RANDOM_TL.get();
    }

    public static int nextInt(int bound) {
        return getRandom().nextInt(bound);
    }

    public static byte[] nextBytes(int len) {
        byte[] bytes = new byte[len];
        getRandom().nextBytes(bytes);
        return bytes;
    }

    public static long nextLong() {
        return getRandom().nextLong();
    }

    /**
     * 生成十六进制随机字符串
     * @param byteLen 原始随机字节长度，输出字符串长度 = byteLen * 2
     * @return 小写16进制串
     */
    public static String nextHexString(int byteLen) {
        byte[] bytes = nextBytes(byteLen);
        return HexFormat.of().formatHex(bytes);
    }

    /**
     * 大写十六进制
     */
    public static String nextHexUpperString(int byteLen) {
        byte[] bytes = nextBytes(byteLen);
        return HexFormat.of().withUpperCase().formatHex(bytes);
    }
}
