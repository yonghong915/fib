package com.fib.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenCodeTest {
	public static void main(String[] args) {
		String channelDeployPath = "E:\\git_source\\fib\\codes\\fib-boot\\fib-framework\\fib-gateway\\src\\main\\resources\\config\\upp\\deploy\\upp_cnaps2_recv\\";
		CommGateway cg = new CommGateway();
		cg.generateSourceFile(channelDeployPath, null);
		assertEquals("D:\\upp\\upp_recv\\", channelDeployPath);

	}
}
