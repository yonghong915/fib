package com.fib.core;

import static org.junit.Assert.assertTrue;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.core.base.entity.ErrorCodeEntity;
import com.fib.core.base.service.IErrorCodeService;

import cn.hutool.core.util.StrUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
class ErrorCodeServiceTest {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IErrorCodeService errorCodeService;

	private Locale DEFAULT_LOCALE = Locale.getDefault();

	@Test
	void testGetErrorCodeList() {
		ErrorCodeEntity entity = new ErrorCodeEntity();
		String errorCode1 = "E000009";
		entity.setErrorCode(errorCode1);
		entity.setLanguage(DEFAULT_LOCALE.toString());
		for (int i = 0; i < 5; i++) {
			ErrorCodeEntity errorCode = errorCodeService.findByKey(entity);
			String params = "LpcbcCommunicationService";
			String errorDesc = errorCode.getErrorDesc();
			logger.info(errorCode + "   " + StrUtil.format(errorDesc, params));

		}
		assertTrue(true);
	}
}
