package com.fib.tcp.socket.service;

public class HelloServiceImpl implements HelloService {
	@Override
	public String sayHello(String msg) {
		System.out.println("provider receive msg:" + msg);
		if (msg != null && !"".equals(msg)) {
			return "Hello Client,I had already received your msg==>{" + msg + "}";
		} else {
			return "provider return msg: hello";
		}

	}
}
