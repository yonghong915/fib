package com.fib.autoconfigure.disruptor;

import org.springframework.beans.factory.annotation.Autowired;

import com.fib.autoconfigure.disruptor.event.DisruptorBindEvent;
import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.fib.autoconfigure.disruptor.executor.Executor;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * Disruptor调用模板
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-18 10:32:06
 */
public class DisruptorTemplate {

	@Autowired
	private Disruptor<DisruptorEvent> disruptor;

	@Autowired
	private EventTranslatorOneArg<DisruptorEvent, DisruptorEvent> oneArgEventTranslator;

	public void publishEvent(DisruptorBindEvent event) {
		disruptor.publishEvent(oneArgEventTranslator, event);
	}

	public void publishEvent(Executor executor, Object msg) {
		DisruptorBindEvent bindEvent = new DisruptorBindEvent();
		bindEvent.setExecutor(executor);
		bindEvent.setMsg(msg);
		disruptor.publishEvent(oneArgEventTranslator, bindEvent);
	}
}