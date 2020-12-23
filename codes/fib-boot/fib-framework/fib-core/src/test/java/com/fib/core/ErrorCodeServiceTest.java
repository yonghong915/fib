package com.fib.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.core.base.datasource.DataSourceProperties;
import com.fib.core.base.entity.ErrorCodeEntity;
import com.fib.core.base.service.IErrorCodeService;
import com.fib.core.config.BloomFilterConfig;
import com.fib.core.util.RedisUtil;

import cn.hutool.core.util.StrUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
class ErrorCodeServiceTest {

	@Autowired
	private IErrorCodeService errorCodeService;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private BloomFilterConfig bloomFilterConfig;

	private Locale DEFAULT_LOCALE = Locale.getDefault();

	@Autowired
	private DataSourceProperties dataSourceProperties;

	@Test
	void testGetErrorCodeList() {
		System.out.println("dataSourceProperties=" + dataSourceProperties);
		ErrorCodeEntity entity = new ErrorCodeEntity();
		String errorCode1 = "E000009";
		// Object obj = redisUtil.get(DEFAULT_LOCALE.toString() + "~" + errorCode1);
		// System.out.println(obj);
		entity.setErrorCode(errorCode1);
		entity.setLanguage(DEFAULT_LOCALE.toString());
		for (int i = 0; i < 10; i++) {
			ErrorCodeEntity errorCode = errorCodeService.findByKey(entity);
			String params = "LpcbcCommunicationService";
			System.out.println("errorCode=" + errorCode);
			String errorDesc = errorCode.getErrorDesc();
			System.out.println(StrUtil.format(errorDesc, params));

		}
		assertEquals(100, 100);
	}
}
