package com.fib.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenCodeTest {
	public static void main(String[] args) {
		String channelDeployPath = "D:\\upp\\upp_recv\\";
		CommGateway cg = new CommGateway();
		cg.generateSourceFile(channelDeployPath, null);
		assertEquals("D:\\upp\\upp_recv\\", channelDeployPath);
	}
}
