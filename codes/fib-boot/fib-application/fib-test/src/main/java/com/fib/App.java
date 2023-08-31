package com.fib;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fib.event.CustomEntity;
import com.fib.event.CustomsEvent;
import com.fib.event.PublisherCustomsEvent;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class App {
	@Autowired
	private PublisherCustomsEvent publisherCustomsEvent;

	private static PublisherCustomsEvent initCustomsEvent;

	@PostConstruct
	public void init() {
		initCustomsEvent = publisherCustomsEvent;
	}

	public static void main(String[] args) {
		
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		SpringApplication.run(App.class, args);

		CustomEntity entity = new CustomEntity();
		entity.setServiceName("abcde");
		initCustomsEvent.publisherEvent(new CustomsEvent(entity));

		LongAdder la = new LongAdder();
		AtomicLong al = new AtomicLong(0);
		ExecutorService executor = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(500));
		for (int i = 0; i < 10; i++) {
			executor.execute(() -> {
				//synchronized (App.class) {
					la.increment();
					System.out.println("curretValue=" + la.intValue());
				//}
				System.out.println("al=" + al.incrementAndGet());
			});
		}
		long sum = la.sum();
		executor.shutdown();
		
		
	}

}
