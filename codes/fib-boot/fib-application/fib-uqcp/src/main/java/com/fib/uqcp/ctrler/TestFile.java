package com.fib.uqcp.ctrler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;

public class TestFile {

	public static void main(String[] args) {

		File file = new File("F:/test.txt");
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 1; i <= 100; i++) {
			FileUtil.appendUtf8String("Data " + i + "\r\n", file);
		}
		
		List<CompletableFuture<List<String>>> futures = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			int row = 0;
			// 读取读取文件内容并打印
			List<String> data = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				data.add(line);
				row++;
				if (row == 20) {
					List<String> tmp = CollUtil.newCopyOnWriteArrayList(data);
					enterFu(tmp, futures);
					row = 0;
					data.clear();
				}
			}

//			CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> performQuery(data));
//			futures.add(future);
		} catch (Exception e) {
		}

//		// 创建CompletableFuture列表，用于存储每个查询的结果
//		List<CompletableFuture<List<String>>> futures = new ArrayList<>();
//		// 每个CompletableFuture负责查询一部分数据
//		int batchSize = 20;
//		for (int i = 0; i < data.size(); i += batchSize) {
//			int endIndex = Math.min(i + batchSize, data.size());
//			List<String> subList = data.subList(i, endIndex);
//			CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> performQuery(subList));
//			futures.add(future);
//		}

		// 使用CompletableFuture的allOf方法等待所有查询完成
		CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

		// 当所有查询完成时，对所有结果进行合并
		CompletableFuture<List<String>> mergedResult = allFutures
				.thenApply(v -> futures.stream().map(CompletableFuture::join).flatMap(List::stream).collect(Collectors.toList()));

		// 阻塞等待合并结果
		List<String> result = mergedResult.join();
		// 输出合并后的结果
		System.out.println("Merged Result:");
		for (String r : result) {
			System.out.println(r);
		}

		Executor executor = asyncThreadPoolExecutor();

		executor = Executors.newVirtualThreadPerTaskExecutor();

		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			System.out.println("thread name:" + Thread.currentThread().getName() + " first step...");
			return "hello";
		}, executor);
		allFutures = CompletableFuture.allOf(future);
		CompletableFuture<String> resultFuture = allFutures.thenApply(v -> {
			try {
				String rtnStr = future.get();
				return rtnStr;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		String s = resultFuture.join();
		System.out.println(s);
		// executor.shutdown();
	}

	private static void enterFu(List<String> data, List<CompletableFuture<List<String>>> futures) {
		CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> performQuery(data));
		futures.add(future);
	}

	public static <T, V> List<V> handle4Async(List<T> dataList, Function<T, V> execFunction) {
		ExecutorService executorService = Executors.newFixedThreadPool(dataList.size());
		try {
			return handle4Async(dataList, executorService, execFunction);
		} finally {
			executorService.shutdown();
		}
	}

	public static <T, V> List<V> handle4Async(List<T> dataList, ExecutorService executorService, Function<T, V> execFunction) {
		if (CollUtil.isEmpty(dataList)) {
			return Collections.emptyList();
		}
		List<V> resultList = new ArrayList<>();
		List<CompletableFuture<V>> completableFutures = dataList.stream().map(eachList -> CompletableFuture
				.supplyAsync(() -> execFunction.apply(eachList), executorService).whenComplete((v, e) -> resultList.add(v))).toList();
		CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new)).join();
		return resultList;
	}

	public static void vv(boolean isV) {

	}

	private static List<String> performQuery(List<String> data) {
		// 在这里进行查询操作，返回查询结果

		// 模拟查询耗时
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 返回查询结果
		List<String> result = new ArrayList<>();
		for (String item : data) {
			result.add("Result for " + item);
		}
		return result;
	}

	@Bean(value = "customAsyncTaskExecutor")
	static Executor asyncThreadPoolExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(5);
		threadPoolTaskExecutor.setMaxPoolSize(10);
		threadPoolTaskExecutor.setKeepAliveSeconds(60);
		threadPoolTaskExecutor.setQueueCapacity(2048);
		threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		threadPoolTaskExecutor.setThreadNamePrefix("customAsyncTaskExecutor-");
		threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor;
	}

	@Bean(value = "threadPoolExecutor")
	ThreadPoolExecutor threadPoolExecutor() {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000),
				new ThreadPoolExecutor.CallerRunsPolicy());
		return threadPoolExecutor;
	}
}
