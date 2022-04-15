package com.fib.upp.modules.beps.service.impl;

import org.springframework.stereotype.Service;

import com.fib.upp.modules.beps.entity.BatchProcess;
import com.fib.upp.modules.beps.service.IBatchProcessService;

import cn.hutool.core.lang.Opt;

@Service("batchProcessService")
public class BatchProcessServiceImpl implements IBatchProcessService {

	@Override
	public Opt<BatchProcess> getBatchProcess(String batchId) {
		return Opt.ofNullable(null);
	}
}
