package com.fib.autoconfigure.disruptor;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fib.autoconfigure.util.PrefixUtil;

/**
 * Disruptor配置属性
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-18 10:32:38
 */
@ConfigurationProperties(PrefixUtil.DISRUPTOR_PREFIX)
public class DisruptorProperties {

	/** Enable Disruptor */
	private boolean enabled = false;
	/** 是否自动创建RingBuffer对象 */
	private boolean ringBuffer = false;
	/** RingBuffer缓冲区大小, 默认 1024 */
	private int ringBufferSize = 1024;
	/** 消息消费线程池大小, 默认 4 */
	private int ringThreadNumbers = 4;
	/**
	 * 是否对生产者，如果是则通过
	 * RingBuffer.createMultiProducer创建一个多生产者的RingBuffer，否则通过RingBuffer.createSingleProducer创建一个单生产者的RingBuffer
	 */
	private boolean multiProducer = false;

	/** 消费模式,P2P点对点，PS发布订阅模式 */
	private String consumeMode;

	/** 消费者数量 */
	private int consumers = 1;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isRingBuffer() {
		return ringBuffer;
	}

	public void setRingBuffer(boolean ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public int getRingBufferSize() {
		return ringBufferSize;
	}

	public void setRingBufferSize(int ringBufferSize) {
		this.ringBufferSize = ringBufferSize;
	}

	public int getRingThreadNumbers() {
		return ringThreadNumbers;
	}

	public void setRingThreadNumbers(int ringThreadNumbers) {
		this.ringThreadNumbers = ringThreadNumbers;
	}

	public boolean isMultiProducer() {
		return multiProducer;
	}

	public void setMultiProducer(boolean multiProducer) {
		this.multiProducer = multiProducer;
	}

	public String getConsumeMode() {
		return consumeMode;
	}

	public void setConsumeMode(String consumeMode) {
		this.consumeMode = consumeMode;
	}

	public int getConsumers() {
		return consumers;
	}

	public void setConsumers(int consumers) {
		this.consumers = consumers;
	}
}
