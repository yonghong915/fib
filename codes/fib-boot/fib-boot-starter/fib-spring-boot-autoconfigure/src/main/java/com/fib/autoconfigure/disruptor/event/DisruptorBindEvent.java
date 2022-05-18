package com.fib.autoconfigure.disruptor.event;

public class DisruptorBindEvent extends DisruptorEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 当前事件绑定的数据对象
	 */
	protected transient Object bind;

	public DisruptorBindEvent() {
		super(null);
	}

	public DisruptorBindEvent(Object source) {
		super(source);
	}

	public DisruptorBindEvent(Object source, Object bind) {
		super(source);
		this.bind = bind;
	}

	public Object getBind() {
		return bind;
	}

	public void bind(Object bind) {
		this.bind = bind;
	}
}