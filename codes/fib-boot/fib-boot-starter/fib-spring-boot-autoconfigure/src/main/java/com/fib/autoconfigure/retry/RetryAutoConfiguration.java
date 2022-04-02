package com.fib.autoconfigure.retry;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.CompositeRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.fib.commons.exception.BusinessException;

//RetryTemplateService
@Configuration
public class RetryAutoConfiguration {

	public RetryTemplate retryTemplate(int maxAttempts, int timeout) {
		RetryTemplate retryTemplate = new RetryTemplate();

		// 每隔1s后再重试
		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(1000);
		retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

		// 最大重试次数策略
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(maxAttempts);

		// 在指定的一段时间内重试
		TimeoutRetryPolicy timeOutretryPolicy = new TimeoutRetryPolicy();
		timeOutretryPolicy.setTimeout(timeout);

		CompositeRetryPolicy compositeRetryPolicy = new CompositeRetryPolicy();
		compositeRetryPolicy.setPolicies(new RetryPolicy[] { retryPolicy, timeOutretryPolicy });
		compositeRetryPolicy.setOptimistic(false);

		retryTemplate.setRetryPolicy(compositeRetryPolicy);
		return retryTemplate;
	}

	public Object retryServicetest(Function<String, String> func, Function<String, String> failFunc, String value)
			throws BusinessException {
		RetryTemplate retryTemplate = retryTemplate(5, 10000);
		return retryTemplate.execute(new RetryCallback<Object, BusinessException>() {
			@Override
			public Object doWithRetry(final RetryContext context) throws BusinessException {
				System.out.println("doWithRetry---");
				try {
					TimeUnit.MILLISECONDS.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return func.apply(value);
			}
		}, new RecoveryCallback<Object>() {
			@Override
			public Object recover(final RetryContext context) throws BusinessException {
				System.out.println("recover---");
				return failFunc.apply(value);
			}
		});
	}

	public static void main(String[] args) {
		RetryAutoConfiguration t = new RetryAutoConfiguration();
		try {
			// t.retryServicetest(getFunctById(), " tom");
			Object ss = t.queryByIdWithRetry("tom");
			System.out.println(ss);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public Object queryByIdWithRetry(String id) {
		return retryServicetest(this::queryById, this::queryFailed, id);
	}

	private String queryFailed(String id) {
		return "failed:" + id;
	}

	public String queryById(String id) {
		if ("tom".equals(id)) {
			throw new RuntimeException("aaaa");
		}
		return id;
	}
}
