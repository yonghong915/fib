package com.fib.upp.message.parser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fib.commons.exception.CommonException;
import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.metadata.Field;
import com.fib.upp.message.metadata.Message;
import com.fib.upp.util.ClassUtil;
import com.fib.upp.util.CodeUtil;
import com.fib.upp.util.MultiLanguageResourceBundle;
import com.fib.upp.util.StringUtil;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public class TagMessageParser extends DefaultMessageParser {

	public static final String M = ":";
	private Map<String, Field> L;
	//protected Map variableCache;

	public TagMessageParser() {
		L = null;
		variableCache = new HashMap<>(5);
	}

	protected int parse(int i) {
		messageBean.setMetadata(this.message);
		if (null != getParentBean())
			messageBean.setParent(getParentBean());
		byte abyte0[] = this.message.getPrefix();
		int j = CodeUtil.byteArrayIndexOf(messageData, abyte0, i);
		if (-1 == j)
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("parse.locatePrefix.failed", new String[] {
					new String(CodeUtil.BytetoHex(this.message.getPrefix())), (new StringBuilder()).append("").append(i).toString(), "-1" }));
		if (j != i)
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("parse.locatePrefix.failed",
					new String[] { new String(CodeUtil.BytetoHex(this.message.getPrefix())), (new StringBuilder()).append("").append(i).toString(),
							(new StringBuilder()).append("").append(j).toString() }));
		byte abyte1[] = this.message.getSuffix();
		int k = CodeUtil.byteArrayIndexOf(messageData, abyte1, j + abyte0.length);
		if (-1 == k)
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("parse.locateSuffix.failed",
					new String[] { new String(CodeUtil.BytetoHex(this.message.getSuffix())), (new StringBuilder()).append("").append(i).toString(),
							(new StringBuilder()).append("").append(j).toString() }));
		byte abyte2[] = new byte[k - j - abyte0.length];
		System.arraycopy(messageData, j + abyte0.length, abyte2, 0, abyte2.length);
		C();
		String s = null;
		if (null != this.message.getMsgCharset())
			try {
				s = new String(abyte2, this.message.getMsgCharset());
			} catch (UnsupportedEncodingException unsupportedencodingexception) {
				unsupportedencodingexception.printStackTrace();
				throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport",
						new String[] { this.message.getId(), this.message.getMsgCharset() }));
			}
		else
			s = new String(abyte2);
		String[] as = s.split(":");
		int l1 = 0;
		for (int i2 = 1; i2 < as.length; i2++) {
			String s1 = as[i2];
			String s2;
			if (++i2 < as.length)
				s2 = as[i2];
			else
				s2 = "";
			Field field = L.get(s1);
			if (null == field)
				throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("parse.tag.field.isNotExist", new String[] { s1 }));
			if (field.getPreParseEvent() != null)
				exeShell(field, "pre-parse", field.getPreParseEvent());
			if (2000 == field.getFieldType()) {
				s2 = new String(B(field, s2.getBytes()));
				if (3010 == field.getDataType())
					if (null == s2 || s2.length() == 0)
						s2 = "0";
					else if (field.getPattern().indexOf(",") != -1 && s2.charAt(0) == '.')
						s2 = (new StringBuilder()).append("0").append(s2).toString();
			}
			if (2004 == field.getFieldType()) {
				if (null == s2 || 0 == s2.length())
					continue;
				if ("dynamic".equalsIgnoreCase(field.getReferenceType())) {
					Message message = B(field, this.message, messageBean);
					int k2 = B(field);
					List<MessageBean> arraylist = new ArrayList<>(k2);
					int l2 = 0;
					do {
						if (l2 >= k2)
							break;
						if (null != field.getPreRowParseEvent())
							exeShell(field, "row-pre-parse", field.getPreRowParseEvent(), null, arraylist, k2, l2);
						MessageBean messagebean = (MessageBean) ClassUtil.createClassInstance(message.getClassName());
						String s3 = (new StringBuilder()).append(":").append(s1).append(":").toString();
						int l = CodeUtil.byteArrayIndexOf(abyte2, s3.getBytes(), l1);
						l += s3.getBytes().length;
						l1 = CodeUtil.byteArrayIndexOf(abyte2, ":".getBytes(), l);
						if (-1 == l1)
							l1 = abyte2.length;
						byte abyte3[] = new byte[l1 - l];
						System.arraycopy(abyte2, l, abyte3, 0, abyte3.length);
						i = B(field, message, messagebean, 0, abyte3);
						arraylist.add(messagebean);
						if (null != field.getPostRowParseEvent())
							exeShell(field, "row-post-parse", field.getPostRowParseEvent(), messagebean, arraylist, k2, l2);
						if (++l2 < k2) {
							i2 += 2;
							if (i2 < as.length)
								s2 = as[i2];
						}
					} while (true);
					l1 = 0;
					ClassUtil.setBeanAttributeValue(messageBean, field.getName(), arraylist, List.class);
				} else {
					int j2 = B(field);
					Message message3 = field.getReference();
					if (null == message3) {
						message3 = new Message();
						message3.setId((new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
						message3.setShortText(field.getShortText());
						message3.setFields(field.getSubFields());
					}
					String s8 = (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
					int i3 = 0;
					do {
						if (i3 >= j2)
							break;
						if (null != field.getPreRowParseEvent())
							exeShell(field, "row-pre-parse", field.getPreRowParseEvent(), null, null, j2, i3);
						MessageBean messagebean1 = (MessageBean) ClassUtil.invoke(messageBean, s8, null, null);
						String s4 = (new StringBuilder()).append(":").append(s1).append(":").toString();
						int i1 = CodeUtil.byteArrayIndexOf(abyte2, s4.getBytes(), l1);
						i1 += s4.getBytes().length;
						l1 = CodeUtil.byteArrayIndexOf(abyte2, ":".getBytes(), i1);
						if (-1 == l1)
							l1 = abyte2.length;
						byte abyte4[] = new byte[l1 - i1];
						System.arraycopy(abyte2, i1, abyte4, 0, abyte4.length);
						i = B(field, message3, messagebean1, 0, abyte4);
						if (++i3 < j2) {
							i2 += 2;
							if (i2 < as.length)
								s2 = as[i2];
						}
						if (null != field.getPostRowParseEvent())
							exeShell(field, "row-post-parse", field.getPostRowParseEvent(), messagebean1, null, j2, i3);
					} while (true);
					l1 = 0;
				}
			} else if (2002 == field.getFieldType() || 2008 == field.getFieldType()) {
				if (null == s2 || 0 == s2.length())
					continue;
				if ("dynamic".equalsIgnoreCase(field.getReferenceType())) {
					Message message1 = B(field, this.message, messageBean);
					String s5 = (new StringBuilder()).append(":").append(s1).append(":").toString();
					int j1 = CodeUtil.byteArrayIndexOf(abyte2, s5.getBytes(), l1);
					j1 += s5.getBytes().length;
					l1 = CodeUtil.byteArrayIndexOf(abyte2, ":".getBytes(), j1);
					if (-1 == l1)
						l1 = abyte2.length;
					byte abyte5[] = new byte[l1 - j1];
					System.arraycopy(abyte2, j1, abyte5, 0, abyte5.length);
					i = B(field, messageBean, message1, 0, abyte5);
					j1 = 0;
					l1 = 0;
				} else {
					Message message2 = field.getReference();
					if (null == message2) {
						message2 = new Message();
						message2.setId((new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
						message2.setShortText(field.getShortText());
						message2.setFields(field.getSubFields());
					}
					String s7 = (new StringBuilder()).append("get").append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
					MessageBean messagebean2 = (MessageBean) ClassUtil.invoke(messageBean, s7, null, null);
					String s6 = (new StringBuilder()).append(":").append(s1).append(":").toString();
					int k1 = CodeUtil.byteArrayIndexOf(abyte2, s6.getBytes(), l1);
					k1 += s6.getBytes().length;
					l1 = CodeUtil.byteArrayIndexOf(abyte2, ":".getBytes(), k1);
					if (-1 == l1)
						l1 = abyte2.length;
					byte abyte6[] = new byte[l1 - k1];
					System.arraycopy(abyte2, k1, abyte6, 0, abyte6.length);
					i = B(field, message2, messagebean2, 0, abyte6);
					k1 = 0;
					l1 = 0;
				}
			} else {
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), s2.trim());
			}
			if (field.getPostParseEvent() != null)
				exeShell(field, "post-parse", field.getPostParseEvent());
		}

		return k + abyte1.length;
	}

	private int B(Field field) {
		int i = 0;
		if (null != field.getRowNumField()) {
			i = parseIntValue(field.getRowNumField(), ClassUtil.getBeanAttributeValue(messageBean, field.getRowNumField().getName()));
		} else {
			Message message = this.message;
			Field field1 = null;
			String as[] = field.getRowNumFieldName().split("\\.");
			int j = 0;
			do {
				if (j >= as.length)
					break;
				field1 = ((Message) message).getField(as[j].trim());
				if (++j < as.length)
					if (null != field1.getReference())
						message = field1.getReference();
					else if (0 != field1.getSubFields().size()) {
						message = new Message();
						((Message) message).setFields(field1.getSubFields());
					}
			} while (true);
			i = parseIntValue(field1, ClassUtil.getBeanValueByPath(messageBean, field.getRowNumFieldName()));
		}
		return i;
	}

	private void C() {
		L = new HashMap<>(message.getFields().size());
		Field field;
		for (Iterator<Field> iterator = message.getFields().values().iterator(); iterator.hasNext(); L.put(field.getTag(), field))
			field = iterator.next();
	}
}
