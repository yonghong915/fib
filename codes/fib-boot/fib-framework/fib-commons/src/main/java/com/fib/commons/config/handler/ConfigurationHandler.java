package com.fib.commons.config.handler;

import com.fib.commons.config.Configuration;

/**
 * 配置处理器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-12-01
 */
public interface ConfigurationHandler {

	/**
	 * 加载配置
	 * 
	 * @param configName
	 * @return
	 */
	public Configuration load();

	/**
	 * 检查有效性
	 * 
	 * @return
	 */
	default boolean checkValidate() {
		return true;
	}
}
