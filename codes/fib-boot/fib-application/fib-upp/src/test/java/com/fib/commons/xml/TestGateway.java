package com.fib.commons.xml;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.upp.UppApplication;
import com.fib.upp.service.gateway.CommGateway;

import cn.hutool.core.text.StrMatcher;
import cn.hutool.core.util.StrUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class TestGateway {
	@Autowired
	CommGateway commGateway;

	@Test
	public void testGateway() {
		String gatewayId = "cnaps2";
		commGateway.setId(gatewayId);
		commGateway.setDeployPath("E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-application\\fib-upp\\src\\main\\resources\\config\\deploy");
		commGateway.start();
	}
	
	public static void main(String[] args) {
		String attrValue = "${monitor_port}";
		String port = "8080 abseg";
		StrMatcher sm = new StrMatcher(attrValue);
		Map<String,String> ret = sm.match(port);
		System.out.println(ret);
		System.out.println(StrUtil.DELIM_START);
		
		String key = "";
		if (StrUtil.isWrap(attrValue, "${", "}")) {
			key = StrUtil.sub(attrValue, 2, attrValue.length() - 1);
		}
		System.out.println(key);
	}
}
