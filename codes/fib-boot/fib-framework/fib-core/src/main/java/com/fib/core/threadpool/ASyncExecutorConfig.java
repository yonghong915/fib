package com.fib.core.threadpool;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步线程池配置
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-06-04
 */
@Configuration
public class ASyncExecutorConfig {
	private static final Logger logger = LoggerFactory.getLogger(ASyncExecutorConfig.class);

	@Autowired
	private CustomThreadPoolProperties customThreadPoolConfigProperties;

	@Bean
	public Executor customAsyncExcecutor() {
		logger.info("start asyncServiceExecutor");
		ThreadPoolTaskExecutor executor = new ThreadPoolExecutorMdcWrapper();
		executor.setCorePoolSize(customThreadPoolConfigProperties.getCorePoolSize());
		executor.setMaxPoolSize(customThreadPoolConfigProperties.getMaxPoolSize());
		executor.setQueueCapacity(customThreadPoolConfigProperties.getQueueCapacity());
		executor.setThreadNamePrefix(customThreadPoolConfigProperties.getNamePrefix());
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}
}