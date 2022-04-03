package com.fib.autoconfigure.retry;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.fib.commons.exception.BusinessException;

@Component("retryService")
public class RetryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RetryService.class);

	@Autowired
	private RetryTemplate retryTemplate;

	public Object retryService(Function<Object, ?> func, Function<Object, ?> failFunc, Object value)
			throws BusinessException {
		LOGGER.info("retryService...");
		return retryTemplate.execute(new RetryCallback<Object, BusinessException>() {
			@Override
			public Object doWithRetry(final RetryContext context) throws BusinessException {
				LOGGER.info("doWithRetry---");
				return func.apply(value);
			}
		}, new RecoveryCallback<Object>() {
			@Override
			public Object recover(final RetryContext context) throws BusinessException {
				LOGGER.info("recover---");
				return failFunc.apply(value);
			}
		});
	}
}