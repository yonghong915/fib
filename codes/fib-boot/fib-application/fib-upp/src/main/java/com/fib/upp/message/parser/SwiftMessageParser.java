package com.fib.upp.message.parser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.fib.commons.exception.CommonException;
import com.fib.commons.util.SortHashMap;
import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.metadata.Field;
import com.fib.upp.message.metadata.Message;
import com.fib.upp.util.ByteBuffer;
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
public class SwiftMessageParser extends DefaultMessageParser {

	private Map<String, Field> K;
	public static final byte[] J = { 13, 10 };

	public SwiftMessageParser() {
		K = null;
	}

	@Override
	protected int parse(int i) {
		byte[] abyte0 = message.getPrefix();
		int j = CodeUtil.byteArrayIndexOf(messageData, abyte0, i);
		if (-1 == j)
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("parse.locatePrefix.failed", new String[] {
					new String(CodeUtil.bytetoHex(message.getPrefix())), (new StringBuilder()).append("").append(i).toString(), "-1" }));
		if (j != i)
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("parse.locatePrefix.failed",
					new String[] { new String(CodeUtil.bytetoHex(message.getPrefix())), (new StringBuilder()).append("").append(i).toString(),
							(new StringBuilder()).append("").append(j).toString() }));
		byte[] abyte1 = message.getSuffix();
		int k = CodeUtil.byteArrayIndexOf(messageData, abyte1, j + abyte0.length);
		if (-1 == k)
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("parse.locateSuffix.failed",
					new String[] { new String(CodeUtil.bytetoHex(message.getSuffix())), (new StringBuilder()).append("").append(i).toString(),
							(new StringBuilder()).append("").append(j).toString() }));
		byte[] abyte2 = new byte[k - j - abyte0.length];
		System.arraycopy(messageData, j + abyte0.length, abyte2, 0, abyte2.length);
		B();
		String s = null;
		if (null != message.getMsgCharset())
			try {
				s = new String(abyte2, message.getMsgCharset());
			} catch (UnsupportedEncodingException unsupportedencodingexception) {
				unsupportedencodingexception.printStackTrace();
				throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport",
						new String[] { message.getId(), message.getMsgCharset() }));
			}
		else
			s = new String(abyte2);
		String[] as = s.split(":");
		for (int l = 1; l < as.length; l++) {
			String s1 = as[l];
			String s2;
			if (++l < as.length)
				s2 = as[l];
			else
				s2 = "";
			Field field = (Field) K.get(s1);
			if (null == field)
				throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("parse.tag.field.isNotExist", new String[] { s1 }));
			if (2000 == field.getFieldType()) {
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), B(field, s2));
				continue;
			}
			if (2002 == field.getFieldType())
				ClassUtil.setBeanAttributeValue(messageBean, field.getName(), A(field, s2));
			else
				throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("SwiftMessageParser.parse.field.illegalType"));
		}

		return k + abyte1.length;
	}

	protected String B(Field field, String s) {
		if (null == field.getExtendAttribute("remove_crlf") || !field.getExtendAttribute("remove_crlf").equalsIgnoreCase("NO"))
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
		byte[] abyte0 = s.getBytes();
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
		int i = 0;
		int j = 0;
		SortHashMap<String, Field> sorthashmap = null;
		Message message = field.getReference();
		if (null != message)
			sorthashmap = message.getFields();
		else
			sorthashmap = field.getSubFields();
		int k = sorthashmap.size();
		for (int l = 0; l < k; l++) {
			Field field1 = sorthashmap.get(l);
			Field field2 = null;
			do {
				if (null != field2 || l >= k - 1)
					break;
				l++;
				field2 = sorthashmap.get(l);
				j = s.indexOf(field2.getTag(), i);
				if (field2.getTag().equals(field1.getTag()))
					j = s.indexOf(field2.getTag(), j + field2.getTag().length());
				if (-1 != j)
					break;
				if (field2.isRequired())
					throw new CommonException(MultiLanguageResourceBundle.getInstance().getString(
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
					throw new CommonException(
							MultiLanguageResourceBundle.getInstance().getString("SwiftMessageParser.parse.field.subField.illegalType"));
			} else if (field1.isRequired())
				throw new CommonException(
						MultiLanguageResourceBundle.getInstance().getString("SwiftMessageParser.parse.subField.notOptional.and.canNotBeMatched",
								new String[] { field1.getName(), field1.getTag(), field.getName(), field.getTag(), s }));
			if (field2 != null)
				l--;
		}

		return messagebean;
	}

	protected void A(MessageBean messagebean, Field field, LinkedList<String> linkedlist) {
		String s = (String) ClassUtil.getBeanValueByPath(messagebean, field.getRowNumFieldName());
		int i = Integer.parseInt(s);
		SortHashMap<String, Field> sorthashmap = null;
		Message message = field.getReference();
		if (null != message)
			sorthashmap = (SortHashMap<String, Field>) message.getFields();
		else
			sorthashmap = (SortHashMap<String, Field>) field.getSubFields();
		int j = sorthashmap.size();
		String s2 = (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
		for (int k = 0; k < i; k++) {
			if (null != field.getPreRowParseEvent())
				exeShell(field, "row-pre-parse", field.getPreRowParseEvent(), null, null, i, k);
			String s1 = linkedlist.removeFirst();
			String[] as = s1.split("/");
			if (as.length < j)
				throw new CommonException(
						MultiLanguageResourceBundle.getInstance().getString("SwiftMessageParser.parseTable.fieldNum.greatThan.columnNumber",
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
						throw new CommonException(MultiLanguageResourceBundle.getInstance().getString(
								"SwiftMessageParser.parseTable.field.isNotOptional.and.valueIsNull",
								new String[] { field.getName(), field1.getName(), s1 }));
					continue;
				}
				if (2004 == field1.getFieldType())
					A(messagebean1, field1, linkedlist);
				else
					throw new CommonException(
							MultiLanguageResourceBundle.getInstance().getString("SwiftMessageParser.parseTable.field.tableField.illegalType"));
			}

			if (null != field.getPostRowParseEvent())
				exeShell(field, "row-post-parse", field.getPostRowParseEvent(), messagebean1, null, i, k);
		}

	}

	protected LinkedList<String> B(String paramString) {
		LinkedList<String> localLinkedList = new LinkedList<>();
		paramString = paramString.trim();
		String[] arrayOfString = paramString.split("\r\n");
		String str = null;
		for (int i = arrayOfString.length - 1; i >= 0; i--) {
			if (str != null) {
				int tmp50_48 = i;
				String[] tmp50_47 = arrayOfString;
				tmp50_47[tmp50_48] = (tmp50_47[tmp50_48] + str);
			}
			if (arrayOfString[i].startsWith("//")) {
				str = arrayOfString[i].substring(2);
			} else {
				str = null;
				localLinkedList.addFirst(arrayOfString[i]);
			}
		}
		return localLinkedList;
	}

	protected MessageBean A(Field field) {
		String s = field.getCombineOrTableFieldClassName();
		if (null == s)
			s = (new StringBuilder()).append(messageBean.getClass().getName()).append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
		MessageBean messagebean = (MessageBean) ClassUtil.createClassInstance(s);
		messagebean.setParent(messageBean);
		return messagebean;
	}

	protected void B() {
		K = new HashMap<>(message.getFields().size());
		Field field;
		for (Iterator<Field> iterator = message.getFields().values().iterator(); iterator.hasNext(); K.put(field.getTag(), field))
			field = iterator.next();

	}

}
