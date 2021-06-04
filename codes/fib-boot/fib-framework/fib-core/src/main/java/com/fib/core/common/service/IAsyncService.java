package com.fib.core.common.service;

/**
 * 异步服务接口
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-06-04
 */
public interface IAsyncService {
	/**
	 * 执行方法
	 * 
	 * @param source
	 */
	void execute(Object source);
}
