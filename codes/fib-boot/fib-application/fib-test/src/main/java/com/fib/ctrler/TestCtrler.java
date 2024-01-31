package com.fib.ctrler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.core.threadpool.ThreadPoolExecutorMdcWrapper;

@RestController
public class TestCtrler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping("/teststory")
	public String test() {
		logger.info("......test.........");

		ThreadPoolTaskExecutor threadPool = new ThreadPoolExecutorMdcWrapper();
		threadPool.initialize();
		threadPool.execute(() -> {
			logger.info("处理子线程事务");
		});

		return "OK";
	}
}
