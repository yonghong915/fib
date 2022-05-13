package com.fib.upp.modules.cnaps.beps.service;

import java.util.List;

import com.fib.upp.modules.cnaps.beps.entity.BatchProcessDetail;

public interface IBatchProcessDetailService {
	List<BatchProcessDetail> getBatchProcessDetail(String batchId);
}
