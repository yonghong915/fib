//package com.fib.test;
//
//import java.util.concurrent.Executor;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//public class ThreadPoolTest {
//	public static void main(String[] args) {
//		System.out.println(Runtime.getRuntime().availableProcessors());
//		int corePoolSize = 5;
//		int maximumPoolSize = 10;
//		long keepAliveTime = 1L;
//		int queueSize = 10;
//		ExecutorService threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
//				new LinkedBlockingQueue<>(queueSize), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
//
//		try {
//			// 模拟10个用户来办理业务，每个用户就是来自外部的请求线程
//			for (int i = 1; i <= 20; i++) {
//				threadPool.execute(() -> {
//					try {
//						TimeUnit.SECONDS.sleep(1);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					System.out.println(Thread.currentThread().getName() + "\t 办理业务");
//				});
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			threadPool.shutdown();
//		}
//	}
//
//	// 获取当前机器的核数
//	public static final int cpuNum = Runtime.getRuntime().availableProcessors();
//
//	public Executor getAsyncExecutor() {
//		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//		taskExecutor.setCorePoolSize(cpuNum);// 核心线程大小
//		taskExecutor.setMaxPoolSize(cpuNum * 2);// 最大线程大小
//		taskExecutor.setQueueCapacity(500);// 队列最大容量
//		// 当提交的任务个数大于QueueCapacity，就需要设置该参数，但spring提供的都不太满足业务场景，可以自定义一个，也可以注意不要超过QueueCapacity即可
//		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
//		taskExecutor.setAwaitTerminationSeconds(60);
//		taskExecutor.setThreadNamePrefix("BCarLogo-Thread-");
//		taskExecutor.initialize();
//		return taskExecutor;
//	}
//}