package com.fib.upp.service.beps.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fib.upp.entity.BatchProcessDetail;
import com.fib.upp.service.beps.IBatchProcessDetailService;

@Service("batchProcessDetailService")
public class BatchProcessDetailServiceImpl implements IBatchProcessDetailService {

	@Override
	public List<BatchProcessDetail> getBatchProcessDetail(String batchId) {
		return Collections.emptyList();
	}
}
