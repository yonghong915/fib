package com.fib.upp.message.parser;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fib.commons.exception.BusinessException;
import com.fib.core.util.StatusCode;
import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.metadata.Field;
import com.fib.upp.message.metadata.Message;

import cn.hutool.core.lang.Assert;

public class DefaultMessageParser extends AbstractMessageParser {
	private Set<String> exitsfieldNames = new HashSet(32);;

	@Override
	public MessageBean parse() {
		Assert.notNull(message, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL, "message"));
		Assert.notNull(messageData, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL, "messageData"));
		parse(0);
		return messageBean;
	}

	@Override
	public int parse(int idx) {
		Iterator<Field> iterator = message.getFields().values().iterator();
		while (iterator.hasNext()) {
			Field field = iterator.next();
			idx = parseField(field, idx);
		}
		return idx;
	}

	private int parseField(Field field, int idx) {
		if (exitsfieldNames != null && exitsfieldNames.contains(field.getName())) {
			return idx;
		}
		String referenceType = field.getReferenceType();
		if ("dynamic".equalsIgnoreCase(referenceType)) {

		} else if ("expression".equalsIgnoreCase(referenceType)) {

		} else {
			switch (field.getFieldType()) {
			case 2000:
				break;
			case 2006:
				break;
			case 2007:
				break;
			case 2005:
				break;
			case 2001:
				break;
			case 2002, 2008:
				idx = parseCombineField(field, idx);
				break;
			case 2003:
				break;
			case 2009:
				break;
			case 2011:
				break;
			case 2004:
				break;
			case 2010:
				break;
			default:
				throw new BusinessException(StatusCode.RTN_NULL, "field.fieldType.notExist");
			}
		}
		return 0;
	}

	private int parseCombineField(Field field, int idx) {
		Message message = field.getReference();
		if (null == message) {
			message = new Message();
			message.setId((new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
			message.setShortText(field.getShortText());
			message.setFields(field.getSubFields());
		}
		MessageBean messagebean = (MessageBean) getBeanAttributeValue(messageBean, field.getName());
		return B(field, message, messagebean, idx, messageData);
	}

	protected int B(Field field, Message message, MessageBean messagebean, int idx, byte abyte0[]) {
		AbstractMessageParser b = MessageParserFactory.getMessageParser(message);
		b.setMessage(message);
		b.setMessageData(abyte0);
		b.messageBean = messagebean;
		b.setParentBean(messageBean);
		return b.parse(idx);
	}

	public static Object getBeanAttributeValue(Object obj, String s) {
		return getBeanAttributeValue(obj, s, null);
	}

	public static Object getBeanAttributeValue(Object obj, String attrName, String s1) {
		Assert.notNull(obj, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL, "obj"));
		Assert.notBlank(attrName, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL, "attrName"));

		String s2 = (new StringBuilder()).append("get").append(attrName.substring(0, 1).toUpperCase()).toString();
		if (attrName.length() > 1)
			s2 = (new StringBuilder()).append(s2).append(attrName.substring(1)).toString();
		if (s1 != null)
			s2 = (new StringBuilder()).append(s2).append(s1).toString();
		return invoke(obj, s2, null, null);
	}

	public static Object invoke(Object obj, String methodName, Class<?> aclass[], Object aobj[]) {
		Assert.notNull(obj, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL, "obj"));
		Assert.notBlank(methodName, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL, "methodName"));

		Class class1 = obj.getClass();
		Method method = null;
		try {
			method = class1.getMethod(methodName, aclass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object obj1 = null;
		try {
			obj1 = method.invoke(obj, aobj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj1;
	}
}
