package com.fib.autoconfigure.disruptor.event.handler.chain;

import com.fib.autoconfigure.disruptor.event.DisruptorEvent;

public interface HandlerChain<T extends DisruptorEvent> {
	void doHandler(T event) throws Exception;
}
