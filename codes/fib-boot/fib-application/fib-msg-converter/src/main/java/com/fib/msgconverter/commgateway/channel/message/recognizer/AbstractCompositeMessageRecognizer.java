package com.fib.msgconverter.commgateway.channel.message.recognizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 组合消息识别器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-30
 */
public abstract class AbstractCompositeMessageRecognizer extends AbstractMessageRecognizer {
	/**
	 * 参数名：连接符
	 */
	public static final String PARAM_TEXT_COUPLING = "coupling";

	/**
	 * 连接符
	 */
	protected String coupling = null;

	/**
	 * 组件列表
	 */
	protected List<MessageRecognizer> componentList = new ArrayList<>();

	public String recognize(byte[] message) {
		StringBuffer buf = new StringBuffer(128);

		MessageRecognizer component = null;
		for (int i = 0; i < componentList.size(); i++) {
			component = (MessageRecognizer) componentList.get(i);
			buf.append(component.recognize(message));
			if (coupling != null && i < componentList.size() - 1) {
				buf.append(coupling);
			}
		}

		return buf.toString();
	}

	public void add(MessageRecognizer component) {
		componentList.add(component);
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);
		parseParameters();
	}

	protected void parseParameters() {
		if (null != parameters) {
			Object o = parameters.get(PARAM_TEXT_COUPLING);
			if (null != o) {
				// coupling
				coupling = (String) o;
			}
		}
	}
}
