package com.fib.autoconfigure.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.CompositeRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableConfigurationProperties(RetryProperties.class)
@ComponentScan("com.fib.autoconfigure.retry")
public class RetryAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(RetryAutoConfiguration.class);

	@Autowired
	private RetryProperties retryProperties;

	@Bean("retryTemplate")
	@ConditionalOnExpression("${fib.retry.retryCount} > 0 && ${fib.retry.timeout} > 0")
	public RetryTemplate retryTemplate() {
		LOGGER.info("重试机制：重试次数且重试时间");
		RetryTemplate retryTemplate = new RetryTemplate();

		// 每隔1s后再重试
		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(1000);
		retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

		// 最大重试次数策略
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(retryProperties.getRetryCount());

		// 在指定的一段时间内重试
		TimeoutRetryPolicy timeOutretryPolicy = new TimeoutRetryPolicy();
		timeOutretryPolicy.setTimeout(retryProperties.getTimeout());

		CompositeRetryPolicy compositeRetryPolicy = new CompositeRetryPolicy();
		compositeRetryPolicy.setPolicies(new RetryPolicy[] { retryPolicy, timeOutretryPolicy });
		compositeRetryPolicy.setOptimistic(false);

		retryTemplate.setRetryPolicy(compositeRetryPolicy);
		return retryTemplate;
	}

	@Bean("retryTemplate")
	@ConditionalOnExpression("${fib.retry.retryCount} > 0")
	public RetryTemplate retryTemplate4RetryCount() {
		LOGGER.info("重试机制：重试次数");
		return RetryTemplate.builder().maxAttempts(retryProperties.getRetryCount()).build();
	}

	@Bean("retryTemplate")
	@ConditionalOnExpression("${fib.retry.timeout} > 0")
	public RetryTemplate retryTemplate4Timeout() {
		LOGGER.info("重试机制：重试时间");
		// 在指定的一段时间内重试
		TimeoutRetryPolicy timeOutretryPolicy = new TimeoutRetryPolicy();
		timeOutretryPolicy.setTimeout(retryProperties.getTimeout());
		return RetryTemplate.builder().customPolicy(timeOutretryPolicy).build();
	}
}