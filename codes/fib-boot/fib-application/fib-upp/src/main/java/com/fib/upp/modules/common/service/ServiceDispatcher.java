package com.fib.upp.modules.common.service;

import java.util.Map;
import java.util.concurrent.Executor;

import com.fib.core.util.SpringContextUtils;

/**
 * 服务分发器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-11 16:37:14
 */
public class ServiceDispatcher {
	private ServiceDispatcher() {
		// nothing to do
	}

	/**
	 * 同步服务
	 * 
	 * @param serviceName
	 * @param context
	 * @return
	 */
	public static Map<String, Object> runSync(String serviceName, Map<String, ? extends Object> context) {
		ICommonService commSrv = SpringContextUtils.getBean(serviceName);
		return commSrv.execute(context);
	}

	/**
	 * 异步服务
	 * 
	 * @param serviceName
	 * @param context
	 */
	public static void runAsync(String serviceName, Map<String, ? extends Object> context) {
		Executor executor = SpringContextUtils.getBean("customAsyncExcecutor");
		executor.execute(() -> {
			ICommonService commSrv = SpringContextUtils.getBean(serviceName);
			commSrv.execute(context);
		});
	}
}
