package com.fib.gateway;

public class GenCodeTest {
	public static void main(String[] args) {
		String channelDeployPath = "D:\\upp\\upp_recv\\";
		CommGateway cg = new CommGateway();
		cg.generateSourceFile(channelDeployPath, null);
	}
}
