package com.fib.pcms.config;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class StartJob implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void run() {
		//Objects.equals(a, b)
		logger.info(">> 启动定时任务...");
//		QuartzManager.addJob("MyJob", "MyJobGroup", "MyTrigger", "MyTriggerGroup", ButtonTimerJob.class,
//				"0/10 * 8-20 * * ?");
		QuartzManager.startJobs();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		System.out.println("启动定时任务......");
		run();
	}
}
