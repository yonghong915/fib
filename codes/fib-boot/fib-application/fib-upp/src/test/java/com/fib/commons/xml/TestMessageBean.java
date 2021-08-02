package com.fib.commons.xml;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.upp.UppApplication;
import com.fib.upp.service.gateway.IConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class TestMessageBean {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IConfig messageBeanConfigService;

	@Test
	public void testMessageBeanConfig() {
		logger.info("testConfig....");
		String fileName = "E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-application\\fib-upp\\src\\main\\resources\\config\\message-bean";
		String channleId = "abcdegee";
		Object retObj1 = messageBeanConfigService.load(channleId, fileName);
		logger.info("retObj1={}", retObj1);

		retObj1 = messageBeanConfigService.load(channleId, fileName);
		logger.info("retObj1={}", retObj1);

		retObj1 = messageBeanConfigService.load(channleId, fileName);
		logger.info("retObj1={}", retObj1);

	}
}
