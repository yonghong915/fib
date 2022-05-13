package com.fib.upp.modules.cnaps.beps.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.upp.modules.cnaps.beps.service.IBepsQueueService;
import com.fib.upp.modules.common.service.ICommonService;
import com.fib.upp.util.Constant;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;

/**
 * 小额组包
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-12 10:42:17
 */
@Service("smallPackService")
public class SmallPackServiceImpl implements ICommonService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmallPackServiceImpl.class);

	private IBepsQueueService bepsQueueService;

	@Autowired
	public SmallPackServiceImpl(IBepsQueueService bepsQueueService) {
		this.bepsQueueService = bepsQueueService;
	}

	@Override
	public Map<String, Object> execute(Map<String, ? extends Object> context) {
		LOGGER.info("SmallPackServiceImpl--->execute...");
		LOGGER.info("small pack begin: now is {}", LocalDateTimeUtil.now());

		bepsQueueService.closeQueueByType(Constant.QueueType.TMA001.code());
		Map<String, Object> rtnMap = MapUtil.of("returnType", "S");
		rtnMap.put("returnCode", "0000000000");
		return rtnMap;
	}
}