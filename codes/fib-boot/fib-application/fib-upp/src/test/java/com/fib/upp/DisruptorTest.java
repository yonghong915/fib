package com.fib.upp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.autoconfigure.disruptor.DisruptorTemplate;
import com.fib.autoconfigure.disruptor.event.DisruptorBindEvent;
import com.fib.commons.util.CommUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class DisruptorTest {
	@Autowired
	private DisruptorTemplate disruptorTemplate;

	@Test
	public void testDisruptor() {
		String[] tags = { "TagA-Output", "TagB-Output" };
		AtomicInteger counter = new AtomicInteger(0);
		int threadCnt = 10;
		ExecutorService executor = Executors.newFixedThreadPool(threadCnt);
		for (int i = 0; i < 10; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					DisruptorBindEvent event = new DisruptorBindEvent(this, "message " + Math.random());
					/// Event-Output/TagA-Output/**
					/// Event-Output/TagB-Output/**
					String random = CommUtils.getRandom("01", 1);
					event.setEvent("Event-Output");
					event.setTag(tags[Integer.parseInt(random)]);
					event.setKey("id-" + Math.random() + "-" + Thread.currentThread().getName() + " counter=" + counter.incrementAndGet());
                    event.setBody("body=12323444");
                    
                   // Session session = new DefaultSession(null);
                    //event.bind(session);
					disruptorTemplate.publishEvent(event);
				}
			});
		}
		executor.shutdown();
		Assert.assertTrue(true);
	}
}