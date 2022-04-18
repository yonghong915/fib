package com.fib.upp.modules.beps.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fib.commons.exception.BusinessException;
import com.fib.core.util.StatusCode;
import com.fib.upp.modules.beps.entity.BatchProcess;
import com.fib.upp.modules.beps.entity.BatchProcessDetail;
import com.fib.upp.modules.beps.entity.BatchProcessGroup;
import com.fib.upp.modules.beps.mapper.BatchProcessMapper;
import com.fib.upp.modules.beps.service.IBatchProcessService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.IdUtil;

@Service("batchProcessService")
public class BatchProcessServiceImpl implements IBatchProcessService {
	private BatchProcessMapper batchProcessMapper;

	@Autowired
	public BatchProcessServiceImpl(BatchProcessMapper batchProcessMapper) {
		this.batchProcessMapper = batchProcessMapper;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertBatchProcessGroup(BatchProcessGroup bpg) {
		try {
			bpg.setBatchGroupId(IdUtil.randomUUID());
			return batchProcessMapper.insertBatchProcessGroup(bpg);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
	}

	@Override
	public List<BatchProcessGroup> getBatchProcessGroup(BatchProcessGroup bpg) {
		List<BatchProcessGroup> list = null;
		try {
			batchProcessMapper.getBatchProcessGroup(bpg);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
		return CollUtil.isEmpty(list) ? Collections.emptyList() : list;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateBatchProcessGroup(BatchProcessGroup bpg) {
		try {
			return batchProcessMapper.updateBatchProcessGroup(bpg);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertBatchProcess(BatchProcess bp) {
		try {
			bp.setBatchId(IdUtil.randomUUID());
			return batchProcessMapper.insertBatchProcess(bp);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
	}

	@Override
	public List<BatchProcess> getBatchProcessList(BatchProcess bp) {
		List<BatchProcess> list = null;
		try {
			batchProcessMapper.getBatchProcessList(bp);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
		return CollUtil.isEmpty(list) ? Collections.emptyList() : list;
	}

	@Override
	public Opt<BatchProcess> getBatchProcess(BatchProcess bp) {
		List<BatchProcess> list = getBatchProcessList(bp);
		return CollUtil.isEmpty(list) ? Opt.empty() : Opt.of(list.get(0));
	}

	@Override
	public Opt<BatchProcess> getBatchProcessByBatchId(String batchId) {
		BatchProcess bp = new BatchProcess();
		bp.setBatchId(batchId);
		return getBatchProcess(bp);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateBatchProcess(BatchProcess bp) {
		try {
			return batchProcessMapper.updateBatchProcess(bp);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertBatchProcessDetail(BatchProcessDetail bpd) {
		try {
			return batchProcessMapper.insertBatchProcessDetail(bpd);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
	}

	@Override
	public List<BatchProcessDetail> getBatchProcessDetail(BatchProcessDetail bpd) {
		List<BatchProcessDetail> list = null;
		try {
			batchProcessMapper.getBatchProcessDetail(bpd);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
		return CollUtil.isEmpty(list) ? Collections.emptyList() : list;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateBatchProcessDetail(BatchProcessDetail bpd) {
		try {
			return batchProcessMapper.updateBatchProcessDetail(bpd);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
	}
}