package com.fib.commons.disruptor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.fib.commons.disruptor.base.Executor;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 环形工作池工厂
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-01-26 11:18:17
 */
public enum RingWorkerPoolFactory {
	INSTANCE;

	private final int defaultRingBufferSize = 8 * 1024;
	private final ThreadFactory threadFactory = Executors.defaultThreadFactory();

	public final ThreadLocal<Disruptor<MsgEvent>> threadLocal = ThreadLocal.withInitial(() -> {
		Disruptor<MsgEvent> disruptor = new Disruptor<>(MsgEventFactory.DEFAULT, defaultRingBufferSize,
				threadFactory, ProducerType.MULTI, new YieldingWaitStrategy());
		disruptor.setDefaultExceptionHandler(new EventExceptionHandler());

		int handlerCount = 5;
		MsgHandler[] eventHandlers = new MsgHandler[handlerCount];
		for (int i = 0; i < handlerCount; i++) {
			eventHandlers[i] = new MsgHandler();
		}
		disruptor.handleEventsWithWorkerPool(eventHandlers);
		disruptor.start();
		return disruptor;
	});

	/**
	 * 异常静态类
	 */
	static class EventExceptionHandler implements ExceptionHandler<MsgEvent> {
		@Override
		public void handleEventException(Throwable ex, long sequence, MsgEvent event) {
			ex.printStackTrace();
		}

		@Override
		public void handleOnStartException(Throwable ex) {
			ex.printStackTrace();
		}

		@Override
		public void handleOnShutdownException(Throwable ex) {
			ex.printStackTrace();
		}
	}

	public void produce(Executor executor, Object msg) {
		RingBuffer<MsgEvent> ringBuffer = threadLocal.get().getRingBuffer();
		long nextSequence = ringBuffer.next();
		try {
			MsgEvent commandEvent = ringBuffer.get(nextSequence);
			commandEvent.setExecutor(executor);
			commandEvent.setValues(msg);
		} finally {
			ringBuffer.publish(nextSequence);
		}
	}
}