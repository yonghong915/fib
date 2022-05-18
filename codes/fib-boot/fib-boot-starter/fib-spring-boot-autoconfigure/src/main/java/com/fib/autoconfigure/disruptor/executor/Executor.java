package com.fib.autoconfigure.disruptor.executor;

/**
 * 执行器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-18 11:12:52
 */
public interface Executor {
	/**
	 * 执行
	 * 
	 * @param msg
	 */
	void onExecute(Object msg);
}
