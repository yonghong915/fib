package com.fib.commons.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.internal.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import cn.hutool.core.io.BOMInputStream;

/**
 * 抽象文件处理通用服务(文本文件)
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-10-29
 * @param <T>
 */
public abstract class AbstractFileService<T> implements IFileService<T> {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileService.class);
	protected int corePoolSize = 20;
	protected int maxPoolSize = 100;
	protected int queueSize = 1000;
	protected int taskQueueSize = 20;

	@Override
	public T execute(String fileName) {
		T rtnValue = null;
		CountDownLatch startSignal = new CountDownLatch(1);
		long startTime = System.currentTimeMillis();
		RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
		ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 60, TimeUnit.MINUTES,
				new ArrayBlockingQueue<>(queueSize), new ThreadFactoryBuilder().setNameFormat("FileThread-%d").build(),
				handler);

		/* 预处理 */
		rtnValue = prepare();

		startSignal.countDown();

		String lineValue = null;
		List<String> lines = Lists.newArrayList();
		List<List<String>> tasks = Lists.newArrayList();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new BOMInputStream(new FileInputStream(fileName)), "UTF-8"))) {
			while ((lineValue = br.readLine()) != null) {
				lines.add(lineValue);
				if (lines.size() == taskQueueSize) {
					tasks.add(Lists.newArrayList(lines));
					lines.clear();
				}
			}
			if (!lines.isEmpty()) {
				tasks.add(Lists.newArrayList(lines));
			}
		} catch (IOException e) {
			LOGGER.error("文件读取失败", e);
		}

		int len = tasks.size();
		CountDownLatch doneSignal = new CountDownLatch(len);
		List<Future<T>> resultList = new ArrayList<>();
		for (int i = 0; i < len; i++) {
			Future<T> future = executorService.submit(new ExecSubThread(doneSignal, tasks.get(i)));
			resultList.add(future);
		}

		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		List<T> execRtnList = Lists.newArrayList();
		for (Future<T> fs : resultList) {
			try {
				if (fs.isDone()) {
					execRtnList.add(fs.get());
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		/* 执行后续处理 */
		rtnValue = afterExecute(execRtnList);
		executorService.shutdown();

		long endTime = System.currentTimeMillis();
		LOGGER.info("duration={} ms", (endTime - startTime));
		return rtnValue;
	}

	private class ExecSubThread implements Callable<T> {
		private CountDownLatch countDownLatch;
		private List<String> lines;

		public ExecSubThread(CountDownLatch countDownLatch, List<String> lines) {
			this.countDownLatch = countDownLatch;
			this.lines = lines;
		}

		@Override
		public T call() {
			// startSignal.await();
			T exeRtn = executeTask(lines);
			countDownLatch.countDown();
			return exeRtn;
		}
	}

	/**
	 * 预处理
	 */
	protected abstract T prepare();

	/**
	 * 任务执行
	 * 
	 * @param lines 行记录
	 * @return
	 */
	protected abstract T executeTask(List<String> lines);

	/**
	 * 执行后续处理
	 * 
	 * @param retList 执行任务返回结果
	 */
	protected abstract T afterExecute(List<T> execRtnList);
}