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
import com.fib.upp.modules.beps.entity.BespMsgServiceMapping;
import com.fib.upp.modules.beps.entity.MessagePackElement;
import com.fib.upp.modules.beps.entity.MessagePackRule;
import com.fib.upp.modules.beps.mapper.BepsQueueMapper;
import com.fib.upp.modules.beps.service.IBepsQueueService;
import com.fib.upp.modules.beps.service.IMessagePackRuleService;
import com.fib.upp.modules.common.service.ServiceDispatcher;
import com.fib.upp.util.Constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

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
	static Map<String, MessagePackRule> msgTypePackRuleMap = new HashMap<>();
	static Map<String, String> msgTypeServiceNameMap = new HashMap<>();
	@Autowired
	private IMessagePackRuleService messagePackRuleService;

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

		msgTypeRecordIdListMap.keySet().stream().forEach(msgType -> {
			List<String> recordIdList = msgTypeRecordIdListMap.get(msgType);
			List<List<String>> packList = packPaymentOrder(msgType, recordIdList);
			packList.stream().forEach(pack -> sendMessage(queueId, msgType, pack));
		});
	}

	private void sendMessage(String queueId, String msgType, List<String> oppIdList) {
		LOGGER.info("queueId=[{}], msgType=[{}],pack=[{}]", queueId, msgType, oppIdList);
		// 根据人行报文类型，查询发报服务名称
		String serviceName = getServiceNameByMsgType(msgType);

		Map<String, Object> sendMessageContext = MapUtil.newHashMap();
		sendMessageContext.put("oppIdList", oppIdList);
		sendMessageContext.put(Constant.FieldKey.MSG_TYPE.code(), msgType);

		Map<String, Object> rtnMap = null;
		try {
			rtnMap = ServiceDispatcher.runSync(serviceName, msgTypePackRuleMap);
		} catch (BusinessException e) {
			LOGGER.error("Failed to invoke service,service name is [{}]", serviceName, e);
		}
		if (null == rtnMap) {
			//
		}
		// 修改订单状态为已发报 04

		BepsQueueItem bqi = new BepsQueueItem();
		bqi.setQueueId(queueId);
		bqi.setRecordId(serviceName);
		bqi.setStatus(Constant.QueueItemStatus.END.code());// 调用serviceName失败则FAIL
		bepsQueueMapper.updateQueueItemStatus(bqi, oppIdList);

		// 检查所有队列项是否已完成 BepsQueueItem
		boolean fullCloseFlag = true;
		List<BepsQueueItem> queueItemList = bepsQueueMapper.getQueueItemsByQueueId(queueId);
		for (BepsQueueItem queueItem : queueItemList) {
			if (!Constant.QueueItemStatus.END.code().equals(queueItem.getStatus())) {
				fullCloseFlag = false;
				break;
			}
		}
		if (fullCloseFlag) {
			// BepsQueueHeader 当队列类型为实时，则不关闭
			BepsQueueHeader bqh = new BepsQueueHeader();
			bqh.setQueueId(queueId);
			List<BepsQueueHeader> bqhLst = getQueueHeader(bqh);
			for (BepsQueueHeader quHead : bqhLst) {
				if (!quHead.getQueueType().startsWith("IS")) {
					updateQueueHeaderStatus(queueId, Constant.QueueStatus.CLS.code());
				}
			}
		}
	}

	private String getServiceNameByMsgType(String msgType) {
		synchronized (msgTypeServiceNameMap) {
			if (CollUtil.isEmpty(msgTypeServiceNameMap)) {
				buildMsgTypeServiceNameMap();
			}
			String serviceName = msgTypeServiceNameMap.get(msgType);

			if (StrUtil.isBlankIfStr(serviceName)) {
				buildMsgTypeServiceNameMap();
			}

			serviceName = msgTypeServiceNameMap.get(msgType);
			if (StrUtil.isBlankIfStr(serviceName)) {
				throw new BusinessException(StatusCode.RTN_NULL, "根据报文类型[" + msgType + "]找不到对应的发报服务，请先维护报文类型与发报服务的映射");
			}
			return serviceName;
		}
	}

	private void buildMsgTypeServiceNameMap() {
		// BepsMessageServiceMap
		List<BespMsgServiceMapping> bmsmList = new ArrayList<>();
		BespMsgServiceMapping bpsm = new BespMsgServiceMapping();
		bpsm.setMsgType("beps.121.001.01");
		bpsm.setServiceName("PEBepsSendMessage_beps121");
		bmsmList.add(bpsm);

		bmsmList.stream().forEach(bmsm -> msgTypeServiceNameMap.put(bmsm.getMsgType(), bmsm.getServiceName()));
	}

	private List<List<String>> packPaymentOrder(String msgType, List<String> oppIdList) {
		if (CollUtil.isEmpty(oppIdList)) {
			return Collections.emptyList();
		}

		MessagePackRule messagePackRule = getMessagePackRuleByMsgType(msgType);
		return packPaymentOrder(messagePackRule, oppIdList);
	}

	private List<List<String>> packPaymentOrder(MessagePackRule messagePackRule, List<String> oppIdList) {
		LOGGER.info("oppIdList is :[{}]", oppIdList);
		// key = packString（分包的标识号）; value = List<OrderId>（每个分包里的订单号）
		Map<String, List<String>> packMap = new HashMap<>();
		oppIdList.stream().forEach(oppId -> {
			MessagePackElement packElem = extractPackElement(oppId);
			// 根据组包规则，将组包要素转化成一个字符串（分包的标识号）
			String packStr = messagePackRule.getPackStr(packElem);
			// 拿到分包标识号对应的list
			packMap.computeIfAbsent(packStr, key -> new ArrayList<>());

			// 拿到分包标识号对应的list
			List<String> subOrderIdList = packMap.get(packStr);
			// 允许最大参与者数目控制，如果超过则新组一个包
			while (subOrderIdList.size() >= 2000) {
				int packSeq = Integer.valueOf(packStr.indexOf("pkg")) + 3;// 取得当前包序号
				int packNO = Integer.parseInt(packStr.substring(packSeq).trim());
				packNO = packNO + 1;// 包序号自增
				packStr = packStr.substring(0, packSeq) + String.format("%1$-3s", packNO);
				subOrderIdList = packMap.computeIfAbsent(packStr, key -> new ArrayList<>());
			}
			subOrderIdList.add(oppId);
		});
		return new ArrayList<>(packMap.values());
	}

	/**
	 * 提取组包元素值
	 * 
	 * @param oppId
	 * @return
	 */
	private MessagePackElement extractPackElement(String oppId) {
		LOGGER.info("oppId:[{}]", oppId);
		MessagePackElement messagePackElement = new MessagePackElement();
		messagePackElement.setTransactionType("A100");
		messagePackElement.setSendClearingBank("313651071504");
		messagePackElement.setReceiveClearingBank("323651066615");
		messagePackElement.setReturnLimited("123");
		messagePackElement.setOriginalMessageId("");
		messagePackElement.setBatchId("1000001");
		return messagePackElement;
	}

	public MessagePackRule getMessagePackRuleByMsgType(String msgType) {
		synchronized (msgTypePackRuleMap) {
			if (CollUtil.isEmpty(msgTypePackRuleMap)) {
				buildMsgPackRuleMap();
			}
			MessagePackRule packRule = msgTypePackRuleMap.get(msgType);

			if (null == packRule) {
				buildMsgPackRuleMap();
			}

			packRule = msgTypePackRuleMap.get(msgType);
			if (null == packRule) {
				throw new BusinessException(StatusCode.RTN_NULL, "根据报文类型[" + msgType + "]找不到对应的打包规则，请先维护报文类型的打包规则");
			}
			return packRule;
		}
	}

	private void buildMsgPackRuleMap() {
		List<MessagePackRule> mprLst = messagePackRuleService.queryMessagePackRuleList();
		mprLst.stream().forEach(mpr -> msgTypePackRuleMap.put(mpr.getMessageTypeCode(), mpr));
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