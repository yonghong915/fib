package com.fib.upp.modules.beps.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fib.upp.modules.beps.entity.BepsQueue;
import com.fib.upp.modules.beps.entity.BepsQueueHeader;
import com.fib.upp.modules.beps.entity.BepsQueueItem;

import cn.hutool.core.lang.Opt;

/**
 * 小额队列服务
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-25
 */
public interface IBepsQueueService extends IService<BepsQueue> {
	/**
	 * 
	 * @param queueId
	 */
	public void sendMessage(String queueId);

	/**
	 * 
	 * @param queueId
	 * @param queueHeaderStatus
	 */
	public int updateQueueHeaderStatus(String queueId, String headerStatus);

	public List<BepsQueueHeader> getQueueHeader(BepsQueueHeader bqh);

	/**
	 * 
	 * @param queueId
	 * @return
	 */
	public List<BepsQueueItem> getQueueItemsByQueueId(String queueId);

	/**
	 * 
	 * @param queueItem
	 * @return
	 */
	int updateQueueItemStatus(BepsQueueItem queueItem);

	public Opt<BepsQueue> getQueueByQueueType(String queueType);

	public BepsQueueHeader getOpenedQueueHeader(String queueType);

	public void createQueueHeader(BepsQueueHeader queueHeader);

	public void closeQueueByType(String queueType);

}
