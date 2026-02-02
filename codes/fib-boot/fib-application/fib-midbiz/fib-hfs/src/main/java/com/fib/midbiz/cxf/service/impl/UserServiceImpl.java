package com.fib.midbiz.cxf.service.impl;

import org.springframework.stereotype.Component;

import com.fib.midbiz.cxf.service.UserService;

@Component
public class UserServiceImpl implements UserService {

	@Override
	public String getHello(String name) {
		return "hello " + name;
	}
}
