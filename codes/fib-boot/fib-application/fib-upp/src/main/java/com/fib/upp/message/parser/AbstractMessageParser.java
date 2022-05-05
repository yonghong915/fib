package com.fib.upp.message.parser;

import java.util.HashMap;
import java.util.Map;

import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.metadata.Field;
import com.fib.upp.message.metadata.Message;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public abstract class AbstractMessageParser {
	protected Message message;
	protected Map<String, Object> variableCache;
	protected byte[] messageData;
	protected MessageBean messageBean;
	protected MessageBean parentBean;
	private String fileEncoding;

	protected AbstractMessageParser() {
		message = null;
		variableCache = new HashMap<>(5);
		messageData = null;
		messageBean = null;
		parentBean = null;
		fileEncoding = System.getProperty("file.encoding");
	}

	public String getDefaultCharset() {
		return fileEncoding;
	}

	public void setDefaultCharset(String s) {
		fileEncoding = s;
	}

	public abstract MessageBean parse();

	protected int parse(int idx) {
		return 0;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message1) {
		message = message1;
	}

	public byte[] getMessageData() {
		return messageData;
	}

	public void setMessageData(byte[] abyte0) {
		messageData = abyte0;
	}

	public MessageBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(MessageBean messagebean) {
		messageBean = messagebean;
	}

	public void ignore(Field field) {
		ignore(field.getName());
	}

	public void ignore(String s) {
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

	public void setVariableCache(Map<String, Object> map) {
		variableCache = map;
	}

	protected void loadVariable() {
//		if (null != message.getVariable()) {
//			Iterator<Variable> iterator = message.getVariable().values().iterator();
//			Variable a;
//			Object obj2;
//			for (; iterator.hasNext(); variableCache.put(a.getName(), obj2)) {
//				a = iterator.next();
//				switch (a.getDataType()) {
//				case 3000:
//				case 3001:
//					obj2 = a.getValue();
//					break;
//
//				case 3004:
//					obj2 = Byte.valueOf(a.getValue());
//					break;
//
//				case 3003:
//				case 3007:
//					obj2 = Integer.valueOf(a.getValue());
//					break;
//
//				case 3005:
//				case 3008:
//					obj2 = Short.valueOf(a.getValue());
//					break;
//
//				case 3002:
//				case 3006:
//				default:
//					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
//							.getString("tableRowField.dataType.unsupport", new String[] { a.getName(),
//									(new StringBuilder()).append("").append(a.getDataType()).toString() }));
//				}
//			}
//		}
	}
}