package com.fib.autoconfigure.disruptor.event.handler;

import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.fib.autoconfigure.disruptor.event.handler.chain.HandlerChain;

public interface DisruptorHandler<T extends DisruptorEvent> {

	public void doHandler(T event, HandlerChain<T> handlerChain) throws Exception;

}
