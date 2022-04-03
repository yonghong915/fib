package com.fib.upp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.autoconfigure.retry.RetryService;
import com.fib.commons.exception.BusinessException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class RetryTest {
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

	public String queryById(String id) {
		if ("tom".equals(id)) {
			throw new RuntimeException("aaaa");
		}
		return id;
	}

	private Object queryFailed(Object id) {
		return "failed:" + id;
	}

	private Object queryById(Object id) {
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