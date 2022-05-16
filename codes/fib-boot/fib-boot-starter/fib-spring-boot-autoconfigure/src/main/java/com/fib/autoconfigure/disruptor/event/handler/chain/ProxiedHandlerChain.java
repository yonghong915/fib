package com.fib.autoconfigure.disruptor.event.handler.chain;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.fib.autoconfigure.disruptor.event.handler.DisruptorHandler;

public class ProxiedHandlerChain implements HandlerChain<DisruptorEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxiedHandlerChain.class);

	private ProxiedHandlerChain originalChain;
	private List<DisruptorHandler<DisruptorEvent>> handlers;
	private int currentPosition = 0;

	public ProxiedHandlerChain() {
		this.currentPosition = -1;
	}

	public ProxiedHandlerChain(ProxiedHandlerChain orig, List<DisruptorHandler<DisruptorEvent>> handlers) {
		if (orig == null) {
			throw new NullPointerException("original HandlerChain cannot be null.");
		}
		this.originalChain = orig;
		this.handlers = handlers;
		this.currentPosition = 0;
	}

	@Override
	public void doHandler(DisruptorEvent event) throws Exception {
		if (this.handlers == null || this.handlers.size() == this.currentPosition) {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Invoking original filter chain.");
			}
			if (this.originalChain != null) {
				this.originalChain.doHandler(event);
			}
		} else {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Invoking wrapped filter at index [" + this.currentPosition + "]");
			}
			this.handlers.get(this.currentPosition++).doHandler(event, this);
		}
	}
}