package com.fib.upp.service.beps;

import com.fib.upp.entity.BatchProcess;

import cn.hutool.core.lang.Opt;

public interface IBatchProcessService {
	Opt<BatchProcess> getBatchProcess(String batchId);
}
