package com.fib.commons.disruptor;

import com.fib.commons.disruptor.base.Executor;

/**
 * 事件
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-18 11:13:16
 */
public class MsgEvent {

	private Executor executor;

	private Object msg;

	public Executor getExecutor() {
		return executor;
	}

	public Object getMsg() {
		return msg;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public void setValues(Object msg) {
		this.msg = msg;
	}

	public void clearValues() {
		setValues(null);
		setExecutor(null);
	}
}
