package com.fib.msgconverter.commgateway.channel.longconnection.login;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 登陆程序抽象类
 * 
 * @author 刘恭亮
 * 
 */
public abstract class AbstractLogin implements Login {
	/**
	 * 参数
	 */
	protected Map parameters = null;

	protected Logger logger = null;

	/**
	 * @return the parameters
	 */
	public Map getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}