package com.fib.upp.mq.rocketmq.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fib.upp.util.SysCharsetUtil;

@Component
@RocketMQMessageListener(topic = "Topic_Order", consumerGroup = "boot_producer_group")
public class RocketMQConsumer implements RocketMQListener<String> {
	private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQConsumer.class);

	@Override
	public void onMessage(String message) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("RocketMQ consumer received message:{}", message);
		}
		byte[] data = message.getBytes(SysCharsetUtil.CNAPS.getCharset());

	}
}
