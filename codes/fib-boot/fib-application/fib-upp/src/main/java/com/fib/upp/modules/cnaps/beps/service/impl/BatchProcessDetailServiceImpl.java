package com.fib.upp.modules.cnaps.beps.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fib.upp.modules.cnaps.beps.entity.BatchProcessDetail;
import com.fib.upp.modules.cnaps.beps.service.IBatchProcessDetailService;

@Service("batchProcessDetailService")
public class BatchProcessDetailServiceImpl implements IBatchProcessDetailService {

	@Override
	public List<BatchProcessDetail> getBatchProcessDetail(String batchId) {
		return Collections.emptyList();
	}
}
