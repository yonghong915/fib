package com.fib.msgconverter;

import com.fib.msgconverter.util.I18nUtil;

public class I18nTest {

	public static void main(String[] args) {
		String name = I18nUtil.INSTANCE.getString("name", "aaaa", "bbbb");
		System.out.println("name=" + name);
		new I18nTest().new MyThread().start();
		
		byte[] byts = new byte[100 * 1024 * 1024];
		
		System.out.println(byts);
		System.out.println("end");
		//while(true) {}
	}

	class MyThread extends Thread {
		@Override
		public void run() {
			byte[] byts = new byte[100 * 1024 * 1024];
			System.out.println(byts);
			while(true) {}
		}

	}
}
