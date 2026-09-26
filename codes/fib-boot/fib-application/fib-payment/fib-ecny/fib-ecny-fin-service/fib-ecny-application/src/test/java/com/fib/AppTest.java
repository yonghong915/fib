package com.fib;


/**
 * Unit test for simple App.
 */
public class AppTest {
    static void main() {
        // 16字节 → 32位hex，常用salt/token
        String token32 = SecureRandomUtil.nextHexString(16);
       // 8字节 → 16位大写hex
        String code16 = SecureRandomUtil.nextHexUpperString(8);
        System.out.println(token32);
        System.out.println(code16);
        System.out.println(String.format("%s,%s",token32,code16));
    }
}
