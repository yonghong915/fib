package com.fib.gateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.gateway.util.i18n.I18nUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewayApplication.class)
public class GatewayTest {
	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testI18n() throws Exception {
		String msg = I18nUtil.getMessage("username", new Object[] { "李四", "bb" });
		System.out.println(msg);
	}
}
