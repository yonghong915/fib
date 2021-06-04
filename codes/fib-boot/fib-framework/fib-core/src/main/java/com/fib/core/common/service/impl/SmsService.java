package com.fib.core.common.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fib.core.common.service.IAsyncService;

/**
 * 短信服务
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-06-04
 */
@Service("smsService")
public class SmsService implements IAsyncService {
	private Logger logger = LoggerFactory.getLogger(SmsService.class);

	@Override
	public void execute(Object source) {
		logger.info("SmsService--->execute,source={}", source);
	}
}
