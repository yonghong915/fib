package com.fib.upp.service.beps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fib.commons.exception.BusinessException;
import com.fib.upp.beps.BepsBatchFactory;
import com.fib.upp.modules.beps.entity.BatchProcess;
import com.fib.upp.modules.beps.service.IBatchProcessService;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-02-24
 */
public class BepsDealBatchService {
	private Logger logger = LoggerFactory.getLogger(BepsDealBatchService.class);

	@Autowired
	private IBatchProcessService batchProcessService;

	public void execute(String batchId) {
		logger.info("Enter BepsDealBatch...batchId:[{}]", batchId);
		if (StrUtil.isEmptyIfStr(batchId)) {
			return;
		}
		// 查询批次信息，若批次状态为登记（00），则说明未进行过批处理，调用订单流程;否则不调用订单流程，只需判断是否到期状态
		// batch_process where batch_id=?
		Opt<BatchProcess> optBp = batchProcessService.getBatchProcessByBatchId(batchId);
		BatchProcess batchProcess = optBp.orElseThrow(() -> new BusinessException("aaaa", "bbbb"));
		String batchType = batchProcess.getBatchType();
		// 判断是否超期
		BepsBatchService service = BepsBatchFactory.getBepsBatchService(batchType);
		service.execute(batchId);
	}
}
