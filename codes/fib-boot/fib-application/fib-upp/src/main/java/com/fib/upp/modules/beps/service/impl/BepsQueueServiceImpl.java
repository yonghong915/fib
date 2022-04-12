package com.fib.upp.modules.beps.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.commons.exception.BusinessException;
import com.fib.commons.util.CommUtils;
import com.fib.core.util.StatusCode;
import com.fib.upp.modules.beps.entity.BepsQueue;
import com.fib.upp.modules.beps.entity.BepsQueueHeader;
import com.fib.upp.modules.beps.entity.BepsQueueItem;
import com.fib.upp.modules.beps.mapper.BepsQueueMapper;
import com.fib.upp.modules.beps.service.IBepsQueueService;
import com.fib.upp.modules.common.service.ServiceDispatcher;
import com.fib.upp.util.Constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;

/**
 * 小额队列服务
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-12 11:00:16
 */
@Service("bepsQueueService")
public class BepsQueueServiceImpl extends ServiceImpl<BepsQueueMapper, BepsQueue> implements IBepsQueueService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BepsQueueServiceImpl.class);
	private BepsQueueMapper bepsQueueMapper;
	private final Lock lock = new ReentrantLock();

	@Autowired
	public BepsQueueServiceImpl(BepsQueueMapper bepsQueueMapper) {
		this.bepsQueueMapper = bepsQueueMapper;
	}

	@Override
	public void sendMessage(String queueId) {
		LOGGER.info("sendMessage queueId=[{}]", queueId);
		Assert.notNull(queueId, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
		List<BepsQueueItem> bqiList = getQueueItemsByQueueId(queueId);
		if (CollUtil.isEmpty(bqiList)) {
			updateQueueHeaderStatus(queueId, Constant.QueueStatus.CLS.code());
			return;
		}

		// key = 人行报文类型；value = 报文下关联的待发报记录
		Map<String, List<String>> msgTypeRecordIdListMap = new HashMap<>();

		bqiList.stream().forEach(bpd -> {
			String msgType = bpd.getMsgType();
			List<String> recordIdList = msgTypeRecordIdListMap.get(msgType);
			if (null == recordIdList) {
				recordIdList = new ArrayList<>();
				msgTypeRecordIdListMap.put(msgType, recordIdList);
			}
			// 将当前待发报记录加入list
			recordIdList.add(bpd.getRecordId());
		});

		msgTypeRecordIdListMap.keySet().stream().forEach((msgType) -> {
			List<String> recordIdList = msgTypeRecordIdListMap.get(msgType);
			List<List<String>> packList = packPaymentOrder(msgType, recordIdList);
			packList.stream().forEach(pack -> {
				sendMessage(queueId, msgType, pack);
			});
		});
	}

	private void sendMessage(String queueId, String msgType, List<String> pack) {

	}

	private List<List<String>> packPaymentOrder(String msgType, List<String> recordIdList) {

		return Collections.emptyList();
	}

	@Override
	public int updateQueueHeaderStatus(String queueId, String headerStatus) {
		BepsQueueHeader bqh = new BepsQueueHeader();
		bqh.setQueueId(queueId);
		bqh.setStatus(headerStatus);
		return bepsQueueMapper.updateQueueHeaderStatus(bqh);
	}

	@Override
	public List<BepsQueueItem> getQueueItemsByQueueId(String queueId) {
		List<BepsQueueItem> bqhList = null;
		try {
			bqhList = bepsQueueMapper.getQueueItemsByQueueId(queueId);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
		return CollUtil.isEmpty(bqhList) ? Collections.emptyList() : bqhList;
	}

	@Override
	public int updateQueueItemStatus(BepsQueueItem queueItem) {
		return 0;
	}

	@Override
	public Opt<BepsQueue> getQueueByQueueType(String queueType) {
		Assert.notNull(queueType, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
		try {
			return Opt.ofNullable(bepsQueueMapper.getQueueByQueueType(queueType));
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
	}

	@Override
	public BepsQueueHeader getOpenedQueueHeader(String queueType) {
		Assert.notNull(queueType, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
		BepsQueueHeader bqh = new BepsQueueHeader();
		bqh.setQueueType(queueType);
		bqh.setStatus(Constant.QueueStatus.OPN.code());
		List<BepsQueueHeader> bqhList = null;
		try {
			bqhList = bepsQueueMapper.getQueueHeader(bqh);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}

		Assert.isTrue(CollUtil.isNotEmpty(bqhList), () -> new BusinessException(StatusCode.RTN_NULL, "打开的队列不存在"));
		Assert.isFalse(bqhList.size() > 1,
				() -> new BusinessException(StatusCode.RTN_NULL, "出现严重的错误：类型为[" + queueType + "]、状态为[打开]的支付项队列不止1个"));
		return bqhList.get(0);
	}

	@Override
	public void createQueueHeader(BepsQueueHeader queueHeader) {
		queueHeader.setQueueId(CommUtils.getRandom("0123456789", 10));
		bepsQueueMapper.createQueueHeader(queueHeader);
	}

	@Override
	public void closeQueueByType(String queueType) {
		lock.lock();
		BepsQueueHeader newQueueHeader = null;
		try {
			Opt<BepsQueue> bqOpt = getQueueByQueueType(queueType);
			BepsQueue bepsQueue = bqOpt
					.orElseThrow(() -> new BusinessException(StatusCode.RTN_NULL, "没有定义的队列类型" + queueType));

			BepsQueueHeader bqh = getOpenedQueueHeader(queueType);
			String queueId = bqh.getQueueId();
			LOGGER.info("打开新队列，队列号为：[{}]", queueId);

			if ("VLD".equals(bepsQueue.getQueueStatus())) {
				// 创建新队列
				newQueueHeader = new BepsQueueHeader();
				newQueueHeader.setQueueType(queueType);
				newQueueHeader.setStatus(Constant.QueueStatus.OPN.code());
				createQueueHeader(newQueueHeader);
			}

			// 更新原队列为"即将关闭"状态
			updateQueueHeaderStatus(queueId, Constant.QueueStatus.FPC.code());

			// 异步发报
			ServiceDispatcher.runAsync(Constant.ServiceName.SEND_MESSAGE_BY_QUEUE.code(),
					MapUtil.of(Constant.FieldKey.QUEUE_ID.code(), queueId));
		} finally {
			lock.unlock();
		}
	}

	@Override
	public List<BepsQueueHeader> getQueueHeader(BepsQueueHeader bqh) {
		List<BepsQueueHeader> bqhList = null;
		try {
			bqhList = bepsQueueMapper.getQueueHeader(bqh);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
		return CollUtil.isEmpty(bqhList) ? Collections.emptyList() : bqhList;
	}
}