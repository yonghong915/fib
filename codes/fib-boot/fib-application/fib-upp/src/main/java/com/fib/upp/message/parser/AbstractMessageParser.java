package com.fib.upp.message.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fib.commons.util.I18nUtils;
import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.metadata.Message;
import com.fib.upp.message.metadata.Variable;

import lombok.Data;

@Data
public abstract class AbstractMessageParser {

	protected Message message;
	protected byte messageData[];
	protected MessageBean messageBean;
	protected Map<String, Object> variableCache;
	protected String defaultCharset;
	protected MessageBean parentBean;

	public AbstractMessageParser() {
		defaultCharset = System.getProperty("file.encoding");
		variableCache = new HashMap<>(5);
	}

	public abstract MessageBean parse();

	public Map<String, Object> getVariableCache() {
		return variableCache;
	}

	public void setVariableCache(Map<String, Object> variableCache) {
		this.variableCache = variableCache;
	}

	public MessageBean getParentBean() {
		return parentBean;
	}

	public void setParentBean(MessageBean parentBean) {
		this.parentBean = parentBean;
	}

	public int parse(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public byte[] getMessageData() {
		return messageData;
	}

	public void setMessageData(byte[] messageData) {
		this.messageData = messageData;
	}

	public MessageBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(MessageBean messageBean) {
		this.messageBean = messageBean;
	}

	public String getDefaultCharset() {
		return defaultCharset;
	}

	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = defaultCharset;
	}

	protected void loadVariable() {
		if (null != message.getVariable()) {
			Iterator<Variable> iterator = message.getVariable().values().iterator();
			Variable a;
			Object obj2;
			for (; iterator.hasNext(); variableCache.put(a.getName(), obj2)) {
				a = iterator.next();
				switch (a.getDataType()) {
				case 3000:
				case 3001:
					obj2 = a.getValue();
					break;

				case 3004:
					obj2 = Byte.valueOf(a.getValue());
					break;

				case 3003:
				case 3007:
					obj2 = Integer.valueOf(a.getValue());
					break;

				case 3005:
				case 3008:
					obj2 = Short.valueOf(a.getValue());
					break;

				case 3002:
				case 3006:
				default:
					throw new RuntimeException(I18nUtils.getMessage("tableRowField.dataType.unsupport", new String[] {
							a.getName(), (new StringBuilder()).append("").append(a.getDataType()).toString() }));
				}
			}
		}
	}

}
