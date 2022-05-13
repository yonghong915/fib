package com.fib.upp.modules.cnaps.beps.service;

import java.util.List;

import com.fib.upp.modules.cnaps.beps.entity.BatchProcess;
import com.fib.upp.modules.cnaps.beps.entity.BatchProcessDetail;
import com.fib.upp.modules.cnaps.beps.entity.BatchProcessGroup;

import cn.hutool.core.lang.Opt;

public interface IBatchProcessService {

	int insertBatchProcessGroup(BatchProcessGroup bpg);

	List<BatchProcessGroup> getBatchProcessGroup(BatchProcessGroup bpg);

	int updateBatchProcessGroup(BatchProcessGroup bpg);

	int insertBatchProcess(BatchProcess bp);

	List<BatchProcess> getBatchProcessList(BatchProcess bp);

	Opt<BatchProcess> getBatchProcess(BatchProcess bp);

	Opt<BatchProcess> getBatchProcessByBatchId(String batchId);

	int updateBatchProcess(BatchProcess bp);

	int insertBatchProcessDetail(BatchProcessDetail bpd);

	List<BatchProcessDetail> getBatchProcessDetail(BatchProcessDetail bpd);

	int updateBatchProcessDetail(BatchProcessDetail bpd);

	int insertBatch4BPD(List<BatchProcessDetail> list);
}
