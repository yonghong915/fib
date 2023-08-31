package com.fib.sync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import com.google.common.collect.Lists;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;

public class AsyncUtil {
	private AsyncUtil() {
		// nothing to do
	}

	public static <T, V> List<V> partitionCall2ListAsync(List<T> dataList, int size, Function<List<T>, List<V>> execFunction) {
		if (CollUtil.isEmpty(dataList)) {
			return Collections.emptyList();
		}
		Assert.isTrue(size > 0, () -> new IllegalArgumentException("size must not be a minus"));

		List<V> resultList = new ArrayList<>();
		List<List<T>> partitionLst = Lists.partition(dataList, size);
		ExecutorService executorService = Executors.newFixedThreadPool(partitionLst.size());
		try {
			List<CompletableFuture<List<V>>> completableFutures = partitionLst.stream().map(eachList -> CompletableFuture
					.supplyAsync(() -> execFunction.apply(eachList), executorService).whenComplete((v, e) -> resultList.addAll(v))).toList();
			CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new)).join();
			return resultList;
		} finally {
			executorService.shutdown();
		}
	}

	public static <T, V> List<V> partitionCall2ListAsync(List<T> dataList, int size, ExecutorService executorService,
			Function<List<T>, List<V>> execFunction) {
		if (CollUtil.isEmpty(dataList)) {
			return Collections.emptyList();
		}
		Assert.isTrue(size > 0, () -> new IllegalArgumentException("size must not be a minus"));

		List<V> resultList = new ArrayList<>();
		List<CompletableFuture<List<V>>> completableFutures = Lists.partition(dataList, size).stream().map(eachList -> CompletableFuture
				.supplyAsync(() -> execFunction.apply(eachList), executorService).whenComplete((v, e) -> resultList.addAll(v))).toList();
		CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new)).join();
		return resultList;
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
}