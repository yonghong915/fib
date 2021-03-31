package com.fib.upp.service.beps;

import java.util.List;

import com.fib.upp.entity.BatchProcessDetail;

public interface IBatchProcessDetailService {
	List<BatchProcessDetail> getBatchProcessDetail(String batchId);
}
