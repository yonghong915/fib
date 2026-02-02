package com.fib.midbiz.service;

import java.util.Random;

import cn.hutool.core.util.RandomUtil;

public class TestJdk25 {
	final static ScopedValue<String> USER_ID = ScopedValue.newInstance();

	TestJdk25() {
	}

	TestJdk25(String rawId) {
	}


	public void processReq(String userId) {
		ScopedValue.where(USER_ID, userId).run(this::doWork);
	}

	public void doWork() {
		IO.println("aaaa=" + USER_ID.get());
	}

	void main() {
		IO.println("Hello, JDK 25!"); //
		for (int i = 0; i < 100; i++) {
			Random random = RandomUtil.getSecureRandom();
			Thread.ofVirtual().start(() -> processReq(random.nextInt() + ""));
		}
	}
}
