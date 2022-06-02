package com.fib.autoconfigure.msgconverter.channel.mq.rocketmq;

import java.nio.charset.StandardCharsets;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.fib.autoconfigure.msgconverter.channel.mq.IMQMessageService;

import cn.hutool.core.io.FileUtil;

public class RocketMQMessageService implements IMQMessageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQMessageService.class);

	@Autowired
	private RocketMQTemplate rocketMQTemplate;

	@Override
	public void sendMessage(String destination, String message) {
		rocketMQTemplate.convertAndSend(destination, message);
	}

	@Override
	public void sendSyncMessage(String destination, String id, String msg) {
		Message<String> message = MessageBuilder.withPayload(msg).setHeader(RocketMQHeaders.KEYS, id).build();
		rocketMQTemplate.syncSend(destination, message);
	}

	@Override
	public void sendAsyncMessage(String destination, String id, String msg) {
		Message<String> message = MessageBuilder.withPayload(msg).setHeader(RocketMQHeaders.KEYS, id).build();
		rocketMQTemplate.asyncSend(destination, message, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
					LOGGER.info("send async message successfully!");
				}
			}

			@Override
			public void onException(Throwable throwable) {
				LOGGER.error("Failed to send async message", throwable);
			}
		});
	}

	@Override
	public void sendOnewayMessage(String destination, String id, String msg) {
		Message<String> message = MessageBuilder.withPayload(msg).setHeader(RocketMQHeaders.KEYS, id).build();
		rocketMQTemplate.sendOneWay(destination, message);
	}

	@Override
	public byte[] receiveMessage() {
		String data = FileUtil.readString("data/cnaps/ccms.801.001.02.txt", StandardCharsets.UTF_8);
		return data.getBytes();
	}
}