package com.fib.core.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class ThreadPoolExecutorMdcWrapper extends ThreadPoolTaskExecutor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4879412615050523021L;

	@Override
	public void execute(Runnable task) {
		super.execute(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public Future<?> submit(Runnable task) {
		return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
	}
}
