package com.fib.upp;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class Test {
	final static int POOL_SIZE = 1000;

	public static void main(String[] args) {
		// 抽奖啦
		long start = System.currentTimeMillis();
		AtomicLong counter = new AtomicLong(0);
		ExecutorService service = Executors.newFixedThreadPool(POOL_SIZE);

		ArrayList<Future> futures = new ArrayList<>(POOL_SIZE);
		for (int i = 0; i < POOL_SIZE * 1000; i++) {
			futures.add(service.submit(new Task(counter)));
		}
		// 等待所有线程执行完
		for (Future future : futures) {
			try {
				future.get();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		NumberFormat numberFormat = NumberFormat.getInstance();
		System.out.printf("统计结果为：[%s]\n", numberFormat.format(counter.get()));
		System.out.printf("耗时：[%d]毫秒", (System.currentTimeMillis() - start));
		// 关闭线程池
		service.shutdown();
	}

	static class Task implements Runnable {

		private final AtomicLong counter;

		public Task(AtomicLong counter) {
			this.counter = counter;
		}

		/**
		 * 每个线程执行N次+1操作
		 */
		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				counter.incrementAndGet();
			}
		}// end run
	}// end class

}
