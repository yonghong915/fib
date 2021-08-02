package com.fib.oims.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.oims.util.HttpConnectionPoolUtil;
import com.google.gson.JsonObject;

public class TestHttp {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testHttp() {
		logger.info("testHttp....");
		System.out.println("aaaaa");
		JsonObject retJson = HttpConnectionPoolUtil.post(null, null);
        assertEquals(10, 10);
	}
}
