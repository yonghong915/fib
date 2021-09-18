package com.fib.msgconverter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountdownLatchTest {

	static class LatchThread implements Runnable {
		private CountDownLatch latch;

		public LatchThread(CountDownLatch latch) {
			this.latch = latch;
		}

		public LatchThread() {
			super();
		}

		@Override
		public void run() {
			synchronized (this) {
				try {
					doWork();
				} finally {
					latch.countDown();
				}
			}
		}

		private void doWork() {
			System.out.println("选手" + Thread.currentThread().getName() + "正在等待裁判发布口令");
		}

	}

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		int count = 4;
		final CountDownLatch cdOrder = new CountDownLatch(1);
		final CountDownLatch cdAnswer = new CountDownLatch(count);
		long begin = System.currentTimeMillis();

		LatchThread lt = new CountdownLatchTest.LatchThread(cdAnswer);
		for (int i = 0; i < count; i++) {
//			Runnable runnable = new Runnable() {
//				@Override
//				public void run() {
//					try {
//						System.out.println("选手" + Thread.currentThread().getName() + "正在等待裁判发布口令");
//						cdOrder.await();
//						System.out.println("选手" + Thread.currentThread().getName() + "已接受裁判口令");
//						Thread.sleep((long) (Math.random() * 10000));
//						System.out.println("选手" + Thread.currentThread().getName() + "到达终点");
//						cdAnswer.countDown();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
			service.execute(new Thread(lt));
		}

		try

		{
			Thread.sleep((long) (Math.random() * 1000));
			System.out.println("裁判" + Thread.currentThread().getName() + "即将发布口令");
			cdOrder.countDown();
			System.out.println("裁判" + Thread.currentThread().getName() + "已发送口令，正在等待所有选手到达终点");
			cdAnswer.await();
			System.out.println("所有选手都到达终点");
			System.out.println("裁判" + Thread.currentThread().getName() + "汇总成绩排名");
			long end = System.currentTimeMillis();

			System.out.println("耗费时间：" + (end - begin));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		service.shutdown();

	}
}
