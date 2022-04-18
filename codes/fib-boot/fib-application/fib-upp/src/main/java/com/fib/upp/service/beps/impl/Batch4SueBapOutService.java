package com.fib.upp.service.beps.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.commons.exception.BusinessException;
import com.fib.upp.modules.beps.entity.BatchProcess;
import com.fib.upp.modules.beps.entity.BatchProcessDetail;
import com.fib.upp.modules.beps.service.IBatchProcessDetailService;
import com.fib.upp.modules.beps.service.IBatchProcessService;
import com.fib.upp.service.beps.BepsBatchService;
import com.fib.upp.util.Constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;

@Service("sueBapOutService")
public class Batch4SueBapOutService implements BepsBatchService {
	@Autowired
	private IBatchProcessService batchProcessService;

	@Autowired
	private IBatchProcessDetailService batchProcessDetailService;

	@Override
	public void execute(String batchId) {
		// 查询批次信息，若批次状态为登记（00），则说明未进行过批处理，调用订单流程;否则不调用订单流程，只需判断是否到期状态
		// batch_process where batch_id=?
		BatchProcess bp = new BatchProcess();
		bp.setBatchId(batchId);
		Opt<BatchProcess> optBp = batchProcessService.getBatchProcess(bp);
		BatchProcess batchProcess = optBp.orElseThrow(() -> new BusinessException("aaaa", "bbbb"));
		String processStatus = batchProcess.getProcessStatus();
		if (Constant.BatchStatus.REGISTER.code().equals(processStatus)) {
			// 将批次状态置为处理中，同时进行明细遍历调用订单处理
			dealTOCBatchForOrderIt(batchId);
		}
	}

	private void dealTOCBatchForOrderIt(String batchId) {
		List<BatchProcessDetail> list = batchProcessDetailService.getBatchProcessDetail(batchId);
		if (CollUtil.isEmpty(list)) {
			return;
		}
		list.stream().forEach(batchProcessDetail -> {

		});
	}
}