package com.fib.autoconfigure.disruptor.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.fib.autoconfigure.disruptor.executor.Executor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * Disruptor 事件处理 消息的处理方式。消费者对于同一消息：
 * 都分别独立消费应当实现EventHandler接口；不重复消费则应当实现WorkHandler接口
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-16 10:13:44
 */
public class DisruptorEventHandler implements EventHandler<DisruptorEvent>, WorkHandler<DisruptorEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DisruptorEventHandler.class);

	/**
	 * 事件处理
	 */
	@Override
	public void onEvent(DisruptorEvent event) throws Exception {
		LOGGER.info("Consumer consumes handle event [{}]", event);
		if (null == event) {
			return;
		}
		Executor executor = event.getExecutor();
		if (null == executor) {
			return;
		}

		try {
			executor.onExecute(event.getMsg());
		} catch (Exception e) {
			LOGGER.error("Failed to execute DisruptorEventHandler.", e);
		}
	}

	@Override
	public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) throws Exception {
		this.onEvent(event);
	}
}