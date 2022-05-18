package com.fib.autoconfigure.disruptor.event;

import java.io.Serializable;

import com.fib.autoconfigure.disruptor.executor.Executor;

/**
 * Disruptor事件
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-18 10:38:06
 */
public abstract class DisruptorEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** System time when the event happened */
	private final long timestamp;

	/** Event body */
	private transient Object msg;

	private transient Executor executor;

	protected DisruptorEvent(Object source) {
		this.timestamp = System.currentTimeMillis();
		this.msg = source;
	}

	public final long getTimestamp() {
		return this.timestamp;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	@Override
	public String toString() {
		return new StringBuilder("DisruptorEvent [Executor :").append(executor.getClass().getName()).append(" msg:").append(msg).append(" timestamp:")
				.append(timestamp).append("]").toString();
	}
}