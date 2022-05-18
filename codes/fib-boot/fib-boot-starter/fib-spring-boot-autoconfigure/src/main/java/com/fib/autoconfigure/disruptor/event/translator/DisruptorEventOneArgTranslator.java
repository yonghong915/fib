package com.fib.autoconfigure.disruptor.event.translator;

import com.fib.autoconfigure.disruptor.event.DisruptorBindEvent;
import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.lmax.disruptor.EventTranslatorOneArg;

public class DisruptorEventOneArgTranslator implements EventTranslatorOneArg<DisruptorEvent, DisruptorEvent> {

	@Override
	public void translateTo(DisruptorEvent event, long sequence, DisruptorEvent bind) {
		event.setMsg(bind.getMsg());
		event.setExecutor(bind.getExecutor());
		if (event instanceof DisruptorBindEvent bindEvent) {
			bindEvent.bind(bind);
		}
	}
}