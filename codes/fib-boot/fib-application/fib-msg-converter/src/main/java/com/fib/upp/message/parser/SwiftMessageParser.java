// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.fib.upp.message.parser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ByteBuffer;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.SortHashMap;
import com.giantstone.common.util.StringUtil;
import com.giantstone.message.bean.MessageBean;
import com.giantstone.message.metadata.Field;
import com.giantstone.message.metadata.Message;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public class SwiftMessageParser extends DefaultMessageParser {

	private Map K;
	public static final byte J[] = { 13, 10 };

	public SwiftMessageParser() {
		K = null;
	}

	protected int parse(int i) {
		byte abyte0[] = message.getPrefix();
		int j = CodeUtil.byteArrayIndexOf(messageData, abyte0, i);
		if (-1 == j)
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("parse.locatePrefix.failed",
					new String[] { new String(CodeUtil.BytetoHex(message.getPrefix())),
							(new StringBuilder()).append("").append(i).toString(), "-1" }));
		if (j != i)
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("parse.locatePrefix.failed",
					new String[] { new String(CodeUtil.BytetoHex(message.getPrefix())),
							(new StringBuilder()).append("").append(i).toString(),
							(new StringBuilder()).append("").append(j).toString() }));
		byte abyte1[] = message.getSuffix();
		int k = CodeUtil.byteArrayIndexOf(messageData, abyte1, j + abyte0.length);
		if (-1 == k)
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("parse.locateSuffix.failed",
					new String[] { new String(CodeUtil.BytetoHex(message.getSuffix())),
							(new StringBuilder()).append("").append(i).toString(),
							(new StringBuilder()).append("").append(j).toString() }));
		byte abyte2[] = new byte[k - j - abyte0.length];
		System.arraycopy(messageData, j + abyte0.length, abyte2, 0, abyte2.length);
		B();
		String s = null;
		if (null != message.getMsgCharset())
			try {
				s = new String(abyte2, message.getMsgCharset());
			} catch (UnsupportedEncodingException unsupportedencodingexception) {
				unsupportedencodingexception.printStackTrace();
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"message.encoding.unsupport", new String[] { message.getId(), message.getMsgCharset() }));
			}
		else
			s = new String(abyte2);
		String as[] = s.split(":");
		Object obj = null;
		Object obj1 = null;
		Object obj2 = null;
		for (int l = 1; l < as.length; l++) {
			String s1 = as[l];
			String s2;
			if (++l < as.length)
				s2 = as[l];
			else
				s2 = "";
			Field field = (Field) K.get(s1);
			if (null == field)
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("parse.tag.field.isNotExist", new String[] { s1 }));
			if (2000 == field.getFieldType()) {
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), B(field, s2));
				continue;
			}
			if (2002 == field.getFieldType())
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), A(field, s2));
			else
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("SwiftMessageParser.parse.field.illegalType"));
		}

		return k + abyte1.length;
	}

	protected String B(Field field, String s) {
		if (null == field.getExtendAttribute("remove_crlf")
				|| !field.getExtendAttribute("remove_crlf").equalsIgnoreCase("NO"))
			s = C(s);
		if (5000 != field.getPaddingDirection())
			s = new String(B(field, s.getBytes()));
		if (3010 == field.getDataType())
			if (null == s || s.length() == 0)
				s = "0";
			else if (field.getPattern().indexOf(",") != -1 && s.charAt(0) == '.')
				s = (new StringBuilder()).append("0").append(s).toString();
		s = new String(s.getBytes());
		return s;
	}

	protected String C(String s) {
		if (null == s)
			return null;
		byte abyte0[] = s.getBytes();
		ByteBuffer bytebuffer = new ByteBuffer(abyte0.length);
		for (int i = 0; i < abyte0.length; i++)
			if (i < abyte0.length - 1 && abyte0[i] == J[0] && abyte0[i + 1] == J[1])
				i++;
			else
				bytebuffer.append(abyte0[i]);

		return new String(bytebuffer.toBytes());
	}

	protected MessageBean A(Field field, String s) {
		MessageBean messagebean = A(field);
		Object obj = null;
		Object obj1 = null;
		int i = 0;
		int j = 0;
		SortHashMap sorthashmap = null;
		Message message = field.getReference();
		if (null != message)
			sorthashmap = message.getFields();
		else
			sorthashmap = field.getSubFields();
		int k = sorthashmap.size();
		for (int l = 0; l < k; l++) {
			Field field1 = (Field) sorthashmap.get(l);
			Field field2 = null;
			do {
				if (null != field2 || l >= k - 1)
					break;
				l++;
				field2 = (Field) sorthashmap.get(l);
				j = s.indexOf(field2.getTag(), i);
				if (field2.getTag().equals(field1.getTag()))
					j = s.indexOf(field2.getTag(), j + field2.getTag().length());
				if (-1 != j)
					break;
				if (field2.isRequired())
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"SwiftMessageParser.parseSubFields.subField.notOptional.and.valueNotInContent",
							new String[] { field2.getName(), field2.getTag(), field.getName(), field.getTag(), s }));
				field2 = null;
			} while (true);
			i = s.indexOf(field1.getTag(), i);
			if (i != -1) {
				i += field1.getTag().length();
				String s1;
				if (field2 != null) {
					s1 = s.substring(i, j);
					i = j;
				} else {
					s1 = s.substring(i);
				}
				if (2000 == field1.getFieldType() || 2005 == field1.getFieldType())
					ClassUtil.setBeanAttributeValue(messagebean, field1.getName(), B(field1, s1));
				else if (2004 == field1.getFieldType())
					A(messagebean, field1, B(s1));
				else
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("SwiftMessageParser.parse.field.subField.illegalType"));
			} else if (field1.isRequired())
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"SwiftMessageParser.parse.subField.notOptional.and.canNotBeMatched",
						new String[] { field1.getName(), field1.getTag(), field.getName(), field.getTag(), s }));
			if (field2 != null)
				l--;
		}

		return messagebean;
	}

	protected void A(MessageBean messagebean, Field field, LinkedList linkedlist) {
		String s = (String) ClassUtil.getBeanValueByPath(messagebean, field.getRowNumFieldName());
		int i = Integer.parseInt(s);
		Object obj = null;
		Object obj1 = null;
		SortHashMap sorthashmap = null;
		Message message = field.getReference();
		if (null != message)
			sorthashmap = message.getFields();
		else
			sorthashmap = field.getSubFields();
		int j = sorthashmap.size();
		Object obj2 = null;
		Object obj3 = null;
		String s2 = (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(field.getName()))
				.toString();
		for (int k = 0; k < i; k++) {
			if (null != field.getPreRowParseEvent())
				exeShell(field, "row-pre-parse", field.getPreRowParseEvent(), null, null, i, k);
			String s1 = (String) linkedlist.removeFirst();
			String as[] = s1.split("/");
			if (as.length < j)
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"SwiftMessageParser.parseTable.fieldNum.greatThan.columnNumber",
						new String[] { field.getName(), (new StringBuilder()).append("").append(j).toString(),
								(new StringBuilder()).append("").append(as.length).toString(), s1 }));
			MessageBean messagebean1 = (MessageBean) ClassUtil.invoke(messagebean, s2, null, null);
			for (int l = 0; l < j; l++) {
				Field field1 = (Field) sorthashmap.get(l);
				if (2000 == field1.getFieldType() || 2005 == field1.getFieldType()) {
					if (l + 1 <= as.length - 1) {
						ClassUtil.setBeanAttributeValue(messagebean1, field1.getName(), B(field1, as[l + 1]));
						continue;
					}
					if (field1.isRequired())
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"SwiftMessageParser.parseTable.field.isNotOptional.and.valueIsNull",
								new String[] { field.getName(), field1.getName(), s1 }));
					continue;
				}
				if (2004 == field1.getFieldType())
					A(messagebean1, field1, linkedlist);
				else
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("SwiftMessageParser.parseTable.field.tableField.illegalType"));
			}

			if (null != field.getPostRowParseEvent())
				exeShell(field, "row-post-parse", field.getPostRowParseEvent(), messagebean1, null, i, k);
		}

	}

	protected LinkedList B(String s) {
		LinkedList linkedlist;
		String as[];
		String s1;
		int i;
		linkedlist = new LinkedList();
		s = s.trim();
		as = s.split("\r\n");
		s1 = null;
		i = as.length - 1;
//_L5:
//		if (i < 0) goto _L2; else goto _L1
//_L1:
//		if (s1 == null) goto _L4; else goto _L3
//_L3:
//		new StringBuilder();
//		as;
//		i;
//		JVM INSTR dup2_x1 ;
//		JVM INSTR aaload ;
//		append();
//		s1;
//		append();
//		toString();
//		JVM INSTR aastore ;
//_L4:
//		if (as[i].startsWith("//"))
//		{
//			s1 = as[i].substring(2);
//		} else
//		{
//			s1 = null;
//			linkedlist.addFirst(as[i]);
//		}
//		i--;
//		  goto _L5
//_L2:
		return linkedlist;
	}

	protected MessageBean A(Field field) {
		String s = field.getCombineOrTableFieldClassName();
		if (null == s)
			s = (new StringBuilder()).append(messageBean.getClass().getName())
					.append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
		MessageBean messagebean = (MessageBean) ClassUtil.createClassInstance(s);
		messagebean.setParent(messageBean);
		return messagebean;
	}

	protected void B() {
		K = new HashMap(message.getFields().size());
		Object obj = null;
		Field field;
		for (Iterator iterator = message.getFields().values().iterator(); iterator.hasNext(); K.put(field.getTag(),
				field))
			field = (Field) iterator.next();

	}

}