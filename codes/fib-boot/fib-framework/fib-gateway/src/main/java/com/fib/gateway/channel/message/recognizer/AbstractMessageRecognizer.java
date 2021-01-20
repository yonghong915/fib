package com.fib.gateway.channel.message.recognizer;

import java.util.Map;

public abstract class AbstractMessageRecognizer implements MessageRecognizer {

	/**
	 * 参数
	 */
	protected Map parameters = null;

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

}
