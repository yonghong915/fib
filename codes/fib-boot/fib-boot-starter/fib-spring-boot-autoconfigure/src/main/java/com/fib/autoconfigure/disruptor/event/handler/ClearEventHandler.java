package com.fib.autoconfigure.disruptor.event.handler;

import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.lmax.disruptor.EventHandler;

public class ClearEventHandler implements EventHandler<DisruptorEvent> {

	@Override
	public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) throws Exception {
		//event.clear();
	}

}
