package com.fib.upp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.upp.mapper.BepsQueueMapper;
import com.fib.upp.pay.beps.pack.BepsPackUtil;
import com.fib.upp.pay.beps.pack.BepsQueue;
import com.fib.upp.pay.beps.pack.BepsQueueHeader;
import com.fib.upp.pay.beps.pack.BepsQueueItem;
import com.fib.upp.service.IBepsQueueService;
import com.fib.upp.util.BepsUtil;


@Service("bepsQueueService")
public class BepsQueueServiceImpl implements IBepsQueueService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BepsQueueMapper bepsQueueMapper;

	@Override
	public void sendMessage(Long queueId) {
		sendMessageForWholeQueue(queueId);
	}

	private void sendMessageForWholeQueue(Long queueId) {
//		if (Objects.isNull(queueId)) {
//			throw new CommonException("queueId cannot be empty.");
//		}

		List<BepsQueueItem> recordList = null;
		recordList = getQueueItemsByQueueId(queueId);
		recordList = recordList.stream().filter(queue -> !queue.getStatus().equalsIgnoreCase("END"))
				.collect(Collectors.toList());

//		if (CollUtil.isEmpty(recordList)) {
//			int rowNum = updateQueueHeaderStatus(queueId, BepsUtil.QueueHeaderStatus.CLS.code());
//			logger.info("rowNum={}", rowNum);
//			return;
//		}
		// key = 人行报文类型；value = 报文下关联的待发报记录
		Map<String, List<String>> messageTypeRecordIdListMap = new HashMap<>();

		recordList.stream().forEach((BepsQueueItem queue) -> {
			String messageType = queue.getMessageType();
			// 报文类型已经关联的待发报记录列表
			List<String> recordIdList = messageTypeRecordIdListMap.get(messageType);
			// 如果还没有关联的列表，new一个实例，并与报文类型关联
			if (null == recordIdList) {
				recordIdList = new ArrayList<>();
				messageTypeRecordIdListMap.put(messageType, recordIdList);
			}
			// 将当前待发报记录加入list
			recordIdList.add(queue.getRecordId());
		});

		// 组包
		messageTypeRecordIdListMap.entrySet().forEach(entry -> {
			String messageType = entry.getKey();
			List<String> recordIdList = entry.getValue();
			// 根据组包规则，进行组包
			List<List<String>> packList = BepsPackUtil.packPaymentOrder(messageType, recordIdList);
			// 发报
			for (List<String> pack : packList) {
				sendMessage(queueId, messageType, pack);
			}

		});
	}

	public int updateQueueHeaderStatus(Long queueId, String headerStatus) {
		BepsQueueHeader queueHeader = new BepsQueueHeader();
		queueHeader.setPkId(queueId);
		queueHeader.setStatus(headerStatus);
		return bepsQueueMapper.updateQueueHeaderStatus(queueHeader);
	}

	private void sendMessage(Long queueId, String messageType, List<String> pack) {
		logger.info("queueId={},messageType={},pack={}", queueId, messageType, pack);
	}

	@Override
	public List<BepsQueueItem> getQueueItemsByQueueId(Long queueId) {
		return bepsQueueMapper.getQueueItemsByQueueId(queueId);
	}

	@Override
	public int updateQueueItemStatus(BepsQueueItem queueItem) {
		return bepsQueueMapper.updateQueueItemStatus(queueItem);
	}

	@Override
	public BepsQueue getQueueByQueueType(String queueType) {
		return bepsQueueMapper.getQueueByQueueType(queueType);
	}

	@Override
	public BepsQueueHeader getOpenedQueueHeader(String queueType) {
		BepsQueueHeader param = new BepsQueueHeader();
		param.setQueueType(queueType);
		param.setStatus(BepsUtil.QueueHeaderStatus.OPN.code());
		List<BepsQueueHeader> queueHeaderList = bepsQueueMapper.getQueueHeader(param);
//		if (CollUtil.isEmpty(queueHeaderList)) {
//			throw new CommonException("未有打开的队列");
//		}
//
//		if (queueHeaderList.size() > 1) {
//			throw new CommonException("出现严重的错误：类型为[" + queueType + "]、状态为[打开]的支付项队列不止1个");
//		}
		return queueHeaderList.get(0);
	}

	@Override
	public void createQueueHeader(BepsQueueHeader queueHeader) {
		bepsQueueMapper.createQueueHeader(queueHeader);
	}
}
