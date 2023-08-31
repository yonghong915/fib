package com.fib.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
public class TestApp {
//	public static void main(String[] args) {
//		SpringApplication.run(TestApp.class, args);
//	}
	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		int len = 10000;
		for (int i = 0; i < len; i++) {
			executor.submit(() -> {
				System.out.println(Thread.currentThread().getName());
			});
			TimeUnit.MILLISECONDS.sleep(10);
		}
		executor.shutdown();

	}
}
