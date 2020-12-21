package com.fib.core.runner;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fib.core.base.entity.ErrorCodeEntity;
import com.fib.core.base.service.IErrorCodeService;
import com.fib.core.base.service.impl.RedisService;
import com.fib.core.util.BloomFilterHelper;
import com.fib.core.util.RedisUtil;
import com.google.common.hash.Funnel;

@Component
@Order(value = 1)
public class StartupRunner implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IErrorCodeService errorCodeService;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private RedisService redisService;

	@Override
	public void run(String... args) throws Exception {
		// 将errorCode信息放入缓存
		logger.info("初始化ErrorCode缓存");
		ErrorCodeEntity entity = new ErrorCodeEntity();
		List<ErrorCodeEntity> list = errorCodeService.selectList(entity);
		for (ErrorCodeEntity errorCodeObj : list) {
			//String errorDesc = errorCodeObj.getErrorDesc();
			String errorCode = errorCodeObj.getErrorCode();
			String language = errorCodeObj.getLanguage();
			 redisUtil.set(language + "~" + errorCode, errorCodeObj);

//			BloomFilterHelper<String> myBloomFilterHelper = new BloomFilterHelper<>((Funnel<String>) (from,
//					into) -> into.putString(from, StandardCharsets.UTF_8).putString(from, StandardCharsets.UTF_8), 1500000, 0.00001);
//			redisService.addByBloomFilter(myBloomFilterHelper, language + "~" + errorCode, errorDesc);
		}
	}

}
