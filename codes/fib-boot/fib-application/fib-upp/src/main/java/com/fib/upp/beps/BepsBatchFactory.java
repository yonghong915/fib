package com.fib.upp.beps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.upp.service.beps.BepsBatchService;
import com.fib.upp.util.Constant;


/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-02-24
 */
public class BepsBatchFactory {
	private static Logger logger = LoggerFactory.getLogger(BepsBatchFactory.class);

	private BepsBatchFactory() {
	}

	public static BepsBatchService getBepsBatchService(String batchType) {
		return null;
//		if (StrUtil.isEmpty(batchType)) {
//			logger.error("batchType can not be empty.");
//			throw new IllegalArgumentException("batchType can not be empty.");
//		}
//		String serviceName = Constant.BatchType.getServiceName(batchType);
//		return SpringUtil.getBean(serviceName, BepsBatchService.class);
	}
}
