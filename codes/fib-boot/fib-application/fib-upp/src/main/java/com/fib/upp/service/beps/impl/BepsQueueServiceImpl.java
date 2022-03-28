package com.fib.upp.service.beps.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fib.upp.entity.BepsQueue;
import com.fib.upp.entity.BepsQueueHeader;
import com.fib.upp.entity.BepsQueueItem;
import com.fib.upp.mapper.BepsQueueMapper;
import com.fib.upp.service.IBepsQueueService;

@Service("bepsQueueService")
public class BepsQueueServiceImpl implements IBepsQueueService {

	private BepsQueueMapper bepsQueueMapper;

	public BepsQueueServiceImpl(BepsQueueMapper bepsQueueMapper) {
		this.bepsQueueMapper = bepsQueueMapper;
	}

	@Override
	public void sendMessage(Long queueId) {
		// TODO Auto-generated method stub

	}

	@Override
	public int updateQueueHeaderStatus(Long queueId, String headerStatus) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<BepsQueueItem> getQueueItemsByQueueId(Long queueId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateQueueItemStatus(BepsQueueItem queueItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BepsQueue getQueueByQueueType(String queueType) {
		return bepsQueueMapper.getQueueByQueueType(queueType);
	}

	@Override
	public BepsQueueHeader getOpenedQueueHeader(String queueType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createQueueHeader(BepsQueueHeader queueHeader) {
		// TODO Auto-generated method stub

	}

}
