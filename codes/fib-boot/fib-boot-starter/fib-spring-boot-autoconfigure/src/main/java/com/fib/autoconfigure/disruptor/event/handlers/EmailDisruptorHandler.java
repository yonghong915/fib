package com.fib.autoconfigure.disruptor.event.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.disruptor.annotation.EventRule;
import com.fib.autoconfigure.disruptor.event.DisruptorBindEvent;
import com.fib.autoconfigure.disruptor.event.handler.DisruptorHandler;
import com.fib.autoconfigure.disruptor.event.handler.chain.HandlerChain;

@EventRule("/Event-Output/TagA-Output/**")
@Component("emailHandler")
public class EmailDisruptorHandler implements DisruptorHandler<DisruptorBindEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailDisruptorHandler.class);

	@Override
	public void doHandler(DisruptorBindEvent event, HandlerChain<DisruptorBindEvent> handlerChain) throws Exception {
		LOGGER.info("event={}", event.toString());
	}

}