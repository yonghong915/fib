package com.fib.gateway.netty.disruptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-12
 */
public class RingBufferWorkerPoolFactory {
	// 静态内部类的单例模式
	private static class SingletonHolder {
		static final RingBufferWorkerPoolFactory instance = new RingBufferWorkerPoolFactory();
	}

	// 对外不能暴露的接口
	private RingBufferWorkerPoolFactory() {
		// do nothing
	}

	public static RingBufferWorkerPoolFactory getInstance() {
		return SingletonHolder.instance;
	}

	// 生产者池
	private static Map<String, MessageProducer> producers = new ConcurrentHashMap<>();
	// 消费者池
	private static Map<String, MessageConsumer> consumers = new ConcurrentHashMap<>();

	private RingBuffer<TranslatorDataWapper> ringBuffer;

	private SequenceBarrier sequenceBarrier;

	private WorkerPool<TranslatorDataWapper> workerPool;

	// ProducerType 生产者类型是多生产还是单生产
	public void initAndStart(ProducerType type, int bufferSize, WaitStrategy waitStrategy,
			MessageConsumer[] messageConsumers) {
		// 1. 构建ringBuffer对象
		this.ringBuffer = RingBuffer.create(type, new EventFactory<TranslatorDataWapper>() {
			public TranslatorDataWapper newInstance() {
				return new TranslatorDataWapper();
			}
		}, bufferSize, waitStrategy);
		// 2.设置序号栅栏
		this.sequenceBarrier = this.ringBuffer.newBarrier();

		// 3.设置工作池
		this.workerPool = new WorkerPool<>(this.ringBuffer, this.sequenceBarrier, new EventExceptionHandler(),
				messageConsumers);

		// 4 把所构建的消费者置入池中
		for (MessageConsumer mc : messageConsumers) {
			consumers.put(mc.getConsumerId(), mc);
		}

		// 5 添加我们的sequences
		this.ringBuffer.addGatingSequences(this.workerPool.getWorkerSequences());

		// 6 启动我们的工作池
		this.workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2));
	}

	// 生产者
	public MessageProducer getMessageProducer(String producerId) {
		return producers.computeIfAbsent(producerId, k -> new MessageProducer(producerId, this.ringBuffer));
	}

	/**
	 * 异常静态类
	 */
	static class EventExceptionHandler implements ExceptionHandler<TranslatorDataWapper> {
		public void handleEventException(Throwable ex, long sequence, TranslatorDataWapper event) {
			//
		}

		public void handleOnStartException(Throwable ex) {
			//
		}

		public void handleOnShutdownException(Throwable ex) {
			//
		}
	}
}
