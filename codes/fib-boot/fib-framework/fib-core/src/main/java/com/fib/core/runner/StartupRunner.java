package com.fib.core.runner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fib.core.base.entity.ErrorCodeEntity;
import com.fib.core.base.service.IErrorCodeService;
import com.fib.core.config.BloomFilterConfig;
import com.fib.core.util.RedisUtil;

@Component
@Order(value = 1)
public class StartupRunner implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IErrorCodeService errorCodeService;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private BloomFilterConfig bloomFilterConfig;

	@Override
	public void run(String... args) throws Exception {
		// 将errorCode信息放入缓存
		logger.info("init ErrorCode Cache");
		ErrorCodeEntity entity = new ErrorCodeEntity();
		List<ErrorCodeEntity> list = errorCodeService.selectList(entity);
		for (ErrorCodeEntity errorCodeObj : list) {
			String errorCode = errorCodeObj.getErrorCode();
			String language = errorCodeObj.getLanguage();
			String key = "errorCode~" + language + "~" + errorCode;
			redisUtil.set(key, errorCodeObj);
			bloomFilterConfig.put(key);
		}
	}

}
