package com.fib.test;

import com.fib.commons.classloader.CustomClassLoader;

public class JVMTest {

	public static void main(String[] args) {
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		System.out.println(systemClassLoader);

		ClassLoader cl = JVMTest.class.getClassLoader();
		System.out.println(cl);

		ClassLoader cl2 = cl.getParent();
		System.out.println(cl2);

		ClassLoader cl3 = cl2.getParent();
		System.out.println(cl3);

		ClassLoader thCl = Thread.currentThread().getContextClassLoader();
		System.out.println("thCl=" + thCl);

		CustomClassLoader cCl = new CustomClassLoader(thCl);
		Thread.currentThread().setContextClassLoader(cCl);
		thCl = Thread.currentThread().getContextClassLoader();
		System.out.println("thCl===" + thCl);

		new Thread(() -> {
			ClassLoader thCl1 = Thread.currentThread().getContextClassLoader();
			System.out.println("thCl1===" + thCl1);
		}).start();
	}

}
