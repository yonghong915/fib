//package com.fib.tcp.socket.netty;
//
//import com.fib.tcp.socket.service.HelloService;
//
//public class ClientBootStrap {
//	public static final String DEFAULT_PROVIDER_NAME = "HelloService#hello#";
//
//	public static void main(String[] args) {
//		// 创建一个消费者
//		NettyClient customer = new NettyClient();
//		// 创建代理对象
//		HelloService helloService = (HelloService) customer.getBean(HelloService.class, DEFAULT_PROVIDER_NAME);
//		// 调用代理对象的方法
//		String res = helloService.sayHello("hello dubboService");
//		System.out.println("调用的结果==>" + res);
//
//	}
//}
