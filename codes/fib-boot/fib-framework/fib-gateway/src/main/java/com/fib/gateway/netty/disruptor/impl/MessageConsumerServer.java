package com.fib.gateway.netty.disruptor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fib.gateway.netty.disruptor.MessageConsumer;
import com.fib.gateway.netty.disruptor.TranslatorData;
import com.fib.gateway.netty.disruptor.TranslatorDataWapper;
import com.fib.gateway.netty.service.BaseService;

import io.netty.channel.ChannelHandlerContext;

@Component("messageConsumerServer")
public class MessageConsumerServer extends MessageConsumer {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BaseService baseService;

	public MessageConsumerServer() {
		super();
	}

	public MessageConsumerServer(String consumerId) {
		super(consumerId);
	}

	public void onEvent(TranslatorDataWapper event) throws Exception {
		TranslatorData request = event.getData();
		ChannelHandlerContext ctx = event.getCtx();
		// 1.业务处理逻辑:
		logger.info("Sever端: id={}, name={}, message={}  ", request.getId(), request.getName(), request.getMessage());

		// 2.回送响应信息:
		Object obj = baseService.test(request);
		TranslatorData response = new TranslatorData();
		response.setId(request.getId());
		response.setName("respName: " + request.getName());
		response.setMessage("respMsg: " + obj.toString());
		// 写出response响应信息:
		ctx.writeAndFlush(response);
	}
}
