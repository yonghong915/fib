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
import com.fib.upp.util.configuration.jconfig.Configuration;

import cn.hutool.core.io.FileUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class TestConfig {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	IConfig fibConfiguration;

	@Test
	public void testConfig() {
		logger.info("testConfig....");
		String filePath = "config" + FileUtil.FILE_SEPARATOR + "syspara";
		Object retObj1 = fibConfiguration.load(filePath);
		logger.info("retObj1={}", retObj1);

		Configuration config = (Configuration) retObj1;
		String categoryName = "sysVersion";
		String propertyName = "sysVersion";
		String value = config.getProperty(propertyName, null, categoryName);
		System.out.println("propValue=" + value);
		System.out.println("varege ${cnaps2_monitor-port}="+config.getVariable("gatewayConfig", "cnaps2_monitor-port"));
		Object retObj2 = fibConfiguration.load(filePath);
		logger.info("retObj2={}", retObj2);

		String sendUrl = config.getVariable("gatewayConfig", "cnaps2_bosent_cnaps2_send_url");
		logger.info("sendUrl={}", sendUrl);

		categoryName = "services";
		propertyName = "IbpsMQServiceA";
		Configuration config2 = (Configuration) retObj2;
		String value2 = config2.getProperty(propertyName, null, categoryName);
		System.out.println("value2=" + value2);
	}
}
