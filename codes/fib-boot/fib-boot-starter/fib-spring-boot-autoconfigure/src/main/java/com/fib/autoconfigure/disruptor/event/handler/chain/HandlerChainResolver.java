package com.fib.autoconfigure.disruptor.event.handler.chain;

import com.fib.autoconfigure.disruptor.event.DisruptorEvent;

public interface HandlerChainResolver<T extends DisruptorEvent> {

	HandlerChain<T> getChain(T event, HandlerChain<T> originalChain);

}
