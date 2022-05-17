package com.fib.autoconfigure.disruptor.event.factory;

import com.fib.autoconfigure.disruptor.event.DisruptorBindEvent;
import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.lmax.disruptor.EventFactory;


public class DisruptorBindEventFactory implements EventFactory<DisruptorEvent> {
	@Override
	public DisruptorEvent newInstance() {
		return new DisruptorBindEvent(this);
	}
}
