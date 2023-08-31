package com.fib.msgconverter.message.packer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fib.msgconverter.message.bean.MessageBean;
import com.fib.msgconverter.message.metadata.Field;
import com.fib.msgconverter.message.metadata.Message;
import com.fib.msgconverter.message.metadata.ValueRange;
import com.fib.msgconverter.util.ConstantMB;
import com.giantstone.common.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.script.BeanShellEngine;
import com.giantstone.common.util.ByteBuffer;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.common.util.StringUtil;

public class XmlMessagePacker extends AbstractMessagePacker {

	private BeanShellEngine A;

	public XmlMessagePacker() {
		A = null;
	}

	public byte[] pack() {
		if (null == message)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("paramter.null",
					new String[] { "originalMessage" }));
		if (null == messageBean)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("paramter.null",
					new String[] { "messageBean" }));
		if (0 == variableCache.size())
			loadVariable();
		if (null != getParentBean())
			messageBean.setParent(getParentBean());
		messageBean.setMetadata(message);
		Iterator<Field> iterator = message.getFields().values().iterator();
		Object obj = null;
		do {
			if (!iterator.hasNext())
				break;
			Field field = iterator.next();
			if (2005 == field.getFieldType())
				A(field, new Object());
		} while (true);
		byte abyte0[] = message.getTemplate().getBytes();
		if (null != message.getPrePackEvent())
			A("pre-pack", message.getPostPackEvent(), ((ByteBuffer) (null)));
		byte abyte1[] = A(messageBean, abyte0);
		if (message.getPostPackEvent() != null) {
			ByteBuffer bytebuffer = new ByteBuffer();
			bytebuffer.append(abyte1);
			A("post-pack", message.getPostPackEvent(), bytebuffer);
			abyte1 = bytebuffer.toBytes();
		}
		if (null != message.getMsgCharset())
			try {
				abyte1 = (new String(abyte1)).getBytes(message.getMsgCharset());
			} catch (UnsupportedEncodingException unsupportedencodingexception) {
				unsupportedencodingexception.printStackTrace();
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"message.encoding.unsupport", new String[] { message.getId(), message.getMsgCharset() }));
			}
		return abyte1;
	}

	protected void A(String s, String s1, ByteBuffer bytebuffer) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		beanshellengine.set("messagePacker", this);
		beanshellengine.set("message", message);
		beanshellengine.set("messageBean", messageBean);
		beanshellengine.set("messageBuf", bytebuffer);
		if (0 != variableCache.size()) {
			Iterator iterator = variableCache.keySet().iterator();
			Object obj = null;
			String s2;
			for (; iterator.hasNext(); beanshellengine.set(s2, variableCache.get(s2)))
				s2 = (String) iterator.next();

		}
		beanshellengine.eval(s1);
		bytebuffer = (ByteBuffer) beanshellengine.get("messageBuf");
		if (0 != variableCache.size()) {
			Iterator iterator1 = variableCache.keySet().iterator();
			Object obj1 = null;
			String s3;
			for (; iterator1.hasNext(); variableCache.put(s3, beanshellengine.get(s3)))
				s3 = (String) iterator1.next();

		}
	}

	public byte[] A(MessageBean messagebean, byte abyte0[]) {
		ByteBuffer bytebuffer = new ByteBuffer(abyte0.length);
		boolean flag = false;
		Object obj = null;
		Object obj1 = null;
		boolean flag1 = false;
		boolean flag2 = false;
		Object obj3 = null;
		int i1 = 0;
		int j1 = 0;
		Object obj4 = null;
		boolean flag3 = false;
		boolean flag4 = false;
		Object obj5 = null;
		int i2 = 0;
		do {
			if (i2 >= abyte0.length)
				break;
			byte byte0 = abyte0[i2];
			if (byte0 == 36 && i2 < abyte0.length - 1 && abyte0[i2 + 1] == 123) {
				i2++;
				int i = ++i2;
				int k = -1;
				do {
					if (i2 >= abyte0.length)
						break;
					byte0 = abyte0[i2];
					if (byte0 == 125) {
						k = i2++;
						break;
					}
					i2++;
				} while (true);
				if (i2 <= abyte0.length && -1 != k) {
					String s = new String(abyte0, i, k - i);
					if (null == s || 0 == s.length())
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("XmlMessagePacker.variableName.null"));
					Object obj2 = variableCache.get(s);
					if (null == obj2)
						obj2 = A(messagebean, s);
					if (obj2 != null)
						bytebuffer.append(A(messagebean, obj2, s).getBytes());
				} else {
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance().getString("XmlMessagePacker.variable.wrong"));
				}
			} else if (byte0 == 60 && i2 < abyte0.length - 1 && abyte0[i2 + 1] == 37) {
				i2++;
				int k1 = ++i2;
				int l1 = -1;
				do {
					if (i2 >= abyte0.length)
						break;
					byte0 = abyte0[i2];
					if (byte0 == 62 && abyte0[i2 - 1] == 37) {
						l1 = i2 - 1;
						i2++;
						break;
					}
					i2++;
				} while (true);
				if (i2 <= abyte0.length && -1 != l1) {
					String s2 = new String(abyte0, k1, l1 - k1);
					if (null == s2 || 0 == s2.length())
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("XmlMessagePacker.merge.script.null"));
					Object obj6 = B(messagebean, s2);
					if (obj6 != null) {
						if (obj6 instanceof String)
							bytebuffer.append(((String) obj6).getBytes());
						else if (obj6 instanceof byte[])
							bytebuffer.append((byte[]) (byte[]) obj6);
						else
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
									"XmlMessagePacker.merge.script.exec.resultType.illegal",
									new String[] { s2, obj6.getClass().getName() }));
						obj6 = null;
					}
				} else {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("XmlMessagePacker.merge.script.form.wrong"));
				}
			} else if (byte0 == 60 && i2 < abyte0.length - 1 && abyte0[i2 + 1] == 35) {
				i1++;
				i2 += 2;
				int j = i2++;
				int l = -1;
				do {
					if (i2 >= abyte0.length)
						break;
					byte0 = abyte0[i2];
					if (byte0 == 35) {
						l = i2++;
						break;
					}
					i2++;
				} while (true);
				String s1 = new String(abyte0, j, l - j);
				String as[] = s1.split(" ");
				s1 = as[1];
				j = i2;
				do {
					if (i2 >= abyte0.length)
						break;
					byte0 = abyte0[i2];
					if (byte0 == 35 && i2 < abyte0.length - 1 && abyte0[i2 + 1] == 62) {
						l = i2;
						i2 += 2;
						j1++;
					} else if (byte0 == 60 && i2 < abyte0.length - 1 && abyte0[i2 + 1] == 35) {
						i2 += 2;
						i1++;
					}
					if (i1 <= j1)
						break;
					i2++;
				} while (true);
				String s3 = new String(abyte0, j, l - j);
				ArrayList arraylist = (ArrayList) A(messagebean, s1);
				Field field = this.message.getField(s1);
				Object obj7 = null;
				Object obj8 = null;
				int j2 = 0;
				while (j2 < arraylist.size()) {
					if (null != field.getPreRowPackEvent())
						A(field, "row-pre-pack", field.getPreRowPackEvent(), arraylist.size(), j2);
					Message message = field.getReference();
					if (null == message) {
						message = new Message();
						message.setId((new StringBuilder()).append(this.message.getId()).append("-")
								.append(field.getName()).toString());
						message.setShortText(field.getShortText());
						message.setFields(field.getSubFields());
						message.setType(ConstantMB.MessageType.XML);
						message.setTemplate(s3);
					}
					if (2011 == field.getFieldType()) {
						bytebuffer.append(A(message, (MessageBean) arraylist.get(j2)));
					} else {
						AbstractMessagePacker abstractmessagepacker = MessagePackerFactory.getMessagePacker(message);
						abstractmessagepacker.setVariableCache(variableCache);
						abstractmessagepacker.setMessage(message);
						abstractmessagepacker.setMessageBean((MessageBean) arraylist.get(j2));
						abstractmessagepacker.setParentBean(messagebean);
						bytebuffer.append(abstractmessagepacker.pack());
					}
					if (null != field.getPostRowPackEvent())
						A(field, "row-post-pack", field.getPostRowPackEvent(), arraylist.size(), j2);
					j2++;
				}
			} else {
				bytebuffer.append(byte0);
				i2++;
			}
		} while (true);
		return bytebuffer.toBytes();
	}

	private Object A(MessageBean messagebean, String s) {
		return ClassUtil.getBeanValueByPath(messagebean, s);
	}

	private String A(MessageBean messagebean, Object obj, String s) {
		Field field1 = null;
		Message message;
		AbstractMessagePacker abstractmessagepacker;
		String s1;
		if (obj instanceof Integer)
			return String.valueOf(obj);
		if (obj instanceof Long)
			return String.valueOf(obj);
		if (obj instanceof Short)
			return String.valueOf(obj);
		if (obj instanceof Byte)
			return String.valueOf(obj);
		if (obj instanceof String) {
			Field field = this.message.getField(s);
			if (null != field && null != field.getExtendAttribute("replace_all")) {
				byte abyte0[] = ((String) obj).getBytes();
				String as[] = field.getExtendAttribute("replace_all").split("\\|");
				Object obj1 = null;
				for (int i = 0; i < as.length; i++) {
					String as1[] = as[i].split("=");
					if (2 != as1.length)
						throw new RuntimeException((new StringBuilder()).append("field[@name=").append(field.getName())
								.append("]'s extended-attributes[@key=").append("replace_all")
								.append("] pattern error. pattern : hex=hex;hex=hex!").toString());
					abyte0 = CodeUtil.replaceAll(abyte0, CodeUtil.HextoByte(as1[0].trim()),
							CodeUtil.HextoByte(as1[1].trim()), 0, abyte0.length);
				}

				obj = new String(abyte0);
			}
			return StringUtil.formatXmlValue((String) obj);
		}
		if (obj instanceof MessageBean)
			// break MISSING_BLOCK_LABEL_557;
			field1 = this.message.getField(s);
		message = null;
		if ("dynamic".equalsIgnoreCase(field1.getReferenceType())) {
			message = A(field1, this.message, messagebean);
		} else {
			message = field1.getReference();
			if (null == message) {
				message = new Message();
				message.setId((new StringBuilder()).append(this.message.getId()).append("-").append(field1.getName())
						.toString());
				message.setShortText(field1.getShortText());
				message.setFields(field1.getSubFields());
			}
		}
		abstractmessagepacker = MessagePackerFactory.getMessagePacker(message);
		abstractmessagepacker.setVariableCache(variableCache);
		abstractmessagepacker.setMessage(message);
		abstractmessagepacker.setMessageBean((MessageBean) obj);
		abstractmessagepacker.setParentBean(messagebean);
		s1 = field1.getDataCharset();
		if (null == s1)
			s1 = this.message.getMsgCharset();
		if (null == s1)
			s1 = getDefaultCharset();
		if (null == s1)
			if (ConstantMB.MessageType.XML != message.getType())
				return StringUtil.formatXmlValue(new String(abstractmessagepacker.pack()));
			else
				return new String(abstractmessagepacker.pack());
		if (ConstantMB.MessageType.XML != message.getType())
			try {
				return StringUtil.formatXmlValue(new String(abstractmessagepacker.pack(), field1.getDataCharset()));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			return new String(abstractmessagepacker.pack(), s1);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Exception exception;
//		exception;
//		exception.printStackTrace();
//		ExceptionUtil.throwActualException(exception);
//		return null;
		if (obj instanceof List) {
			Field field2 = this.message.getField(s);
			ByteBuffer bytebuffer = new ByteBuffer(512);
			Message message1 = null;
			if ("dynamic".equalsIgnoreCase(field2.getReferenceType())) {
				message1 = A(field2, this.message, messagebean);
			} else {
				message1 = field2.getReference();
				if (null == message1) {
					message1 = new Message();
					message1.setId((new StringBuilder()).append(this.message.getId()).append("-")
							.append(field2.getName()).toString());
					message1.setShortText(field2.getShortText());
					message1.setFields(field2.getSubFields());
				}
			}
			Object obj2 = null;
			for (int j = 0; j < ((List) obj).size(); j++) {
				if (null != field2.getPreRowPackEvent())
					A(field2, "row-pre-pack", field2.getPreRowPackEvent(), ((List) obj).size(), j);
				if (null != field2.getRowXpath()) {
					bytebuffer.append("<".getBytes());
					bytebuffer.append(field2.getRowXpath().getBytes());
					bytebuffer.append(">".getBytes());
				}
				AbstractMessagePacker abstractmessagepacker1 = MessagePackerFactory.getMessagePacker(message1);
				abstractmessagepacker1.setVariableCache(variableCache);
				abstractmessagepacker1.setMessage(message1);
				abstractmessagepacker1.setMessageBean((MessageBean) ((List) obj).get(j));
				abstractmessagepacker1.setParentBean(messagebean);
				String s2 = field2.getDataCharset();
				if (null == s2)
					s2 = this.message.getMsgCharset();
				if (null == s2)
					s2 = getDefaultCharset();
				if (null == s2) {
					if (ConstantMB.MessageType.XML != message1.getType())
						bytebuffer.append(
								StringUtil.formatXmlValue(new String(abstractmessagepacker1.pack())).getBytes());
					else
						bytebuffer.append(abstractmessagepacker1.pack());
				} else {
					try {
						if (ConstantMB.MessageType.XML != message1.getType())
							bytebuffer.append(StringUtil.formatXmlValue(new String(abstractmessagepacker1.pack(), s2))
									.getBytes());
						else
							bytebuffer.append(abstractmessagepacker1.pack());
					} catch (Exception exception1) {
						exception1.printStackTrace();
						ExceptionUtil.throwActualException(exception1);
					}
				}
				if (null != field2.getRowXpath()) {
					bytebuffer.append("</".getBytes());
					bytebuffer.append(field2.getRowXpath().getBytes());
					bytebuffer.append(">".getBytes());
				}
				if (null != field2.getPostRowPackEvent())
					A(field2, "row-post-pack", field2.getPostRowPackEvent(), ((List) obj).size(), j);
			}

			return bytebuffer.toString();
		} else {
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"XmlMessagePacker.data2String.variable.valueType.illegal",
					new String[] { s, obj.getClass().getName() }));
		}
	}

	protected void A(Field field, String s, String s1, int i, int j) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		beanshellengine.set("messagePacker", this);
		beanshellengine.set("message", message);
		beanshellengine.set("messageBean", messageBean);
		beanshellengine.set("messageBuf", buf);
		beanshellengine.set("field", field);
		beanshellengine.set("rowNum", Integer.valueOf(i));
		beanshellengine.set("currRow", Integer.valueOf(j));
		if (0 != variableCache.size()) {
			Iterator iterator = variableCache.keySet().iterator();
			Object obj = null;
			String s2;
			for (; iterator.hasNext(); beanshellengine.set(s2, variableCache.get(s2)))
				s2 = (String) iterator.next();

		}
		beanshellengine.eval(s1);
		if (0 != variableCache.size()) {
			Iterator iterator1 = variableCache.keySet().iterator();
			Object obj1 = null;
			String s3;
			for (; iterator1.hasNext(); variableCache.put(s3, beanshellengine.get(s3)))
				s3 = (String) iterator1.next();

		}
	}

	protected Object B(MessageBean messagebean, String s) {
		BeanShellEngine beanshellengine = A();
		beanshellengine.set("bean", messagebean);
		return beanshellengine.eval(s);
	}

	private BeanShellEngine A() {
		if (null == A)
			A = new BeanShellEngine();
		return A;
	}

	protected void A(Field field, Object obj) {
		List list = (List) ClassUtil.getBeanAttributeValue(messageBean, field.getTable().getName());
		Object obj1 = null;
		switch (field.getDataType()) {
		case 3001:
			obj1 = Integer.toString(list.size());
			break;

		case 3004:
			obj1 = Byte.valueOf((byte) list.size());
			break;

		case 3003:
		case 3007:
			obj1 = Integer.valueOf(list.size());
			break;

		case 3005:
		case 3008:
			obj1 = Short.valueOf((short) list.size());
			break;

		case 3002:
		case 3006:
		default:
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("tableRowField.dataType.unsupport", new String[] { field.getName(),
							(new StringBuilder()).append("").append(field.getDataType()).toString() }));
		}
		if (field.isEditable())
			switch (field.getDataType()) {
			case 3004:
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), obj1, Byte.TYPE);
				break;

			case 3003:
			case 3007:
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), obj1, Integer.TYPE);
				break;

			case 3005:
			case 3008:
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), obj1, Short.TYPE);
				break;

			case 3009:
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), obj1, Long.TYPE);
				break;

			case 3006:
			default:
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), obj1);
				break;
			}
	}

	protected byte[] A(Message message, MessageBean messagebean) {
		int i = 0;
		byte abyte0[] = message.getTemplate().getBytes();
		boolean flag = false;
		ByteBuffer bytebuffer = new ByteBuffer(abyte0.length);
		ByteBuffer bytebuffer1 = new ByteBuffer(abyte0.length);
		byte byte2 = -1;
		byte byte3 = -1;
		Object obj = null;
		Object obj1 = null;
		label0: do {
			if (i >= abyte0.length)
				break;
			byte byte0 = abyte0[i];
			bytebuffer1.clear();
			if (byte0 == 60)
				do {
					if (i >= abyte0.length)
						continue label0;
					byte byte1 = abyte0[i];
					if (byte1 == 36 && i < abyte0.length - 1 && abyte0[i + 1] == 123) {
						i++;
						int j = ++i;
						int k = -1;
						do {
							if (i >= abyte0.length)
								break;
							byte1 = abyte0[i];
							if (byte1 == 125) {
								k = i++;
								break;
							}
							i++;
						} while (true);
						if (i <= abyte0.length && -1 != k) {
							String s = new String(abyte0, j, k - j);
							if (null == s || 0 == s.length())
								throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
										.getString("XmlMessagePacker.variableName.null"));
							Object obj2 = variableCache.get(s);
							if (null == obj2)
								obj2 = A(messagebean, s);
							if (obj2 != null) {
								if (!(obj2 instanceof String))
									throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
											.getString("XmlMessagePacker.packerVarTable.varTable.subField.mustBeSame"));
								bytebuffer1.append(A(messagebean, obj2, s).getBytes());
								do {
									if (i >= abyte0.length)
										break;
									byte1 = abyte0[i];
									bytebuffer1.append(byte1);
									i++;
								} while (byte1 != 62);
								bytebuffer.append(bytebuffer1.toBytes());
							} else {
								do {
									if (i >= abyte0.length)
										break;
									i++;
								} while (abyte0[i] != 62);
							}
						} else {
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
									.getString("XmlMessagePacker.variable.wrong"));
						}
						continue label0;
					}
					bytebuffer1.append(byte1);
					i++;
				} while (true);
			i++;
		} while (true);
		bytebuffer.append(bytebuffer1.toBytes());
		return bytebuffer.toBytes();
	}

	protected Message A(Field field, Message message, MessageBean messagebean) {
		String as[] = field.getReferenceId().split("\\.");
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
}
