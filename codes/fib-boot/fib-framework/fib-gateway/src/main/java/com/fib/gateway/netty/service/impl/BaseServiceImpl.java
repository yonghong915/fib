package com.fib.gateway.netty.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fib.gateway.netty.service.BaseService;

@Service
public class BaseServiceImpl implements BaseService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Object test(Object msg) {
		logger.info("调用service服务");
		return "resp " + msg;
	}
}
