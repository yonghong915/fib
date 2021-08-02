package com.fib.commons.xml;

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
public class TestCache {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	IConfig gatewayConfigService;

	@Test
	public void testCache() {
		logger.info("testCache....");
		//String filePath = "E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-application\\fib-upp\\src\\main\\resources\\config\\gateway_cnaps2.xml";
		String filePath = "config\\gateway_cnaps2.xml";
		Object retObj1 = gatewayConfigService.load(filePath);
		logger.info("retObj1={}", retObj1);

		Object retObj2 = gatewayConfigService.load(filePath);
		logger.info("retObj2={}", retObj2);
	}
}
