package com.fib.autoconfigure.msgconverter.channel.message.recognizer;

import java.util.Map;

/**
 * 抽象的消息识别器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-20 11:57:43
 */
public abstract class AbstractMessageRecognizer implements MessageRecognizer {

	/** 参数 */
	protected Map<String, String> parameters = null;

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
}