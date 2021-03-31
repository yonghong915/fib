package com.fib.upp.service;

import java.util.List;

import com.fib.upp.pay.beps.pack.BepsQueue;
import com.fib.upp.pay.beps.pack.BepsQueueHeader;
import com.fib.upp.pay.beps.pack.BepsQueueItem;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-25
 */
public interface IBepsQueueService {
	/**
	 * 
	 * @param queueId
	 */
	public void sendMessage(Long queueId);

	/**
	 * 
	 * @param queueId
	 * @param queueHeaderStatus
	 */
	public int updateQueueHeaderStatus(Long queueId, String headerStatus);

	/**
	 * 
	 * @param queueId
	 * @return
	 */
	public List<BepsQueueItem> getQueueItemsByQueueId(Long queueId);

	/**
	 * 
	 * @param queueItem
	 * @return
	 */
	int updateQueueItemStatus(BepsQueueItem queueItem);

	public BepsQueue getQueueByQueueType(String queueType);

	public BepsQueueHeader getOpenedQueueHeader(String queueType);

	public void createQueueHeader(BepsQueueHeader queueHeader);
}
