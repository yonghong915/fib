package com.fib.upp.modules.beps.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.commons.exception.BusinessException;
import com.fib.core.util.StatusCode;
import com.fib.upp.modules.beps.entity.BepsQueueHeader;
import com.fib.upp.modules.beps.service.IBepsQueueService;
import com.fib.upp.modules.common.service.ICommonService;
import com.fib.upp.util.Constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;

@Service("sendMessageByQueue")
public class SendMessageByQueueService implements ICommonService {

	@Autowired
	private IBepsQueueService bepsQueueService;

	@Override
	public Map<String, Object> execute(Map<String, ? extends Object> context) {
		String queueId = MapUtil.getStr(context, Constant.FieldKey.QUEUE_ID.code());
		BepsQueueHeader bqh = new BepsQueueHeader();
		bqh.setQueueId(queueId);
		List<BepsQueueHeader> bqhList = bepsQueueService.getQueueHeader(bqh);

		Assert.isFalse(CollUtil.isEmpty(bqhList),
				() -> new BusinessException(StatusCode.RTN_NULL, "不存在ID为[" + queueId + "]的队列"));

		bepsQueueService.sendMessage(queueId);
		return MapUtil.of("returnType", "S");
	}
}
