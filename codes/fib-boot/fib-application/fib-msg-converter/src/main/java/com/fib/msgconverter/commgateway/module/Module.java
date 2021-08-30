/**
 * 北京长信通信息技术有限公司
 * 2008-11-19 下午05:51:41
 */
package com.fib.msgconverter.commgateway.module;

import java.util.Map;

/**
 * 组件接口。网关启动时初始化组件，运行过程中使用，停止服务前先停组件。
 * 
 * @author 刘恭亮
 * 
 */
public abstract class Module {
	private Map parameters;

	/**
	 * 初始化
	 */
	public abstract void initialize();

	/**
	 * 停止
	 */
	public abstract void shutdown();

	public Map getParameters() {
		return parameters;
	}

	public void setParameter(Map parameters) {
		this.parameters = parameters;
	}

}
