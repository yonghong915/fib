package com.fib.jvm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleFactory {
	private volatile static SingleFactory singleFactory;

	private SingleFactory() {
	}

	public static SingleFactory getInstance() {
		if (null == singleFactory) {
			synchronized (SingleFactory.class) {
				if (null == singleFactory) {
					singleFactory = new SingleFactory();
				}
			}
		}
		return singleFactory;
	}

	public static void main(String[] args) {
		ExecutorService service = Executors.newFixedThreadPool(50);
		for (int i = 0; i < 1000; i++) {
			service.submit(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + "  " + SingleFactory.getInstance());
				}
			});
		}
		service.shutdown();
	}
}
