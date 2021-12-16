/**
 * 北京长信通信息技术有限公司
 * 2008-8-29 上午11:25:29
 */
package com.fib.upp.converter.xml.channel.message.recognizer;

import java.util.Map;

/**
 * 抽象的消息识别器
 * 
 * @author 刘恭亮
 * 
 */
public abstract class AbstractMessageRecognizer implements MessageRecognizer {

	/**
	 * 参数
	 */
	protected Map<String, String> parameters = null;

	/**
	 * @return the parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
}