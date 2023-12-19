//package com.fib.sync;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import org.junit.platform.commons.util.CollectionUtils;
//
//import com.google.common.base.Preconditions;
//import com.google.common.collect.Lists;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.lang.Assert;
//import cn.hutool.core.util.RandomUtil;
//
//public class SyncTest {
//
//	public static void main(String[] args) {
//		Long start = System.currentTimeMillis();
////		final List<Integer> taskList = Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8, 9, 10);
//		ExecutorService exs = Executors.newFixedThreadPool(10);
////		List<String> list = new ArrayList<>();
////		List<String> list2 = new ArrayList<>();
////		try {
////			// 方式二：全流式处理转换成CompletableFuture[]+组装成一个无返回值CompletableFuture，join等待执行完毕。返回结果whenComplete获取
////			CompletableFuture<Integer>[] cfs = taskList.stream().map(i ->
////			// 把计算任务 交给CompletableFuture异步去处理执行
////			CompletableFuture.supplyAsync(() -> calc(i), exs)
////					// 把计算完成结果做Function处理：此处是转换成了字符串
////					.thenApply(h -> Integer.toString(h))
////					// 如需获取任务完成先后顺序，此处代码即可 会先处理先完成的任务 后处理后完成的任务 使用起来比CompletionService确实方便不少
////					.whenComplete((v, e) -> {
////						System.out.println("任务" + v + "完成!result=" + v + "，异常 e=" + e + "," + new Date());
////						list2.add(v);
////					})).toArray(CompletableFuture[]::new); // 此处直接toArray 不toList了
////
////			// 等待总任务完成，但是封装后无返回值，必须自己whenComplete()获取 此处使用join来获取结果
////			CompletableFuture.allOf(cfs).join();
////			System.out.println("任务完成先后顺序，结果list2=" + list2 + "；任务提交顺序，结果list=" + list + ",耗时=" + (System.currentTimeMillis() - start));
////		} catch (Exception e) {
////			e.printStackTrace();
////		} finally {
////			exs.shutdown();
////		}
//		SyncTest t = new SyncTest();
//		t.test();
//	}
//
//	public void test() {
//		Long start = System.currentTimeMillis();
//		final List<Integer> taskList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
//		System.out.println("准备工作处理......");
//		List<Integer> rtnList = AsyncUtil.handle4Async(taskList, this::someCall);
//		System.out.println("rtnList=" + rtnList);
//		System.out.println("任务完成先后顺序，结果list2=" + rtnList + "；任务提交顺序，结果list=" + taskList + ",耗时=" + (System.currentTimeMillis() - start));
//		System.out.println("结束工作处理....");
//	}
//
//	private Integer someCall(Integer eachList) {
//		List<Integer> rtnList = new ArrayList<>();
//		try {
//			int sleepTime = RandomUtil.randomInt(1000, 2000);
//			System.out.println(Thread.currentThread().getName() + "  eachList=" + eachList + "  sleepTime=" + sleepTime);
//			TimeUnit.MILLISECONDS.sleep(sleepTime);
//		} catch (InterruptedException e) {
//
//			eachList += 10;
//			rtnList.add(eachList);
//		}
//		return eachList;
//	}
//}
