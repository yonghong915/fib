package com.fib.upp.message.parser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fib.commons.exception.CommonException;
import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.metadata.ConstantMB;
import com.fib.upp.message.metadata.Field;
import com.fib.upp.message.metadata.Message;
import com.fib.upp.message.metadata.ValueRange;
import com.fib.upp.util.BeanShellEngine;
import com.fib.upp.util.ByteBuffer;
import com.fib.upp.util.ClassUtil;
import com.fib.upp.util.CodeUtil;
import com.fib.upp.util.ExceptionUtil;
import com.fib.upp.util.StringUtil;

import cn.hutool.core.lang.Assert;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public class DefaultMessageParser extends AbstractMessageParser {

	public static final boolean C = false;
	protected static final Map<String, String> mbMap;
	public static final boolean G = false;
	protected byte[] F;
	protected Set<String> fieldNameSet;
	protected int E;
	protected boolean flag;

	public DefaultMessageParser() {
		F = null;
		fieldNameSet = null;
		E = 1;
		flag = false;
	}

	protected void A() {
		if (!G)
			messageBean = null;
		F = null;
		fieldNameSet = null;
	}

	public MessageBean parse() {
		Assert.notNull(message, () -> new CommonException("parameter.null.message"));

		Assert.notNull(messageData, () -> new CommonException("parameter.null.messageData"));

		for (Iterator<String> iterator = mbMap.keySet().iterator(); iterator.hasNext();) {
			String s = iterator.next();
			messageData = CodeUtil.replaceAll(messageData, CodeUtil.hextoByte(s), CodeUtil.hextoByte(mbMap.get(s)), 0, messageData.length);
		}

		A();
		if (0 == variableCache.size())
			loadVariable();
		if (null == messageBean)
			messageBean = (MessageBean) ClassUtil.createClassInstance(message.getClassName());
		messageBean.setMetadata(message);
		if (null != getParentBean())
			messageBean.setParent(getParentBean());
		flag = true;
		if (message.getPreParseEvent() != null)
			exeShell("pre-parse", message.getPreParseEvent());
		parse(0);
		if (message.getPostParseEvent() != null)
			exeShell("post-parse", message.getPostParseEvent());
		return messageBean;
	}

	private static final String MESSAGE_PARSER_KEY = "messageParser";
	private static final String MESSAGE_KEY = "message";
	private static final String MESSAGE_BEAN_KEY = "messageBean";
	private static final String MESSAGE_DATA_KEY = "messageData";
	private static final String FIELD_KEY = "field";

	protected void exeShell(String eventType, String script) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		beanshellengine.set(MESSAGE_PARSER_KEY, this);
		beanshellengine.set(MESSAGE_KEY, message);
		beanshellengine.set(MESSAGE_BEAN_KEY, messageBean);
		beanshellengine.set(MESSAGE_DATA_KEY, messageData);
		if (0 != variableCache.size()) {
			Iterator<String> iterator = variableCache.keySet().iterator();
			String s2;
			for (; iterator.hasNext(); beanshellengine.set(s2, variableCache.get(s2)))
				s2 = iterator.next();

		}
		beanshellengine.eval(script);
		messageData = (byte[]) beanshellengine.get(MESSAGE_DATA_KEY);
		if (0 != variableCache.size()) {
			Iterator<String> iterator1 = variableCache.keySet().iterator();
			String s3;
			for (; iterator1.hasNext(); variableCache.put(s3, beanshellengine.get(s3)))
				s3 = iterator1.next();
		}
	}

	protected void exeShell(Field field, String s, String s1) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		beanshellengine.set(MESSAGE_PARSER_KEY, this);
		beanshellengine.set(MESSAGE_KEY, message);
		beanshellengine.set(MESSAGE_BEAN_KEY, messageBean);
		beanshellengine.set(MESSAGE_DATA_KEY, messageData);
		beanshellengine.set(FIELD_KEY, field);
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

	protected void exeShell(Field field, String s, String s1, MessageBean messagebean, List<?> list, int i, int j) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		beanshellengine.set(MESSAGE_PARSER_KEY, this);
		beanshellengine.set(MESSAGE_KEY, message);
		beanshellengine.set(MESSAGE_BEAN_KEY, messageBean);
		beanshellengine.set(MESSAGE_DATA_KEY, messageData);
		beanshellengine.set(FIELD_KEY, field);
		beanshellengine.set("list", list);
		beanshellengine.set("rowNum", Integer.valueOf(i));
		beanshellengine.set("currRow", Integer.valueOf(j));
		beanshellengine.set("currentBean", messagebean);
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

	@Override
	protected int parse(int i) {
		if (!flag && null != getParentBean())
			messageBean.setParent(getParentBean());
		Iterator<Field> iterator = message.getFields().values().iterator();
		while (iterator.hasNext()) {
			Field field = iterator.next();
			i = B(field, i);
		}
		return i;
	}

	protected int A(Field field, MessageBean messagebean, Message message, int i, byte[] abyte0) {
		Message message1 = this.message;
		Field field1 = null;
		if (null == field.getRowNumField()) {
			String[] as = field.getRowNumFieldName().split("\\.");
			int k = 0;
			do {
				if (k >= as.length)
					break;
				field1 = message1.getField(as[k].trim());
				if (++k < as.length) {
					if (null != field1.getReference())
						message1 = field1.getReference();
					else if (0 != field1.getSubFields().size()) {
						message1 = new Message();
						message1.setFields(field1.getSubFields());
					}
				}
			} while (true);
		} else {
			field1 = field.getRowNumField();
		}
		int j = parseIntValue(field1, ClassUtil.getBeanValueByPath(messagebean, field.getRowNumFieldName()));
		List<MessageBean> arraylist = new ArrayList<>(j);
		for (int l = 0; l < j; l++) {
			if (null != field.getPreRowParseEvent())
				exeShell(field, ConstantMB.EventType.ROW_PRE_PARSE.getName(), field.getPreRowParseEvent(), null, arraylist, j, l);
			MessageBean messagebean1 = (MessageBean) ClassUtil.createClassInstance(message.getClassName());
			i = parseMessage(message, messagebean1, i, abyte0);
			arraylist.add(messagebean1);
			if (null != field.getPostRowParseEvent())
				exeShell(field, ConstantMB.EventType.ROW_POST_PARSE.getName(), field.getPostRowParseEvent(), messagebean1, arraylist, j, l);
		}

		ClassUtil.setBeanAttributeValue(messagebean, field.getName(), arraylist, List.class);
		return i;
	}

	protected int B(Field field, MessageBean messagebean, Message message, int i, byte[] abyte0) {
		MessageBean messagebean1 = (MessageBean) ClassUtil.createClassInstance(message.getClassName());
		i = parseMessage(message, messagebean1, i, abyte0);
		String s = (new StringBuilder()).append("set").append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
		try {
			ClassUtil.invoke(messagebean, s, new Class[] { MessageBean.class }, new Object[] { messagebean1 });
		} catch (Exception exception) {
			exception.printStackTrace();
			ExceptionUtil.throwActualException(exception);
		}
		return i;
	}

	protected Message getRefMessage(Field field, Message message, MessageBean messagebean) {
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
		ValueRange valuerange = field1.getValueRange().get(obj);
		if (null == valuerange)
			valuerange = field1.getValueRange().get("default-ref");
		return valuerange.getReference();
	}

	protected Message exeShell(Field field, MessageBean messagebean) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		BeanShellEngine beanshellengine1 = new BeanShellEngine();
		String s = null;
		beanshellengine1.set(MESSAGE_BEAN_KEY, messagebean);
		if (0 != variableCache.size()) {
			Iterator<String> iterator = variableCache.keySet().iterator();
			String s1;
			for (; iterator.hasNext(); beanshellengine.set(s1, variableCache.get(s1)))
				s1 = iterator.next();

		}
		Object obj = beanshellengine1.eval(field.getExpression());
		if (0 != variableCache.size()) {
			Iterator<String> iterator1 = variableCache.keySet().iterator();
			String s2;
			for (; iterator1.hasNext(); variableCache.put(s2, beanshellengine.get(s2)))
				s2 = iterator1.next();

		}
		if (obj != null) {
			if (obj instanceof String str) {
				s = str;
			} else {
				s = obj.toString();
			}
		}
		ValueRange valuerange = field.getValueRange().get(s);
		if (null == valuerange)
			valuerange = field.getValueRange().get("default-ref");
		return valuerange.getReference();
	}

	protected int B(Field field, int i) {
		ConstantMB.FieldType fieldType = ConstantMB.FieldType.getFieldTypeByCode(field.getFieldType());

		if (fieldNameSet != null && fieldNameSet.contains(field.getName()))
			return i;
		if (field.getIso8583No() > 0 && F != null) {
			if (8 == F.length && field.getIso8583No() > 64)
				return i;
			E++;
			if (0 == CodeUtil.getBit(F, field.getIso8583No()))
				return i;
		}
		if (field.getPreParseEvent() != null)
			exeShell(field, "pre-parse", field.getPreParseEvent());
		if ("dynamic".equalsIgnoreCase(field.getReferenceType())) {
			Message message = getRefMessage(field, this.message, messageBean);
			if (ConstantMB.FieldType.TABLE.getCode() == field.getFieldType())
				i = A(field, messageBean, message, i, messageData);
			else
				i = B(field, messageBean, message, i, messageData);
		} else if ("expression".equalsIgnoreCase(field.getReferenceType())) {
			Message message1 = exeShell(field, messageBean);
			if (ConstantMB.FieldType.TABLE.getCode() == field.getFieldType())
				i = A(field, messageBean, message1, i, messageData);
			else
				i = B(field, messageBean, message1, i, messageData);
		} else {
			switch (fieldType) {
			case FIXED_FIELD:
				i = parseFixedFieldValue(field, i);
				break;

			case BITMAP_FIELD:
				Object[] aobj = new Object[1];
				i = parseFixedFieldValue(field, i, aobj);
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), aobj[0]);
				if (F == null) {
					if (aobj[0] instanceof String str)
						F = CodeUtil.hextoByte(str.getBytes());
					else
						F = (byte[]) aobj[0];
					break;
				}
				ByteBuffer bytebuffer = new ByteBuffer(F.length * 2);
				bytebuffer.append(F);
				if (aobj[0] instanceof String str)
					bytebuffer.append(CodeUtil.hextoByte(str.getBytes()));
				else
					bytebuffer.append((byte[]) aobj[0]);
				F = bytebuffer.toBytes();
				break;

			case LENGTH_FIELD:
				i = parseLengthFieldValue(field, i);
				break;

			case TABLE_ROW_NUM_FIELD:
				i = parseTableRowNumFieldValue(field, i);
				break;

			case VAR_FIELD:
				i = parseVarFieldValue(field, i);
				break;

			case COMBINE_FIELD, REFERENCE_FIELD:
				i = setMessageBeanAttrVal(field, i);
				break;

			case VAR_COMBINE_FIELD:
				i = parseVarCombineField(field, i);
				break;

			case VAR_REFERENCE_FIELD:
				i = parseVarReferencFieldValue(field, i);
				break;

			case VAR_TABLE:
				i = parseVarTableValue(field, i);
				break;

			case TABLE:
				i = parseTableValue(field, i);
				break;

			case MAC_FIELD:
				i = parseMacFieldValue(field, i);
				break;

			default:
				throw new CommonException("field.fieldType.notExist");
			}
		}
		if (field.getPostParseEvent() != null)
			exeShell(field, ConstantMB.EventType.POST_PARSE.getName(), field.getPostParseEvent());
		return i;
	}

	protected int parseMacFieldValue(Field field, int i) {
		Object[] aobj = new Object[1];
		byte[] abyte0 = new byte[field.getLength()];
		System.arraycopy(messageData, i, abyte0, 0, abyte0.length);
		aobj[0] = abyte0;
		ClassUtil.setBeanAttributeValue(messageBean, field.getName(), aobj[0]);
		i += field.getLength();
		return i;
	}

	protected int parseVarTableValue(Field field, int i) {
		int j = 0;
		int k = 0;
		if (null != field.getTabPrefix())
			i += field.getTabPrefix().length;
		if (null != field.getRefLengthFieldName()) {
			String[] as = field.getRefLengthFieldName().split("\\.");
			Message message = this.message;
			if (as.length > 1) {
				MessageBean messagebean = messageBean;
				for (int i1 = 0; i1 < as.length - 1; i1++)
					messagebean = messagebean.getParent();

				message = messagebean.getMetadata();
			}
			Field field1 = message.getField(as[as.length - 1]);
			Object obj1 = ClassUtil.getBeanValueByPath(messageBean, field.getRefLengthFieldName());
			if (null == obj1)
				throw new CommonException(
						(new StringBuilder()).append("field[@name='").append(field1.getName()).append("'] ").append("null").toString());
			k = parseIntValue(field1, obj1);
			k -= field.getRefLengthFieldOffset();
		} else if (null != field.getTabSuffix()) {
			int l = CodeUtil.byteArrayIndexOf(messageData, field.getTabSuffix(), i);
			if (l == -1)
				k = messageData.length - i;
			else
				k = l - i;
		} else {
			if (field.isLastRowSuffix())
				return C(field, i);
			k = messageData.length - i;
		}
		byte[] abyte0 = new byte[k];
		System.arraycopy(messageData, i, abyte0, 0, k);
		Message message1 = field.getReference();
		if (null == message1) {
			message1 = new Message();
			message1.setId((new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
			message1.setShortText(field.getShortText());
			message1.setFields(field.getSubFields());
		}
		String s = (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
		int j1 = 0;
		while (j1 < abyte0.length) {
			if (null != field.getPrefix() && (j == 0 && field.isFirRowPrefix() || j > 0))
				j1 += field.getPrefix().length;
			if (null != field.getPreRowParseEvent())
				exeShell(field, ConstantMB.EventType.ROW_PRE_PARSE.getName(), field.getPreRowParseEvent(), null, null, j, j);
			MessageBean messagebean1 = (MessageBean) ClassUtil.invoke(messageBean, s, null, null);
			if (field.isRowCut()) {
				int k1 = CodeUtil.byteArrayIndexOf(abyte0, field.getSuffix(), j1);
				if (-1 == k1)
					k1 = abyte0.length;
				byte[] abyte1 = new byte[k1 - j1];
				System.arraycopy(abyte0, j1, abyte1, 0, abyte1.length);
				parseMessage(message1, messagebean1, 0, abyte1);
				j1 = k1;
			} else {
				j1 = parseMessage(message1, messagebean1, j1, abyte0);
			}
			if (null != field.getSuffix())
				j1 += field.getSuffix().length;
			if (null != field.getPostRowParseEvent())
				exeShell(field, ConstantMB.EventType.ROW_POST_PARSE.getName(), field.getPostRowParseEvent(), messagebean1, null, j, j);
			j++;
		}
		i += abyte0.length;
		if (null != field.getTabSuffix())
			i += field.getTabSuffix().length;
		return i;
	}

	protected int C(Field field, int i) {
		int j = 0;
		Message message = field.getReference();
		if (null == message) {
			message = new Message();
			message.setId((new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
			message.setShortText(field.getShortText());
			message.setFields(field.getSubFields());
		}
		String s = (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
		do {
			if (null != field.getPrefix() && (j == 0 && field.isFirRowPrefix() || j > 0))
				i += field.getPrefix().length;
			int k = CodeUtil.byteArrayIndexOf(messageData, field.getSuffix(), i);
			if (k == -1) {
				if (null != field.getPrefix() && (j == 0 && field.isFirRowPrefix() || j > 0))
					i -= field.getPrefix().length;
				break;
			}
			int l = k - i;
			if (0 == l)
				break;
			byte[] abyte0 = new byte[l];
			System.arraycopy(messageData, i, abyte0, 0, abyte0.length);
			if (null != field.getPreRowParseEvent())
				exeShell(field, "row-pre-parse", field.getPreRowParseEvent(), null, null, j, j);
			MessageBean messagebean = (MessageBean) ClassUtil.invoke(messageBean, s, null, null);
			parseMessage(message, messagebean, 0, abyte0);
			i += l;
			if (null != field.getSuffix())
				i += field.getSuffix().length;
			if (null != field.getPostRowParseEvent())
				exeShell(field, "row-post-parse", field.getPostRowParseEvent(), messagebean, null, j, j);
			j++;
		} while (true);
		return i;
	}

	protected int parseTableValue(Field field, int i) {
		int j = 0;
		if (null != field.getRowNumField()) {
			j = parseIntValue(field.getRowNumField(), ClassUtil.getBeanAttributeValue(messageBean, field.getRowNumField().getName()));
		} else {
			String[] as = field.getRowNumFieldName().split("\\.");
			Field field1 = getParentBean().getMetadata().getField(as[1]);
			j = parseIntValue(field1, ClassUtil.getBeanValueByPath(messageBean, field.getRowNumFieldName()));
		}
		Message message = field.getReference();
		if (null == message) {
			message = new Message();
			message.setId((new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
			message.setShortText(field.getShortText());
			message.setFields(field.getSubFields());
		}
		String s = (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
		for (int k = 0; k < j; k++) {
			if (null != field.getPreRowParseEvent())
				exeShell(field, "row-pre-parse", field.getPreRowParseEvent(), null, null, j, k);
			MessageBean messagebean = (MessageBean) ClassUtil.invoke(messageBean, s, null, null);
			if (null != field.getPrefix() && (k == 0 && field.isFirRowPrefix() || k > 0))
				i += field.getPrefix().length;
			i = parseMessage(message, messagebean, i, messageData);
			if (null != field.getSuffix() && (k + 1 == j && field.isLastRowSuffix() || k < j - 1))
				i += field.getSuffix().length;
			if (null != field.getPostRowParseEvent())
				exeShell(field, "row-post-parse", field.getPostRowParseEvent(), messagebean, null, j, k);
		}

		return i;
	}

	protected int parseVarReferencFieldValue(Field field, int i) {
		if (ConstantMB.MessageType.XML.getCode() == field.getReference().getType()) {
			int[] ai = new int[1];
			i = A(field, i, ai);
			byte[] abyte0 = new byte[ai[0]];
			System.arraycopy(messageData, i, abyte0, 0, abyte0.length);
			XmlMessageParser c = new XmlMessageParser();
			c.setVariableCache(variableCache);
			c.setMessage(field.getReference());
			c.setMessageData(abyte0);
			c.messageBean = (MessageBean) ClassUtil.getBeanAttributeValue(messageBean, field.getName());
			c.parse();
			return i + ai[0];
		} else {
			return parseVarCombineField(field, i);
		}
	}

	protected int parseVarCombineField(Field field, int i) {
		int[] ai = new int[1];
		i = A(field, i, ai);
		int j = i;
		if (field.getReference() != null && 1001 == field.getReference().getType()) {
			byte[] abyte0 = new byte[ai[0]];
			System.arraycopy(messageData, i, abyte0, 0, abyte0.length);
			AbstractMessageParser b = MessageParserFactory.getMessageParser(field.getReference());
			b.setVariableCache(variableCache);
			b.setMessage(field.getReference());
			b.setMessageData(abyte0);
			ClassUtil.setBeanAttributeValue(messageBean, field.getName(), b.parse());
			j += abyte0.length;
		} else {
			j = setMessageBeanAttrVal(field, i);
			if (j - i != ai[0])
				throw new CommonException("DefaultMessageParser.parseVarCombineField.varCombineField.dataRealLength.wrong");
		}
		return j;
	}

	protected int setMessageBeanAttrVal(Field field, int i) {
		Message message = field.getReference();
		if (null == message) {
			message = new Message();
			message.setId((new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
			message.setShortText(field.getShortText());
			message.setFields(field.getSubFields());
		}
		MessageBean messagebean = (MessageBean) ClassUtil.getBeanAttributeValue(messageBean, field.getName());
		return parseMessage(message, messagebean, i, messageData);
	}

	protected int parseMessage(Message message, MessageBean messagebean, int i, byte[] abyte0) {
		AbstractMessageParser b = MessageParserFactory.getMessageParser(message);
		b.setVariableCache(variableCache);
		b.setMessage(message);
		b.setMessageData(abyte0);
		b.messageBean = messagebean;
		b.setParentBean(messageBean);
		return b.parse(i);
	}

	protected int parseVarFieldValue(Field field, int i) {
		int[] ai = new int[1];
		i = A(field, i, ai);
		Field field1 = new Field();
		field1.setFieldType(2001);
		field1.setName(field.getName());
		field1.setDataType(field.getDataType());
		field1.setDataEncoding(field.getDataEncoding());
		field1.setPaddingDirection(5000);
		field1.setPattern(field.getPattern());
		field1.setLengthFieldLength(field.getLength());
		field1.setStrictDataLength(true);
		if (4001 == field.getDataEncoding())
			field1.setLength(ai[0] * 2);
		else
			field1.setLength(ai[0]);
		field1.setDataCharset(field.getDataCharset());
		field1.setRefLengthField(field.getRefLengthField());
		field1.setExtendedAttributeText(field.getExtendedAttributeText());
		field1.setExtendedAttributes(field.getExtendedAttributes());
		i = parseFixedFieldValue(field1, i);
		return i;
	}

	protected int A(Field field, int i, int[] ai) {
		if (field.getRefLengthField() != null) {
			Object obj = ClassUtil.getBeanAttributeValue(messageBean, field.getRefLengthField().getName());
			ai[0] = parseIntValue(field.getRefLengthField(), obj);
			ai[0] -= field.getRefLengthFieldOffset();
			field.setLength(ai[0]);
		} else {
			Field field1 = new Field();
			field1.setName((new StringBuilder()).append(field.getName()).append(".length").toString());
			field1.setFieldType(2000);
			field1.setDataType(field.getLengthFieldDataType());
			field1.setDataEncoding(field.getLengthFieldDataEncoding());
			field1.setLength(field.getLengthFieldLength());
			field1.setPaddingDirection(5001);
			field1.setPattern(field.getPattern());
			field1.setPadding((byte) 48);
			Object[] aobj = new Object[1];
			i = parseFixedFieldValue(field1, i, aobj);
			ai[0] = parseIntValue(field1, aobj[0]);
			field.setLength(ai[0]);
			if (4001 == field.getDataEncoding())
				ai[0] = ai[0] % 2 != 0 ? ai[0] / 2 + 1 : ai[0] / 2;
		}
		return i;
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

		case 3009:
			i = ((Long) obj).intValue();
			break;

		case 3002, 3006:
		default:
			throw new CommonException("parseIntValue.dataType.unsupport");
		}
		return i;
	}

	protected int parseTableRowNumFieldValue(Field field, int i) {
		return parseFixedFieldValue(field, i);
	}

	protected int parseLengthFieldValue(Field field, int i) {
		return parseFixedFieldValue(field, i);
	}

	protected int parseFixedFieldValue(Field field, int i) {
		Object[] aobj = new Object[1];
		if (-1 == field.getLength()) {
			Object obj = ClassUtil.getBeanValueByPath(messageBean, field.getLengthScript());
			String[] as = field.getLengthScript().split("\\.");
			Message message = this.message;
			if (as.length > 1) {
				MessageBean messagebean = messageBean;
				for (int j = 0; j < as.length - 1; j++)
					messagebean = messagebean.getParent();

				message = messagebean.getMetadata();
			}
			Field field1 = message.getField(as[as.length - 1]);
			field = field.copy();
			field.setLength(parseIntValue(field1, obj));
		}
		i = parseFixedFieldValue(field, i, aobj);
		if (field.isEditable())
			switch (field.getDataType()) {
			case 3004:
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), aobj[0], Byte.TYPE);
				break;

			case 3003, 3007:
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), aobj[0], Integer.TYPE);
				break;

			case 3005, 3008:
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), aobj[0], Short.TYPE);
				break;

			case 3009:
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), aobj[0], Long.TYPE);
				break;

			case 3006:
			default:
				if (!(aobj[0] instanceof String) || 3001 == field.getDataType() || 0 != ((String) aobj[0]).length())
					ClassUtil.setBeanAttributeValue(messageBean, field.getName(), aobj[0]);
				break;
			}
		return i;
	}

	protected int parseFixedFieldValue(Field field, int i, Object[] aobj) {
		int j = 0;
		switch (field.getDataType()) {
		case 3000, 3001, 3006, 3010, 3011:
			if (field.getPrefix() != null) {
				byte[] abyte8 = field.getPrefix();
				for (int k1 = 0; k1 < abyte8.length; k1++)
					if (abyte8[k1] != messageData[i + k1])
						throw new CommonException("DefaultMessageParser.parseFixedFieldValue.prefix.illegal");

				i += abyte8.length;
			}
			if (!field.isStrictDataLength() && field.getPaddingDirection() == 5000) {
				if (field.getSuffix() == null && field != message.getFields().get(message.getFields().size() - 1))
					throw new CommonException("DefaultMessageParser.parseFixedFieldValue.field.mustHaveSuffix");
				if (field.getSuffix() != null) {
					int l = CodeUtil.byteArrayIndexOf(messageData, field.getSuffix(), i);
					if (-1 == l) {
						if (field == message.getFields().get(message.getFields().size() - 1))
							l = messageData.length;
						else
							throw new CommonException("DefaultMessageParser.parseFixedFieldValue.field.noSuffixInMessage");
					}
					j = l - i;
				} else {
					j = messageData.length - i;
				}
			} else if (null != field.getSuffix()) {
				int i1 = i;
				i1 = CodeUtil.byteArrayIndexOf(messageData, field.getSuffix(), i);
				if (-1 == i1) {
					if (field == message.getFields().get(message.getFields().size() - 1))
						i1 = messageData.length;
					else
						throw new CommonException("DefaultMessageParser.parseFixedFieldValue.field.noSuffixInMessage");
				}
				j = i1 - i;
			} else {
				j = field.getLength();
				if (4001 == field.getDataEncoding())
					j = j % 2 != 0 ? j / 2 + 1 : j / 2;
			}
			if (j <= 0) {
				if (field.getSuffix() != null)
					i += field.getSuffix().length;
				aobj[0] = "";
				break;
			}
			byte[] abyte0 = new byte[j];
			System.arraycopy(messageData, i, abyte0, 0, abyte0.length);
			if ("true".equals(field.getExtendAttribute("remove_over_length")) && abyte0.length > field.getLength()) {
				byte[] abyte9 = abyte0;
				abyte0 = new byte[field.getLength()];
				System.arraycopy(abyte9, 0, abyte0, 0, abyte0.length);
			}
			if (4001 == field.getDataEncoding())
				abyte0 = CodeUtil.bCDtoASC(abyte0);
			abyte0 = B(field, abyte0);
			if (4001 == field.getDataEncoding() || C) {
				if (2001 == field.getFieldType() && field.getRefLengthField() == null) {
					if (field.getLengthFieldLength() < abyte0.length) {
						byte[] abyte10 = new byte[field.getLengthFieldLength()];
						if (null != field.getExtendedAttributeText()
								&& "left".equalsIgnoreCase(field.getExtendedAttributes().get("padding-direction")))
							System.arraycopy(abyte0, abyte0.length - field.getLengthFieldLength(), abyte10, 0, abyte10.length);
						else
							System.arraycopy(abyte0, 0, abyte10, 0, field.getLengthFieldLength());
						abyte0 = abyte10;
					}
				} else if (2000 == field.getFieldType() && field.getLength() < abyte0.length) {
					byte[] abyte11 = new byte[field.getLength()];
					if (null != field.getExtendedAttributeText() && "left".equalsIgnoreCase(field.getExtendedAttributes().get("padding-direction")))
						System.arraycopy(abyte0, abyte0.length - field.getLength(), abyte11, 0, abyte11.length);
					else
						System.arraycopy(abyte0, 0, abyte11, 0, field.getLength());
					abyte0 = abyte11;
				}
			}
			if (3010 == field.getDataType()) {
				if (field.getPattern().indexOf(",") == -1) {
					if (null == abyte0 || abyte0.length == 0)
						abyte0 = "0".getBytes();
				} else if (null == abyte0 || abyte0.length == 0)
					abyte0 = "0".getBytes();
				else if (abyte0[0] == 46) {
					byte[] abyte12 = new byte[abyte0.length + 1];
					abyte12[0] = 48;
					System.arraycopy(abyte0, 0, abyte12, 1, abyte0.length);
					abyte0 = abyte12;
				}
			}
			if (field.isRemoveUnwatchable()) {
				int j1;
				for (j1 = 0; j1 < abyte0.length && 0 <= abyte0[j1] && abyte0[j1] < 32; j1++)
					;
				int l1;
				for (l1 = abyte0.length - 1; j1 != abyte0.length && 0 < abyte0.length && 0 <= abyte0[l1] && abyte0[l1] < 32; l1--)
					;
				byte[] abyte13 = new byte[(l1 - j1) + 1];
				System.arraycopy(abyte0, j1, abyte13, 0, abyte13.length);
				abyte0 = new byte[abyte13.length];
				System.arraycopy(abyte13, 0, abyte0, 0, abyte13.length);
			}
			String s = field.getDataCharset();
			if (null == s)
				s = message.getMsgCharset();
			if (null == s)
				s = getDefaultCharset();
			if (null != s)
				try {
					aobj[0] = new String(abyte0, s);
				} catch (UnsupportedEncodingException unsupportedencodingexception) {
					throw new CommonException("field.encoding.unsupport");
				}
			else
				aobj[0] = new String(abyte0);
			if (field.getSuffix() != null)
				i += field.getSuffix().length;
			break;

		case 3002:
			if (field == message.getFields().get(message.getFields().size() - 1))
				j = messageData.length - i;
			else
				j = field.getLength();
			if (j > field.getLength())
				j = field.getLength();
			byte[] abyte1 = new byte[j];
			System.arraycopy(messageData, i, abyte1, 0, abyte1.length);
			aobj[0] = abyte1;
			break;

		case 3003:
			j = field.getLength();
			byte[] abyte2 = new byte[j];
			System.arraycopy(messageData, i, abyte2, 0, abyte2.length);
			aobj[0] = Integer.valueOf(CodeUtil.bytesToInt(abyte2));
			break;

		case 3004:
			j = field.getLength();
			byte[] abyte3 = new byte[j];
			System.arraycopy(messageData, i, abyte3, 0, abyte3.length);
			aobj[0] = Byte.valueOf(abyte3[0]);
			break;

		case 3005:
			j = field.getLength();
			byte[] abyte4 = new byte[j];
			System.arraycopy(messageData, i, abyte4, 0, abyte4.length);
			aobj[0] = Short.valueOf(CodeUtil.bytesToShort(abyte4));
			break;

		case 3007:
			j = field.getLength();
			byte[] abyte5 = new byte[j];
			System.arraycopy(messageData, i, abyte5, 0, abyte5.length);
			aobj[0] = Integer.valueOf(CodeUtil.ntohi(abyte5));
			break;

		case 3008:
			j = field.getLength();
			byte[] abyte6 = new byte[j];
			System.arraycopy(messageData, i, abyte6, 0, abyte6.length);
			aobj[0] = Short.valueOf((short) CodeUtil.ntohs(abyte6));
			break;

		case 3009:
			j = field.getLength();
			byte[] abyte7 = new byte[j];
			System.arraycopy(messageData, i, abyte7, 0, abyte7.length);
			aobj[0] = Long.valueOf(CodeUtil.bytesToLong(abyte7));
			break;

		default:
			throw new CommonException("dataType.unsupport");
		}
		i += j;
		return i;
	}

	protected byte[] B(Field field, byte[] abyte0) {
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

	@Override
	public void ignore(Field field) {
		ignore(field.getName());
	}

	@Override
	public void ignore(String fieldName) {
		if (null == fieldNameSet) {
			fieldNameSet = new HashSet<>(32);
		}
		fieldNameSet.add(fieldName);
	}

	static {
		mbMap = new HashMap<>();
//		try {
//			if (FileUtil.isExist(ConfigManager.getFullPathFileName("messagebean.properties"))) {
//				Properties properties = ConfigManager.loadProperties("messagebean.properties");
//				if (null != properties.getProperty("cutAccordingToLength") && 0 != properties.getProperty("cutAccordingToLength").length()
//						&& "1".equalsIgnoreCase(properties.getProperty("cutAccordingToLength")))
//					C = true;
//				if (null != properties.getProperty("replace_all") && 0 != properties.getProperty("replace_all").length()) {
//					String[] as = properties.getProperty("replace_all").split("\\|");
//					for (int i = 0; i < as.length; i++) {
//						String[] as1 = as[i].split(":");
//						if (2 != as1.length)
//							throw new CommonException("file messagebean.properties[@key=replace_all] pattern error. pattern : hex=hex;hex=hex!");
//						mbMap.put(as1[0], as1[1]);
//					}
//
//				}
//				if (null != properties.getProperty("use_bean") && 0 != properties.getProperty("use_bean").length()
//						&& "true".equalsIgnoreCase(properties.getProperty("use_bean")))
//					G = true;
//			}
//		} catch (Exception exception) {
//			exception.printStackTrace();
//			ExceptionUtil.throwActualException(exception);
//		}
	}
}
