package com.fib.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.date.LocalDateTimeUtil;

public class JVMTest {
	static List<Integer> list = new ArrayList<>();

	public static void main(String[] args) {
		System.out.println("aaa");
		System.out.println(System.currentTimeMillis());
		System.out.println(LocalDateTimeUtil.parse("2040-12-12 12:23:23", "yyyy-MM-dd HH:mm:ss"));
		System.setProperty("jdk.virtualThreadScheduler.parallelism", "5");
		// 开启线程 统计平台线程数
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(() -> {
			ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
			ThreadInfo[] threadInfo = threadBean.dumpAllThreads(false, false);
			updateMaxThreadNum(threadInfo.length);
		}, 10, 10, TimeUnit.MILLISECONDS);
		try {
			// 线程睡眠 0.5 s，模拟业务处理
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException ignored) {
		}
		long start = System.currentTimeMillis();

		for (int i = 0; i < 1000000; i++) {
			Thread.startVirtualThread(() -> {
				// System.out.println("Virtual Thread test1.");
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}

		scheduledExecutorService.shutdown();
		System.out.println("max：" + list.get(0) + " platform thread/os thread");
		System.out.printf("totalMillis：%dms\n", System.currentTimeMillis() - start);
	}

	// 更新创建的平台最大线程数
	private static void updateMaxThreadNum(int num) {
		if (list.isEmpty()) {
			list.add(num);
		} else {
			Integer integer = list.get(0);
			if (num > integer) {
				list.add(0, num);
			}
		}
	}
}
