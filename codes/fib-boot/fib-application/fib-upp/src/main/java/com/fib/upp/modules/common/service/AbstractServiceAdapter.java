package com.fib.upp.modules.common.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-06-17 17:17:08
 */
public abstract class AbstractServiceAdapter {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public Map<String, Object> execute(Map<String, Object> reqMap) {
		logger.info("inputMap is [{}]", reqMap);

		before(reqMap);

		Map<String, Object> rspMap = invoke(reqMap);

		after(reqMap, rspMap);

		return rspMap;
	}

	protected Map<String, Object> after(Map<String, Object> reqMap, Map<String, Object> rspMap) {
		return rspMap;
	}

	protected Map<String, Object> invoke(Map<String, Object> reqMap) {
		return null;
	}

	protected Map<String, Object> before(Map<String, Object> reqMap) {
		return null;
	}

	protected abstract String getProductId();
}
