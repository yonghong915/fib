package com.fib.upp.modules.beps.service;

import java.util.List;

import com.fib.upp.modules.beps.entity.BatchProcessDetail;

public interface IBatchProcessDetailService {
	List<BatchProcessDetail> getBatchProcessDetail(String batchId);
}
