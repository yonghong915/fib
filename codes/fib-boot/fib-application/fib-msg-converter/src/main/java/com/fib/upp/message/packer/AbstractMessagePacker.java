package com.fib.upp.message.packer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.metadata.Field;
import com.fib.upp.message.metadata.Message;
import com.fib.upp.message.metadata.Variable;
import com.giantstone.common.util.ByteBuffer;

/**
 * 抽象消息组装
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-30
 */
public abstract class AbstractMessagePacker {

	protected Message message;
	protected MessageBean messageBean;
	protected MessageBean parentBean;
	protected ByteBuffer buf;
	protected Map<String, Object> variableCache;
	private String defaultCharset;

	protected AbstractMessagePacker() {
		message = null;
		messageBean = null;
		parentBean = null;
		buf = null;
		variableCache = new HashMap<>(5);
		defaultCharset = System.getProperty("file.encoding");
	}

	public String getDefaultCharset() {
		return defaultCharset;
	}

	public void setDefaultCharset(String charset) {
		this.defaultCharset = charset;
	}

	public abstract byte[] pack();

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public MessageBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(MessageBean messagebean) {
		this.messageBean = messagebean;
	}

	public ByteBuffer getBuf() {
		return buf;
	}

	public void ignore(Field field) {
		ignore(field.getName());
	}

	public void ignore(String fieldName) {
	}

	public MessageBean getParentBean() {
		return parentBean;
	}

	public void setParentBean(MessageBean messagebean) {
		parentBean = messagebean;
	}

	public Map<String, Object> getVariableCache() {
		return variableCache;
	}

	public void setVariableCache(Map<String, Object> variableCache) {
		this.variableCache = variableCache;
	}

	protected void loadVariable() {
		if (null != message.getVariable() && 0 < message.getVariable().size()) {
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
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("tableRowField.dataType.unsupport",
							new String[] { a.getName(), (new StringBuilder()).append("").append(a.getDataType()).toString() }));
				}
			}
		}
	}
}