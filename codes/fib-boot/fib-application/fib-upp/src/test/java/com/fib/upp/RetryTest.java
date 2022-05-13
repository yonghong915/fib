package com.fib.upp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.autoconfigure.retry.RetryService;
import com.fib.commons.exception.BusinessException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class RetryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(RetryTest.class);
	@Autowired
	private RetryService retryService;

	@Test
	public void testRetryCount() {
		queryByIdWithRetry("tom");
		assertDoesNotThrow(() -> new BusinessException("a", "bbbb"));
	}

	public Object queryByIdWithRetry(String id) {
		return retryService.retryService(this::queryById, this::queryFailed, id);
	}

	private Object queryFailed(Object id) {
		LOGGER.info("失败执行方法");
		return "failed:" + id;
	}

	private Object queryById(Object id) {
		LOGGER.info("重试执行方法");
		try {
			TimeUnit.MILLISECONDS.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if ("tom".equals(id)) {
			throw new RuntimeException("aaaa");
		}
		return id;
	}
}