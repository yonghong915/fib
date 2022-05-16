package com.fib.autoconfigure.disruptor;

import org.springframework.beans.factory.annotation.Autowired;

import com.fib.autoconfigure.disruptor.event.DisruptorBindEvent;
import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorTemplate {

	@Autowired
	private Disruptor<DisruptorEvent> disruptor;

	@Autowired
	private EventTranslatorOneArg<DisruptorEvent, DisruptorEvent> oneArgEventTranslator;

	public void publishEvent(DisruptorBindEvent event) {
		disruptor.publishEvent(oneArgEventTranslator, event);
	}

	public void publishEvent(String event, String tag, Object body) {
		DisruptorBindEvent bindEvent = new DisruptorBindEvent();
		bindEvent.setEvent(event);
		bindEvent.setTag(tag);
		bindEvent.setBody(body);
		disruptor.publishEvent(oneArgEventTranslator, bindEvent);
	}

	public void publishEvent(String event, String tag, String key, Object body) {
		DisruptorBindEvent bindEvent = new DisruptorBindEvent();
		bindEvent.setEvent(event);
		bindEvent.setTag(tag);
		bindEvent.setKey(key);
		bindEvent.setBody(body);
		disruptor.publishEvent(oneArgEventTranslator, bindEvent);
	}
}
