package com.fib.upp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fib.upp.mapper.BepsMessagePackRuleMapper;
import com.fib.upp.pay.beps.pack.BepsMessagePackRule;
import com.fib.upp.pay.beps.pack.BepsQueue;
import com.fib.upp.pay.beps.pack.BepsQueueHeader;
import com.fib.upp.service.IBepsPackService;
import com.fib.upp.service.IBepsQueueService;
import com.fib.upp.util.BepsUtil;


@Service("bepsPackService")
public class BepsPackServiceImpl implements IBepsPackService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BepsMessagePackRuleMapper bepsMessagePackRuleMapper;

	@Autowired
	private IBepsQueueService bepsQueueService;

	@Override
	public List<BepsMessagePackRule> queryBepsPackRuleList() {
		QueryWrapper<BepsMessagePackRule> wrapper = new QueryWrapper<>();
		return bepsMessagePackRuleMapper.selectList(wrapper);
	}

	@Async("taskExecutor")
	@Transactional
	@Override
	public void packBepsMessage() {
		String queueType = BepsUtil.QueueType.TMA001.code();
		logger.info("queueType={}", queueType);

		// 获取队列的定义
		BepsQueue queue = bepsQueueService.getQueueByQueueType(queueType);
//		if (Objects.isNull(queue)) {
//			throw new CommonException("没有定义的队列类型" + queueType);
//		}

		// 查询队列头'打开'的队列
		BepsQueueHeader queueHeader = bepsQueueService.getOpenedQueueHeader(queueType);
//		if (Objects.isNull(queueHeader)) {
//			throw new CommonException("关闭队列失败：队列类型[" + queueType + "]还没有打开");
//		}
		Long queueId = queueHeader.getPkId();
		logger.info("queueId={}", queueId);

		String queueStatus = queue.getQueueStatus();
		if (BepsUtil.QueueStatus.VLD.code().equals(queueStatus)) {
			// 新建状态为'打开'的队列
			BepsQueueHeader newQueueHeader = new BepsQueueHeader();
			//newQueueHeader.setPkId(IdUtil.createSnowflake(1, 1).nextId());
			newQueueHeader.setQueueType(queueType);
			newQueueHeader.setStatus(BepsUtil.QueueHeaderStatus.OPN.code());
			bepsQueueService.createQueueHeader(newQueueHeader);
		}

		// 更新原队列为"即将关闭"状态
		bepsQueueService.updateQueueHeaderStatus(queueId, BepsUtil.QueueHeaderStatus.FPC.code());
	}
}
