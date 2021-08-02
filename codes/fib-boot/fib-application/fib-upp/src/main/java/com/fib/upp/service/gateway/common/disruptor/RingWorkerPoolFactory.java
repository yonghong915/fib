package com.fib.upp.service.gateway.common.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

public class RingWorkerPoolFactory {
	private RingBuffer<EventContext> ringBuffer;
	private SequenceBarrier sequenceBarrier;

	private static class SingletonHolder {
		private static final RingWorkerPoolFactory INSTANCE = new RingWorkerPoolFactory();
	}

	private RingWorkerPoolFactory() {
	}

	public static RingWorkerPoolFactory instance() {
		return SingletonHolder.INSTANCE;
	}

	public void initialize(ProducerType type, int bufferSize, int maxConnection, WaitStrategy waitStrategy,
			int consumerThreadSize) {
		// 1. 构建ringBuffer对象
		this.ringBuffer = RingBuffer.create(type, new EventFactory() {
			@Override
			public EventContext newInstance() {
				return new EventContext();
			}
		}, bufferSize, waitStrategy);
		// 2.设置序号栅栏
		this.sequenceBarrier = this.ringBuffer.newBarrier();
	}
}
