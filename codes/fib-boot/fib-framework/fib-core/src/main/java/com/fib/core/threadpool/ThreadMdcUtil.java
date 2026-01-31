package com.fib.core.threadpool;

import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.MDC;

import com.fib.core.util.ConstantUtil;

import cn.hutool.core.util.IdUtil;

public class ThreadMdcUtil {
	private ThreadMdcUtil() {
	}

	public static void setTraceIdIfAbsent() {
		if (MDC.get(ConstantUtil.TRACE_ID) == null) {
			MDC.put(ConstantUtil.TRACE_ID, IdUtil.simpleUUID());
		}
	}

	public static void remove() {
		MDC.remove(ConstantUtil.TRACE_ID);
	}

	public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
		return () -> {
			if (context == null) {
				MDC.clear();
			} else {
				MDC.setContextMap(context);
			}
			setTraceIdIfAbsent();
			try {
				return callable.call();
			} finally {
				MDC.clear();
			}
		};
	}

	public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
		return () -> {
			if (context == null) {
				MDC.clear();
			} else {
				MDC.setContextMap(context);
			}
			setTraceIdIfAbsent();
			try {
				runnable.run();
			} finally {
				MDC.clear();
			}
		};
	}
}