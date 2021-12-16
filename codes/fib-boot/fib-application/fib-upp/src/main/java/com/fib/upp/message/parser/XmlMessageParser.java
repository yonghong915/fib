package com.fib.upp.message.parser;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeanUtils;

import com.fib.commons.xml.Dom4jUtils;
import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.metadata.Field;
import com.fib.upp.message.metadata.Message;
import com.fib.upp.util.ClassUtil;
import com.fib.upp.util.Constant;
import com.fib.upp.util.SortHashMap;

import cn.hutool.core.util.StrUtil;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public class XmlMessageParser extends AbstractMessageParser {

	Document document;

	public XmlMessageParser() {
		document = null;
	}

	public MessageBean parse() {
		if (null == messageData)
			throw new RuntimeException("messageData parameter.null");
		if (null == message)
			throw new RuntimeException("message parameter.null");
//		if (0 == variableCache.size())
//			loadVariable();

		if (null != message.getMsgCharset())
			try {
				messageData = (new String(messageData, message.getMsgCharset())).getBytes();
			} catch (UnsupportedEncodingException unsupportedencodingexception) {
				unsupportedencodingexception.printStackTrace();
				throw new RuntimeException("message.encoding.unsupport");
			}
		if (message.getPreParseEvent() != null)
			A("pre-parse", message.getPreParseEvent(), ((MessageBean) (null)));
		SAXReader saxreader = SAXReader.createDefault();
		try {
			// document = saxreader.read(new InputSource(new StringReader(new
			// String(messageData))));
			document = Dom4jUtils.createDocument(new ByteArrayInputStream(messageData));
		} catch (Exception exception) {
			exception.printStackTrace();
			// ExceptionUtil.throwActualException(exception);
		}
		MessageBean messagebean = A(message);
		if (message.getPostParseEvent() != null)
			A("post-parse", message.getPostParseEvent(), messagebean);
		return messagebean;
	}

	public int parse(int i) {
		int len = messageData.length;
		String st = new String(messageData);
		int aL = "{H:020000          PMTS313684093748  HVPS20191219154619XMLccms.990.001.02     NA21261AEA0000130697201912190000240845773D         }"
				.getBytes().length;
		byte[] aa = new byte[messageData.length - aL];
		System.arraycopy(messageData, aL, aa, 0, aa.length);
		String s = new String(aa);
		messageData = aa;
		MessageBean mb = parse();
		//messageBean = mb;
		//ClassUtil.setBeanAttributeValue(messageBean, "cCMS_990_Out", mb, MessageBean.class);
		BeanUtils.copyProperties(mb, messageBean);
		return 0;
	}

	protected void A(String s, String s1, MessageBean messagebean) {
//		BeanShellEngine beanshellengine = new BeanShellEngine();
//		beanshellengine.set("messageParser", this);
//		beanshellengine.set("message", message);
//		beanshellengine.set("messageBean", messagebean);
//		beanshellengine.set("messageData", messageData);
//		if (0 != variableCache.size()) {
//			Iterator iterator = variableCache.keySet().iterator();
//			Object obj = null;
//			String s2;
//			for (; iterator.hasNext(); beanshellengine.set(s2, variableCache.get(s2)))
//				s2 = (String) iterator.next();
//
//		}
//		beanshellengine.eval(s1);
//		messageData = (byte[]) (byte[]) beanshellengine.get("messageData");
//		if (0 != variableCache.size()) {
//			Iterator iterator1 = variableCache.keySet().iterator();
//			Object obj1 = null;
//			String s3;
//			for (; iterator1.hasNext(); variableCache.put(s3, beanshellengine.get(s3)))
//				s3 = (String) iterator1.next();
//
//		}
	}

	public MessageBean A(Message message) {
		return A(message, ((Node) (null)));
	}

	public MessageBean A(Message message, Node node) {
		if (null == message.getClassName())
			throw new IllegalArgumentException("XmlMessageParser.beanAssignment.message.className.null");
		MessageBean messagebean = messageBean;
		try {

			// Class class1 = Class.forName(message.getClassName());

			URL url = new URL(
					"file:/E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-application\\fib-upp\\src\\main\\resources\\config\\deploy\\npc_cnaps2_recv\\classes\\");

			URL[] urls = { url };
			URLClassLoader classloader = new URLClassLoader(urls);
			Class class1 = classloader.loadClass(message.getClassName());

			if (null != messagebean && !messagebean.getClass().equals(class1) || null == messagebean)
				messagebean = (MessageBean) class1.getDeclaredConstructor().newInstance();
		} catch (Exception exception) {
			exception.printStackTrace(System.err);
			// ExceptionUtil.throwActualException(exception);
		}
//		if (null != getParentBean())
//			messagebean.setParent(getParentBean());
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
			throw new IllegalArgumentException("MessageBean parameter.null");
		if (null == field)
			throw new IllegalArgumentException("field parameter.null");
		switch (field.getFieldType()) {
		case 2002:
		case 2008:
			B(messagebean, message, field, s, node);
			break;

		case 2004:
			E(messagebean, message, field, s, node);
			break;

		case 2011:
			D(messagebean, message, field, s, node);
			break;

		case 2003:
		case 2005:
		case 2006:
		case 2007:
		case 2009:
		case 2010:
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
		Object obj = null;
		if ("dynamic".equalsIgnoreCase(field.getReferenceType())) {
			message1 = A(field, message, messagebean);
		} else {
			Class class1 = getFieldClass(messagebean, field);
			if (null != field.getReference()) {
				message1 = field.getReference();
			} else {
				message1 = new Message();
				message1.setId(s1);
				message1.setClassName(class1.getName());
				message1.setFields(field.getSubFields());
			}
		}
		SortHashMap sorthashmap = message1.getFields();
		String as[] = new String[sorthashmap.size()];
		String s2 = s1;
		if (null != message1.getXpath())
			s2 = (new StringBuilder()).append(s1).append("/").append(message1.getXpath()).toString();
		for (int i = 0; i < as.length; i++)
			as[i] = (new StringBuilder()).append(s2).append("/")
					.append(null != ((Field) sorthashmap.get(i)).getXpath() ? ((Field) sorthashmap.get(i)).getXpath()
							: ((Field) sorthashmap.get(i)).getName())
					.toString();

		ArrayList arraylist = new ArrayList();
		Object obj1 = null;
		for (int j = 0; j < as.length; j++) {
			List list;
			if (node == null)
				list = getXPathNodes(as[j], null);
			else
				list = getXPathNodes(as[j], node);
			if (0 != list.size())
				arraylist.add(list);
		}

		if (0 == arraylist.size())
			return;
		int k = ((List) arraylist.get(0)).size();
		int l = 0;
		ArrayList arraylist1 = new ArrayList(k);
		MessageBean messagebean1 = null;
		for (int i1 = 0; i1 < k; i1++) {
//			if (null != field.getPreRowParseEvent())
//				executeBeanShell(field, "row-pre-parse", field.getPreRowParseEvent(), messagebean, null, arraylist1, k,
//						i1);
			try {
				Class class2 = Class.forName(message1.getClassName());
				messagebean1 = (MessageBean) class2.getDeclaredConstructor().newInstance();
			} catch (Exception exception) {
				exception.printStackTrace(System.err);
				// ExceptionUtil.throwActualException(exception);
			}
			for (int j1 = 0; j1 < arraylist.size(); j1++)
				A(messagebean1, message1, (Field) sorthashmap.get(j1), ((String) (null)),
						(Node) ((List) arraylist.get(j1)).get(i1));

			l++;
			arraylist1.add(messagebean1);
//			if (null != field.getPostRowParseEvent())
//				executeBeanShell(field, "row-post-parse", field.getPostRowParseEvent(), messagebean, messagebean1,
//						arraylist1, k, i1);
		}

		if (0 == l) {
			return;
		} else {
			A(messagebean, field, List.class, arraylist1);
			return;
		}
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
		Object obj = null;
		if ("dynamic".equalsIgnoreCase(field.getReferenceType())) {
			message1 = A(field, message, messagebean);
		} else {
			Class class1 = getFieldClass(messagebean, field);
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
			String s2 = getXPathValue(s1, node, field);
			if (null == s2 || 0 == s2.length())
				return;
//			int i = convertDataType(field.getRowNumField(),
//					ClassUtil.getBeanAttributeValue(messagebean, field.getRowNumField().getName()));
//			if ("dynamic".equalsIgnoreCase(field.getReferenceType())) {
//				ArrayList arraylist = new ArrayList(i);
//				Object obj3 = null;
//				int k = 0;
//				for (int i1 = 0; i1 < i; i1++) {
//					if (null != field.getPreRowParseEvent())
//						executeBeanShell(field, "row-pre-parse", field.getPreRowParseEvent(), messagebean, null,
//								arraylist, i, i1);
//					MessageBean messagebean3 = (MessageBean) ClassUtil.createClassInstance(message1.getClassName());
//					messagebean.setMetadata(message);
//					if (null != message.getMsgCharset()) {
//						byte abyte0[] = null;
//						try {
//							abyte0 = s2.getBytes(message.getMsgCharset());
//						} catch (UnsupportedEncodingException unsupportedencodingexception) {
//							unsupportedencodingexception.printStackTrace();
//							throw new RuntimeException(
//									MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport",
//											new String[] { this.message.getId(), this.message.getMsgCharset() }));
//						}
//						k = A(field, message1, messagebean3, k, abyte0);
//					} else {
//						k = A(field, message1, messagebean3, k, s2.getBytes());
//					}
//					arraylist.add(messagebean3);
//					if (null != field.getPostRowParseEvent())
//						executeBeanShell(field, "row-post-parse", field.getPostRowParseEvent(), messagebean,
//								messagebean3, arraylist, i, i1);
//				}
//
//				ClassUtil.setBeanAttributeValue(messagebean, field.getName(), arraylist, List.class);
//			} else {
//				Object obj1 = null;
//				String s3 = (new StringBuilder()).append("create")
//						.append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
//				int l = 0;
//				for (int j1 = 0; j1 < i; j1++) {
//					if (null != field.getPreRowParseEvent())
//						executeBeanShell(field, "row-pre-parse", field.getPreRowParseEvent(), messagebean, null, null,
//								i, j1);
//					MessageBean messagebean2 = (MessageBean) ClassUtil.invoke(messagebean, s3, null, null);
//					messagebean.setMetadata(message);
//					if (null != message.getMsgCharset()) {
//						byte abyte1[] = null;
//						try {
//							abyte1 = s2.getBytes(message.getMsgCharset());
//						} catch (UnsupportedEncodingException unsupportedencodingexception1) {
//							unsupportedencodingexception1.printStackTrace();
//							throw new RuntimeException(
//									MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport",
//											new String[] { this.message.getId(), this.message.getMsgCharset() }));
//						}
//						l = A(field, message1, messagebean2, l, abyte1);
//					} else {
//						l = A(field, message1, messagebean2, l, s2.getBytes());
//					}
////					if (null != field.getPostRowParseEvent())
////						executeBeanShell(field, "row-post-parse", field.getPostRowParseEvent(), messagebean,
////								messagebean2, null, i, j1);
//				}
//
//			}
		} else {
			Field field1 = (Field) message1.getFieldList().get(0);
			if (field1.getXpath().indexOf("${") == -1) {
				List list = null;
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
				ArrayList arraylist2 = new ArrayList(list.size());
				for (int l1 = 0; l1 < list.size(); l1++) {
//					if (null != field.getPreRowParseEvent())
//						executeBeanShell(field, "row-pre-parse", field.getPreRowParseEvent(), messagebean, null,
//								arraylist2, list.size(), l1);
					j++;
					Node node1 = (Node) list.get(l1);
					MessageBean messagebean4 = A(message2, node1);
					arraylist2.add(messagebean4);
//					if (null != field.getPostRowParseEvent())
//						executeBeanShell(field, "row-post-parse", field.getPostRowParseEvent(), messagebean,
//								messagebean4, arraylist2, list.size(), l1);
				}

				if (0 == j)
					return;
				A(messagebean, field, List.class, arraylist2);
			} else {
				if (null != message1.getXpath())
					s1 = (new StringBuilder()).append(s1).append("/").append(message1.getXpath()).toString();
				MessageBean messagebean1 = null;
				Object obj2 = null;
				Object obj4 = null;
				Object obj5 = null;
				ArrayList arraylist1 = new ArrayList();
				int k1 = 0;
				do {
//					if (null != field.getPreRowParseEvent())
//						executeBeanShell(field, "row-pre-parse", field.getPreRowParseEvent(), messagebean, null,
//								arraylist1, k1, k1);
					Iterator iterator = message1.getFields().values().iterator();
//					messagebean1 = (MessageBean) ClassUtil.createClassInstance(message1.getClassName());
					Field field2;
					String s4;
					for (; iterator.hasNext(); A(messagebean1, message, field2, s4)) {
						field2 = (Field) iterator.next();
						s4 = (new StringBuilder()).append(s1).append("/").append(field2.getXpath()).toString();
						// String s6 = variableCache.get(s4.substring(s4.indexOf("${") + 2,
						// s4.indexOf("}"))).toString();
//						s4 = (new StringBuilder()).append(s4.substring(0, s4.indexOf("${"))).append(s6)
//								.append(s4.substring(s4.indexOf("}") + 1)).toString();
						messagebean.setMetadata(message);
						// messagebean1.setParent(messagebean);
					}

					arraylist1.add(messagebean1);
//					if (null != field.getPostRowParseEvent())
//						executeBeanShell(field, "row-post-parse", field.getPostRowParseEvent(), messagebean,
//								messagebean1, arraylist1, k1, k1);
				} while (null != messagebean1.toString());
				arraylist1.remove(arraylist1.size() - 1);
				A(messagebean, field, List.class, arraylist1);
			}
		}
	}

	private void A(MessageBean messagebean, Field field, Class class1, Object obj) {
		if (null != class1 && null != obj)
			try {
				Method method = messagebean.getClass().getMethod(
						(new StringBuilder()).append("set").append(StrUtil.upperFirst(field.getName())).toString(),
						new Class[] { class1 });
				method.invoke(messagebean, new Object[] { obj });
			} catch (Exception exception) {
				exception.printStackTrace(System.err);
				// ExceptionUtil.throwActualException(exception);
			}
	}

	private int A(Field field, Message message, MessageBean messagebean, int i, byte abyte0[]) {
		AbstractMessageParser b = MessageParserFactory.getMessageParser(message);
		// b.setVariableCache(variableCache);
		b.setMessage(message);
		b.setMessageData(abyte0);
		b.setMessageBean(messagebean);
		// b.setParentBean(messagebean);
		return b.parse(i);
	}

	private MessageBean A(Message message, MessageBean messagebean, byte abyte0[]) {
		AbstractMessageParser b = MessageParserFactory.getMessageParser(message);
		// b.setVariableCache(variableCache);
		b.setMessage(message);
		b.setMessageData(abyte0);
		// b.setParentBean(messagebean);
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
				message1.setClassName(((Class) (obj)).getName());
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
			A(messagebean, field, ((Class) (obj)), messagebean1);
		} else if (s1.indexOf("@") != -1 || s1.indexOf("(") != -1) {
			String s2 = getXPathValue(s1, node, field);
			if (null == s2 || 0 == s2.length())
				return;
			messagebean.setMetadata(message);
			if (null != message.getMsgCharset()) {
				byte abyte0[] = null;
				try {
					abyte0 = s2.getBytes(message.getMsgCharset());
				} catch (UnsupportedEncodingException unsupportedencodingexception) {
					unsupportedencodingexception.printStackTrace();
					throw new RuntimeException("message.encoding.unsupport");
				}
				MessageBean messagebean3 = A(message1, messagebean, abyte0);
				A(messagebean, field, ((Class) (obj)), messagebean3);
			} else {
				MessageBean messagebean2 = A(message1, messagebean, s2.getBytes());
				A(messagebean, field, ((Class) (obj)), messagebean2);
			}
		} else {
			List list = null;
			if (node == null)
				list = getXPathNodes(s1, node);
			else
				list = getXPathNodes(s1, node);
			if (list.size() == 0)
				return;
			if (null != message1.getXpath())
				s1 = (new StringBuilder()).append(s1).append("/").append(message1.getXpath()).toString();
			Message message3 = message1.copy();
			message3.setXpath(s1);
			MessageBean messagebean4 = A(message3, (Node) list.get(0));
			A(messagebean, field, ((Class) (obj)), messagebean4);
		}
	}

	private void A(MessageBean messagebean, Message message, Field field, String s, Node node) {
		String s1 = null;
		if (field.getXpath().indexOf("${") == -1) {
			if (null == s)
				s1 = field.getXpath();
			else
				// s1 = (new
				// StringBuilder()).append(s).append("/").append(field.getXpath()).toString();
				s1 = (new StringBuilder()).append("/").append(field.getXpath()).toString();
		} else {
			s1 = s;
		}
		String s2 = getXPathValue(s1, node, field);
		s2 = new String(A(field, s2.getBytes()));
		if (null == s2 || 0 == s2.length()) {
			return;
		} else {
			String s3 = Constant.getJavaTypeByDataType(field.getDataType());
			A(messagebean, field, convertClass(s3), convertObj(s2, s3));
			return;
		}
	}

	protected Message A(Field field, Message message, MessageBean messagebean) {
		return message;
//		String as[] = field.getReferenceId().split("\\.");
//		Message message1 = message;
//		if (as.length > 1) {
//			MessageBean messagebean1 = messagebean;
//			for (int i = 0; i < as.length - 1; i++)
//				messagebean1 = messagebean1.getParent();
//
//			message1 = messagebean1.getMetadata();
//		}
//		Field field1 = message1.getField(as[as.length - 1]);
//		Object obj = ClassUtil.getBeanValueByPath(messagebean, field.getReferenceId());
//		ValueRange valuerange = (ValueRange) field1.getValueRange().get(obj);
//		if (null == valuerange)
//			valuerange = (ValueRange) field1.getValueRange().get("default-ref");
//		return valuerange.getReference();
	}

//	private String getXPathValue(String xpathExpression, Node node, Field field) {
//		int i = xpathExpression.indexOf("/text()");
//		if (-1 < i) {
//			xpathExpression = xpathExpression.substring(0, i);
//		}
//		Node node1 = null;
//		if (node == null) {
//			node1 = document.selectSingleNode(xpathExpression);
//		} else {
//			node1 = node.selectSingleNode(xpathExpression);
//		}
//		return (null == node1) ? "" : node1.getText();
//	}

	private Class<?> convertClass(String s) {
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
			throw new RuntimeException("XmlMessageParser.fieldType.illegal");
	}

	private Object convertObj(String s, String s1) {
		if (null == s)
			return null;
		if ("String".equals(s1))
			return s;
		if ("byte[]".equals(s1))
			return s.getBytes();
		if ("int".equals(s1)) {
			if ("".equals(s))
				s = "0";
			return Integer.valueOf(Integer.parseInt(s));
		}
		if ("byte".equals(s1))
			if (!"".equals(s))
				return Byte.valueOf(s.getBytes()[0]);
			else
				return Byte.valueOf("0");
		if ("short".equals(s1)) {
			if ("".equals(s))
				s = "0";
			return Short.valueOf(Short.parseShort(s));
		}
		if ("long".equals(s1)) {
			if ("".equals(s))
				s = "0";
			return Long.valueOf(Long.parseLong(s));
		} else {
			throw new RuntimeException("XmlMessageParser.fieldType.illegal");
		}
	}

	private Class<?> getFieldClass(MessageBean messagebean, Field field) {
		String s = null;
		if (null != field.getReference()) {
			s = field.getReference().getClassName();
//		} else if (null != field.getCombineOrTableFieldClassName()) {
//			s = field.getCombineOrTableFieldClassName();
		} else {
			String s1 = messagebean.getClass().getName();
			s = (new StringBuilder()).append(s1).append(StrUtil.upperFirst(field.getName())).toString();
		}
		Class<?> class1 = null;
		try {
			class1 = Class.forName(s);
		} catch (ClassNotFoundException classnotfoundexception) {
			throw new RuntimeException("XmlMessageParser.getFieldClass.class.notFound");
		}
		return class1;
	}

	protected int convertDataType(Field field, Object obj) {
		int i = 0;
		switch (field.getDataType()) {
		case 3001:
			if ("".equals(obj))
				obj = "0";
			i = Integer.parseInt((String) obj);
			break;

		case 3003:
		case 3007:
			i = ((Integer) obj).intValue();
			break;

		case 3004:
			i = ((Byte) obj).intValue();
			break;

		case 3005:
		case 3008:
			i = ((Short) obj).intValue();
			break;

		case 3002:
		case 3006:
		default:
			throw new RuntimeException("parseIntValue.dataType.unsupport");
		}
		return i;
	}

	protected byte[] A(Field field, byte abyte0[]) {
		if (5000 == field.getPaddingDirection())
			return abyte0;
//		String s = field.getExtendAttribute("remove_start");
//		if (null != s) {
//			int i = CodeUtil.byteArrayIndexOf(abyte0, CodeUtil.HextoByte(s), 0);
//			byte abyte1[] = abyte0;
//			if (-1 != i) {
//				abyte0 = new byte[i];
//				System.arraycopy(abyte1, 0, abyte0, 0, i);
//			}
//		}
//		int j = 0;
//		if (5001 == field.getPaddingDirection()) {
//			for (j = 0; j < abyte0.length && abyte0[j] == field.getPadding(); j++)
//				;
//			if (0 == j)
//				return abyte0;
//			if (abyte0.length == j) {
//				return new byte[0];
//			} else {
//				byte abyte2[] = new byte[abyte0.length - j];
//				System.arraycopy(abyte0, j, abyte2, 0, abyte2.length);
//				return abyte2;
//			}
//		}
//		for (j = abyte0.length - 1; j >= 0 && abyte0[j] == field.getPadding(); j--)
//			;
//		if (abyte0.length - 1 == j)
//			return abyte0;
//		if (0 > j) {
//			return new byte[0];
//		} else {
//			byte abyte3[] = new byte[j + 1];
//			System.arraycopy(abyte0, 0, abyte3, 0, abyte3.length);
//			return abyte3;
//		}
		return abyte0;
	}

	protected void executeBeanShell(Field field, String s, String s1, MessageBean messagebean, MessageBean messagebean1,
			List list, int i, int j) {
//		BeanShellEngine beanshellengine = new BeanShellEngine();
//		beanshellengine.set("messageParser", this);
//		beanshellengine.set("message", message);
//		beanshellengine.set("messageBean", messagebean);
//		beanshellengine.set("messageData", messageData);
//		beanshellengine.set("field", field);
//		beanshellengine.set("list", list);
//		beanshellengine.set("rowNum", Integer.valueOf(i));
//		beanshellengine.set("currRow", Integer.valueOf(j));
//		beanshellengine.set("currentBean", messagebean1);
//		if (0 != variableCache.size()) {
//			Iterator<String> iterator = variableCache.keySet().iterator();
//			Object obj = null;
//			String s2;
//			for (; iterator.hasNext(); beanshellengine.set(s2, variableCache.get(s2)))
//				s2 = (String) iterator.next();
//
//		}
//		beanshellengine.eval(s1);
//		if (0 != variableCache.size()) {
//			Iterator<String> iterator1 = variableCache.keySet().iterator();
//			Object obj1 = null;
//			String s3;
//			for (; iterator1.hasNext(); variableCache.put(s3, beanshellengine.get(s3)))
//				s3 = (String) iterator1.next();
//
//		}
	}

	private void setFieldValue(MessageBean messagebean, Field field, Class<?> parameterType, Object obj) {
		if (null != parameterType && null != obj) {
			invoke(messagebean, "set" + StrUtil.upperFirst(field.getName()), new Object[] { obj }, parameterType);
		}
	}

	@SuppressWarnings("unchecked")
	<T> T invoke(Object obj, String methodName, Object[] args, Class<?>... parameterTypes) {
		try {
			Method method = obj.getClass().getMethod(methodName, parameterTypes);
			return (T) method.invoke(obj, args);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final String DEFAULT_NAMESPACE_NAME = "default";

	private String getXPathValue(String xpathExpression, Node node, Field field) {
		Element rootEle = document.getRootElement();
		Map<String, String> nameSpaceMap = new HashMap<String, String>();
		String namespaceURI = rootEle.getNamespaceURI();

		int i = xpathExpression.indexOf("/text()");
		if (-1 < i) {
			xpathExpression = xpathExpression.substring(0, i);
		}

//		if (StrUtil.isNotEmpty(namespaceURI)) {
//			nameSpaceMap.put(DEFAULT_NAMESPACE_NAME, rootEle.getNamespaceURI());
//			DocumentFactory.getInstance().setXPathNamespaceURIs(nameSpaceMap);
//			xpathExpression = xpathExpression.replaceAll("/", "/" + DEFAULT_NAMESPACE_NAME + ":");
//		}

		Node node1 = null;
		if (node == null) {
			// node1 = document.selectSingleNode(xpathExpression);
			node1 = Dom4jUtils.selectSingleNode(document, xpathExpression);
		} else {
			node1 = node.selectSingleNode(xpathExpression);
		}
		return (null == node1) ? "" : node1.getText();
	}

	private List<Node> getXPathNodes(String xpathExpression, Node node) {
		Element rootEle = document.getRootElement();
		Map<String, String> nameSpaceMap = new HashMap<String, String>();
		String namespaceURI = rootEle.getNamespaceURI();

		int i = xpathExpression.indexOf("/text()");
		if (-1 < i) {
			xpathExpression = xpathExpression.substring(0, i);
		}

		if (StrUtil.isNotEmpty(namespaceURI)) {
			nameSpaceMap.put(DEFAULT_NAMESPACE_NAME, rootEle.getNamespaceURI());
			DocumentFactory.getInstance().setXPathNamespaceURIs(nameSpaceMap);
			xpathExpression = xpathExpression.replaceAll("/", "/" + DEFAULT_NAMESPACE_NAME + ":");
		}

		List<Node> nodes = null;
		if (node == null) {
			nodes = document.selectNodes(xpathExpression);
		} else {
			nodes = node.selectNodes(xpathExpression);
		}
		return (null == nodes) ? Collections.emptyList() : nodes;
	}
}
