package com.fib.core.threadpool;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步线程池配置
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-06-04
 */
@Configuration
@EnableAsync
public class ASyncExecutorConfig {
	private static final Logger logger = LoggerFactory.getLogger(ASyncExecutorConfig.class);

	@Bean("customAsyncExcecutor")
	public Executor customAsyncExcecutor(CustomThreadPoolProperties customThreadPoolConfigProperties) {
		logger.info("start customAsyncExcecutor");
		ThreadPoolExecutor.CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
		return initExcutor(customThreadPoolConfigProperties, callerRunsPolicy);
	}

	private Executor initExcutor(AbstractExecutorPool abstractExecutorPool, RejectedExecutionHandler rejectedExecutionHandler) {
		ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
		threadPool.setCorePoolSize(abstractExecutorPool.getCorePoolSize());
		threadPool.setMaxPoolSize(abstractExecutorPool.getMaxPoolSize());
		threadPool.setKeepAliveSeconds(abstractExecutorPool.getKeepAliveTime());
		threadPool.setQueueCapacity(abstractExecutorPool.getQueueCapacity());
		threadPool.setThreadNamePrefix(abstractExecutorPool.getNamePrefix());
		threadPool.setRejectedExecutionHandler(rejectedExecutionHandler);
		threadPool.initialize();
		return threadPool;
	}
}