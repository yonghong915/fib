package com.fib.msgconverter.message.parser;

import java.io.UnsupportedEncodingException;
import java.util.*;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.fib.msgconverter.message.bean.MessageBean;
import com.fib.msgconverter.message.metadata.Field;
import com.fib.msgconverter.message.metadata.Message;
import com.fib.msgconverter.message.metadata.ValueRange;
import com.giantstone.common.config.ConfigManager;
import com.giantstone.common.script.BeanShellEngine;
import com.giantstone.common.util.ByteBuffer;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.common.util.FileUtil;
import com.giantstone.common.util.StringUtil;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public class DefaultMessageParser extends AbstractMessageParser {

	public static boolean C = false;
	public static Map H;
	public static boolean G = false;
	protected byte F[];
	protected Set D;
	protected int E;
	protected boolean I;

	public DefaultMessageParser() {
		F = null;
		D = null;
		E = 1;
		I = false;
	}

	protected void A() {
		if (!G)
			messageBean = null;
		F = null;
		D = null;
	}

	public MessageBean parse() {
		if (null == message)
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "message" }));
		if (null == messageData)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "messageData" }));
		Object obj = null;
		for (Iterator iterator = H.keySet().iterator(); iterator.hasNext();) {
			String s = (String) iterator.next();
			messageData = CodeUtil.replaceAll(messageData, CodeUtil.HextoByte(s), CodeUtil.HextoByte((String) H.get(s)),
					0, messageData.length);
		}

		A();
		if (0 == variableCache.size())
			loadVariable();
		if (null == messageBean)
			messageBean = (MessageBean) ClassUtil.createClassInstance(message.getClassName());
		messageBean.setMetadata(message);
		if (null != getParentBean())
			messageBean.setParent(getParentBean());
		I = true;
		if (message.getPreParseEvent() != null)
			B("pre-parse", message.getPreParseEvent());
		parse(0);
		if (message.getPostParseEvent() != null)
			B("post-parse", message.getPostParseEvent());
		return messageBean;
	}

	protected void B(String s, String s1) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		beanshellengine.set("messageParser", this);
		beanshellengine.set("message", message);
		beanshellengine.set("messageBean", messageBean);
		beanshellengine.set("messageData", messageData);
		if (0 != variableCache.size()) {
			Iterator iterator = variableCache.keySet().iterator();
			Object obj = null;
			String s2;
			for (; iterator.hasNext(); beanshellengine.set(s2, variableCache.get(s2)))
				s2 = (String) iterator.next();

		}
		beanshellengine.eval(s1);
		messageData = (byte[]) (byte[]) beanshellengine.get("messageData");
		if (0 != variableCache.size()) {
			Iterator iterator1 = variableCache.keySet().iterator();
			Object obj1 = null;
			String s3;
			for (; iterator1.hasNext(); variableCache.put(s3, beanshellengine.get(s3)))
				s3 = (String) iterator1.next();

		}
	}

	protected void A(Field field, String s, String s1) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		beanshellengine.set("messageParser", this);
		beanshellengine.set("message", message);
		beanshellengine.set("messageBean", messageBean);
		beanshellengine.set("messageData", messageData);
		beanshellengine.set("field", field);
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

	protected void A(Field field, String s, String s1, MessageBean messagebean, List list, int i, int j) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		beanshellengine.set("messageParser", this);
		beanshellengine.set("message", message);
		beanshellengine.set("messageBean", messageBean);
		beanshellengine.set("messageData", messageData);
		beanshellengine.set("field", field);
		beanshellengine.set("list", list);
		beanshellengine.set("rowNum", Integer.valueOf(i));
		beanshellengine.set("currRow", Integer.valueOf(j));
		beanshellengine.set("currentBean", messagebean);
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

	protected int parse(int i) {
		if (!I && null != getParentBean())
			messageBean.setParent(getParentBean());
		Iterator iterator = message.getFields().values().iterator();
		Object obj = null;
		while (iterator.hasNext()) {
			Field field = (Field) iterator.next();
			i = B(field, i);
		}
		return i;
	}

	protected int A(Field field, MessageBean messagebean, Message message, int i, byte abyte0[]) {
		Message message1 = this.message;
		Field field1 = null;
		if (null == field.getRowNumField()) {
			String as[] = field.getRowNumFieldName().split("\\.");
			int k = 0;
			do {
				if (k >= as.length)
					break;
				field1 = ((Message) message1).getField(as[k].trim());
				if (++k < as.length)
					if (null != field1.getReference())
						message1 = field1.getReference();
					else if (0 != field1.getSubFields().size()) {
						message1 = new Message();
						((Message) message1).setFields(field1.getSubFields());
					}
			} while (true);
		} else {
			field1 = field.getRowNumField();
		}
		int j = B(field1, ClassUtil.getBeanValueByPath(messagebean, field.getRowNumFieldName()));
		Object obj = null;
		ArrayList arraylist = new ArrayList(j);
		for (int l = 0; l < j; l++) {
			if (null != field.getPreRowParseEvent())
				A(field, "row-pre-parse", field.getPreRowParseEvent(), null, ((List) (arraylist)), j, l);
			MessageBean messagebean1 = (MessageBean) ClassUtil.createClassInstance(message.getClassName());
			i = B(field, message, messagebean1, i, abyte0);
			arraylist.add(messagebean1);
			if (null != field.getPostRowParseEvent())
				A(field, "row-post-parse", field.getPostRowParseEvent(), messagebean1, ((List) (arraylist)), j, l);
		}

		ClassUtil.setBeanAttributeValue(messagebean, field.getName(), arraylist, List.class);
		return i;
	}

	protected int B(Field field, MessageBean messagebean, Message message, int i, byte abyte0[]) {
		MessageBean messagebean1 = (MessageBean) ClassUtil.createClassInstance(message.getClassName());
		i = B(field, message, messagebean1, i, abyte0);
		String s = (new StringBuilder()).append("set").append(StringUtil.toUpperCaseFirstOne(field.getName()))
				.toString();
		try {
			ClassUtil.invoke(messagebean, s, new Class[] { MessageBean.class }, new Object[] { messagebean1 });
		} catch (Exception exception) {
			exception.printStackTrace();
			ExceptionUtil.throwActualException(exception);
		}
		return i;
	}

	protected Message B(Field field, Message message, MessageBean messagebean) {
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

	protected Message C(Field field, Message message, MessageBean messagebean) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		BeanShellEngine beanshellengine1 = new BeanShellEngine();
		String s = null;
		beanshellengine1.set("messageBean", messagebean);
		if (0 != variableCache.size()) {
			Iterator iterator = variableCache.keySet().iterator();
			Object obj1 = null;
			String s1;
			for (; iterator.hasNext(); beanshellengine.set(s1, variableCache.get(s1)))
				s1 = (String) iterator.next();

		}
		Object obj = beanshellengine1.eval(field.getExpression());
		if (0 != variableCache.size()) {
			Iterator iterator1 = variableCache.keySet().iterator();
			Object obj2 = null;
			String s2;
			for (; iterator1.hasNext(); variableCache.put(s2, beanshellengine.get(s2)))
				s2 = (String) iterator1.next();

		}
		if (obj != null)
			if (obj instanceof String)
				s = (String) obj;
			else
				s = obj.toString();
		ValueRange valuerange = (ValueRange) field.getValueRange().get(s);
		if (null == valuerange)
			valuerange = (ValueRange) field.getValueRange().get("default-ref");
		return valuerange.getReference();
	}

	protected int B(Field field, int i) {
		if (D != null && D.contains(field.getName()))
			return i;
		if (field.getIso8583_no() > 0 && F != null) {
			if (8 == F.length && field.getIso8583_no() > 64)
				return i;
			E++;
			if (0 == CodeUtil.getBit(F, field.getIso8583_no()))
				return i;
		}
		if (field.getPreParseEvent() != null)
			A(field, "pre-parse", field.getPreParseEvent());
		if ("dynamic".equalsIgnoreCase(field.getReferenceType())) {
			Message message = B(field, this.message, messageBean);
			if (2004 == field.getFieldType())
				i = A(field, messageBean, message, i, messageData);
			else
				i = B(field, messageBean, message, i, messageData);
		} else if ("expression".equalsIgnoreCase(field.getReferenceType())) {
			Message message1 = C(field, this.message, messageBean);
			if (2004 == field.getFieldType())
				i = A(field, messageBean, message1, i, messageData);
			else
				i = B(field, messageBean, message1, i, messageData);
		} else {
			switch (field.getFieldType()) {
			case 2000:
				i = J(field, i);
				break;

			case 2006:
				Object aobj[] = new Object[1];
				i = A(field, i, aobj);
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), aobj[0]);
				if (F == null) {
					if (aobj[0] instanceof String)
						F = CodeUtil.HextoByte(((String) aobj[0]).getBytes());
					else
						F = (byte[]) (byte[]) aobj[0];
					break;
				}
				ByteBuffer bytebuffer = new ByteBuffer(F.length * 2);
				bytebuffer.append(F);
				if (aobj[0] instanceof String)
					bytebuffer.append(CodeUtil.HextoByte(((String) aobj[0]).getBytes()));
				else
					bytebuffer.append((byte[]) (byte[]) aobj[0]);
				F = bytebuffer.toBytes();
				break;

			case 2007:
				i = G(field, i);
				break;

			case 2005:
				i = K(field, i);
				break;

			case 2001:
				i = L(field, i);
				break;

			case 2002:
			case 2008:
				i = E(field, i);
				break;

			case 2003:
				i = H(field, i);
				break;

			case 2009:
				i = D(field, i);
				break;

			case 2011:
				i = A(field, i);
				break;

			case 2004:
				i = F(field, i);
				break;

			case 2010:
				i = I(field, i);
				break;

			default:
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"field.fieldType.notExist",
						new String[] { (new StringBuilder()).append("").append(field.getFieldType()).toString() }));
			}
		}
		if (field.getPostParseEvent() != null)
			A(field, "post-parse", field.getPostParseEvent());
		return i;
	}

	protected int I(Field field, int i) {
		Object aobj[] = new Object[1];
		byte abyte0[] = new byte[field.getLength()];
		System.arraycopy(messageData, i, abyte0, 0, abyte0.length);
		aobj[0] = abyte0;
		ClassUtil.setBeanAttributeValue(messageBean, field.getName(), aobj[0]);
		i += field.getLength();
		return i;
	}

	protected int A(Field field, int i) {
		int j = 0;
		int k = 0;
		if (null != field.getTabPrefix())
			i += field.getTabPrefix().length;
		if (null != field.getRefLengthFieldName()) {
			String as[] = field.getRefLengthFieldName().split("\\.");
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
				throw new RuntimeException((new StringBuilder()).append("field[@name='").append(field1.getName())
						.append("'] ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			k = B(field1, obj1);
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
		byte abyte0[] = new byte[k];
		System.arraycopy(messageData, i, abyte0, 0, k);
		Message message1 = field.getReference();
		if (null == message1) {
			message1 = new Message();
			message1.setId(
					(new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
			message1.setShortText(field.getShortText());
			message1.setFields(field.getSubFields());
		}
		Object obj = null;
		String s = (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(field.getName()))
				.toString();
		int j1 = 0;
		boolean flag = false;
		while (j1 < abyte0.length) {
			if (null != field.getPrefix() && (j == 0 && field.isFirRowPrefix() || j > 0))
				j1 += field.getPrefix().length;
			if (null != field.getPreRowParseEvent())
				A(field, "row-pre-parse", field.getPreRowParseEvent(), null, null, j, j);
			MessageBean messagebean1 = (MessageBean) ClassUtil.invoke(messageBean, s, null, null);
			if (field.isRowCut()) {
				int k1 = CodeUtil.byteArrayIndexOf(abyte0, field.getSuffix(), j1);
				if (-1 == k1)
					k1 = abyte0.length;
				byte abyte1[] = new byte[k1 - j1];
				System.arraycopy(abyte0, j1, abyte1, 0, abyte1.length);
				B(field, message1, messagebean1, 0, abyte1);
				j1 = k1;
			} else {
				j1 = B(field, message1, messagebean1, j1, abyte0);
			}
			if (null != field.getSuffix())
				j1 += field.getSuffix().length;
			if (null != field.getPostRowParseEvent())
				A(field, "row-post-parse", field.getPostRowParseEvent(), messagebean1, null, j, j);
			j++;
		}
		i += abyte0.length;
		if (null != field.getTabSuffix())
			i += field.getTabSuffix().length;
		return i;
	}

	protected int C(Field field, int i) {
		int j = 0;
		boolean flag = false;
		boolean flag1 = false;
		Object obj = null;
		Message message = field.getReference();
		if (null == message) {
			message = new Message();
			message.setId(
					(new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
			message.setShortText(field.getShortText());
			message.setFields(field.getSubFields());
		}
		Object obj1 = null;
		String s = (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(field.getName()))
				.toString();
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
			byte abyte0[] = new byte[l];
			System.arraycopy(messageData, i, abyte0, 0, abyte0.length);
			if (null != field.getPreRowParseEvent())
				A(field, "row-pre-parse", field.getPreRowParseEvent(), null, null, j, j);
			MessageBean messagebean = (MessageBean) ClassUtil.invoke(messageBean, s, null, null);
			B(field, message, messagebean, 0, abyte0);
			i += l;
			if (null != field.getSuffix())
				i += field.getSuffix().length;
			if (null != field.getPostRowParseEvent())
				A(field, "row-post-parse", field.getPostRowParseEvent(), messagebean, null, j, j);
			j++;
		} while (true);
		return i;
	}

	protected int F(Field field, int i) {
		int j = 0;
		if (null != field.getRowNumField()) {
			j = B(field.getRowNumField(),
					ClassUtil.getBeanAttributeValue(messageBean, field.getRowNumField().getName()));
		} else {
			String as[] = field.getRowNumFieldName().split("\\.");
			Field field1 = getParentBean().getMetadata().getField(as[1]);
			j = B(field1, ClassUtil.getBeanValueByPath(messageBean, field.getRowNumFieldName()));
		}
		Message message = field.getReference();
		if (null == message) {
			message = new Message();
			message.setId(
					(new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
			message.setShortText(field.getShortText());
			message.setFields(field.getSubFields());
		}
		Object obj = null;
		String s = (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(field.getName()))
				.toString();
		for (int k = 0; k < j; k++) {
			if (null != field.getPreRowParseEvent())
				A(field, "row-pre-parse", field.getPreRowParseEvent(), null, null, j, k);
			MessageBean messagebean = (MessageBean) ClassUtil.invoke(messageBean, s, null, null);
			if (null != field.getPrefix() && (k == 0 && field.isFirRowPrefix() || k > 0))
				i += field.getPrefix().length;
			i = B(field, message, messagebean, i, messageData);
			if (null != field.getSuffix() && (k + 1 == j && field.isLastRowSuffix() || k < j - 1))
				i += field.getSuffix().length;
			if (null != field.getPostRowParseEvent())
				A(field, "row-post-parse", field.getPostRowParseEvent(), messagebean, null, j, k);
		}

		return i;
	}

	protected int D(Field field, int i) {
		if (1001 == field.getReference().getType()) {
			int ai[] = new int[1];
			i = A(field, i, ai);
			byte abyte0[] = new byte[ai[0]];
			System.arraycopy(messageData, i, abyte0, 0, abyte0.length);
			XmlMessageParser c = new XmlMessageParser();
			c.setVariableCache(variableCache);
			c.setMessage(field.getReference());
			c.setMessageData(abyte0);
			c.messageBean = (MessageBean) ClassUtil.getBeanAttributeValue(messageBean, field.getName());
			c.parse();
			return i + ai[0];
		} else {
			return H(field, i);
		}
	}

	protected int H(Field field, int i) {
		int ai[] = new int[1];
		i = A(field, i, ai);
		int j = i;
		if (field.getReference() != null && 1001 == field.getReference().getType()) {
			byte abyte0[] = new byte[ai[0]];
			System.arraycopy(messageData, i, abyte0, 0, abyte0.length);
			AbstractMessageParser b = MessageParserFactory.A(field.getReference());
			b.setVariableCache(variableCache);
			b.setMessage(field.getReference());
			b.setMessageData(abyte0);
			ClassUtil.setBeanAttributeValue(messageBean, field.getName(), b.parse());
			j += abyte0.length;
		} else {
			j = E(field, i);
			if (j - i != ai[0])
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"DefaultMessageParser.parseVarCombineField.varCombineField.dataRealLength.wrong",
						new String[] { field.getName(), String.valueOf(j - i),
								(new StringBuilder()).append("").append(ai[0]).toString() }));
		}
		return j;
	}

	protected int E(Field field, int i) {
		Message message = field.getReference();
		if (null == message) {
			message = new Message();
			message.setId(
					(new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
			message.setShortText(field.getShortText());
			message.setFields(field.getSubFields());
		}
		MessageBean messagebean = (MessageBean) ClassUtil.getBeanAttributeValue(messageBean, field.getName());
		return B(field, message, messagebean, i, messageData);
	}

	protected int B(Field field, Message message, MessageBean messagebean, int i, byte abyte0[]) {
		AbstractMessageParser b = MessageParserFactory.A(message);
		b.setVariableCache(variableCache);
		b.setMessage(message);
		b.setMessageData(abyte0);
		b.messageBean = messagebean;
		b.setParentBean(messageBean);
		return b.parse(i);
	}

	protected int L(Field field, int i) {
		int ai[] = new int[1];
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
		i = J(field1, i);
		return i;
	}

	protected int A(Field field, int i, int ai[]) {
		if (field.getRefLengthField() != null) {
			Object obj = ClassUtil.getBeanAttributeValue(messageBean, field.getRefLengthField().getName());
			ai[0] = B(field.getRefLengthField(), obj);
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
			Object aobj[] = new Object[1];
			i = A(field1, i, aobj);
			ai[0] = B(field1, aobj[0]);
			field.setLength(ai[0]);
			if (4001 == field.getDataEncoding())
				ai[0] = ai[0] % 2 != 0 ? ai[0] / 2 + 1 : ai[0] / 2;
		}
		return i;
	}

	protected int B(Field field, Object obj) {
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

		case 3009:
			i = ((Long) obj).intValue();
			break;

		case 3002:
		case 3006:
		default:
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("parseIntValue.dataType.unsupport",
							new String[] { (new StringBuilder()).append("").append(field.getDataType()).toString(),
									field.getName() }));
		}
		return i;
	}

	protected int K(Field field, int i) {
		return J(field, i);
	}

	protected int G(Field field, int i) {
		return J(field, i);
	}

	protected int J(Field field, int i) {
		Object aobj[] = new Object[1];
		if (-1 == field.getLength()) {
			Object obj = ClassUtil.getBeanValueByPath(messageBean, field.getLengthScript());
			String as[] = field.getLengthScript().split("\\.");
			Message message = this.message;
			if (as.length > 1) {
				MessageBean messagebean = messageBean;
				for (int j = 0; j < as.length - 1; j++)
					messagebean = messagebean.getParent();

				message = messagebean.getMetadata();
			}
			Field field1 = message.getField(as[as.length - 1]);
			field = field.copy();
			field.setLength(B(field1, obj));
		}
		i = A(field, i, aobj);
		if (field.isEditable())
			switch (field.getDataType()) {
			case 3004:
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), aobj[0], Byte.TYPE);
				break;

			case 3003:
			case 3007:
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), aobj[0], Integer.TYPE);
				break;

			case 3005:
			case 3008:
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

	protected int A(Field field, int i, Object aobj[]) {
		int j = 0;
		Object obj = null;
		switch (field.getDataType()) {
		case 3000:
		case 3001:
		case 3006:
		case 3010:
		case 3011:
			if (field.getPrefix() != null) {
				byte abyte8[] = field.getPrefix();
				for (int k1 = 0; k1 < abyte8.length; k1++)
					if (abyte8[k1] != messageData[i + k1])
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"DefaultMessageParser.parseFixedFieldValue.prefix.illegal",
								new String[] { field.getName(), new String(field.getPrefix()),
										(new StringBuilder()).append("").append(i).toString() }));

				i += abyte8.length;
			}
			if (!field.isStrictDataLength() && field.getPaddingDirection() == 5000) {
				int k = i;
				if (field.getSuffix() == null && field != message.getFields().get(message.getFields().size() - 1))
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"DefaultMessageParser.parseFixedFieldValue.field.mustHaveSuffix",
							new String[] { field.getName() }));
				if (field.getSuffix() != null) {
					int l = CodeUtil.byteArrayIndexOf(messageData, field.getSuffix(), i);
					if (-1 == l)
						if (field == message.getFields().get(message.getFields().size() - 1))
							l = messageData.length;
						else
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
									"DefaultMessageParser.parseFixedFieldValue.field.noSuffixInMessage",
									new String[] { field.getName(), new String(field.getSuffix()),
											(new StringBuilder()).append("").append(i).toString() }));
					j = l - i;
				} else {
					j = messageData.length - i;
				}
			} else if (null != field.getSuffix()) {
				int i1 = i;
				i1 = CodeUtil.byteArrayIndexOf(messageData, field.getSuffix(), i);
				if (-1 == i1)
					if (field == message.getFields().get(message.getFields().size() - 1))
						i1 = messageData.length;
					else
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"DefaultMessageParser.parseFixedFieldValue.field.noSuffixInMessage",
								new String[] { field.getName(), new String(field.getSuffix()),
										(new StringBuilder()).append("").append(i).toString() }));
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
			byte abyte0[] = new byte[j];
			System.arraycopy(messageData, i, abyte0, 0, abyte0.length);
			if ("true".equals(field.getExtendAttribute("remove_over_length")) && abyte0.length > field.getLength()) {
				byte abyte9[] = abyte0;
				abyte0 = new byte[field.getLength()];
				System.arraycopy(abyte9, 0, abyte0, 0, abyte0.length);
			}
			if (4001 == field.getDataEncoding())
				abyte0 = CodeUtil.BCDtoASC(abyte0);
			abyte0 = B(field, abyte0);
			if (4001 == field.getDataEncoding() || C)
				if (2001 == field.getFieldType() && field.getRefLengthField() == null) {
					if (field.getLengthFieldLength() < abyte0.length) {
						byte abyte10[] = new byte[field.getLengthFieldLength()];
						if (null != field.getExtendedAttributeText() && "left"
								.equalsIgnoreCase((String) field.getExtendedAttributes().get("padding-direction")))
							System.arraycopy(abyte0, abyte0.length - field.getLengthFieldLength(), abyte10, 0,
									abyte10.length);
						else
							System.arraycopy(abyte0, 0, abyte10, 0, field.getLengthFieldLength());
						abyte0 = abyte10;
					}
				} else if (2000 == field.getFieldType() && field.getLength() < abyte0.length) {
					byte abyte11[] = new byte[field.getLength()];
					if (null != field.getExtendedAttributeText()
							&& "left".equalsIgnoreCase((String) field.getExtendedAttributes().get("padding-direction")))
						System.arraycopy(abyte0, abyte0.length - field.getLength(), abyte11, 0, abyte11.length);
					else
						System.arraycopy(abyte0, 0, abyte11, 0, field.getLength());
					abyte0 = abyte11;
				}
			if (3010 == field.getDataType())
				if (field.getPattern().indexOf(",") == -1) {
					if (null == abyte0 || abyte0.length == 0)
						abyte0 = "0".getBytes();
				} else if (null == abyte0 || abyte0.length == 0)
					abyte0 = "0".getBytes();
				else if (abyte0[0] == 46) {
					byte abyte12[] = new byte[abyte0.length + 1];
					abyte12[0] = 48;
					System.arraycopy(abyte0, 0, abyte12, 1, abyte0.length);
					abyte0 = abyte12;
				}
			if (field.isRemoveUnwatchable()) {
				int j1;
				for (j1 = 0; j1 < abyte0.length && 0 <= abyte0[j1] && abyte0[j1] < 32; j1++)
					;
				int l1;
				for (l1 = abyte0.length - 1; j1 != abyte0.length && 0 < abyte0.length && 0 <= abyte0[l1]
						&& abyte0[l1] < 32; l1--)
					;
				byte abyte13[] = new byte[(l1 - j1) + 1];
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
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("field.encoding.unsupport", new String[] { field.getName(), s }));
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
			byte abyte1[] = new byte[j];
			System.arraycopy(messageData, i, abyte1, 0, abyte1.length);
			aobj[0] = abyte1;
			break;

		case 3003:
			j = field.getLength();
			byte abyte2[] = new byte[j];
			System.arraycopy(messageData, i, abyte2, 0, abyte2.length);
			aobj[0] = Integer.valueOf(CodeUtil.BytesToInt(abyte2));
			break;

		case 3004:
			j = field.getLength();
			byte abyte3[] = new byte[j];
			System.arraycopy(messageData, i, abyte3, 0, abyte3.length);
			aobj[0] = Byte.valueOf(abyte3[0]);
			break;

		case 3005:
			j = field.getLength();
			byte abyte4[] = new byte[j];
			System.arraycopy(messageData, i, abyte4, 0, abyte4.length);
			aobj[0] = Short.valueOf(CodeUtil.BytesToShort(abyte4));
			break;

		case 3007:
			j = field.getLength();
			byte abyte5[] = new byte[j];
			System.arraycopy(messageData, i, abyte5, 0, abyte5.length);
			aobj[0] = Integer.valueOf(CodeUtil.ntohi(abyte5));
			break;

		case 3008:
			j = field.getLength();
			byte abyte6[] = new byte[j];
			System.arraycopy(messageData, i, abyte6, 0, abyte6.length);
			aobj[0] = Short.valueOf((short) CodeUtil.ntohs(abyte6));
			break;

		case 3009:
			j = field.getLength();
			byte abyte7[] = new byte[j];
			System.arraycopy(messageData, i, abyte7, 0, abyte7.length);
			aobj[0] = Long.valueOf(CodeUtil.BytesToLong(abyte7));
			break;

		default:
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("dataType.unsupport",
					new String[] { (new StringBuilder()).append("").append(field.getDataType()).toString() }));
		}
		i += j;
		return i;
	}

	protected byte[] B(Field field, byte abyte0[]) {
		if (5000 == field.getPaddingDirection())
			return abyte0;
		String s = field.getExtendAttribute("remove_start");
		if (null != s) {
			int i = CodeUtil.byteArrayIndexOf(abyte0, CodeUtil.HextoByte(s), 0);
			byte abyte1[] = abyte0;
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
				byte abyte2[] = new byte[abyte0.length - j];
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
			byte abyte3[] = new byte[j + 1];
			System.arraycopy(abyte0, 0, abyte3, 0, abyte3.length);
			return abyte3;
		}
	}

	public void ignore(Field field) {
		ignore(field.getName());
	}

	public void ignore(String s) {
		if (null == D)
			D = new HashSet(32);
		D.add(s);
	}

	static {
		H = new HashMap();
		try {
			if (FileUtil.isExist(ConfigManager.getFullPathFileName("messagebean.properties"))) {
				Properties properties = ConfigManager.loadProperties("messagebean.properties");
				if (null != properties.getProperty("cutAccordingToLength")
						&& 0 != properties.getProperty("cutAccordingToLength").length()
						&& "1".equalsIgnoreCase(properties.getProperty("cutAccordingToLength")))
					C = true;
				if (null != properties.getProperty("replace_all")
						&& 0 != properties.getProperty("replace_all").length()) {
					String as[] = properties.getProperty("replace_all").split("\\|");
					Object obj = null;
					for (int i = 0; i < as.length; i++) {
						String as1[] = as[i].split(":");
						if (2 != as1.length)
							throw new RuntimeException(
									"file messagebean.properties[@key=replace_all] pattern error. pattern : hex=hex;hex=hex!");
						H.put(as1[0], as1[1]);
					}

				}
				if (null != properties.getProperty("use_bean") && 0 != properties.getProperty("use_bean").length()
						&& "true".equalsIgnoreCase(properties.getProperty("use_bean")))
					G = true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			ExceptionUtil.throwActualException(exception);
		}
	}
}
