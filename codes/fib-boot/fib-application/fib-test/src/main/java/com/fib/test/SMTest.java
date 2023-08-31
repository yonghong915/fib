package com.fib.test;

import java.util.Date;

import cn.hutool.crypto.SmUtil;

public class SMTest {
	
    public void testGenPri() {
    	SmUtil.sm2();
    }
    
    public static void main(String[] args) {
		System.out.println(new Date(1667722920000l));
	}
}
