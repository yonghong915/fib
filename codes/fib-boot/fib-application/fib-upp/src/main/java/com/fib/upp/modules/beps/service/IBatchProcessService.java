package com.fib.upp.modules.beps.service;

import com.fib.upp.entity.BatchProcess;

import cn.hutool.core.lang.Opt;

public interface IBatchProcessService {
	Opt<BatchProcess> getBatchProcess(String batchId);
}
