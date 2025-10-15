//package com.fib.dom4j;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.function.Function;
//
//import javax.money.CurrencyQuery;
//import javax.money.CurrencyQueryBuilder;
//import javax.money.CurrencyUnit;
//import javax.money.Monetary;
//
//import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.core.util.IdUtil;
//
//public class VMTest {
//	static File file = new File("D:/file.txt");
//	static {
//		if (file.exists()) {
//			file.delete();
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static <T, V> List<V> handle4Async(List<T> dataList, Function<T, V> execFunction) {
//		// ExecutorService executorService =
//		// Executors.newFixedThreadPool(dataList.size());
//		ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
//		try {
//			return handle4Async(dataList, executorService, execFunction);
//		} finally {
//			executorService.shutdown();
//		}
//	}
//
//	public static <T, V> List<V> handle4Async(List<T> dataList, ExecutorService executorService, Function<T, V> execFunction) {
//		if (CollUtil.isEmpty(dataList)) {
//			return Collections.emptyList();
//		}
//		List<V> resultList = new ArrayList<>();
//		List<CompletableFuture<V>> completableFutures = dataList.stream().map(eachList -> CompletableFuture
//				.supplyAsync(() -> execFunction.apply(eachList), executorService).whenComplete((v, e) -> resultList.add(v))).toList();
//		CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new)).join();
//		return resultList;
//	}
//
//	private static ThreadLocal<Boolean> isAppenderThread = new ThreadLocal<Boolean>();
//
//	public static void main(String[] args) {
//		Logger logger = LoggerFactory.getLogger(VMTest.class);
//		MDC.put("TraceId", IdUtil.simpleUUID());
////		 org.apache.logging.log4j.Logger logger = LogManager.getLogger(VMTest.class);
//		logger.info("aaaa");
//		if (logger.isDebugEnabled()) {
//			logger.debug("日志输出，DEBUG日志---{}", "bbbbbb");
//		}
//
//		boolean lsg = AsyncLoggerContextSelector.isSelected();
//
//		new Thread(() -> {
//			logger.info("ok..............");
//		}).start();
//
//		CurrencyQuery cnyQuery = CurrencyQueryBuilder.of().setCurrencyCodes("CNY").setCountries(Locale.CHINA).setNumericCodes(-1).build();
//		Collection<CurrencyUnit> cnyCurrencies = Monetary.getCurrencies(cnyQuery);
//		System.out.println(cnyCurrencies);
//		CurrencyUnit currencyUnitChina = Monetary.getCurrency(Locale.CHINA);
//		System.out.println(currencyUnitChina.toString());
////		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
////		scheduledExecutorService.scheduleAtFixedRate(() -> {
////			ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
////			ThreadInfo[] threadInfo = threadBean.dumpAllThreads(false, false);
////			System.out.println(threadInfo.length + " os thread");
////		}, 10, 10, TimeUnit.MILLISECONDS);
////		long l = System.currentTimeMillis();
////		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
////			IntStream.range(0, 10000).forEach(i -> {
////				executor.submit(() -> {
////					Thread.sleep(Duration.ofSeconds(1));
////					System.out.println(i);
////					return i;
////				});
////			});
////		}
////		System.out.printf("耗时：%dms\n", System.currentTimeMillis() - l);
////		System.out.println(Runtime.getRuntime().availableProcessors());
//	}
////		int len = 100000;
////		List<Integer> dataList = new ArrayList<>(len);
////		for (int i = 0; i < len; i++) {
////			dataList.add(i);
////		}
////		long start = System.currentTimeMillis();
////		System.out.println("begin.........");
////
////		handle4Async(dataList, k -> handleBiz(k));
////		System.out.println("end...........................");
////		long end = System.currentTimeMillis();
////		System.out.println("duration=" + (end - start) + " ms");
////		VMTest t = new VMTest();
////		try (ExecutorService es = Executors.newVirtualThreadPerTaskExecutor();) {
////			Future<?> submit = es.submit(t.new MyThread());
//////			Object o = submit.get();
////			CompletableFuture<T>
////			IntStream.range(10000, 20000).forEach(null);
////		} catch (InterruptedException e) {
//////			e.printStackTrace();
////		} catch (ExecutionException e) {
////			e.printStackTrace();
////		}
//////
////		ThreadFactory threadFactory = Thread.ofVirtual().name("coroutine-", 0).factory();
////		Lock lock = new ReentrantLock();
////		IntStream.range(0, 10000).forEach(i -> threadFactory.newThread(() -> {
////			lock.lock();
////			try {
////				Thread.sleep(Duration.ofMillis(1));
////				System.out.println(Thread.currentThread().threadId() + " " + Thread.currentThread().getName());
////				User user = new VMTest().new User();
////				user.setId(1);
////				user.setName("blueStarWei");
////			} catch (Exception e) {
////				throw new RuntimeException(e);
////			} finally {
////				lock.unlock();
////			}
////		}).start());
//
////		org.slf4j.Logger log = LoggerFactory.getLogger(VMTest.class);
////		log.info("aaaavvvvvvvvvvvvvvvvvvvv");
////		IntStream.range(10000, 20000).forEach(i -> threadFactory.newThread(() -> {
////			try {
////				Thread.sleep(Duration.ofSeconds(10));
////				System.out.println(Thread.currentThread().threadId() + " " + Thread.currentThread().getName());
////			} catch (Exception e) {
////				throw new RuntimeException(e);
////			}
////		}).start());
////
////		try {
////			Thread.sleep((Duration.ofSeconds(100)));
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
//
////		long start = System.currentTimeMillis();
////		User user = null;
////		for (int i = 0; i < 100000000; i++) {
////			//alloc();
////			 user = new VMTest().new User();
////			user.setId(1);
////			user.setName("blueStarWei");
////		}
////		long end = System.currentTimeMillis();
////		System.out.println((end - start) + " ms");
//
//	private static Integer handleBiz(Integer k) {
//		FileUtil.appendUtf8String(String.valueOf(k) + "\n", file);
//		return k;
//	}
//
//	class MyThread implements Runnable {
//
//		@Override
//		public void run() {
//			System.out.println(new Date() + " current thread name is:" + Thread.currentThread().threadId() + "  " + Thread.currentThread().getName());
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				throw new RuntimeException(e);
//			}
//		}
//
//	}
//
////
////	private static void alloc() {
////		User user = new VMTest().new User();
////		user.setId(1);
////		user.setName("blueStarWei");
////	}
////
//	record User(int id, String name) {
//
//	}
//
//	static void print(Object obj) {
//		if (obj instanceof User u) {
//			int id = u.id();
//			String name = u.name();
//		}
//	}
//
//	static void testSwtich(int type) {
//		switch (type) {
//
//		}
//	}
//}
