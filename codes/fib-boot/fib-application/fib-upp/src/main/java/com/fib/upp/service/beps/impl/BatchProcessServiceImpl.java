package com.fib.upp.service.beps.impl;

import org.springframework.stereotype.Service;

import com.fib.upp.entity.BatchProcess;
import com.fib.upp.service.beps.IBatchProcessService;

import cn.hutool.core.lang.Opt;

@Service("batchProcessService")
public class BatchProcessServiceImpl implements IBatchProcessService {

	@Override
	public Opt<BatchProcess> getBatchProcess(String batchId) {
		return Opt.ofNullable(null);
	}
}
