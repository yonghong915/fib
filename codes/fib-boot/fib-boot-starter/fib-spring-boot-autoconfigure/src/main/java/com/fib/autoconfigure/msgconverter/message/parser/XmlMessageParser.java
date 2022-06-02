package com.fib.autoconfigure.msgconverter.message.parser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.fib.autoconfigure.msgconverter.message.bean.MessageBean;
import com.fib.autoconfigure.msgconverter.message.metadata.ConstantMB;
import com.fib.autoconfigure.msgconverter.message.metadata.Field;
import com.fib.autoconfigure.msgconverter.message.metadata.Message;
import com.fib.autoconfigure.msgconverter.message.metadata.ValueRange;
import com.fib.autoconfigure.msgconverter.util.BeanShellEngine;
import com.fib.autoconfigure.msgconverter.util.ClassUtil;
import com.fib.autoconfigure.msgconverter.util.CodeUtil;
import com.fib.autoconfigure.msgconverter.util.ExceptionUtil;
import com.fib.autoconfigure.msgconverter.util.StringUtil;
import com.fib.commons.exception.CommonException;
import com.fib.commons.util.SortHashMap;

import cn.hutool.core.util.StrUtil;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public class XmlMessageParser extends AbstractMessageParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlMessageParser.class);
	Document document;

	public XmlMessageParser() {
		document = null;
	}

	@Override
	protected int parse(int idx) {
		byte[] dest = new byte[messageData.length - idx];
		System.arraycopy(messageData, idx, dest, 0, messageData.length - idx);
		LOGGER.info("dest={}", new String(dest));
		messageData = dest;
		parse();
		return idx;
	}

	public MessageBean parse() {
		if (null == messageData)
			throw new CommonException("parameter.null.messageData");
		if (null == message)
			throw new CommonException("parameter.null.message");
		if (0 == variableCache.size())
			loadVariable();
		if (null != message.getMsgCharset())
			try {
				messageData = (new String(messageData, message.getMsgCharset())).getBytes();
			} catch (UnsupportedEncodingException unsupportedencodingexception) {
				unsupportedencodingexception.printStackTrace();
				throw new CommonException("message.encoding.unsupport");
			}
		if (message.getPreParseEvent() != null)
			exeShell("pre-parse", message.getPreParseEvent(), null);
		SAXReader saxreader = new SAXReader();
		try {
			document = saxreader.read(new InputSource(new StringReader(new String(messageData))));
			String nsURI = document.getRootElement().getNamespaceURI();
			Map<String, String> namespaceURIs = new HashMap<>();
			namespaceURIs.put("default", nsURI);
			DocumentFactory.getInstance().setXPathNamespaceURIs(namespaceURIs);
		} catch (Exception exception) {
			exception.printStackTrace();
			ExceptionUtil.throwActualException(exception);
		}

		MessageBean messagebean = beanAssignment(message);
		if (message.getPostParseEvent() != null)
			exeShell("post-parse", message.getPostParseEvent(), messagebean);
		return messagebean;
	}

	protected void exeShell(String s, String s1, MessageBean messagebean) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		beanshellengine.set("messageParser", this);
		beanshellengine.set("message", message);
		beanshellengine.set("messageBean", messagebean);
		beanshellengine.set("messageData", messageData);
		if (0 != variableCache.size()) {
			Iterator<String> iterator = variableCache.keySet().iterator();
			String s2;
			for (; iterator.hasNext(); beanshellengine.set(s2, variableCache.get(s2)))
				s2 = iterator.next();

		}
		beanshellengine.eval(s1);
		messageData = (byte[]) beanshellengine.get("messageData");
		if (0 != variableCache.size()) {
			Iterator<String> iterator1 = variableCache.keySet().iterator();
			String s3;
			for (; iterator1.hasNext(); variableCache.put(s3, beanshellengine.get(s3)))
				s3 = iterator1.next();

		}
	}

	public MessageBean beanAssignment(Message message) {
		return beanAssignment(message, null);
	}

	public MessageBean beanAssignment(Message message, Node node) {
		if (null == message.getClassName())
			throw new IllegalArgumentException("XmlMessageParser.beanAssignment.message.className.null");
		MessageBean messagebean = messageBean;
		try {
			Class<?> class1 = Class.forName(message.getClassName());
			if (null != messagebean && !messagebean.getClass().equals(class1) || null == messagebean)
				messagebean = (MessageBean) class1.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			LOGGER.error("Failed to....", e);
			ExceptionUtil.throwActualException(e);
		}
		if (null != getParentBean())
			messagebean.setParent(getParentBean());
		String s = null;
		if (null != message.getXpath())
			s = message.getXpath();
		else
			s = message.getId();
		List<Field> list = message.getFieldList();
		for (int i = 0; i < list.size(); i++) {
			Field field = list.get(i);
			if (null == field.getXpath() && 2000 == field.getFieldType() || !field.isEditable())
				continue;
			if (node != null)
				C(messagebean, message, field, null, node);
			else
				A(messagebean, message, field, s);
		}

		return messagebean;
	}

	private void A(MessageBean messagebean, Message message, Field field, String s) {
		C(messagebean, message, field, s, null);
	}

	private void C(MessageBean messagebean, Message message, Field field, String s, Node node) {
		if (null == messagebean)
			throw new IllegalArgumentException("parameter.null.MessageBean");
		if (null == field)
			throw new IllegalArgumentException("parameter.null.field");
		switch (field.getFieldType()) {
		case 2002, 2008:
			B(messagebean, message, field, s, node);
			break;

		case 2004:
			E(messagebean, message, field, s, node);
			break;

		case 2011:
			D(messagebean, message, field, s, node);
			break;

		case 2003, 2005, 2006, 2007, 2009, 2010:
		default:
			A(messagebean, message, field, s, node);
			break;
		}
	}

	private void D(MessageBean messagebean, Message message, Field field, String s, Node node) {
		String s1 = null;
		if (s != null)
			s1 = (new StringBuilder()).append(s).append("/").append(field.getXpath()).toString();
		else
			s1 = field.getXpath();
		if (null != field.getRowXpath())
			s1 = (new StringBuilder()).append(s1).append("/").append(field.getRowXpath()).toString();
		Message message1 = null;
		if (ConstantMB.ReferenceType.DYNAMIC.getCode().equalsIgnoreCase(field.getReferenceType())) {
			message1 = A(field, message, messagebean);
		} else {
			Class<?> class1 = getFieldClass(messagebean, field);
			if (null != field.getReference()) {
				message1 = field.getReference();
			} else {
				message1 = new Message();
				message1.setId(s1);
				message1.setClassName(class1.getName());
				message1.setFields(field.getSubFields());
			}
		}
		SortHashMap<String, Field> sorthashmap = (SortHashMap<String, Field>) message1.getFields();
		String[] as = new String[sorthashmap.size()];
		String s2 = s1;
		if (null != message1.getXpath())
			s2 = (new StringBuilder()).append(s1).append("/").append(message1.getXpath()).toString();
		for (int i = 0; i < as.length; i++)
			as[i] = (new StringBuilder()).append(s2).append("/")
					.append(null != (sorthashmap.get(i)).getXpath() ? (sorthashmap.get(i)).getXpath() : (sorthashmap.get(i)).getName()).toString();

		List<List<Node>> arraylist = new ArrayList<>();
		for (int j = 0; j < as.length; j++) {
			List<Node> list;
			if (node == null)
				list = document.selectNodes(as[j]);
			else
				list = node.selectNodes(as[j]);
			if (!list.isEmpty())
				arraylist.add(list);
		}

		if (arraylist.isEmpty()) {
			return;
		}
		int k = arraylist.get(0).size();
		int l = 0;
		List<MessageBean> arraylist1 = new ArrayList<>(k);
		MessageBean messagebean1 = null;
		for (int i1 = 0; i1 < k; i1++) {
			if (null != field.getPreRowParseEvent())
				A(field, "row-pre-parse", field.getPreRowParseEvent(), messagebean, null, arraylist1, k, i1);
			try {
				Class<?> class2 = Class.forName(message1.getClassName());
				messagebean1 = (MessageBean) class2.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				LOGGER.error("Failed to....", e);
				ExceptionUtil.throwActualException(e);
			}
			for (int j1 = 0; j1 < arraylist.size(); j1++)
				A(messagebean1, message1, sorthashmap.get(j1), null, (arraylist.get(j1)).get(i1));

			l++;
			arraylist1.add(messagebean1);
			if (null != field.getPostRowParseEvent())
				A(field, "row-post-parse", field.getPostRowParseEvent(), messagebean, messagebean1, arraylist1, k, i1);
		}

		if (0 == l) {
			return;
		}

		invokeMessageBeanMethod(messagebean, field, List.class, arraylist1);
	}

	private void E(MessageBean messagebean, Message message, Field field, String s, Node node) {
		String s1 = null;
		if (s != null)
			s1 = (new StringBuilder()).append(s).append("/").append(field.getXpath()).toString();
		else
			s1 = field.getXpath();
		if (null != field.getRowXpath())
			s1 = (new StringBuilder()).append(s1).append("/").append(field.getRowXpath()).toString();
		Message message1 = null;
		if (ConstantMB.ReferenceType.DYNAMIC.getCode().equalsIgnoreCase(field.getReferenceType())) {
			message1 = A(field, message, messagebean);
		} else {
			Class<?> class1 = getFieldClass(messagebean, field);
			if (null != field.getReference()) {
				message1 = field.getReference();
			} else {
				message1 = new Message();
				message1.setId(s1);
				message1.setClassName(class1.getName());
				message1.setFields(field.getSubFields());
			}
		}
		if (s1.indexOf("@") != -1 || s1.indexOf("(") != -1) {
			String s2 = A(s1, node, field);
			if (null == s2 || 0 == s2.length())
				return;
			int i = parseIntValue(field.getRowNumField(), ClassUtil.getBeanAttributeValue(messagebean, field.getRowNumField().getName()));
			if (ConstantMB.ReferenceType.DYNAMIC.getCode().equalsIgnoreCase(field.getReferenceType())) {
				List<MessageBean> arraylist = new ArrayList<>(i);
				int k = 0;
				for (int i1 = 0; i1 < i; i1++) {
					if (null != field.getPreRowParseEvent())
						A(field, "row-pre-parse", field.getPreRowParseEvent(), messagebean, null, arraylist, i, i1);
					MessageBean messagebean3 = (MessageBean) ClassUtil.createClassInstance(message1.getClassName());
					messagebean.setMetadata(message);
					if (null != message.getMsgCharset()) {
						byte[] abyte0 = null;
						try {
							abyte0 = s2.getBytes(message.getMsgCharset());
						} catch (UnsupportedEncodingException unsupportedencodingexception) {
							unsupportedencodingexception.printStackTrace();
							throw new CommonException("message.encoding.unsupport");
						}
						k = getMsgParserIndx(message1, messagebean3, k, abyte0);
					} else {
						k = getMsgParserIndx(message1, messagebean3, k, s2.getBytes());
					}
					arraylist.add(messagebean3);
					if (null != field.getPostRowParseEvent())
						A(field, "row-post-parse", field.getPostRowParseEvent(), messagebean, messagebean3, arraylist, i, i1);
				}

				ClassUtil.setBeanAttributeValue(messagebean, field.getName(), arraylist, List.class);
			} else {
				String s3 = (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
				int l = 0;
				for (int j1 = 0; j1 < i; j1++) {
					if (null != field.getPreRowParseEvent())
						A(field, "row-pre-parse", field.getPreRowParseEvent(), messagebean, null, null, i, j1);
					MessageBean messagebean2 = (MessageBean) ClassUtil.invoke(messagebean, s3, null, null);
					messagebean.setMetadata(message);
					if (null != message.getMsgCharset()) {
						byte[] abyte1 = null;
						try {
							abyte1 = s2.getBytes(message.getMsgCharset());
						} catch (UnsupportedEncodingException unsupportedencodingexception1) {
							unsupportedencodingexception1.printStackTrace();
							throw new CommonException("message.encoding.unsupport");
						}
						l = getMsgParserIndx(message1, messagebean2, l, abyte1);
					} else {
						l = getMsgParserIndx(message1, messagebean2, l, s2.getBytes());
					}
					if (null != field.getPostRowParseEvent())
						A(field, "row-post-parse", field.getPostRowParseEvent(), messagebean, messagebean2, null, i, j1);
				}

			}
		} else {
			Field field1 = message1.getFieldList().get(0);
			if (field1.getXpath().indexOf("${") == -1) {
				List<Node> list = null;
				if (node == null)
					list = document.selectNodes(s1);
				else
					list = node.selectNodes(s1);
				int j = 0;
				String s5 = s1;
				String s7 = message1.getXpath();
				if (null != s7)
					s5 = (new StringBuilder()).append(s1).append("/").append(s7).toString();
				Message message2 = message1.copy();
				message2.setXpath(s5);
				List<MessageBean> arraylist2 = new ArrayList<>(list.size());
				for (int l1 = 0; l1 < list.size(); l1++) {
					if (null != field.getPreRowParseEvent())
						A(field, "row-pre-parse", field.getPreRowParseEvent(), messagebean, null, arraylist2, list.size(), l1);
					j++;
					Node node1 = list.get(l1);
					MessageBean messagebean4 = beanAssignment(message2, node1);
					arraylist2.add(messagebean4);
					if (null != field.getPostRowParseEvent())
						A(field, "row-post-parse", field.getPostRowParseEvent(), messagebean, messagebean4, arraylist2, list.size(), l1);
				}

				if (0 == j)
					return;
				invokeMessageBeanMethod(messagebean, field, List.class, arraylist2);
			} else {
				if (null != message1.getXpath())
					s1 = (new StringBuilder()).append(s1).append("/").append(message1.getXpath()).toString();
				MessageBean messagebean1 = null;
				List<MessageBean> arraylist1 = new ArrayList<>();
				int k1 = 0;
				do {
					if (null != field.getPreRowParseEvent())
						A(field, "row-pre-parse", field.getPreRowParseEvent(), messagebean, null, arraylist1, k1, k1);
					Iterator<Field> iterator = message1.getFields().values().iterator();
					messagebean1 = (MessageBean) ClassUtil.createClassInstance(message1.getClassName());
					Field field2;
					String s4;
					for (; iterator.hasNext(); A(messagebean1, message, field2, s4)) {
						field2 = iterator.next();
						s4 = (new StringBuilder()).append(s1).append("/").append(field2.getXpath()).toString();
						String s6 = variableCache.get(s4.substring(s4.indexOf("${") + 2, s4.indexOf("}"))).toString();
						s4 = (new StringBuilder()).append(s4.substring(0, s4.indexOf("${"))).append(s6).append(s4.substring(s4.indexOf("}") + 1))
								.toString();
						messagebean.setMetadata(message);
						messagebean1.setParent(messagebean);
					}

					arraylist1.add(messagebean1);
					if (null != field.getPostRowParseEvent())
						A(field, "row-post-parse", field.getPostRowParseEvent(), messagebean, messagebean1, arraylist1, k1, k1);
				} while (null != messagebean1.toString());
				arraylist1.remove(arraylist1.size() - 1);
				invokeMessageBeanMethod(messagebean, field, List.class, arraylist1);
			}
		}
	}

	private void invokeMessageBeanMethod(MessageBean messagebean, Field field, Class<?> class1, Object obj) {
		if (null != class1 && null != obj)
			try {
				Method method = messagebean.getClass()
						.getMethod((new StringBuilder()).append("set").append(StringUtil.toUpperCaseFirstOne(field.getName())).toString(), class1);
				method.invoke(messagebean, obj);
			} catch (Exception e) {
				LOGGER.error("Failed to....", e);
				ExceptionUtil.throwActualException(e);
			}
	}

	private int getMsgParserIndx(Message message, MessageBean messagebean, int i, byte[] abyte0) {
		AbstractMessageParser b = MessageParserFactory.getMessageParser(message);
		b.setVariableCache(variableCache);
		b.setMessage(message);
		b.setMessageData(abyte0);
		b.setMessageBean(messagebean);
		b.setParentBean(messagebean);
		return b.parse(i);
	}

	private MessageBean A(Message message, MessageBean messagebean, byte[] abyte0) {
		AbstractMessageParser b = MessageParserFactory.getMessageParser(message);
		b.setVariableCache(variableCache);
		b.setMessage(message);
		b.setMessageData(abyte0);
		b.setParentBean(messagebean);
		return b.parse();
	}

	private void B(MessageBean messagebean, Message message, Field field, String s, Node node) {
		String s1 = null;
		if (s != null)
			s1 = (new StringBuilder()).append(s).append("/").append(field.getXpath()).toString();
		else
			s1 = field.getXpath();
		Message message1 = null;
		Object obj = null;
		if ("dynamic".equalsIgnoreCase(field.getReferenceType())) {
			obj = MessageBean.class;
			message1 = A(field, message, messagebean);
		} else {
			obj = getFieldClass(messagebean, field);
			if (null != field.getReference()) {
				message1 = field.getReference();
			} else {
				message1 = new Message();
				message1.setId(s1);
				message1.setClassName(((Class<?>) (obj)).getName());
				message1.setFields(field.getSubFields());
			}
		}
		if (null == field.getXpath()) {
			Message message2 = message1.copy();
			if (null != message1.getXpath())
				message2.setXpath((new StringBuilder()).append(s).append("/").append(message1.getXpath()).toString());
			else
				message2.setXpath(s);
			messagebean.setMetadata(message);
			MessageBean messagebean1 = A(message2, messagebean, messageData);
			if (null == messagebean1)
				return;
			invokeMessageBeanMethod(messagebean, field, ((Class<?>) (obj)), messagebean1);
		} else if (s1.indexOf("@") != -1 || s1.indexOf("(") != -1) {
			String s2 = A(s1, node, field);
			if (null == s2 || 0 == s2.length())
				return;
			messagebean.setMetadata(message);
			if (null != message.getMsgCharset()) {
				byte[] abyte0 = null;
				try {
					abyte0 = s2.getBytes(message.getMsgCharset());
				} catch (UnsupportedEncodingException unsupportedencodingexception) {
					unsupportedencodingexception.printStackTrace();
					throw new CommonException("message.encoding.unsupport");
				}
				MessageBean messagebean3 = A(message1, messagebean, abyte0);
				invokeMessageBeanMethod(messagebean, field, ((Class<?>) (obj)), messagebean3);
			} else {
				MessageBean messagebean2 = A(message1, messagebean, s2.getBytes());
				invokeMessageBeanMethod(messagebean, field, ((Class<?>) (obj)), messagebean2);
			}
		} else {
			List<Node> list = null;
			if (node == null)
				list = document.selectNodes(s1);
			else
				list = node.selectNodes(s1);
			if (list.isEmpty()) {
				return;
			}
			if (null != message1.getXpath())
				s1 = (new StringBuilder()).append(s1).append("/").append(message1.getXpath()).toString();
			Message message3 = message1.copy();
			message3.setXpath(s1);
			MessageBean messagebean4 = beanAssignment(message3, list.get(0));
			invokeMessageBeanMethod(messagebean, field, ((Class<?>) (obj)), messagebean4);
		}
	}

	private void A(MessageBean messagebean, Message message, Field field, String s, Node node) {
		String s1 = null;
		if (field.getXpath().indexOf("${") == -1) {
			if (null == s)
				s1 = field.getXpath();
			else
				s1 = (new StringBuilder()).append(s).append("/").append(field.getXpath()).toString();
		} else {
			s1 = s;
		}
		String s2 = A(s1, node, field);
		s2 = new String(A(field, s2.getBytes()));
		if (StrUtil.isEmptyIfStr(s1)) {
			return;
		}

		String s3 = ConstantMB.getJavaTypeByDataType(field.getDataType());
		invokeMessageBeanMethod(messagebean, field, A(s3), getObject(s2, s3));
	}

	protected Message A(Field field, Message message, MessageBean messagebean) {
		String[] as = field.getReferenceId().split("\\.");
		Message message1 = message;
		if (as.length > 1) {
			MessageBean messagebean1 = messagebean;
			for (int i = 0; i < as.length - 1; i++)
				messagebean1 = messagebean1.getParent();

			message1 = messagebean1.getMetadata();
		}
		Field field1 = message1.getField(as[as.length - 1]);
		Object obj = ClassUtil.getBeanValueByPath(messagebean, field.getReferenceId());
		ValueRange valuerange = (ValueRange) field1.getValueRange().get(obj);
		if (null == valuerange)
			valuerange = (ValueRange) field1.getValueRange().get("default-ref");
		return valuerange.getReference();
	}

	private String A(String s, Node node, Field field) {
		int i = s.indexOf("/text()");
		if (-1 < i)
			s = s.substring(0, i);
		Node node1 = null;
		if (node == null)
			node1 = document.selectSingleNode(s);
		else
			node1 = node.selectSingleNode(s);
		if (null == node1)
			return "";
		else
			return node1.getText();
	}

	private Class<?> A(String s) {
		if ("String".equals(s))
			return String.class;
		if ("byte[]".equals(s))
			return Byte[].class;
		if ("int".equals(s))
			return Integer.TYPE;
		if ("byte".equals(s))
			return Byte.TYPE;
		if ("short".equals(s))
			return Short.TYPE;
		if ("long".equals(s))
			return Long.TYPE;
		else
			throw new CommonException("XmlMessageParser.fieldType.illegal");
	}

	private Object getObject(String value, String javaType) {
		if (null == value) {
			return null;
		}
		if ("".equals(value)) {
			value = "0";
		}

		if ("String".equals(javaType)) {
			return value;
		} else if ("byte[]".equals(javaType)) {
			return value.getBytes();
		} else if ("int".equals(javaType)) {
			return Integer.valueOf(Integer.parseInt(value));
		} else if ("byte".equals(javaType)) {
			if (!"".equals(value)) {
				return Byte.valueOf(value.getBytes()[0]);
			} else {
				return Byte.valueOf("0");
			}
		} else if ("short".equals(javaType)) {
			return Short.valueOf(Short.parseShort(value));
		} else if ("long".equals(javaType)) {
			return Long.valueOf(Long.parseLong(value));
		} else {
			throw new CommonException("XmlMessageParser.fieldType.illegal");
		}
	}

	private Class<?> getFieldClass(MessageBean messagebean, Field field) {
		String s = null;
		if (null != field.getReference())
			s = field.getReference().getClassName();
		else if (null != field.getCombineOrTableFieldClassName()) {
			s = field.getCombineOrTableFieldClassName();
		} else {
			String s1 = messagebean.getClass().getName();
			s = (new StringBuilder()).append(s1).append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
		}
		Class<?> class1 = null;
		try {
			class1 = Class.forName(s);
		} catch (ClassNotFoundException classnotfoundexception) {
			throw new CommonException((new StringBuilder()).append("XmlMessageParser.getFieldClass.class.notFound")
					.append(classnotfoundexception.getMessage()).toString(), classnotfoundexception);
		}
		return class1;
	}

	protected int parseIntValue(Field field, Object obj) {
		int i = 0;
		switch (field.getDataType()) {
		case 3001:
			if ("".equals(obj))
				obj = "0";
			i = Integer.parseInt((String) obj);
			break;

		case 3003, 3007:
			i = ((Integer) obj).intValue();
			break;

		case 3004:
			i = ((Byte) obj).intValue();
			break;

		case 3005, 3008:
			i = ((Short) obj).intValue();
			break;

		case 3002, 3006:
		default:
			throw new CommonException("parseIntValue.dataType.unsupport");
		}
		return i;
	}

	protected byte[] A(Field field, byte[] abyte0) {
		if (5000 == field.getPaddingDirection())
			return abyte0;
		String s = field.getExtendAttribute("remove_start");
		if (null != s) {
			int i = CodeUtil.byteArrayIndexOf(abyte0, CodeUtil.hextoByte(s), 0);
			byte[] abyte1 = abyte0;
			if (-1 != i) {
				abyte0 = new byte[i];
				System.arraycopy(abyte1, 0, abyte0, 0, i);
			}
		}
		int j = 0;
		if (5001 == field.getPaddingDirection()) {
			for (j = 0; j < abyte0.length && abyte0[j] == field.getPadding(); j++)
				;
			if (0 == j)
				return abyte0;
			if (abyte0.length == j) {
				return new byte[0];
			} else {
				byte[] abyte2 = new byte[abyte0.length - j];
				System.arraycopy(abyte0, j, abyte2, 0, abyte2.length);
				return abyte2;
			}
		}
		for (j = abyte0.length - 1; j >= 0 && abyte0[j] == field.getPadding(); j--)
			;
		if (abyte0.length - 1 == j)
			return abyte0;
		if (0 > j) {
			return new byte[0];
		} else {
			byte[] abyte3 = new byte[j + 1];
			System.arraycopy(abyte0, 0, abyte3, 0, abyte3.length);
			return abyte3;
		}
	}

	protected void A(Field field, String s, String s1, MessageBean messagebean, MessageBean messagebean1, List<?> list, int i, int j) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		beanshellengine.set("messageParser", this);
		beanshellengine.set("message", message);
		beanshellengine.set("messageBean", messagebean);
		beanshellengine.set("messageData", messageData);
		beanshellengine.set("field", field);
		beanshellengine.set("list", list);
		beanshellengine.set("rowNum", Integer.valueOf(i));
		beanshellengine.set("currRow", Integer.valueOf(j));
		beanshellengine.set("currentBean", messagebean1);
		if (0 != variableCache.size()) {
			Iterator<String> iterator = variableCache.keySet().iterator();
			String s2;
			for (; iterator.hasNext(); beanshellengine.set(s2, variableCache.get(s2)))
				s2 = iterator.next();

		}
		beanshellengine.eval(s1);
		if (0 != variableCache.size()) {
			Iterator<String> iterator1 = variableCache.keySet().iterator();
			String s3;
			for (; iterator1.hasNext(); variableCache.put(s3, beanshellengine.get(s3)))
				s3 = iterator1.next();

		}
	}
}
