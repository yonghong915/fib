package com.fib.autoconfigure.disruptor.event.handler;

import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;

public class EventExceptionHandler implements ExceptionHandler<DisruptorEvent> {
	private Disruptor<DisruptorEvent> disruptor = null;

	public EventExceptionHandler(Disruptor<DisruptorEvent> disruptor) {
		this.disruptor = disruptor;
	}

	@Override
	public void handleEventException(Throwable ex, long sequence, DisruptorEvent event) {
		disruptor.shutdown();
	}

	@Override
	public void handleOnStartException(Throwable ex) {
		disruptor.shutdown();
	}

	@Override
	public void handleOnShutdownException(Throwable ex) {
		disruptor.shutdown();
	}
}