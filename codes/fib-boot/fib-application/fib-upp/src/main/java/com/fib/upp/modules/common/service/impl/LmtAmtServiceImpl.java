package com.fib.upp.modules.common.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fib.upp.modules.common.service.ICommonService;

/**
 * 金额限额服务
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-08 17:26:31
 */
@Service("lmtAmtService")
public class LmtAmtServiceImpl implements ICommonService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LmtAmtServiceImpl.class);

	@Override
	public Map<String, Object> execute(Map<String, ? extends Object> context) {
		LOGGER.info("LmtAmtService...,params is :[{}]", context);
		return null;
	}

}
