package com.fib.upp.message.packer;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.fib.commons.exception.CommonException;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.metadata.Field;
import com.fib.upp.message.metadata.MacCalculator;
import com.fib.upp.message.metadata.Message;
import com.fib.upp.message.metadata.ValueRange;
import com.giantstone.common.config.ConfigManager;
import com.giantstone.common.script.BeanShellEngine;
import com.giantstone.common.util.ByteBuffer;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.common.util.FileUtil;
import com.giantstone.common.util.SortHashMap;

public class DefaultMessagePacker extends AbstractMessagePacker {

	protected SortHashMap D;
	protected List C;
	protected List F;
	protected List G;
	protected List I;
	protected List H;
	protected Set B;
	public static String E = "1";

	public DefaultMessagePacker() {
		D = new SortHashMap(32);
		C = null;
		F = null;
		G = null;
		I = null;
		H = null;
		B = null;
	}

	protected void G() {
		if (buf == null)
			buf = new ByteBuffer(10240);
		buf.clear();
		D.clear();
		C = null;
		F = null;
		B = null;
	}

	public byte[] pack() {
		if (null == message)
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "message" }));
		if (null == messageBean)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "messageBean" }));
		G();
		if (0 == variableCache.size())
			loadVariable();
		if (null != getParentBean())
			messageBean.setParent(getParentBean());
		messageBean.setMetadata(message);
		if (message.getPrePackEvent() != null)
			A("pre-pack", message.getPrePackEvent());
		F();
		Iterator iterator = D.entrySet().iterator();
		Object obj = null;
		Object obj1 = null;
		do {
			if (!iterator.hasNext())
				break;
			java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
			byte abyte0[] = (byte[]) (byte[]) entry.getValue();
			if (abyte0 != null)
				buf.append(abyte0);
		} while (true);
		if (message.getPostPackEvent() != null)
			A("post-pack", message.getPostPackEvent());
		return buf.toBytes();
	}

	protected void A(String s, String s1) {
		BeanShellEngine beanshellengine = new BeanShellEngine();
		beanshellengine.set("messagePacker", this);
		beanshellengine.set("message", message);
		beanshellengine.set("messageBean", messageBean);
		beanshellengine.set("messageBuf", buf);
		if (0 != variableCache.size()) {
			Iterator iterator = variableCache.keySet().iterator();
			Object obj = null;
			String s2;
			for (; iterator.hasNext(); beanshellengine.set(s2, variableCache.get(s2)))
				s2 = (String) iterator.next();

		}
		beanshellengine.eval(s1);
		buf = (ByteBuffer) beanshellengine.get("messageBuf");
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
		beanshellengine.set("messagePacker", this);
		beanshellengine.set("message", message);
		beanshellengine.set("messageBean", messageBean);
		beanshellengine.set("messageBuf", buf);
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

	protected void B(Field field, String s, String s1, int i, int j) {
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

	protected SortHashMap F() {
		Iterator iterator = message.getFields().values().iterator();
		Object obj = null;
		Field field;
		for (; iterator.hasNext(); E(field))
			field = (Field) iterator.next();

		D();
		E();
		B();
		H();
		C();
		return D;
	}

	protected void D() {
		if (G != null && G.size() > 0) {
			Field afield[] = new Field[G.size()];
			G.toArray(afield);
			for (int i = afield.length - 1; i >= 0; i--)
				C(afield[i]);

		}
	}

	protected void E() {
		if (I != null && I.size() > 0) {
			Field afield[] = new Field[I.size()];
			I.toArray(afield);
			for (int i = afield.length - 1; i >= 0; i--)
				B(afield[i]);

		}
	}

	protected void B(Field field) {
		if (B != null && B.contains(field.getName()))
			return;
		Object obj = ClassUtil.getBeanValueByPath(messageBean, field.getName());
		BeanShellEngine beanshellengine = new BeanShellEngine();
		String s = null;
		beanshellengine.set("messageBean", messageBean);
		if (0 != variableCache.size()) {
			Iterator iterator = variableCache.keySet().iterator();
			Object obj2 = null;
			String s1;
			for (; iterator.hasNext(); beanshellengine.set(s1, variableCache.get(s1)))
				s1 = (String) iterator.next();

		}
		Object obj1 = beanshellengine.eval(field.getExpression());
		if (obj1 != null)
			s = obj1.toString();
		if (0 != variableCache.size()) {
			Iterator iterator1 = variableCache.keySet().iterator();
			Object obj3 = null;
			String s2;
			for (; iterator1.hasNext(); variableCache.put(s2, beanshellengine.get(s2)))
				s2 = (String) iterator1.next();

		}
		ValueRange valuerange = (ValueRange) field.getValueRange().get(s);
		if (null == valuerange)
			valuerange = (ValueRange) field.getValueRange().get("default-ref");
		Message message = valuerange.getReference();
		Field field1 = field.copy();
		field1.setReference(message);
		if (2004 == field.getFieldType())
			D(field1, obj);
		else
			E(field1, obj);
	}

	protected void C(Field field) {
		if (B != null && B.contains(field.getName()))
			return;
		Object obj = ClassUtil.getBeanValueByPath(messageBean, field.getName());
		String as[] = field.getReferenceId().split("\\.");
		Message message = this.message;
		if (as.length > 1) {
			MessageBean messagebean = messageBean;
			for (int i = 0; i < as.length - 1; i++)
				messagebean = messagebean.getParent();

			message = messagebean.getMetadata();
		}
		Field field1 = message.getField(as[as.length - 1]);
		Object obj1 = ClassUtil.getBeanValueByPath(messageBean, field.getReferenceId());
		ValueRange valuerange = (ValueRange) field1.getValueRange().get(obj1);
		if (null == valuerange)
			valuerange = (ValueRange) field1.getValueRange().get("default-ref");
		Message message1 = valuerange.getReference();
		Field field2 = field.copy();
		field2.setReference(message1);
		if (2004 == field2.getFieldType())
			D(field2, obj);
		else
			E(field2, obj);
	}

	protected void C() {
		if (H != null && H.size() > 0) {
			Field afield[] = new Field[H.size()];
			H.toArray(afield);
			for (int i = afield.length - 1; i >= 0; i--)
				A(afield[i]);

		}
	}

	protected void A(Field field) {
		ByteBuffer bytebuffer;
		label0: {
			Object obj = null;
			boolean flag = false;
			Object obj1 = null;
			boolean flag2 = false;
			bytebuffer = new ByteBuffer(1024);
			boolean flag3 = false;
			if (null != field.getStartField()) {
				Iterator iterator = message.getFields().values().iterator();
				boolean flag1 = false;
				Field field1;
				do {
					do {
						if (!iterator.hasNext())
							break label0;
						field1 = (Field) iterator.next();
					} while (!flag1 && !field1.equalTo(field.getStartField()));
					flag1 = true;
					byte abyte0[] = (byte[]) (byte[]) D.get(field1.getName());
					if (abyte0 != null && !field1.getName().equals(field.getName())) {
						int i = 0;
						int k = abyte0.length;
						if (null == field1.getExtendAttribute("remove_prefix")
								|| !"no".equalsIgnoreCase(field1.getExtendAttribute("remove_prefix"))) {
							if (null != field1.getPrefix()) {
								k -= field1.getPrefix().length;
								i = field1.getPrefix().length;
							}
							if (null != field1.getSuffix())
								k -= field1.getSuffix().length;
						}
						if (k > 0) {
							byte abyte2[] = new byte[k];
							System.arraycopy(abyte0, i, abyte2, 0, k);
							bytebuffer.append(abyte2);
						}
					}
				} while (!field1.equalTo(field.getEndField()));
				flag1 = false;
			} else {
				Iterator iterator1 = field.getMacFldDataCache().values().iterator();
				Object obj2 = null;
				do {
					if (!iterator1.hasNext())
						break;
					String s = (String) iterator1.next();
					byte abyte1[] = (byte[]) (byte[]) D.get(s);
					Field field2 = message.getField(s);
					if (null != abyte1) {
						int j = 0;
						int l = abyte1.length;
						if (null == field2.getExtendAttribute("remove_prefix")
								|| !"no".equalsIgnoreCase(field2.getExtendAttribute("remove_prefix"))) {
							if (null != field2.getPrefix()) {
								l -= field2.getPrefix().length;
								j = field2.getPrefix().length;
							}
							if (null != field2.getSuffix())
								l -= field2.getSuffix().length;
						}
						if (l > 0) {
							byte abyte5[] = new byte[l];
							System.arraycopy(abyte1, j, abyte5, 0, l);
							bytebuffer.append(abyte5);
						}
					}
				} while (true);
			}
		}
		if ("java".equalsIgnoreCase(field.getCalcType())) {
			MacCalculator maccalculator = (MacCalculator) ClassUtil
					.createClassInstance(field.getCombineOrTableFieldClassName());
			byte abyte3[] = maccalculator.calcMac(bytebuffer.toBytes());
			byte abyte6[] = new byte[field.getLength()];
			if (field.getLength() <= abyte3.length) {
				System.arraycopy(abyte3, 0, abyte6, 0, field.getLength());
			} else {
				System.arraycopy(abyte3, 0, abyte6, 0, abyte3.length);
				for (int i1 = abyte3.length; i1 < field.getLength(); i1++)
					abyte6[i1] = 0;

			}
			D.put(field.getName(), abyte6);
		} else {
			try {
				MessageDigest messagedigest = MessageDigest.getInstance(field.getCalcType());
				messagedigest.update(bytebuffer.toBytes());
				byte abyte4[] = messagedigest.digest();
				byte abyte7[] = new byte[field.getLength()];
				if (field.getLength() <= abyte4.length) {
					System.arraycopy(abyte4, 0, abyte7, 0, field.getLength());
				} else {
					System.arraycopy(abyte4, 0, abyte7, 0, abyte4.length);
					for (int j1 = abyte4.length; j1 < field.getLength(); j1++)
						abyte7[j1] = 0;

				}
				D.put(field.getName(), abyte7);
			} catch (NoSuchAlgorithmException nosuchalgorithmexception) {
				nosuchalgorithmexception.printStackTrace();
				ExceptionUtil.throwActualException(nosuchalgorithmexception);
			}
		}
	}

	protected void H() {
		if (F != null && F.size() > 0) {
			Field afield[] = new Field[F.size()];
			F.toArray(afield);
			for (int i = afield.length - 1; i >= 0; i--)
				D(afield[i]);

		}
	}

	protected void D(Field field) {
		int i = 0;
		Iterator iterator = message.getFields().values().iterator();
		Object obj = null;
		boolean flag = false;
		Object obj1 = null;
		do {
			if (!iterator.hasNext())
				break;
			Field field1 = (Field) iterator.next();
			if (!flag && !field1.equalTo(field.getStartField()))
				continue;
			flag = true;
			byte abyte0[] = (byte[]) (byte[]) D.get(field1.getName());
			if (abyte0 != null)
				i += abyte0.length;
			else if (field1.equalTo(field)) {
				int j = field.getLength();
				if ((3001 == field.getDataType() || 3011 == field.getDataType()) && 4001 == field.getDataEncoding())
					j = j % 2 != 0 ? j / 2 + 1 : j / 2;
				i += j;
			}
			if (!field1.equalTo(field.getEndField()))
				continue;
			flag = false;
			break;
		} while (true);
		Object obj2 = A(field.getDataType(), i);
		byte abyte1[] = A(field.getDataType(), field.getDataEncoding(), field.getLength(), field.getPadding(),
				field.getPaddingDirection(), obj2, field);
		D.put(field.getName(), abyte1);
	}

	protected Object A(int i, int j) {
		Object obj = null;
		switch (i) {
		case 3003:
		case 3007:
			obj = Integer.valueOf(j);
			break;

		case 3005:
		case 3008:
			obj = Short.valueOf((short) j);
			break;

		case 3004:
			obj = Byte.valueOf((byte) j);
			break;

		case 3001:
			obj = Integer.toString(j);
			break;

		case 3009:
			obj = Long.valueOf(j);
			break;

		case 3002:
		case 3006:
		default:
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("dataType.unsupport",
					new String[] { (new StringBuilder()).append("").append(i).toString() }));
		}
		return obj;
	}

	protected int C(Field field, Object obj) {
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
			throw new CommonException(
					MultiLanguageResourceBundle.getInstance().getString("parseIntValue.dataType.unsupport",
							new String[] { (new StringBuilder()).append("").append(field.getDataType()).toString(),
									(new StringBuilder()).append("").append(field.getDataType()).toString() }));
		}
		return i;
	}

	protected void B() {
		Object obj = null;
		Object obj1 = null;
		Object obj2 = null;
		Object obj3 = null;
		boolean flag = false;
		if (C != null && C.size() > 0) {
			if (C.size() > 1) {
				Field field = (Field) C.get(1);
				int i;
				if (3000 == field.getDataType())
					i = field.getLength() / 2;
				else
					i = field.getLength();
				byte abyte0[] = new byte[i];
				for (int k = 0; k < abyte0.length; k++)
					abyte0[k] = 0;

				boolean flag1 = false;
				Iterator iterator1 = message.getFields().values().iterator();
				do {
					if (!iterator1.hasNext())
						break;
					Field field2 = (Field) iterator1.next();
					if (field2.getIso8583_no() > 64 && field2.getIso8583_no() < 129) {
						byte abyte2[] = (byte[]) (byte[]) D.get(field2.getName());
						if (abyte2 != null && abyte2.length > 0) {
							CodeUtil.setBit(abyte0, field2.getIso8583_no() - 64, 1);
							flag1 = true;
						}
					}
				} while (true);
				if (flag1 || field.getIso8583_no() < 0)
					if (3000 == field.getDataType())
						D.put(field.getName(), CodeUtil.BytetoHex(abyte0));
					else
						D.put(field.getName(), abyte0);
			}
			Field field1 = (Field) C.get(0);
			int j;
			if (3000 == field1.getDataType())
				j = field1.getLength() / 2;
			else
				j = field1.getLength();
			byte abyte1[] = new byte[j];
			for (int l = 0; l < abyte1.length; l++)
				abyte1[l] = 0;

			Iterator iterator = message.getFields().values().iterator();
			do {
				if (!iterator.hasNext())
					break;
				Field field3 = (Field) iterator.next();
				if (field3.getIso8583_no() > 0 && field3.getIso8583_no() < 65) {
					byte abyte3[] = (byte[]) (byte[]) D.get(field3.getName());
					if (abyte3 != null && abyte3.length > 0)
						CodeUtil.setBit(abyte1, field3.getIso8583_no(), 1);
				}
			} while (true);
			if (3000 == field1.getDataType())
				D.put(field1.getName(), CodeUtil.BytetoHex(abyte1));
			else
				D.put(field1.getName(), abyte1);
		}
	}

	protected void E(Field field) {
		if (B != null && B.contains(field.getName()))
			return;
		Object obj = ClassUtil.getBeanAttributeValue(messageBean, field.getName());
		if (null == obj && field.getIso8583_no() > 0) {
			if (2006 == field.getFieldType()) {
				D.put(field.getName(), null);
				if (C == null)
					C = new ArrayList<>();
				C.add(field);
			}
			return;
		}
		if (field.getIso8583_no() > 0 && (obj instanceof MessageBean) && ((MessageBean) obj).isNull())
			return;
		if (field.getIso8583_no() > 0 && E == "0")
			if (obj instanceof String) {
				if (((String) obj).length() == 0)
					return;
			} else if ((obj instanceof byte[]) && ((byte[]) (byte[]) obj).length == 0)
				return;
		if ("dynamic".equalsIgnoreCase(field.getReferenceType())) {
			D.put(field.getName(), null);
			if (null == G)
				G = new ArrayList<>();
			G.add(field);
		} else if ("expression".equalsIgnoreCase(field.getReferenceType())) {
			D.put(field.getName(), null);
			if (null == I)
				I = new ArrayList<>();
			I.add(field);
		} else {
			if (field.getPrePackEvent() != null)
				A(field, "pre-pack", field.getPrePackEvent());
			switch (field.getFieldType()) {
			case 2000:
				F(field, obj);
				break;

			case 2001:
				I(field, obj);
				break;

			case 2002:
			case 2008:
				E(field, obj);
				break;

			case 2003:
			case 2009:
				H(field, obj);
				break;

			case 2004:
			case 2011:
				D(field, obj);
				break;

			case 2006:
				D.put(field.getName(), null);
				if (C == null)
					C = new ArrayList<>();
				C.add(field);
				break;

			case 2007:
				D.put(field.getName(), new byte[field.getLength()]);
				if (F == null)
					F = new ArrayList<>();
				F.add(field);
				break;

			case 2010:
				D.put(field.getName(), new byte[field.getLength()]);
				if (H == null)
					H = new ArrayList<>();
				H.add(field);
				break;

			case 2005:
				G(field, obj);
				break;

			default:
				throw new CommonException(MultiLanguageResourceBundle.getInstance().getString(
						"field.fieldType.notExist",
						new String[] { (new StringBuilder()).append("").append(field.getFieldType()).toString() }));
			}
			if (field.getPostPackEvent() != null)
				A(field, "post-pack", field.getPostPackEvent());
		}
	}

	protected void G(Field field, Object obj) {
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
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString(
					"DefaultMessagePacker.packTableRowNumField.dataType.unsupport", new String[] { field.getName(),
							(new StringBuilder()).append("").append(field.getDataType()).toString() }));
		}
		byte abyte0[] = A(field.getDataType(), field.getDataEncoding(), field.getLength(), field.getPadding(),
				field.getPaddingDirection(), obj1, field);
		D.put(field.getName(), abyte0);
	}

	protected void D(Field field, Object obj) {
		List list = (List) obj;
		ByteBuffer bytebuffer = new ByteBuffer(1024);
		if (null != field.getTabPrefix())
			bytebuffer.append(field.getTabPrefix());
		Object obj1 = null;
		for (int i = 0; i < list.size(); i++) {
			if (null != field.getPreRowPackEvent())
				B(field, "row-pre-pack", field.getPreRowPackEvent(), list.size(), i);
			if (null != field.getPrefix() && (i > 0 || i == 0 && field.isFirRowPrefix()))
				bytebuffer.append(field.getPrefix());
			byte abyte0[] = B(field, list.get(i));
			bytebuffer.append(abyte0);
			if (null != field.getSuffix() && (i < list.size() - 1 || i + 1 == list.size() && field.isLastRowSuffix()))
				bytebuffer.append(field.getSuffix());
			if (null != field.getPostRowPackEvent())
				B(field, "row-post-pack", field.getPostRowPackEvent(), list.size(), i);
		}

		if (null != field.getTabSuffix())
			bytebuffer.append(field.getTabSuffix());
		D.put(field.getName(), bytebuffer.toBytes());
	}

	protected void H(Field field, Object obj) {
		byte abyte0[] = B(field, obj);
		if (field.getRefLengthField() == null)
			abyte0 = A(field, abyte0, obj);
		D.put(field.getName(), abyte0);
	}

	protected void E(Field field, Object obj) {
		byte abyte0[] = B(field, obj);
		D.put(field.getName(), abyte0);
	}

	protected byte[] B(Field field, Object obj) {
		MessageBean messagebean = null;
		if (null == obj)
			messagebean = (MessageBean) ClassUtil.createClassInstance(field.getReference().getClassName());
		else
			messagebean = (MessageBean) obj;
		Message message = field.getReference();
		if (message == null) {
			message = new Message();
			message.setId(
					(new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
			message.setShortText(field.getShortText());
			message.setFields(field.getSubFields());
		}
		AbstractMessagePacker abstractmessagepacker = MessagePackerFactory.A(message);
		abstractmessagepacker.setVariableCache(variableCache);
		abstractmessagepacker.setMessage(message);
		abstractmessagepacker.setMessageBean(messagebean);
		abstractmessagepacker.setParentBean(messageBean);
		byte abyte0[] = abstractmessagepacker.pack();
		String s = field.getDataCharset();
		if (null == s)
			s = this.message.getMsgCharset();
		if (null == s)
			s = getDefaultCharset();
		if (1000 != this.message.getType() && null != s)
			try {
				abyte0 = (new String(abyte0, s)).getBytes();
			} catch (UnsupportedEncodingException unsupportedencodingexception) {
				throw new CommonException(MultiLanguageResourceBundle.getInstance()
						.getString("field.encoding.unsupport", new String[] { field.getName(), s }));
			}
		return abyte0;
	}

	protected void I(Field field, Object obj) {
		byte abyte0[] = A(field.getDataType(), field.getDataEncoding(), field.getLength(), field.getPadding(), 5000,
				obj, field);
		if (field.getRefLengthField() == null)
			abyte0 = A(field, abyte0, obj);
		D.put(field.getName(), abyte0);
	}

	protected byte[] A(Field field, byte abyte0[], Object obj) {
		int i = 0;
		if (4001 == field.getDataEncoding()) {
			if (field.getDataType() == 3000 || field.getDataType() == 3001 || field.getDataType() == 3006
					|| field.getDataType() == 3010 || field.getDataType() == 3011) {
				if (null == obj)
					i = 0;
				else
					i = ((String) obj).getBytes().length;
				if (field.getPrefix() != null)
					i += field.getPrefix().length;
				if (field.getSuffix() != null)
					i += field.getSuffix().length;
			} else {
				i = abyte0.length * 2;
			}
		} else {
			i = abyte0.length;
		}
		Object obj1 = A(field.getLengthFieldDataType(), i);
		Field field1 = field.copy();
		field1.setPrefix(null);
		field1.setSuffix(null);
		byte abyte1[] = A(field1.getLengthFieldDataType(), field1.getLengthFieldDataEncoding(),
				field1.getLengthFieldLength(), (byte) 48, 5001, obj1, field1);
		byte abyte2[] = abyte0;
		abyte0 = new byte[abyte1.length + abyte2.length];
		System.arraycopy(abyte1, 0, abyte0, 0, abyte1.length);
		System.arraycopy(abyte2, 0, abyte0, abyte1.length, abyte2.length);
		return abyte0;
	}

	protected void F(Field field, Object obj) {
		int i = 0;
		if (-1 == field.getLength()) {
			Object obj1 = ClassUtil.getBeanValueByPath(messageBean, field.getLengthScript());
			String as[] = field.getLengthScript().split("\\.");
			Message message = this.message;
			if (as.length > 1) {
				MessageBean messagebean = messageBean;
				for (int j = 0; j < as.length - 1; j++)
					messagebean = messagebean.getParent();

				message = messagebean.getMetadata();
			}
			Field field1 = message.getField(as[as.length - 1]);
			i = C(field1, obj1);
		} else {
			i = field.getLength();
		}
		byte abyte0[] = A(field.getDataType(), field.getDataEncoding(), i, field.getPadding(),
				field.getPaddingDirection(), obj, field);
		D.put(field.getName(), abyte0);
	}

	protected byte[] A(int i, int j, int k, byte byte0, int l, Object obj, Field field) {
		byte abyte0[] = null;
		switch (i) {
		case 3000:
		case 3001:
		case 3006:
		case 3010:
		case 3011:
			if (null == obj) {
				abyte0 = "".getBytes();
			} else {
				String s = field.getDataCharset();
				if (null == s)
					s = message.getMsgCharset();
				if (null == s)
					s = getDefaultCharset();
				if (null != s)
					try {
						abyte0 = ((String) obj).getBytes(s);
					} catch (UnsupportedEncodingException unsupportedencodingexception) {
						throw new CommonException(MultiLanguageResourceBundle.getInstance()
								.getString("field.encoding.unsupport", new String[] { field.getName(), s }));
					}
				else
					abyte0 = ((String) obj).getBytes();
			}
			if (null != field.getExtendAttribute("replace_all")) {
				String as[] = field.getExtendAttribute("replace_all").split("\\|");
				Object obj1 = null;
				for (int i1 = 0; i1 < as.length; i1++) {
					String as1[] = as[i1].split("=");
					if (2 != as1.length)
						throw new CommonException((new StringBuilder()).append("field[@name=").append(field.getName())
								.append("]'s extended-attributes[@key=").append("replace_all")
								.append("] pattern error. pattern : hex=hex;hex=hex!").toString());
					abyte0 = CodeUtil.replaceAll(abyte0, CodeUtil.HextoByte(as1[0].trim()),
							CodeUtil.HextoByte(as1[1].trim()), 0, abyte0.length);
				}

			}
			abyte0 = A(k, byte0, l, abyte0);
			if (4001 == j) {
				if (null != field.getExtendedAttributeText()) {
					Map map = field.getExtendedAttributes();
					if (null != map.get("padding") && abyte0.length % 2 != 0) {
						byte abyte1[] = new byte[abyte0.length + 1];
						if ("left".equalsIgnoreCase((String) map.get("padding-direction"))) {
							abyte1[0] = ((String) map.get("padding")).getBytes()[0];
							System.arraycopy(abyte0, 0, abyte1, 1, abyte0.length);
						} else {
							System.arraycopy(abyte0, 0, abyte1, 0, abyte0.length);
							abyte1[abyte1.length - 1] = CodeUtil.HextoByte((String) map.get("padding"))[0];
						}
						abyte0 = abyte1;
					}
				}
				abyte0 = CodeUtil.ASCtoBCD(abyte0);
			}
			if (field.getPrefix() == null && field.getSuffix() == null)
				break;
			ByteBuffer bytebuffer = new ByteBuffer(abyte0.length + 10 <= 16 ? abyte0.length + 10 : 16);
			if (field.getPrefix() != null)
				bytebuffer.append(field.getPrefix());
			bytebuffer.append(abyte0);
			if (field.getSuffix() != null)
				bytebuffer.append(field.getSuffix());
			abyte0 = bytebuffer.toBytes();
			break;

		case 3002:
			if (null == obj)
				abyte0 = new byte[0];
			else
				abyte0 = (byte[]) (byte[]) obj;
			break;

		case 3003:
			abyte0 = CodeUtil.IntToBytes(((Integer) obj).intValue());
			break;

		case 3004:
			abyte0 = new byte[1];
			abyte0[0] = ((Byte) obj).byteValue();
			break;

		case 3005:
			abyte0 = CodeUtil.ShortToBytes(((Short) obj).shortValue());
			break;

		case 3007:
			abyte0 = CodeUtil.htoni(((Integer) obj).intValue());
			break;

		case 3008:
			abyte0 = CodeUtil.htons(((Short) obj).shortValue());
			break;

		case 3009:
			abyte0 = CodeUtil.LongToBytes(((Long) obj).longValue());
			break;

		default:
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("dataType.unsupport",
					new String[] { (new StringBuilder()).append("").append(i).toString() }));
		}
		return abyte0;
	}

	protected byte[] A(int i, byte byte0, int j, byte abyte0[]) {
		if (5000 == j)
			return abyte0;
		byte abyte1[] = abyte0;
		if (abyte0.length < i) {
			abyte1 = new byte[i];
			if (5001 == j) {
				for (int k = 0; k < i - abyte0.length; k++)
					abyte1[k] = byte0;

				System.arraycopy(abyte0, 0, abyte1, i - abyte0.length, abyte0.length);
			} else {
				System.arraycopy(abyte0, 0, abyte1, 0, abyte0.length);
				for (int l = abyte0.length; l < i; l++)
					abyte1[l] = byte0;

			}
		}
		return abyte1;
	}

	public void ignore(Field field) {
		ignore(field.getName());
	}

	public void ignore(String s) {
		if (null == B)
			B = new HashSet(32);
		B.add(s);
	}

	static {
		try {
			if (FileUtil.isExist(ConfigManager.getFullPathFileName("messagebean.properties"))) {
				Properties properties = ConfigManager.loadProperties("messagebean.properties");
				if (null != properties.getProperty("oper_blank") && 0 != properties.getProperty("oper_blank").length()
						&& "0".equalsIgnoreCase(properties.getProperty("oper_blank")))
					E = "0";
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			ExceptionUtil.throwActualException(exception);
		}
	}
}
