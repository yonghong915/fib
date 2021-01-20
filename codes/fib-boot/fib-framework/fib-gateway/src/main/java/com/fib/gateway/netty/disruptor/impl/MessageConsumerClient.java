package com.fib.gateway.netty.disruptor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.gateway.netty.disruptor.MessageConsumer;
import com.fib.gateway.netty.disruptor.TranslatorData;
import com.fib.gateway.netty.disruptor.TranslatorDataWapper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class MessageConsumerClient extends MessageConsumer {
	private Logger logger = LoggerFactory.getLogger(getClass());

	public MessageConsumerClient() {

	}

	public MessageConsumerClient(String consumerId) {
		super(consumerId);
	}

	public void onEvent(TranslatorDataWapper event) throws Exception {
		ChannelHandlerContext ctx = event.getCtx();
		TranslatorData response = event.getData();
		// 业务逻辑处理:
		try {
			logger.info("Client: id={},name={}, message= {} ", response.getId(), response.getName(),
					response.getMessage());
		} finally {
			ReferenceCountUtil.release(response);
			ctx.close();
		}
	}

}
