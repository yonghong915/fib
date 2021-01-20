package com.fib.gateway.channel.message.recognizer.impl;

import java.util.Map;

import com.fib.commons.util.ClassUtil;
import com.fib.gateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.gateway.message.MessageParser;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.MessageMetadataManager;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import com.fib.gateway.message.xml.script.BeanShellEngine;

/**
 * 8583报文识别器
 * 
 * @author 白帆
 * 
 */
public class ISO8583MessageRecognizer extends AbstractMessageRecognizer {
	public static final String GROUP_ID = "message-group-id";
	public static final String MESSAGE_ID = "message-id";
	public static final String FIELD_LIST = "field-list";
	public static final String COUPLING = "coupling";
	public static final String EXPRESSION = "expression";

	private String groupId = null;
	private String messageId = null;
	private String fieldList = null;
	private String coupling = null;
	private String expression = null;

	private BeanShellEngine bsh = null;

	public String recognize(byte[] message) {
		if (null == fieldList && null == expression) {
			// throw new RuntimeException(
			// "Both field-list and expression is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"ISO8583MessageRecognizer.recognize.allNull"));
		}

		if (!MessageMetadataManager.isMessageExist(groupId, messageId)) {
			// throw new RuntimeException("message[GroupId=" + groupId
			// + ", Messageid=" + messageId + "] is NULL!");
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"ISO8583MessageRecognizer.recognize.message.notExist",
									new String[] { groupId, messageId }));

		}
		MessageParser parser = new MessageParser();
		parser
				.setMessage(MessageMetadataManager.getMessage(groupId,
						messageId));
		parser.setMessageData(message);
		MessageBean bean = parser.parse();

		String messageId = "";
		if (null != expression) {
			messageId = (String) executeScript("bean", bean, expression);
		} else {
			String tmp = fieldList;
			int index = tmp.indexOf(',');
			if (-1 == index) {
				messageId = (String) ClassUtil.getBeanAttributeValue(bean, tmp);
			} else {
				String sep = "";
				if (null != coupling) {
					sep = coupling;
				}
				while (-1 < index) {
					messageId = messageId
							+ sep
							+ ClassUtil.getBeanAttributeValue(bean, tmp
									.substring(0, index));
					tmp = tmp.substring(index + 1);
					index = tmp.indexOf(',');
				}
				messageId = messageId + sep
						+ ClassUtil.getBeanAttributeValue(bean, tmp);

				messageId = messageId.substring(sep.length());
			}
		}
		return messageId;
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);

		parseParameters();
	}

	private void parseParameters() {
		// group-id
		String tmp = (String) parameters.get(GROUP_ID);
		if (null == tmp) {
			// throw new RuntimeException("parameter[" + GROUP_ID +
			// "] is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("parameter.null",
							new String[] { GROUP_ID }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter[" + GROUP_ID
				// + "] is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { GROUP_ID }));
			}
		}
		groupId = tmp;

		// message-id
		tmp = (String) parameters.get(MESSAGE_ID);
		if (null == tmp) {
			// throw new RuntimeException("parameter[" + MESSAGE_ID +
			// "] is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("parameter.null",
							new String[] { MESSAGE_ID }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter[" + MESSAGE_ID
				// + "] is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { MESSAGE_ID }));
			}
		}
		messageId = tmp;
		//
		// if (null == MessageMetadataManager.getMessage(groupId, messageId)) {
		// throw new RuntimeException("message[GroupId=" + groupId
		// + ", Messageid=" + messageId + "] is NULL!");
		// }

		// field-list
		tmp = (String) parameters.get(FIELD_LIST);
		if (null != tmp) {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter[" + FIELD_LIST
				// + "] is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { FIELD_LIST }));
			}
			fieldList = tmp;
		}

		// coupling
		tmp = (String) parameters.get(COUPLING);
		if (null != tmp) {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter[" + COUPLING
				// + "] is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { COUPLING }));
			}
			coupling = tmp;
		}

		// expression
		tmp = (String) parameters.get(EXPRESSION);
		if (null != tmp) {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter[" + EXPRESSION
				// + "] is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { EXPRESSION }));
			}
			expression = tmp;
		}
	}

	/**
	 * 获得脚本执行器
	 * 
	 * @return
	 */
	private BeanShellEngine getScriptExecutor() {
		if (null == bsh) {
			bsh = new BeanShellEngine();
		}
		return bsh;
	}

	/**
	 * 执行脚本
	 * 
	 * @param map
	 * @param script
	 * @return
	 */
	private Object executeScript(String name, Object object, String script) {
		BeanShellEngine executor = getScriptExecutor();
		// try {
		executor.set(name, object);
		// } catch (EvalError e) {
		// ExceptionUtil.throwActualException(e);
		// }

		// Object result = null;
		// try {
		return executor.eval(script);
		// } catch (EvalError e) {
		// ExceptionUtil.throwActualException(e);
		// }
		// return result;
	}
}
