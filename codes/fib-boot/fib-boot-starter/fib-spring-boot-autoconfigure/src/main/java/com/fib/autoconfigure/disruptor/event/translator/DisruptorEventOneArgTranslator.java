package com.fib.autoconfigure.disruptor.event.translator;

import org.springframework.util.StringUtils;

import com.fib.autoconfigure.disruptor.event.DisruptorBindEvent;
import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.lmax.disruptor.EventTranslatorOneArg;

public class DisruptorEventOneArgTranslator implements EventTranslatorOneArg<DisruptorEvent, DisruptorEvent> {

	@Override
	public void translateTo(DisruptorEvent event, long sequence, DisruptorEvent bind) {
		event.setSource(bind.getSource());
		event.setEvent(bind.getEvent());
		event.setTag(bind.getTag());
		event.setKey(StringUtils.hasText(bind.getKey()) ? bind.getKey() : String.valueOf(sequence));
		if (event instanceof DisruptorBindEvent bindEvent) {
			bindEvent.bind(bind);
		}
	}
}
