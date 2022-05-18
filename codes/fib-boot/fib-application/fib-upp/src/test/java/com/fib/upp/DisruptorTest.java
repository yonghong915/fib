package com.fib.upp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.autoconfigure.disruptor.DisruptorTemplate;
import com.fib.autoconfigure.disruptor.event.DisruptorBindEvent;
import com.fib.autoconfigure.disruptor.executor.Executor;
import com.fib.commons.util.CommUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class DisruptorTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(DisruptorTest.class);
	@Autowired
	private DisruptorTemplate disruptorTemplate;

	@Test
	public void testDisruptor() {
		AtomicInteger counter = new AtomicInteger(0);
		int threadCnt = 10;
		ExecutorService executor = Executors.newFixedThreadPool(threadCnt);
		for (int i = 0; i < 10; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					DisruptorBindEvent event = new DisruptorBindEvent(this, "message " + Math.random());
					String random = CommUtils.getRandom("0123456789", 5);
					Map<String, Object> map = new HashMap<>();
					map.put("random", random);
					map.put("counter", counter.incrementAndGet());
					event.setMsg(map);
					Executor executor = new Executor() {
						@Override
						public void onExecute(Object msg) {
							LOGGER.info("执行自己业务逻辑:{}", msg);
						}
					};
					event.setExecutor(executor);
					disruptorTemplate.publishEvent(event);
				}
			});
		}
		executor.shutdown();
		Assert.assertTrue(true);
	}
}