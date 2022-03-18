package com.fib.commons.disruptor.base;

/**
 * Executor interface.
 */
public interface Executor extends Releasable {

	/**
	 * On execute
	 */
	void onExecute(Object msg);
}
