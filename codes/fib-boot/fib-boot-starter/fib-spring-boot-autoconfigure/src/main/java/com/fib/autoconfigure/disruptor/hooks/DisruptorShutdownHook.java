package com.fib.autoconfigure.disruptor.hooks;

import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorShutdownHook extends Thread {

	private Disruptor<DisruptorEvent> disruptor;

	public DisruptorShutdownHook(Disruptor<DisruptorEvent> disruptor) {
		this.disruptor = disruptor;
	}

	@Override
	public void run() {
		disruptor.shutdown();
	}
}