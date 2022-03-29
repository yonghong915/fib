package com.fib.upp.service.beps.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.upp.entity.BepsQueue;
import com.fib.upp.entity.BepsQueueHeader;
import com.fib.upp.entity.BepsQueueItem;
import com.fib.upp.mapper.BepsQueueMapper;
import com.fib.upp.service.IBepsQueueService;

@Service("bepsQueueService")
public class BepsQueueServiceImpl extends ServiceImpl<BepsQueueMapper, BepsQueue> implements IBepsQueueService {

	private BepsQueueMapper bepsQueueMapper;

	public BepsQueueServiceImpl(BepsQueueMapper bepsQueueMapper) {
		this.bepsQueueMapper = bepsQueueMapper;
	}

	@Override
	public void sendMessage(Long queueId) {
		//
	}

	@Override
	public int updateQueueHeaderStatus(Long queueId, String headerStatus) {
		return 0;
	}

	@Override
	public List<BepsQueueItem> getQueueItemsByQueueId(Long queueId) {
		//
		return Collections.emptyList();
	}

	@Override
	public int updateQueueItemStatus(BepsQueueItem queueItem) {
		return 0;
	}

	@Override
	public BepsQueue getQueueByQueueType(String queueType) {
		return bepsQueueMapper.getQueueByQueueType(queueType);
	}

	@Override
	public BepsQueueHeader getOpenedQueueHeader(String queueType) {
		return null;
	}

	@Override
	public void createQueueHeader(BepsQueueHeader queueHeader) {
		//
	}

}
