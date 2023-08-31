package com.fib.sync;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;

public class ASyncTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ASyncTest.class);

	public static void main(String[] args) {
		LOGGER.info("begin.....");

		List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee", "fff");
		test(list, k -> {
			return Thread.currentThread().getName() + " result";
		});
	}

	public static List<String> test(List<String> list, Function<? super String, ? extends String> fun) {
		ExecutorService executor = Executors.newFixedThreadPool(list.size());
		List<CompletableFuture<String>> futureList = list.stream().map(str -> CompletableFuture.supplyAsync(() -> {
			return fun.apply(str) + "";
		}, executor)).toList();

		CompletableFuture<Void> allFuture = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]));
		CompletableFuture<List<String>> resultFuture = allFuture.thenApply(v -> futureList.stream().map(CompletableFuture::join).toList());
		try {
			LOGGER.info("result={}", resultFuture.get());
			return resultFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("Failed to ....", e);
			Thread.currentThread().interrupt();
		} finally {
			executor.shutdown();
		}
		return Collections.emptyList();
	}
}
