// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.fib.msgconverter.message.metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.config.ConfigManager;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.common.util.FileUtil;
import com.giantstone.common.util.SortHashMap;
import com.giantstone.common.util.StringUtil;
import com.giantstone.message.metadata.Constant;
import com.giantstone.message.metadata.Field;
import com.giantstone.message.metadata.Message;
import com.giantstone.message.metadata.ValueRange;

// Referenced classes of package com.giantstone.message.metadata:
//			Message, A, Field, ValueRange, 
//			Constant

public class MessageMetadataManager {
	private static class ValueRangeHalder extends DefaultHandler {

		private Map<String, Object> CMap;
		private String E;
		private String A;
		private ValueRange B;
		String D;

		public Map A(String s, File file) {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxparser;
			FileInputStream fileinputstream = null;
			try {
				saxparser = saxParserFactory.newSAXParser();

				A = s;
				E = file.getAbsolutePath();
				fileinputstream = null;
				fileinputstream = new FileInputStream(file);
				saxparser.parse(fileinputstream, this);
			} catch (ParserConfigurationException | SAXException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (fileinputstream != null)
				try {
					fileinputstream.close();
				} catch (Exception exception) {
					exception.printStackTrace(System.err);
				}
//			break MISSING_BLOCK_LABEL_143;
//			Exception exception1;
//			exception1;
//			exception1.printStackTrace(System.err);
//			ExceptionUtil.throwActualException(exception1);
//			if (fileinputstream != null)
//				try
//				{
//					fileinputstream.close();
//				}
//				catch (Exception exception2)
//				{
//					exception2.printStackTrace(System.err);
//				}
//			break MISSING_BLOCK_LABEL_143;
//			Exception exception3;
//			exception3;
//			if (fileinputstream != null)
//				try
//				{
//					fileinputstream.close();
//				}
//				catch (Exception exception4)
//				{
//					exception4.printStackTrace(System.err);
//				}
//			throw exception3;
			return CMap;
		}

		public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
			if ("value-range".equals(s2)) {
				CMap.clear();
				String s3 = attributes.getValue("default-ref");
				if (null != s3) {
					s3 = s3.trim();
					if (0 == s3.trim().length())
						throw new RuntimeException((new StringBuilder()).append(E).append(": value-range/value-ref ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					CMap.put("default-ref", s3);
				}
			} else if ("value".equals(s2)) {
				B = new ValueRange();
				String s4 = attributes.getValue("value");
				if (null == s4)
					throw new RuntimeException((new StringBuilder()).append(E).append(": value-range/value/@value ")
							.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s4 = s4.trim();
				if (0 == s4.length())
					throw new RuntimeException((new StringBuilder()).append(E).append(": value-range/value/@value ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				if (CMap.containsKey(s4))
					throw new RuntimeException((new StringBuilder()).append(E).append(": value-range/value[@value='")
							.append(s4).append("'] ").append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.reduplicate"))
							.toString());
				B.setValue(s4);
				B.setShortText(attributes.getValue("short-text"));
				s4 = attributes.getValue("reference");
				if (s4 != null) {
					s4 = s4.trim();
					if (0 == s4.length())
						throw new RuntimeException((new StringBuilder()).append(E)
								.append(": value-range/value/@reference ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					B.setReferenceId(s4);
					Message message = MessageMetadataManager.getMessage(A, s4);
					B.setReference(message);
					if (null == CMap.get("default-ref"))
						throw new RuntimeException((new StringBuilder()).append(E).append(": value-range[@value='")
								.append(B.getValue()).append("']/@default-ref ")
								.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				}
				CMap.put(B.getValue(), B);
			}
		}

		public void endElement(String s, String s1, String s2) throws SAXException {
		}

		public void characters(char ac[], int i, int j) throws SAXException {
//			if (null != D) goto _L2; else goto _L1
//_L1:
//			D = new String(ac, i, j);
//			  goto _L3
//_L2:
//			new StringBuilder();
//			this;
//			JVM INSTR dup_x1 ;
//			D;
//			append();
//			new String(ac, i, j);
//			append();
//			toString();
//			D;
//_L3:
		}

		private ValueRangeHalder() {
			CMap = new HashMap<>();
			D = null;
		}

	}

	private static class _A extends DefaultHandler {
		private static class _AField {
			private List<Field> A;

			public Object A() {
				if (A.size() > 0)
					return A.remove(A.size() - 1);
				else
					return null;
			}

			public void A(Field obj) {
				A.add(obj);
			}

			private _AField() {
				A = new ArrayList<>();
			}
		}

		private Message K;
		private String fileAbsolutePath;
		private String D;
		_AField I;
		Map<String,ValueRange> G;
		ValueRange A;
		Map<String, Variable> BMap;
		Variable H;
		Field J;
		String F;
		String E;

		public Message A(String s, File file) {
			FileInputStream fileinputstream;
			fileAbsolutePath = file.getAbsolutePath();
			fileinputstream = null;
			Message message;
			try {
				fileinputstream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			message = A(s, ((InputStream) (fileinputstream)));
			if (fileinputstream != null)
				try {
					fileinputstream.close();
				} catch (Exception exception2) {
					exception2.printStackTrace(System.err);
				}
			return message;
//			Exception exception;
//			exception;
//			exception.printStackTrace(System.err);
//			ExceptionUtil.throwActualException(exception);
//			if (fileinputstream != null)
//				try
//				{
//					fileinputstream.close();
//				}
//				catch (Exception exception1)
//				{
//					exception1.printStackTrace(System.err);
//				}
//			break MISSING_BLOCK_LABEL_116;
//			Exception exception3;
//			exception3;
//			if (fileinputstream != null)
//				try
//				{
//					fileinputstream.close();
//				}
//				catch (Exception exception4)
//				{
//					exception4.printStackTrace(System.err);
//				}
//			throw exception3;
			// return null;
		}

		public Message A(String s, InputStream inputstream) {
			D = s;
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

			try {
				SAXParser saxparser = saxParserFactory.newSAXParser();
				saxparser.parse(inputstream, this);
			} catch (Exception exception) {
				exception.printStackTrace(System.err);
				ExceptionUtil.throwActualException(exception);
			}
			A();
			A(K.getFields());
			A(K.getClassName(), K.getFields());
			return K;
		}

		private void A(String s, SortHashMap sorthashmap) {
			Object obj = null;
			Iterator<Field> iterator = sorthashmap.values().iterator();
			do {
				if (!iterator.hasNext())
					break;
				Field field = (Field) iterator.next();
				if ((2002 == field.getFieldType() || 2003 == field.getFieldType() || 2004 == field.getFieldType()
						|| 2011 == field.getFieldType()) && !"dynamic".equalsIgnoreCase(field.getReferenceType())
						&& !"expression".equalsIgnoreCase(field.getReferenceType()) && null == field.getReference()) {
					Message message = new Message();
					message.setId(
							(new StringBuilder()).append(K.getId()).append("-").append(field.getName()).toString());
					message.setShortText(field.getShortText());
					message.setFields(field.getSubFields());
					String s1 = field.getCombineOrTableFieldClassName();
					if (null == s1)
						s1 = (new StringBuilder()).append(s).append(StringUtil.toUpperCaseFirstOne(field.getName()))
								.toString();
					message.setClassName(s1);
					MessageMetadataManager.cacheByClass.put(message.getClassName(), message);
					A(message.getClassName(), message.getFields());
				}
			} while (true);
		}

		private void A() {
			label0: {
				if (1002 == K.getType() || 1003 == K.getType()) {
					Object obj = null;
					Iterator iterator = K.getFields().values().iterator();
					HashSet hashset = new HashSet(K.getFields().size());
					Field field;
					for (; iterator.hasNext(); hashset.add(field.getTag())) {
						field = (Field) iterator.next();
						if (null == field.getTag())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.check.tag.noTag",
											new String[] { K.getId(), field.getName() }))
									.toString());
						if (1002 == K.getType() && 2004 != field.getFieldType() && 2005 != field.getFieldType()
								&& 2000 != field.getFieldType() && 2002 != field.getFieldType()
								&& 2008 != field.getFieldType())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.check.tag.illegalType",
											new String[] { K.getId(), field.getName() }))
									.toString());
						if (hashset.contains(field.getTag()))
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.check.tag.reduplicate",
											new String[] { K.getId(), field.getName(), field.getTag() }))
									.toString());
					}

				}
				if (1001 != K.getType())
					break label0;
				Field field1 = null;
				Iterator iterator1 = K.getFields().values().iterator();
				do {
					if (!iterator1.hasNext())
						break label0;
					field1 = (Field) iterator1.next();
				} while (2001 != field1.getFieldType() && 2003 != field1.getFieldType()
						&& 2009 != field1.getFieldType());
				throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
						.append(MultiLanguageResourceBundle.getInstance()
								.getString("MessageMetadataManager.check.xml.illegalFieldType"))
						.toString());
			}
		}

		private void A(SortHashMap sorthashmap) {
			Object obj = null;
			Iterator iterator = sorthashmap.values().iterator();
			int i = 0;
			while (iterator.hasNext()) {
				Field field = (Field) iterator.next();
				i++;
				if (("dynamic".equalsIgnoreCase(field.getReferenceType())
						|| "expression".equalsIgnoreCase(field.getReferenceType())) && 2002 != field.getFieldType()
						&& 2003 != field.getFieldType() && 2004 != field.getFieldType())
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
							.append(MultiLanguageResourceBundle.getInstance().getString(
									"MessageMetadataManager.postParse.dynamicField.illegalType",
									new String[] { field.getName() }))
							.toString());
				if (2001 == field.getFieldType() || 2003 == field.getFieldType() || 2009 == field.getFieldType())
					if (field.getRefLengthFieldName() != null) {
						Field field1 = (Field) sorthashmap.get(field.getRefLengthFieldName());
						if (null == field1)
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.postParse.refLengthField.notExist",
											new String[] { field.getName(), field.getRefLengthFieldName() }))
									.toString());
						if (2007 != field1.getFieldType())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.postParse.refLengthField.notLengthField",
											new String[] { field.getName(), field.getRefLengthFieldName() }))
									.toString());
						field.setRefLengthField(field1);
					} else {
						switch (field.getLengthFieldDataType()) {
						case 3001:
							break;

						case 3009:
							if (field.getLengthFieldLength() != 8)
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.postParse.lengthFieldLength.notEqual8",
												new String[] { field.getName() }))
										.toString());
							break;

						case 3003:
						case 3007:
							if (field.getLengthFieldLength() != 4)
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.postParse.lengthFieldLength.notEqual4",
												new String[] { field.getName() }))
										.toString());
							break;

						case 3004:
							if (field.getLengthFieldLength() != 1)
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.postParse.lengthFieldLength.notEqual1",
												new String[] { field.getName() }))
										.toString());
							break;

						case 3005:
						case 3008:
							if (field.getLengthFieldLength() != 2)
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.postParse.lengthFieldLength.notEqual2",
												new String[] { field.getName() }))
										.toString());
							break;

						case 3002:
						case 3006:
						default:
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.postParse.lengthFieldLength.illegalDataType",
											new String[] { field.getName() }))
									.toString());
						}
					}
				if (2007 == field.getFieldType())
					A(sorthashmap, field);
				if (2005 == field.getFieldType()) {
					if (3001 != field.getDataType() && 3003 != field.getDataType() && 3005 != field.getDataType()
							&& 3004 != field.getDataType() && 3007 != field.getDataType()
							&& 3008 != field.getDataType())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.postParse.tableRowNumberField.illegalDataType",
										new String[] { field.getName() }))
								.toString());
					Field field2 = (Field) sorthashmap.get(field.getTableName());
					if (null == field2)
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.postParse.tableRowNumberField.tableField.null",
										new String[] { field.getName(), field.getTableName() }))
								.toString());
					if (field2.getFieldType() != 2004 && field2.getFieldType() != 2011)
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.postParse.tableRowNumberField.tableField.illegalType",
										new String[] { field.getName(), field.getTableName() }))
								.toString());
					field.setTable(field2);
				}
				if (2004 == field.getFieldType() && field.getRowNumFieldName() != null) {
					Field field3 = (Field) sorthashmap.get(field.getRowNumFieldName());
					if (null != field3) {
						if (field3.getFieldType() != 2005)
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.postParse.tableField.tableRowNumberField.illegalType",
											new String[] { field.getName(), field.getRowNumFieldName() }))
									.toString());
						field.setRowNumField(field3);
					}
				}
				if ((2002 == field.getFieldType() || 2008 == field.getFieldType()) && K.getType() != 1001) {
					Message message = field.getReference();
					if (message != null && 1001 == message.getType())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.postParse.referenceField.xml.illegalFieldType",
										new String[] { field.getName(), message.getId() }))
								.toString());
				}
				if (1002 == K.getType() && null == field.getSubFields() && null == field.getReference())
					throw new RuntimeException(
							(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
									.append(field.getName()).append("']/@reference ").append(MultiLanguageResourceBundle
											.getInstance().getString("null", new String[] { field.getName() }))
									.toString());
				if (2010 == field.getFieldType()) {
					if (3002 != field.getDataType())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": ")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.postParse.macField.illegalDataType",
										new String[] { field.getName() }))
								.toString());
					Field field4 = (Field) sorthashmap.get(field.getStartFieldName());
					if (null != field4) {
						field.setStartField(field4);
						if (field.getStartFieldName().equals(field.getEndFieldName())) {
							field.setEndField(field.getStartField());
						} else {
							Field field5 = (Field) sorthashmap.get(field.getEndFieldName());
							if (null == field5)
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.postParse.field.endField.null",
												new String[] { field.getName(), field.getEndFieldName() }))
										.toString());
							field.setEndField(field5);
						}
					}
				}
				if (2011 == field.getFieldType() && null == field.getRefLengthFieldName()
						&& null == field.getTabSuffix() && (null == field.getSuffix() || !field.isLastRowSuffix())
						&& i < sorthashmap.size())
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
							.append(MultiLanguageResourceBundle.getInstance().getString(
									"MessageMetadataManager.postParse.varField.error",
									new String[] { field.getName() }))
							.toString());
			}
		}

		private void A(SortHashMap sorthashmap, Field field) {
			if (3001 != field.getDataType() && 3003 != field.getDataType() && 3005 != field.getDataType()
					&& 3004 != field.getDataType() && 3007 != field.getDataType() && 3008 != field.getDataType()
					&& 3009 != field.getDataType())
				throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
						.append(MultiLanguageResourceBundle.getInstance().getString(
								"MessageMetadataManager.checkLengthField.lengthField.illegalDataType",
								new String[] { field.getName() }))
						.toString());
			Field field1 = (Field) sorthashmap.get(field.getStartFieldName());
			if (null == field1)
				throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
						.append(MultiLanguageResourceBundle.getInstance().getString(
								"MessageMetadataManager.checkLengthField.lengthField.startField.null",
								new String[] { field.getName(), field.getStartFieldName() }))
						.toString());
			field.setStartField(field1);
			if (field.getStartFieldName().equals(field.getEndFieldName())) {
				field.setEndField(field.getStartField());
			} else {
				Field field2 = (Field) sorthashmap.get(field.getEndFieldName());
				if (null == field2)
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
							.append(MultiLanguageResourceBundle.getInstance().getString(
									"MessageMetadataManager.checkLengthField.lengthField.endField.null",
									new String[] { field.getName(), field.getEndFieldName() }))
							.toString());
				field.setEndField(field2);
			}
		}

		public void endElement(String s, String s1, String s2) throws SAXException {
			if ("field".equals(s2)) {
				Field field = (Field) I.A();
				if (field.getFieldType() == 2002 && field.getReference() == null
						&& !"dynamic".equalsIgnoreCase(field.getReferenceType())
						&& !"expression".equalsIgnoreCase(field.getReferenceType())) {
					if (field.getReference() == null)
						A(field.getSubFields());
					if (1002 == K.getType() && field.getFieldType() == 2002 && field.getReference() == null)
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": field[@name='").append(field.getName()).append("']/@reference ")
								.append(MultiLanguageResourceBundle.getInstance().getString("null",
										new String[] { field.getName() }))
								.toString());
				}
				if ((2002 == field.getFieldType() || 2003 == field.getFieldType() || 2004 == field.getFieldType()
						|| 2011 == field.getFieldType()) && field.getReference() == null)
					A(field.getSubFields());
				if ((2000 == field.getFieldType() || 2001 == field.getFieldType()) && 3010 == field.getDataType()) {
					if (null == field.getPattern())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": field[@name='").append(field.getName()).append("']/@pattern ")
								.append(MultiLanguageResourceBundle.getInstance().getString("null",
										new String[] { field.getName() }))
								.toString());
					String as[] = field.getPattern().split(",");
					if (as.length > 2)
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": field[@name='").append(field.getName()).append("']/@pattern ")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.startElement.wrong", new String[] { field.getName() }))
								.toString());
					try {
						Integer.parseInt(as[0].trim());
					} catch (Exception exception) {
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.endElement.field.pattern.wrong",
										new String[] { field.getName() }))
								.append(exception.getMessage()).toString(), exception);
					}
					if (as.length == 2)
						try {
							Integer.parseInt(as[1].trim());
						} catch (Exception exception1) {
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.endElement.field.pattern.wrong",
											new String[] { field.getName() }))
									.append(exception1.getMessage()).toString(), exception1);
						}
				}
				if (0 != G.size()) {
					J.setValueRange(new HashMap());
					J.getValueRange().putAll(G);
					G.clear();
				}
				J = null;
			} else if ("value-range".equals(s2)) {
				J.setValueRange(new HashMap());
				J.getValueRange().putAll(G);
				G.clear();
			} else if ("expression".equals(s2)) {
				String s3 = E;
				if (null != s3)
					s3 = s3.trim();
				if (0 == s3.length())
					s3 = null;
				if (J != null)
					J.setExpression(s3);
				E = null;
			} else if ("event".equals(s2)) {
				String s4 = E;
				if (s4 != null)
					s4 = s4.trim();
				if (0 == s4.length())
					s4 = null;
				boolean flag = false;
				if (null == J) {
					J = (Field) I.A();
					flag = true;
				}
				if ("row-post-pack".equalsIgnoreCase(F)) {
					if (J != null)
						J.setPostRowPackEvent(s4);
				} else if ("row-pre-pack".equalsIgnoreCase(F)) {
					if (J != null)
						J.setPreRowPackEvent(s4);
				} else if ("row-post-parse".equalsIgnoreCase(F)) {
					if (J != null)
						J.setPostRowParseEvent(s4);
				} else if ("row-pre-parse".equalsIgnoreCase(F)) {
					if (J != null)
						J.setPreRowParseEvent(s4);
				} else if ("post-pack".equalsIgnoreCase(F)) {
					if (J != null)
						J.setPostPackEvent(s4);
					else
						K.setPostPackEvent(s4);
				} else if ("pre-pack".equalsIgnoreCase(F)) {
					if (J != null)
						J.setPrePackEvent(s4);
					else
						K.setPrePackEvent(s4);
				} else if ("post-parse".equalsIgnoreCase(F)) {
					if (J != null)
						J.setPostParseEvent(s4);
					else
						K.setPostParseEvent(s4);
				} else if ("pre-parse".equalsIgnoreCase(F)) {
					if (J != null)
						J.setPreParseEvent(s4);
					else
						K.setPreParseEvent(s4);
				} else {
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
							.append(": Unsupport Event Type[").append(F).append("]!").toString());
				}
				if (flag && null != J)
					I.A(J);
				E = null;
			} else if ("template".equals(s2)) {
				String s5 = E;
				if (s5 != null)
					s5 = s5.trim();
				if (0 == s5.length())
					s5 = null;
				K.setTemplate(s5);
				E = null;
			} else if ("message-bean".equals(s2) && BMap.size() != 0) {
				K.setVariable(new HashMap<>());
				K.getVariable().putAll(BMap);
				BMap.clear();
			}
		}

		public void characters(char ac[], int i, int j) throws SAXException {
//			if (null != E) goto _L2; else goto _L1
//_L1:
//			E = new String(ac, i, j);
//			  goto _L3
//_L2:
//			new StringBuilder();
//			this;
//			JVM INSTR dup_x1 ;
//			E;
//			append();
//			new String(ac, i, j);
//			append();
//			toString();
//			E;
//_L3:
		}

		public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
			if ("message-bean".equals(s2)) {
				String s3 = attributes.getValue("id");
				if (null == s3)
					throw new RuntimeException(
							(new StringBuilder()).append(fileAbsolutePath).append(": message-bean/@id ")
									.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s3 = s3.trim();
				if (0 == s3.length())
					throw new RuntimeException(
							(new StringBuilder()).append(fileAbsolutePath).append(": message-bean/@id ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				K.setId(s3);
				s3 = attributes.getValue("type");
				if (null != s3) {
					s3 = s3.trim();
					if (0 == s3.length())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": message-bean/@type ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					K.setType(Constant.getMessageTypeByText(s3));
				}
				K.setShortText(attributes.getValue("short-text"));
				K.setGroupId(D);
				s3 = attributes.getValue("class");
				if (null == s3)
					throw new RuntimeException(
							(new StringBuilder()).append(fileAbsolutePath).append(": message-bean/@class ")
									.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s3 = s3.trim();
				if (0 == s3.length())
					throw new RuntimeException(
							(new StringBuilder()).append(fileAbsolutePath).append(": message-bean/@class ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				if (MessageMetadataManager.cacheByClass.containsKey(s3)) {
					Message message = (Message) MessageMetadataManager.cacheByClass.get(s3);
					if (D.equalsIgnoreCase(message.getGroupId()))
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": ")
								.append(MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.messageClass.reduplicate"))
								.toString());
				}
				K.setClassName(s3);
				s3 = attributes.getValue("xpath");
				K.setXpath(s3);
				s3 = attributes.getValue("message-charset");
				if (null != s3 && 0 != s3.trim().length())
					K.setMsgCharset(s3);
				if (1002 == K.getType() || 1003 == K.getType()) {
					String s4 = attributes.getValue("prefix");
					if (null == s4)
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": Tag/Swift message-bean/@prefix ")
								.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
					s4 = s4.trim();
					if (0 == s4.length())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": Tag/Swift message-bean/@prefix ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					if (s4.length() > 2 && s4.startsWith("0x")) {
						String s9 = s4.substring(2);
						if (!CodeUtil.isHexString(s9))
							throw new RuntimeException(
									(new StringBuilder()).append(fileAbsolutePath)
											.append(": Tag/Swift message-bean/@prefix ")
											.append(MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.notHexString"))
											.toString());
						K.setPrefixString(s9);
						K.setPrefix(CodeUtil.HextoByte(s9));
					} else {
						K.setPrefixString(new String(CodeUtil.BCDtoASC(s4.getBytes())));
						K.setPrefix(s4.getBytes());
					}
					s4 = attributes.getValue("suffix");
					if (null == s4)
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": Tag/Swift message-bean/@suffix ")
								.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
					s4 = s4.trim();
					if (0 == s4.length())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": Tag/Swift message-bean/@suffix ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					if (s4.length() > 2 && s4.startsWith("0x")) {
						String s10 = s4.substring(2);
						if (!CodeUtil.isHexString(s10))
							throw new RuntimeException(
									(new StringBuilder()).append(fileAbsolutePath)
											.append(": Tag/Swift message-bean/@suffix ")
											.append(MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.notHexString"))
											.toString());
						K.setSuffixString(s10);
						K.setSuffix(CodeUtil.HextoByte(s10));
					} else {
						K.setSuffixString(new String(CodeUtil.BCDtoASC(s4.getBytes())));
						K.setSuffix(s4.getBytes());
					}
				}
			} else if ("field".equals(s2)) {
				Field field = (Field) I.A();
				if (field != null && field.getFieldType() != 2002 && field.getFieldType() != 2003
						&& field.getFieldType() != 2004 && field.getFieldType() != 2011)
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": ")
							.append(MultiLanguageResourceBundle.getInstance().getString(
									"MessageMetadataManager.startElement.fieldType.illegal",
									new String[] { field.getName() }))
							.toString());
				Field field1 = new Field();
				String s11 = attributes.getValue("name");
				if (null == s11)
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": field/@name ")
							.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s11 = s11.trim();
				if (0 == s11.length())
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": field/@name ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				if (MessageMetadataManager.keyWordSet.contains(s11))
					throw new RuntimeException(
							(new StringBuilder()).append(fileAbsolutePath).append(": field/@name [").append(s11)
									.append("] ")
									.append(MultiLanguageResourceBundle.getInstance()
											.getString("MessageMetadataManager.startElement.field.isReserved"))
									.toString());
				if (field != null) {
					if (field.getSubFields().containsKey(s11))
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": ")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.startElement.field.subField.reduplicate",
										new String[] { field.getName() }))
								.append(":").append(s11).toString());
				} else if (K.getFields().containsKey(s11))
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": ")
							.append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.field.reduplicate"))
							.append(":").append(s11).toString());
				field1.setName(s11);
				s11 = attributes.getValue("field-type");
				if (null == s11)
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
							.append(field1.getName()).append("']/@field-type ")
							.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s11 = s11.trim();
				if (0 == s11.length())
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
							.append(field1.getName()).append("']/@field-type ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				try {
					field1.setFieldType(Constant.getFieldTypeByText(s11));
				} catch (Exception exception1) {
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
							.append(field1.getName()).append("']/@field-type ").append(MultiLanguageResourceBundle
									.getInstance().getString("MessageMetadataManager.startElement.wrong"))
							.toString());
				}
				s11 = attributes.getValue("padding");
				if (null != s11) {
					s11 = s11.trim();
					if (0 != s11.length()) {
						byte abyte0[] = s11.getBytes();
						if (1 == abyte0.length)
							field1.setPadding(abyte0[0]);
						else if (4 == abyte0.length && s11.startsWith("0x")) {
							byte abyte1[] = CodeUtil.HextoByte(s11.substring(2));
							field1.setPadding(abyte1[0]);
						} else {
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("']/@padding ")
									.append(MultiLanguageResourceBundle.getInstance()
											.getString("MessageMetadataManager.startElement.padding.format.wrong"))
									.toString());
						}
					}
				}
				s11 = attributes.getValue("padding-direction");
				if (null != s11) {
					s11 = s11.trim();
					if (0 != s11.length())
						if ("right".equalsIgnoreCase(s11))
							field1.setPaddingDirection(5002);
						else if ("left".equalsIgnoreCase(s11))
							field1.setPaddingDirection(5001);
						else if ("none".equalsIgnoreCase(s11))
							field1.setPaddingDirection(5000);
						else
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("']/@padding-direction ")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.paddingDirection.format.wrong"))
									.toString());
				}
				s11 = attributes.getValue("required");
				if (null != s11) {
					s11 = s11.trim();
					if ("true".equalsIgnoreCase(s11))
						field1.setRequired(true);
					else
						field1.setRequired(false);
				} else {
					field1.setRequired(MessageMetadataManager.isRequired);
				}
				s11 = attributes.getValue("editable");
				if (null != s11) {
					s11 = s11.trim();
					if ("false".equalsIgnoreCase(s11))
						field1.setEditable(false);
				}
				s11 = attributes.getValue("isRemoveUnwatchable");
				if (null != s11) {
					s11 = s11.trim();
					if ("false".equalsIgnoreCase(s11))
						field1.setRemoveUnwatchable(false);
				}
				if (field1.isEditable()) {
					s11 = attributes.getValue("xpath");
					field1.setXpath(s11);
				}
				field1.setShortText(attributes.getValue("short-text"));
				s11 = attributes.getValue("iso8583-no");
				if (null != s11) {
					s11 = s11.trim();
					if (0 == s11.length())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": field[@name='").append(field1.getName()).append("']/@iso8583-no ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					try {
						field1.setIso8583_no(Integer.parseInt(s11));
					} catch (Exception exception2) {
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": field[@name='").append(field1.getName()).append("']/@iso8583-no ")
								.append(MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.wrong"))
								.toString());
					}
				}
				s11 = attributes.getValue("prefix");
				if (null != s11 && s11.length() > 0) {
					if (s11.length() > 2 && s11.startsWith("0x")) {
						String s12 = s11.substring(2);
						if (!CodeUtil.isHexString(s12))
							throw new RuntimeException(
									(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
											.append(field1.getName()).append("']/@prefix ")
											.append(MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.notHexString"))
											.toString());
						field1.setPrefixString(s12);
						field1.setPrefix(CodeUtil.HextoByte(s12));
					} else {
						field1.setPrefixString(new String(CodeUtil.BCDtoASC(s11.getBytes())));
						field1.setPrefix(s11.getBytes());
					}
					s11 = attributes.getValue("fir-row-prefix");
					if (null != s11) {
						s11 = s11.trim();
						if (0 != s11.length() && "true".equalsIgnoreCase(s11))
							field1.setFirRowPrefix(true);
					}
				}
				s11 = attributes.getValue("suffix");
				if (null != s11 && s11.length() > 0) {
					if (s11.length() > 2 && s11.startsWith("0x")) {
						String s13 = s11.substring(2);
						if (!CodeUtil.isHexString(s13))
							throw new RuntimeException(
									(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
											.append(field1.getName()).append("']/@suffix ")
											.append(MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.notHexString"))
											.toString());
						field1.setSuffixString(s13);
						field1.setSuffix(CodeUtil.HextoByte(s13));
					} else {
						field1.setSuffix(s11.getBytes());
						field1.setSuffixString(new String(CodeUtil.BCDtoASC(s11.getBytes())));
					}
					s11 = attributes.getValue("last-row-suffix");
					if (null != s11) {
						s11 = s11.trim();
						if (0 != s11.length() && "true".equalsIgnoreCase(s11))
							field1.setLastRowSuffix(true);
					}
				}
				s11 = attributes.getValue("extended-attributes");
				if (null != s11) {
					s11 = s11.trim();
					if (0 == s11.length())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": field[@name='").append(field1.getName()).append("']/@extended-attributes ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					try {
						field1.setExtendedAttributeText(s11);
						String as[] = s11.split(";");
						for (int j = 0; j < as.length; j++)
							field1.setExtendAttribute(as[j].substring(0, as[j].indexOf(":")),
									as[j].substring(as[j].indexOf(":") + 1));

					} catch (Exception exception3) {
						throw new RuntimeException(
								(new StringBuilder())
										.append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
										.append("']/@extended-attributes ").append(MultiLanguageResourceBundle
												.getInstance().getString("MessageMetadataManager.startElement.wrong"))
										.toString());
					}
				}
				s11 = attributes.getValue("data-charset");
				if (null != s11) {
					s11 = s11.trim();
					if (0 != s11.length())
						field1.setDataCharset(s11);
				}
				s11 = attributes.getValue("shield");
				if (null != s11) {
					s11 = s11.trim();
					if ("true".equals(s11))
						field1.setShield(true);
				}
				if (2011 == field1.getFieldType()) {
					s11 = attributes.getValue("row-cut");
					if (null != s11) {
						s11 = s11.trim();
						if ("false".equalsIgnoreCase(s11))
							field1.setRowCut(false);
					}
				}
				if (2001 == field1.getFieldType() || 2003 == field1.getFieldType() || 2009 == field1.getFieldType()
						|| 2011 == field1.getFieldType()) {
					s11 = attributes.getValue("ref-length-field");
					if (null != s11) {
						s11 = s11.trim();
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("']/@ref-length-field ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						field1.setRefLengthFieldName(s11);
						s11 = attributes.getValue("ref-length-field-offset");
						if (null != s11)
							try {
								field1.setRefLengthFieldOffset(Integer.parseInt(s11));
							} catch (Exception exception4) {
								throw new RuntimeException(
										(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
												.append(field1.getName()).append("']/@ref-length-field-offset ")
												.append(MultiLanguageResourceBundle.getInstance()
														.getString("MessageMetadataManager.startElement.wrong"))
												.append(exception4.getMessage()).toString(),
										exception4);
							}
					} else if (2011 == field1.getFieldType()) {
						s11 = attributes.getValue("tab-prefix");
						if (null != s11) {
							s11 = s11.trim();
							if (0 != s11.length())
								if (s11.length() > 2 && s11.startsWith("0x")) {
									String s14 = s11.substring(2);
									if (!CodeUtil.isHexString(s14))
										throw new RuntimeException(
												(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
														.append(field1.getName()).append("']/@tab-suffix ")
														.append(MultiLanguageResourceBundle.getInstance().getString(
																"MessageMetadataManager.startElement.notHexString"))
														.toString());
									field1.setTabPrefixString(s11);
									field1.setTabPrefix(CodeUtil.HextoByte(s14));
								} else {
									field1.setTabPrefixString(new String(CodeUtil.BCDtoASC(s11.getBytes())));
									field1.setTabPrefix(s11.getBytes());
								}
						}
						s11 = attributes.getValue("tab-suffix");
						if (null != s11) {
							s11 = s11.trim();
							if (0 != s11.length())
								if (s11.length() > 2 && s11.startsWith("0x")) {
									String s15 = s11.substring(2);
									if (!CodeUtil.isHexString(s15))
										throw new RuntimeException(
												(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
														.append(field1.getName()).append("']/@tab-suffix ")
														.append(MultiLanguageResourceBundle.getInstance().getString(
																"MessageMetadataManager.startElement.notHexString"))
														.toString());
									field1.setTabSuffixString(s11);
									field1.setTabSuffix(CodeUtil.HextoByte(s15));
								} else {
									field1.setTabSuffixString(new String(CodeUtil.BCDtoASC(s11.getBytes())));
									field1.setTabSuffix(s11.getBytes());
								}
						}
					} else {
						s11 = attributes.getValue("length-field-data-type");
						if (null == s11)
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName())
									.append("']/@length-field-data-type ")
									.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
						s11 = s11.trim();
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName())
									.append("']/@length-field-data-type ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						try {
							field1.setLengthFieldDataType(Constant.getDataTypeByText(s11));
						} catch (Exception exception5) {
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName())
									.append("']/@length-field-data-type ").append(MultiLanguageResourceBundle
											.getInstance().getString("MessageMetadataManager.startElement.wrong"))
									.toString());
						}
						s11 = attributes.getValue("length-field-data-encoding");
						if (null != s11) {
							s11 = s11.trim();
							if ("bcd".equalsIgnoreCase(s11))
								field1.setLengthFieldDataEncoding(4001);
						}
						s11 = attributes.getValue("length-field-length");
						if (null == s11)
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName())
									.append("']/@length-field-length ")
									.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
						s11 = s11.trim();
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getCalcType())
									.append("']/@length-field-length ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						try {
							field1.setLengthFieldLength(Integer.parseInt(s11));
						} catch (Exception exception6) {
							throw new RuntimeException(
									(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
											.append(field1.getName()).append("']/@length-field-length ")
											.append(MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.wrong"))
											.toString());
						}
					}
					s11 = attributes.getValue("max-length");
					if (null != s11) {
						s11 = s11.trim();
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("']/@max-length ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						try {
							field1.setMaxLength(Integer.parseInt(s11));
						} catch (Exception exception7) {
							throw new RuntimeException(
									(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
											.append(field1.getName()).append("']/@max-length ")
											.append(MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.wrong"))
											.toString());
						}
					}
					s11 = attributes.getValue("min-length");
					if (null != s11) {
						s11 = s11.trim();
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("']/@min-length ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						try {
							field1.setMinLength(Integer.parseInt(s11));
						} catch (Exception exception8) {
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("']/@min-length ")
									.append(MultiLanguageResourceBundle.getInstance()
											.getString("MessageMetadataManager.startElement.wrong"))
									.append(exception8.getMessage()).toString(), exception8);
						}
					}
				}
				if (1002 == K.getType() || 1003 == K.getType()) {
					s11 = attributes.getValue("tag");
					if (null != s11) {
						s11 = s11.trim();
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("']/@tag ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						field1.setTag(s11);
					}
				}
				if (2002 == field1.getFieldType() || 2003 == field1.getFieldType() || 2004 == field1.getFieldType()
						|| 2011 == field1.getFieldType() || 2008 == field1.getFieldType()
						|| 2009 == field1.getFieldType()) {
					s11 = attributes.getValue("reference-type");
					if (null != s11) {
						s11 = s11.trim();
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("']/@reference-type ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						field1.setReferenceType(s11);
					}
					s11 = attributes.getValue("reference");
					if (null == s11 && (2008 == field1.getFieldType() || 2009 == field1.getFieldType()))
						throw new RuntimeException(
								(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
										.append(field1.getName()).append("'] ")
										.append(MultiLanguageResourceBundle.getInstance()
												.getString("MessageMetadataManager.startElement.reference.null"))
										.toString());
					if (s11 != null) {
						s11 = s11.trim();
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("']/@reference ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						if (!"dynamic".equalsIgnoreCase(field1.getReferenceType())
								&& !"expression".equalsIgnoreCase(field1.getReferenceType())) {
							Message message3 = MessageMetadataManager.getMessage(D, s11);
							field1.setReference(message3);
						}
						field1.setReferenceId(s11);
					}
					field1.setSubFields(new SortHashMap(32));
					if (2004 == field1.getFieldType()) {
						s11 = attributes.getValue("row-num-field");
						if (s11 != null) {
							s11 = s11.trim();
							if (0 == s11.length())
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
										.append(": field[@name='").append(field1.getName()).append("']/@row-num-field ")
										.append(MultiLanguageResourceBundle.getInstance().getString("blank"))
										.toString());
							field1.setRowNumFieldName(s11);
						}
						s11 = attributes.getValue("row-xpath");
						if (s11 != null) {
							s11 = s11.trim();
							if (0 == s11.length())
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
										.append(": field[@name='").append(field1.getName()).append("']/@row-xpath ")
										.append(MultiLanguageResourceBundle.getInstance().getString("blank"))
										.toString());
							field1.setRowXpath(s11);
						}
					}
					s11 = attributes.getValue("data-encoding");
					if (null != s11) {
						s11 = s11.trim();
						if ("bcd".equalsIgnoreCase(s11))
							field1.setDataEncoding(4001);
					}
				} else {
					if (2007 == field1.getFieldType()) {
						s11 = attributes.getValue("start-field");
						if (null == s11)
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append("'] ")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.lengthField.startField.null"))
									.toString());
						s11 = s11.trim();
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("'] ")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.lengthField.startField.blank"))
									.toString());
						field1.setStartFieldName(s11);
						s11 = attributes.getValue("end-field");
						if (null == s11 || 0 == s11.trim().length())
							field1.setEndFieldName(field1.getStartFieldName());
						else
							field1.setEndFieldName(s11.trim());
					}
					if (2010 == field1.getFieldType()) {
						s11 = attributes.getValue("start-field");
						if (null != s11) {
							s11 = s11.trim();
							if (0 == s11.length())
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
										.append(": field[@name='").append(field1.getName()).append("'] ")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.lengthField.startField.blank"))
										.toString());
							field1.setStartFieldName(s11);
							s11 = attributes.getValue("end-field");
							if (null == s11 || 0 == s11.trim().length())
								field1.setEndFieldName(field1.getStartFieldName());
							else
								field1.setEndFieldName(s11.trim());
						}
						s11 = attributes.getValue("data");
						if (null != s11) {
							s11 = s11.trim();
							if (0 != s11.length()) {
								String as1[] = s11.split(";");
								HashMap hashmap = new HashMap();
								for (int l = 0; l < as1.length; l++)
									hashmap.put(as1[l], as1[l]);

								field1.setMacFldDataCache(hashmap);
							}
						}
					}
					if (2005 == field1.getFieldType()) {
						s11 = attributes.getValue("table");
						if (null == s11)
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("']/@table ")
									.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
						s11 = s11.trim();
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("']/@table ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						field1.setTableName(s11);
					}
					s11 = attributes.getValue("data-type");
					if (null == s11)
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": field[@name='").append(field1.getName()).append("']/@data-type ")
								.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
					s11 = s11.trim();
					if (0 == s11.length())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": field[@name='").append(field1.getName()).append("']/@data-type ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					try {
						field1.setDataType(Constant.getDataTypeByText(s11));
					} catch (Exception exception9) {
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": field[@name='").append(field1.getName()).append("']/@data-type ")
								.append(MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.wrong"))
								.append(exception9.getMessage()).toString(), exception9);
					}
					s11 = attributes.getValue("data-encoding");
					if (null != s11) {
						s11 = s11.trim();
						if ("bcd".equalsIgnoreCase(s11))
							field1.setDataEncoding(4001);
					}
					s11 = attributes.getValue("pattern");
					if (null != s11) {
						s11 = s11.trim();
						if (0 != s11.length())
							field1.setPattern(s11);
					}
					s11 = attributes.getValue("length-script");
					if (null != s11) {
						s11 = s11.trim();
						if (0 != s11.length())
							field1.setLengthScript(s11);
					}
					if (2001 != field1.getFieldType())
						if (3000 == field1.getDataType() || 3001 == field1.getDataType() || 3002 == field1.getDataType()
								|| 3010 == field1.getDataType()) {
							s11 = attributes.getValue("length");
							if (null == s11)
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
										.append(": field[@name='").append(field1.getName()).append("']/@length ")
										.append(MultiLanguageResourceBundle.getInstance().getString("null"))
										.toString());
							s11 = s11.trim();
							if (0 == s11.length())
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
										.append(": field[@name='").append(field1.getName()).append("']/@length ")
										.append(MultiLanguageResourceBundle.getInstance().getString("blank"))
										.toString());
							try {
								field1.setLength(Integer.parseInt(s11));
							} catch (Exception exception10) {
								throw new RuntimeException(
										(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
												.append(field1.getName()).append("']/@length[").append(s11).append("] ")
												.append(MultiLanguageResourceBundle.getInstance()
														.getString("MessageMetadataManager.startElement.format.wrong"))
												.toString());
							}
						} else if (3006 == field1.getDataType()) {
							if (null == field1.getPattern())
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
										.append(": field[@name='").append(field1.getName()).append("'] ")
										.append(MultiLanguageResourceBundle.getInstance()
												.getString("MessageMetadataManager.startElement.datatime.pattern.null"))
										.toString());
							SimpleDateFormat simpledateformat;
							try {
								simpledateformat = new SimpleDateFormat(field1.getPattern());
							} catch (Exception exception11) {
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
										.append(": field[@name='").append(field1.getName()).append("'] ")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.datatime.pattern.wrong"))
										.toString());
							}
							field1.setLength(field1.getPattern().length());
						} else {
							s11 = attributes.getValue("length");
							if (s11 != null)
								s11 = s11.trim();
							int i = 0;
							try {
								i = Integer.parseInt(s11);
							} catch (Exception exception18) {
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
										.append(": field[@name='").append(field1.getName()).append("']/@length[ ")
										.append(s11).append("] ")
										.append(MultiLanguageResourceBundle.getInstance()
												.getString("MessageMetadataManager.startElement.format.wrong"))
										.toString());
							}
							switch (field1.getDataType()) {
							case 3003:
							case 3007:
								if (i != 4)
									throw new RuntimeException(
											(new StringBuilder()).append(fileAbsolutePath).append(": ")
													.append(MultiLanguageResourceBundle.getInstance().getString(
															"MessageMetadataManager.startElement.length.notEqual4",
															new String[] { field1.getName() }))
													.toString());
								break;

							case 3004:
								if (i != 1)
									throw new RuntimeException(
											(new StringBuilder()).append(fileAbsolutePath).append(": ")
													.append(MultiLanguageResourceBundle.getInstance().getString(
															"MessageMetadataManager.startElement.length.notEqual1"))
													.toString());
								break;

							case 3005:
							case 3008:
								if (i != 2)
									throw new RuntimeException(
											(new StringBuilder()).append(fileAbsolutePath).append(": ")
													.append(MultiLanguageResourceBundle.getInstance().getString(
															"MessageMetadataManager.startElement.length.notEqual2"))
													.toString());
								break;

							case 3009:
								if (i != 8)
									throw new RuntimeException(
											(new StringBuilder()).append(fileAbsolutePath).append(": ")
													.append(MultiLanguageResourceBundle.getInstance().getString(
															"MessageMetadataManager.startElement.length.notEqual8"))
													.toString());
								break;

							case 3006:
							default:
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": ")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.unsupportDataType",
												new String[] { field1.getName() }))
										.append(":").append(field1.getDataType()).toString());
							}
							field1.setLength(i);
						}
					s11 = attributes.getValue("strict-data-length");
					if (null != s11) {
						s11 = s11.trim();
						if ("true".equalsIgnoreCase(s11))
							field1.setStrictDataLength(true);
					}
					if (2001 == field1.getFieldType())
						field1.setPaddingDirection(5000);
					s11 = attributes.getValue("value");
					if (null != s11) {
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": ")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.field.defaultValue.null",
											new String[] { field1.getName(), s11 }))
									.toString());
						if (2000 == field1.getFieldType() || 2005 == field1.getFieldType()) {
							if (3000 == field1.getDataType() || 3001 == field1.getDataType()
									|| 3010 == field1.getDataType()) {
								if (s11.length() > field1.getLength())
									throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
											.append(": ")
											.append(MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.tooLong",
													new String[] { field1.getName(), s11 }))
											.toString());
								if (field1.isStrictDataLength() && s11.length() < field1.getLength())
									throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
											.append(": ")
											.append(MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.tooShort",
													new String[] { field1.getName(), s11, (new StringBuilder())
															.append("").append(field1.getLength()).toString() }))
											.toString());
								BigDecimal bigdecimal;
								if (3001 == field1.getDataType())
									try {
										Integer.parseInt(s11);
									} catch (Exception exception12) {
										throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
												.append(": ")
												.append(MultiLanguageResourceBundle.getInstance().getString(
														"MessageMetadataManager.startElement.field.defaultValue.notNumber",
														new String[] { field1.getName(), s11 }))
												.append(exception12.getMessage()).toString(), exception12);
									}
								else if (3010 == field1.getDataType())
									try {
										bigdecimal = new BigDecimal(s11);
									} catch (Exception exception13) {
										throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
												.append(": ")
												.append(MultiLanguageResourceBundle.getInstance().getString(
														"MessageMetadataManager.startElement.field.defaultValue.notDecimalNumber",
														new String[] { field1.getName(), s11 }))
												.toString());
									}
							} else if (3003 == field1.getDataType() || 3007 == field1.getDataType())
								try {
									Integer.parseInt(s11);
								} catch (Exception exception14) {
									throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
											.append(": ")
											.append(MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.notIntNumber",
													new String[] { field1.getName(), s11 }))
											.toString());
								}
							else if (3005 == field1.getDataType() || 3008 == field1.getDataType())
								try {
									Short.parseShort(s11);
								} catch (Exception exception15) {
									throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
											.append(": ")
											.append(MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.notShortNumber",
													new String[] { field1.getName(), s11 }))
											.append(exception15.getMessage()).toString(), exception15);
								}
							else if (3004 == field1.getDataType())
								try {
									if (0 != s11.length()) {
										byte abyte2[] = s11.getBytes();
										if (1 != abyte2.length)
											if (4 == abyte2.length && s11.startsWith("0x"))
												abyte2 = CodeUtil.HextoByte(s11.substring(2));
											else
												throw new RuntimeException((new StringBuilder())
														.append(fileAbsolutePath).append(": field[@name='")
														.append(field1.getName()).append("']/@padding ")
														.append(MultiLanguageResourceBundle.getInstance().getString(
																"MessageMetadataManager.startElement.padding.format.wrong"))
														.toString());
									}
								} catch (Exception exception16) {
									throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
											.append(": ")
											.append(MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.notByteNumber",
													new String[] { field1.getName(), s11 }))
											.toString(), exception16);
								}
							else if (3006 == field1.getDataType()) {
								SimpleDateFormat simpledateformat1 = new SimpleDateFormat(field1.getPattern());
								simpledateformat1.setLenient(false);
								try {
									simpledateformat1.parse(s11);
								} catch (Exception exception19) {
									throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
											.append(": ")
											.append(MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.datetime.illegalPattern",
													new String[] { field1.getName(), s11, field1.getPattern() }))
											.toString());
								}
							} else if (3009 == field1.getDataType())
								try {
									Long.parseLong(s11);
								} catch (Exception exception17) {
									throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
											.append(": ")
											.append(MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.notLongNumber",
													new String[] { field1.getName(), s11 }))
											.toString(), exception17);
								}
							else if (3002 == field1.getDataType()) {
								byte abyte3[] = s11.trim().getBytes();
								if (abyte3.length % 2 != 0)
									throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
											.append(": ")
											.append(MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.notHexString",
													new String[] { field1.getName(),
															Constant.getFieldTypeText(field1.getFieldType()) }))
											.toString());
								if (abyte3.length / 2 != field1.getLength())
									throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
											.append(": ")
											.append(MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.hexString.illegalLength",
													new String[] { field1.getName(),
															Constant.getFieldTypeText(field1.getFieldType()) }))
											.append(" ").append(field1.getLength() * 2).toString());
								for (int k = 0; k < abyte3.length; k++)
									if ((abyte3[k] < 48 || abyte3[k] > 57) && (abyte3[k] < 97 || abyte3[k] > 102)
											&& (abyte3[k] < 65 || abyte3[k] > 70))
										throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
												.append(": ")
												.append(MultiLanguageResourceBundle.getInstance().getString(
														"MessageMetadataManager.startElement.field.defaultValue.notHexString",
														new String[] { field1.getName(),
																Constant.getFieldTypeText(field1.getFieldType()) }))
												.toString());

							} else {
								throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": ")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.canNotHave.defaultValue.1",
												new String[] { field1.getName(),
														Constant.getDataTypeText(field1.getDataType()) }))
										.toString());
							}
							field1.setValue(s11);
						} else {
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": ")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.field.canNotHave.defaultValue.2",
											new String[] { field1.getName(),
													Constant.getFieldTypeText(field1.getFieldType()) }))
									.toString());
						}
					}
					s11 = attributes.getValue("ref-value-range");
					if (null != s11) {
						s11 = s11.trim();
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": field[@name='").append(field1.getName()).append("']/@ref-value-range ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						field1.setRefValueRange(s11);
						File file = (File) MessageMetadataManager.groupPathCache.get(D);
						String s17 = file.getAbsolutePath();
						if (!s17.endsWith(System.getProperty("file.separator")))
							s17 = (new StringBuilder()).append(s17).append(System.getProperty("file.separator"))
									.toString();
						String s18 = (new StringBuilder()).append(s17).append("value-range")
								.append(System.getProperty("file.separator")).append(s11).append(".xml").toString();
						File file1 = new File(s18);
						ValueRangeHalder _lb = new ValueRangeHalder();
						G = _lb.A(D, file1);
					}
				}
				s11 = attributes.getValue("class");
				if (field1.getFieldType() == 2010) {
					String s16 = attributes.getValue("calc-type");
					if (null == s16)
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": mac-field/@calc-type ")
								.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
					s16 = s16.trim();
					if (0 == s16.length())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": mac-field/@calc-type ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					field1.setCalcType(s16);
					if ("java".equalsIgnoreCase(field1.getCalcType())) {
						if (null == s11)
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": mac-field/@class ")
									.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
						s11 = s11.trim();
						if (0 == s11.length())
							throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
									.append(": mac-field/@class ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						field1.setCombineOrTableFieldClassName(s11);
					}
				} else if (null != s11) {
					if (field1.getFieldType() != 2002 && field1.getFieldType() != 2003 && field1.getFieldType() != 2004
							&& field1.getFieldType() != 2011 && field1.getFieldType() != 2010
							&& field1.getFieldType() != 2008 && field1.getFieldType() != 2001)
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": ")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.startElement.field.canNotHave.class",
										new String[] { field1.getName() }))
								.toString());
					s11 = s11.trim();
					if (s11.length() > 0)
						field1.setCombineOrTableFieldClassName(s11);
				}
				if (field != null) {
					field.setSubField(field1.getName(), field1);
					I.A(field);
				} else {
					K.setField(field1.getName(), field1);
				}
				I.A(field1);
				J = field1;
			} else if ("value-range".equals(s2)) {
				G.clear();
				A = new ValueRange();
				String s5 = attributes.getValue("default-ref");
				if (null != s5) {
					s5 = s5.trim();
					if (0 == s5.trim().length())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": field[@name='").append(J.getName()).append("']/value-range/value-ref ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					A.setValue("default-ref");
					A.setReferenceId(s5);
					Message message1 = MessageMetadataManager.getMessage(D, s5);
					A.setReference(message1);
					G.put("default-ref", A);
				}
			} else if ("value".equals(s2)) {
				A = new ValueRange();
				String s6 = attributes.getValue("value");
				if (null == s6)
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
							.append(J.getName()).append("']/value-range/value/@value ")
							.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s6 = s6.trim();
				if (0 == s6.length())
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
							.append(J.getName()).append("']/value-range/value/@value ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				if (G.containsKey(s6))
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
							.append(J.getName()).append("']/value-range/value[@value='").append(s6).append("'] ")
							.append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.reduplicate"))
							.toString());
				A.setValue(s6);
				A.setShortText(attributes.getValue("short-text"));
				s6 = attributes.getValue("reference");
				if (s6 != null) {
					s6 = s6.trim();
					if (0 == s6.length())
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": fiedl[@name='").append(J.getName())
								.append("']/value-range/value/@reference ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					A.setReferenceId(s6);
					Message message2 = MessageMetadataManager.getMessage(D, s6);
					if (null == message2)
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": ")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.startElement.field.valueRange.reference.notExist",
										new String[] { s6 }))
								.toString());
					A.setReference(message2);
					if (null == G.get("default-ref"))
						throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
								.append(": field[@name='").append(J.getName()).append("']/@default-ref ")
								.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				}
				G.put(A.getValue(), A);
			} else if ("event".equals(s2)) {
				String s7 = attributes.getValue("type");
				if (null == s7)
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": event/@type ")
							.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s7 = s7.trim();
				if (0 == s7.length())
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath).append(": event/@type ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				F = s7;
			} else if ("variable".equals(s2)) {
				H = new Variable();
				String s8 = attributes.getValue("name");
				if (null == s8)
					throw new RuntimeException(
							(new StringBuilder()).append(fileAbsolutePath).append(": variable/@name ")
									.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s8 = s8.trim();
				if (0 == s8.length())
					throw new RuntimeException(
							(new StringBuilder()).append(fileAbsolutePath).append(": variable/@name ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				H.setName(s8);
				s8 = attributes.getValue("data-type");
				if (null == s8)
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
							.append(": variable[@name='").append(H.getName()).append("']/@data-type ")
							.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s8 = s8.trim();
				if (0 == s8.length())
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
							.append(": variable[@name='").append(H.getName()).append("']/@data-type ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				try {
					H.setDataType(Constant.getDataTypeByText(s8));
				} catch (Exception exception) {
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
							.append(": variable[@name='").append(H.getName()).append("']/@data-type ")
							.append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.wrong"))
							.append(exception.getMessage()).toString(), exception);
				}
				s8 = attributes.getValue("value");
				if (null == s8)
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
							.append(": variable[@name='").append(H.getName()).append("']/@value ")
							.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s8 = s8.trim();
				if (0 == s8.length())
					throw new RuntimeException((new StringBuilder()).append(fileAbsolutePath)
							.append(": variable[@name='").append(H.getName()).append("']/@value ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				H.setValue(s8);
				BMap.put(H.getName(), H);
			}
		}

		private _A() {
			K = new Message();
			fileAbsolutePath = null;
			D = null;
			I = new _AField();
			G = new HashMap<>();
			A = null;
			BMap = new HashMap<>();
			H = null;
			J = null;
			F = null;
			E = null;
		}

	}

	public static final Set<String> keyWordSet;
	public static boolean isRequired = false;
	private static Map<String, Map<String, Message>> cache = new HashMap<>(128);
	private static Map<String, Message> cacheByClass = new HashMap<>(1024);
	// private static Map cacheById = new HashMap(1024);
	// private static Map fieldCacheById = new HashMap(1024);
	private static Map<String, File> groupPathCache = new HashMap<>(128);

	public MessageMetadataManager() {
	}

	public static Map<String, Message> getClassCache() {
		return cacheByClass;
	}

	public static Map<String, File> getGroupPathCache() {
		return groupPathCache;
	}

	public static File getGroupPath(String s) {
		return (File) groupPathCache.get(s);
	}

	public static void clear() {
		cache.clear();
		cacheByClass.clear();
		groupPathCache.clear();
	}

	public static Message getMessageByClass(String s) {
		if (null == s) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "messageBeanClassName" }));
		} else {
			Message message = (Message) cacheByClass.get(s);
			return message;
		}
	}

	public static Map<String, Map<String, Message>> getAllMessage() {
		return cache;
	}

	public static boolean isMessageExist(String s, String s1) {
		if (null == s)
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "groupId" }));
		if (null == s1)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "messageId" }));
		Map<String, Message> map = cache.get(s);
		if (null == map)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("MessageMetadataManager.getMessage.group.null", new String[] { s }));
		Message message = (Message) map.get(s1);
		return null != message;
	}

	public static void reload(String s, String s1, String s2) {
		if (null == s)
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "groupId" }));
		if (null == s1)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "messageId" }));
		Map<String, Message> map = cache.get(s);
		if (null == map)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("MessageMetadataManager.getMessage.group.null", new String[] { s }));
		File file = (File) groupPathCache.get(s);
		File file1 = new File((new StringBuilder()).append(file.toString()).append(System.getProperty("file.separator"))
				.append(s1).append(".xml").toString());
		Message message = null;
		try {
			message = (Message) cacheByClass.remove(s2);
			Message message1 = load(s, file1);
			if (null == message1)
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MessageMetadataManager.getMessage.file.isNotExist", new String[] { s1 }));
			if (!s1.equals(message1.getId()))
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"MessageMetadataManager.getMessage.messageId.notEqual.fileName",
						new String[] { s1, message1.getId() }));
			map.put(s1, message1);
			cacheByClass.put(message1.getClassName(), message1);
		} catch (Exception exception) {
			if (null != s2)
				cacheByClass.put(s2, message);
			ExceptionUtil.throwActualException(exception);
		}
	}

	public static Message getMessage(String s, String s1) {
		if (null == s)
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "groupId" }));
		if (null == s1)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "messageId" }));
		Map<String, Message> map = cache.get(s);
		if (null == map)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("MessageMetadataManager.getMessage.group.null", new String[] { s }));
		Message message = (Message) map.get(s1);
		if (null == message) {
			File file = (File) groupPathCache.get(s);
			File file1 = new File((new StringBuilder()).append(file.toString())
					.append(System.getProperty("file.separator")).append(s1).append(".xml").toString());
			message = load(s, file1);
			if (null == message)
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MessageMetadataManager.getMessage.file.isNotExist", new String[] { s1 }));
			if (!s1.equals(message.getId()))
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"MessageMetadataManager.getMessage.messageId.notEqual.fileName",
						new String[] { s1, message.getId() }));
			if (cacheByClass.containsKey(message.getClassName())) {
				Message message1 = (Message) cacheByClass.get(message.getClassName());
				if (!message.equalTo(message1))
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"MessageMetadataManager.getMessage.messageClass.reduplicate",
							new String[] { s1, s, message.getClassName() }));
			}
			map.put(s1, message);
			cacheByClass.put(message.getClassName(), message);
		}
		return message;
	}

	public static void loadMetadataGroup(String s, String s1) {
		if (null == s1) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "path" }));
		} else {
			File file = new File(ConfigManager.getFullPathFileName(s1));
			loadMetadataGroup(s, file);
			return;
		}
	}

	public static void loadMetadataGroup(String s, File file) {
		if (null == s)
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "groupId" }));
		if (null == file)
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "dir" }));
		if (!file.isDirectory())
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString(
					"MessageMetadataManager.loadMetadataGroup.dir.isNotDirectory", new String[] { file.toString() }));
		if (!file.canRead())
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString(
					"MessageMetadataManager.loadMetadataGroup.dir.canNotRead", new String[] { file.toString() }));
		File afile[] = file.listFiles(new FilenameFilter() {

			public boolean accept(File file1, String s3) {
				return s3.endsWith(".xml");
			}

		});
		Map<String, Message> obj = cache.get(s);
		if (null == obj) {
			obj = new HashMap<>(1024);
			cache.put(s, obj);
			groupPathCache.put(s, file);
		}
		for (int i = 0; i < afile.length; i++) {
			String s2 = afile[i].getName();
//			System.out.println(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.loadmessage",
//					new String[] { s2 }));
			String s1 = s2.substring(0, s2.lastIndexOf("."));
			if (null != obj.get(s1))
				continue;
			Message message = load(s, afile[i]);
			if (!s1.equals(message.getId()))
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"MessageMetadataManager.getMessage.messageId.notEqual.fileName",
						new String[] { s1, message.getId(), s1 }));
			if (cacheByClass.containsKey(message.getClassName())) {
				Message message1 = (Message) cacheByClass.get(message.getClassName());
				if (!message.equalTo(message1))
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"MessageMetadataManager.loadMetadataGroup.messageClass.reduplicate",
							new String[] { s1, message1.getGroupId(), message.getClassName() }));
			}
			obj.put(s1, message);
			cacheByClass.put(message.getClassName(), message);
		}

	}

	public static void loadMetadataGroupByDB(String s)
	{
//		Connection connection;
//		connection = null;
//		Object obj = null;
//		connection = ConnectionManager.getInstance().getConnection();
//		MessageDAO messagedao = new MessageDAO();
//		messagedao.setConnection(connection);
//		List list = messagedao.getAllMessageInGroup(s);
//		Object obj1 = null;
//		for (int i = 0; i < list.size(); i++)
//		{
//			Message message1 = (Message)list.get(i);
//			Message message = add2MessageCache(message1, connection);
//			if (cacheByClass.containsKey(message.getClassName()))
//			{
//				Message message2 = (Message)cacheByClass.get(message.getClassName());
//				if (!message.equalTo(message2))
//					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.loadMetadataGroup.messageClass.reduplicate", new String[] {
//						message.getId(), message2.getGroupId(), message.getClassName()
//					}));
//			}
//			cacheByClass.put(message.getClassName(), message);
//		}
//
//		connection.commit();
//		if (null != connection)
//			try
//			{
//				connection.close();
//			}
//			catch (Exception exception)
//			{
//				exception.printStackTrace();
//			}
//		break MISSING_BLOCK_LABEL_275;
//		Exception exception1;
//		exception1;
//		if (null != connection)
//			try
//			{
//				connection.rollback();
//			}
//			catch (Exception exception3)
//			{
//				exception3.printStackTrace();
//			}
//		ExceptionUtil.throwActualException(exception1);
//		if (null != connection)
//			try
//			{
//				connection.close();
//			}
//			catch (Exception exception2)
//			{
//				exception2.printStackTrace();
//			}
//		break MISSING_BLOCK_LABEL_275;
//		Exception exception4;
//		exception4;
//		if (null != connection)
//			try
//			{
//				connection.close();
//			}
//			catch (Exception exception5)
//			{
//				exception5.printStackTrace();
//			}
//		throw exception4;
	}
//
//	private static Message add2MessageCache(Message message, Connection connection) {
//		if (cacheById.containsKey(message.getId()))
//			return (Message) cacheById.get(message.getId());
//		Message message1 = new Message();
//		Object obj = (Map) cache.get(message.getMessageBeanGroupId());
//		if (null == obj) {
//			obj = new HashMap();
//			cache.put(message.getMessageBeanGroupId(), obj);
//		}
//		((Map) (obj)).put(message.getMessageBeanId(), message1);
//		message1.setDatabaseMessageId(message.getId());
//		message1.setId(message.getMessageBeanId());
//		message1.setGroupId(message.getMessageBeanGroupId());
//		message1.setClassName(message.getClassName());
//		if (null != message.getMessageCharset())
//			message1.setMsgCharset(message.getMessageCharset());
//		if (null != message.getPostPack())
//			try {
//				message1.setPostPackEvent(new String(message.getPostPack(), "utf-8"));
//			} catch (UnsupportedEncodingException unsupportedencodingexception) {
//				unsupportedencodingexception.printStackTrace();
//			}
//		if (null != message.getPostParse())
//			try {
//				message1.setPostParseEvent(new String(message.getPostParse(), "utf-8"));
//			} catch (UnsupportedEncodingException unsupportedencodingexception1) {
//				unsupportedencodingexception1.printStackTrace();
//			}
//		if (null != message.getPrePack())
//			try {
//				message1.setPrePackEvent(new String(message.getPrePack(), "utf-8"));
//			} catch (UnsupportedEncodingException unsupportedencodingexception2) {
//				unsupportedencodingexception2.printStackTrace();
//			}
//		if (null != message.getPreParse())
//			try {
//				message1.setPreParseEvent(new String(message.getPreParse(), "utf-8"));
//			} catch (UnsupportedEncodingException unsupportedencodingexception3) {
//				unsupportedencodingexception3.printStackTrace();
//			}
//		if (null != message.getPrefix()) {
//			message1.setPrefix(message.getPrefix());
//			message1.setPrefixString(new String(CodeUtil.BytetoHex(message.getPrefix())));
//		}
//		if (null != message.getSuffix()) {
//			message1.setSuffix(message.getSuffix());
//			message1.setSuffixString(new String(CodeUtil.BytetoHex(message.getSuffix())));
//		}
//		message1.setShortText(message.getDescription());
//		if (null != message.getType())
//			message1.setType(Integer.parseInt(message.getType()));
//		if (null != message.getTemplate())
//			try {
//				message1.setTemplate(new String(message.getTemplate(), "utf-8"));
//			} catch (UnsupportedEncodingException unsupportedencodingexception4) {
//				unsupportedencodingexception4.printStackTrace();
//			}
//		message1.setXpath(message.getXpath());
//		if ("0".equals(message.getHasVariable())) {
//			MessageVariableRelationDAO messagevariablerelationdao = new MessageVariableRelationDAO();
//			messagevariablerelationdao.setConnection(connection);
//			List list = messagevariablerelationdao.getMessageVariable(message.getId());
//			VariableDAO variabledao = new VariableDAO();
//			variabledao.setConnection(connection);
//			Object obj2 = null;
//			HashMap hashmap = new HashMap();
//			for (int j = 0; j < list.size(); j++) {
//				Variable variable = variabledao.selectByPK(((MessageVariableRelation) list.get(j)).getVariableId());
//				A a = new A();
//				a.B(variable.getName());
//				if (null != variable.getDataType())
//					a.A(Integer.parseInt(variable.getDataType()));
//				a.A(variable.getValue());
//				hashmap.put(variable.getName(), a);
//			}
//
//			message1.setVariable(hashmap);
//		}
//		MessageBeanFieldRelationDAO messagebeanfieldrelationdao = new MessageBeanFieldRelationDAO();
//		messagebeanfieldrelationdao.setConnection(connection);
//		List list1 = messagebeanfieldrelationdao.getAllMessageFieldId(message.getId());
//		Object obj1 = null;
//		for (int i = 0; i < list1.size(); i++) {
//			MessageBeanFieldRelation messagebeanfieldrelation = (MessageBeanFieldRelation) list1.get(i);
//			Field field = transformField(messagebeanfieldrelation.getFieldId(), connection);
//			message1.setField(field.getName(), field);
//		}
//
//		return message1;
//	}
//
//	private static Field transformField(String s, Connection connection) {
//		if (fieldCacheById.containsKey(s))
//			return (Field) fieldCacheById.get(s);
//		MessageFieldDAO messagefielddao = new MessageFieldDAO();
//		messagefielddao.setConnection(connection);
//		MessageField messagefield = messagefielddao.selectByPK(s);
//		Field field = new Field();
//		fieldCacheById.put(s, field);
//		if ("1".equals(messagefield.getEditable()))
//			field.setEditable(false);
//		else if ("0".equals(messagefield.getEditable()))
//			field.setEditable(true);
//		if (null != messagefield.getFieldType())
//			field.setFieldType(Integer.parseInt(messagefield.getFieldType()));
//		field.setIso8583_no(messagefield.getIso8583No());
//		field.setName(messagefield.getName());
//		if (null != messagefield.getPostPack())
//			try {
//				field.setPostPackEvent(new String(messagefield.getPostPack(), "utf-8"));
//			} catch (UnsupportedEncodingException unsupportedencodingexception) {
//				unsupportedencodingexception.printStackTrace();
//			}
//		if (null != messagefield.getPostParse())
//			try {
//				field.setPostParseEvent(new String(messagefield.getPostParse(), "utf-8"));
//			} catch (UnsupportedEncodingException unsupportedencodingexception1) {
//				unsupportedencodingexception1.printStackTrace();
//			}
//		if (null != messagefield.getPrePack())
//			try {
//				field.setPrePackEvent(new String(messagefield.getPrePack(), "utf-8"));
//			} catch (UnsupportedEncodingException unsupportedencodingexception2) {
//				unsupportedencodingexception2.printStackTrace();
//			}
//		if (null != messagefield.getPreParse())
//			try {
//				field.setPreParseEvent(new String(messagefield.getPreParse(), "utf-8"));
//			} catch (UnsupportedEncodingException unsupportedencodingexception3) {
//				unsupportedencodingexception3.printStackTrace();
//			}
//		if ("1".equals(messagefield.getRequired()))
//			field.setRequired(false);
//		else if ("0".equals(messagefield.getRequired()))
//			field.setRequired(true);
//		field.setShortText(messagefield.getDescription());
//		switch (field.getFieldType()) {
//		case 2006:
//			field = transformBitmapField(field, s, connection);
//			break;
//
//		case 2002:
//		case 2008:
//			field = transformCombineField(field, s, connection);
//			break;
//
//		case 2000:
//			field = transformFixedField(field, s, connection);
//			break;
//
//		case 2001:
//			field = transformVarField(field, s, connection);
//			break;
//
//		case 2007:
//			field = transformLengthField(field, s, connection);
//			break;
//
//		case 2003:
//		case 2009:
//			field = transformVarCombineField(field, s, connection);
//			break;
//
//		case 2010:
//			field = transformMacField(field, s, connection);
//			break;
//
//		case 2004:
//			field = transformTableField(field, s, connection);
//			break;
//
//		case 2005:
//			field = transformTableRowNumField(field, s, connection);
//			break;
//
//		case 2011:
//			field = transformVarTable(field, s, connection);
//			break;
//
//		default:
//			throw new RuntimeException(
//					(new StringBuilder()).append("unkown field type : ").append(field.getFieldType()).toString());
//		}
//		return field;
//	}
//
//	private static Field transformVarTable(Field field, String s, Connection connection) {
//		VarTableDAO vartabledao = new VarTableDAO();
//		vartabledao.setConnection(connection);
//		VarTable vartable = vartabledao.selectByPK(s);
//		field.setDataCharset(vartable.getDataCharset());
//		if (null != vartable.getTabPrefix()) {
//			field.setTabPrefix(vartable.getTabPrefix());
//			field.setTabPrefixString(new String(CodeUtil.BytetoHex(vartable.getTabPrefix())));
//		}
//		if (null != vartable.getTabSuffix()) {
//			field.setTabSuffix(vartable.getTabSuffix());
//			field.setTabSuffixString(new String(CodeUtil.BytetoHex(vartable.getTabSuffix())));
//		}
//		if (null != vartable.getPrefix()) {
//			field.setPrefix(vartable.getPrefix());
//			field.setPrefixString(new String(CodeUtil.BytetoHex(vartable.getPrefix())));
//		}
//		if (null != vartable.getSuffix()) {
//			field.setSuffix(vartable.getSuffix());
//			field.setSuffixString(new String(CodeUtil.BytetoHex(vartable.getSuffix())));
//		}
//		if ("0".equals(vartable.getFirRowPrefix()))
//			field.setFirRowPrefix(true);
//		else if ("1".equals(vartable.getFirRowPrefix()))
//			field.setFirRowPrefix(false);
//		if ("0".equals(vartable.getLastRowSuffix()))
//			field.setLastRowSuffix(true);
//		else if ("1".equals(vartable.getLastRowSuffix()))
//			field.setLastRowSuffix(false);
//		if ("0".equals(vartable.getRowCut()))
//			field.setRowCut(true);
//		else if ("1".equals(vartable.getRowCut()))
//			field.setRowCut(false);
//		if (null != vartable.getRefLengthFieldId()) {
//			field.setRefLengthField(transformField(vartable.getRefLengthFieldId(), connection));
//			field.setRefLengthFieldName(field.getRefLengthField().getName());
//		}
//		field.setRefLengthFieldOffset(vartable.getRefLengthFieldOffset());
//		if (null != vartable.getReferenceMessageId())
//			retrieveReferenceMessage(field, vartable.getReferenceType(), vartable.getReferenceMessageId(), connection);
//		field.setReferenceType(vartable.getReferenceType());
//		field.setCombineOrTableFieldClassName(vartable.getReferenceMbClassName());
//		field.setXpath(vartable.getXpath());
//		if ("0".equals(vartable.getHasSubFields()))
//			field.setSubFields(retrieveSubFields(s, connection));
//		return field;
//	}
//
//	private static Field transformTableRowNumField(Field field, String s, Connection connection) {
//		TableRowNumFieldDAO tablerownumfielddao = new TableRowNumFieldDAO();
//		tablerownumfielddao.setConnection(connection);
//		TableRowNumField tablerownumfield = tablerownumfielddao.selectByPK(s);
//		field.setDataCharset(tablerownumfield.getDataCharset());
//		if (null != tablerownumfield.getDataType())
//			field.setDataType(Integer.parseInt(tablerownumfield.getDataType()));
//		if (null != tablerownumfield.getDataEncoding())
//			field.setDataEncoding(Integer.parseInt(tablerownumfield.getDataEncoding()));
//		if (null != tablerownumfield.getPadding())
//			try {
//				field.setPadding(CodeUtil.HextoByte(tablerownumfield.getPadding().getBytes("utf-8"))[0]);
//			} catch (UnsupportedEncodingException unsupportedencodingexception) {
//				unsupportedencodingexception.printStackTrace();
//			}
//		if (null != tablerownumfield.getPaddingDirection())
//			field.setPaddingDirection(Integer.parseInt(tablerownumfield.getPaddingDirection()));
//		if (null != tablerownumfield.getPrefix()) {
//			field.setPrefix(tablerownumfield.getPrefix());
//			field.setPrefixString(new String(CodeUtil.BytetoHex(tablerownumfield.getPrefix())));
//		}
//		if (null != tablerownumfield.getSuffix()) {
//			field.setSuffix(tablerownumfield.getSuffix());
//			field.setSuffixString(new String(CodeUtil.BytetoHex(tablerownumfield.getSuffix())));
//		}
//		if (null != tablerownumfield.getDataEncoding())
//			field.setDataEncoding(Integer.parseInt(tablerownumfield.getDataEncoding()));
//		field.setPattern(tablerownumfield.getPattern());
//		field.setLength(tablerownumfield.getLength());
//		if ("0".equals(tablerownumfield.getStrictDataLength()))
//			field.setStrictDataLength(true);
//		else if ("1".equals(tablerownumfield.getStrictDataLength()))
//			field.setStrictDataLength(false);
//		field.setValue(tablerownumfield.getDefaultValue());
//		if (null != tablerownumfield.getExtendedAttributes()) {
//			field.setExtendedAttributeText(tablerownumfield.getExtendedAttributes());
//			field.setExtendedAttributes(parseExtendedAttributes(tablerownumfield.getExtendedAttributes()));
//		}
//		if ("0".equals(tablerownumfield.getHasValueRange()))
//			field.setValueRange(retrieveValueRange(s, tablerownumfield.getDefaultRefFieldId(), connection));
//		if ("0".equals(tablerownumfield.getIsRemoveUnwatchable()))
//			field.setRemoveUnwatchable(true);
//		else if ("1".equals(tablerownumfield.getIsRemoveUnwatchable()))
//			field.setRemoveUnwatchable(false);
//		field.setXpath(tablerownumfield.getXpath());
//		field.setTag(tablerownumfield.getTag());
//		if (null != tablerownumfield.getTableFieldId()) {
//			field.setTable(transformField(tablerownumfield.getTableFieldId(), connection));
//			field.setTableName(field.getTable().getName());
//		}
//		if ("0".equals(tablerownumfield.getHasSubFields()))
//			field.setSubFields(retrieveSubFields(tablerownumfield.getId(), connection));
//		return field;
//	}
//
//	private static Field transformTableField(Field field, String s, Connection connection) {
//		TableFieldDAO tablefielddao = new TableFieldDAO();
//		tablefielddao.setConnection(connection);
//		TableField tablefield = tablefielddao.selectByPK(s);
//		field.setDataCharset(tablefield.getDataCharset());
//		if (null != tablefield.getRowNumFieldId())
//			field.setRowNumField(transformField(tablefield.getRowNumFieldId(), connection));
//		field.setRowNumFieldName(tablefield.getRowNumFieldName());
//		field.setCombineOrTableFieldClassName(tablefield.getReferenceMbClassName());
//		field.setReferenceType(tablefield.getReferenceType());
//		field.setTag(tablefield.getTag());
//		field.setXpath(tablefield.getXpath());
//		if (null != tablefield.getReferenceMessageId())
//			retrieveReferenceMessage(field, tablefield.getReferenceType(), tablefield.getReferenceMessageId(),
//					connection);
//		field.setExpression(tablefield.getExpression());
//		if ("0".equals(tablefield.getHasSubFields()))
//			field.setSubFields(retrieveSubFields(s, connection));
//		if ("0".equals(tablefield.getHasValueRange()))
//			field.setValueRange(retrieveValueRange(s, tablefield.getDefaultRefFieldId(), connection));
//		return field;
//	}
//
//	private static Field transformMacField(Field field, String s, Connection connection) {
//		MacFieldDAO macfielddao = new MacFieldDAO();
//		macfielddao.setConnection(connection);
//		MacField macfield = macfielddao.selectByPK(s);
//		field.setDataCharset(macfield.getDataCharset());
//		if (null != macfield.getStartFieldId()) {
//			field.setStartField(transformField(macfield.getStartFieldId(), connection));
//			field.setStartFieldName(field.getStartField().getName());
//		}
//		if (null != macfield.getEndFieldId()) {
//			field.setEndField(transformField(macfield.getEndFieldId(), connection));
//			field.setEndFieldName(field.getEndField().getName());
//		}
//		if (null != macfield.getMacData())
//			field.setMacFldDataCache(parseMacField(macfield.getMacData()));
//		field.setCalcType(macfield.getCalcType());
//		field.setCombineOrTableFieldClassName(macfield.getMacClassName());
//		field.setXpath(macfield.getXpath());
//		return field;
//	}
//
//	private static Field transformVarCombineField(Field field, String s, Connection connection) {
//		VarCombineFieldDAO varcombinefielddao = new VarCombineFieldDAO();
//		varcombinefielddao.setConnection(connection);
//		VarCombineField varcombinefield = varcombinefielddao.selectByPK(s);
//		field.setDataCharset(varcombinefield.getDataCharset());
//		if (null != varcombinefield.getRefLengthFieldId()) {
//			field.setRefLengthField(transformField(varcombinefield.getRefLengthFieldId(), connection));
//			field.setRefLengthFieldName(field.getRefLengthField().getName());
//		}
//		field.setRefLengthFieldOffset(varcombinefield.getRefLengthFieldOffset());
//		if (null != varcombinefield.getLengthFieldDataType())
//			field.setLengthFieldDataType(Integer.parseInt(varcombinefield.getLengthFieldDataType()));
//		if (null != varcombinefield.getLengthFieldDataEncoding())
//			field.setLengthFieldDataEncoding(Integer.parseInt(varcombinefield.getLengthFieldDataEncoding()));
//		field.setLengthFieldLength(varcombinefield.getLengthFieldLength());
//		if (null != varcombinefield.getReferenceMessageId())
//			retrieveReferenceMessage(field, varcombinefield.getReferenceType(), varcombinefield.getReferenceMessageId(),
//					connection);
//		field.setCombineOrTableFieldClassName(varcombinefield.getReferenceMbClassName());
//		field.setReferenceType(varcombinefield.getReferenceType());
//		field.setExpression(varcombinefield.getExpression());
//		if ("0".equals(varcombinefield.getHasSubFields()))
//			field.setSubFields(retrieveSubFields(s, connection));
//		return field;
//	}
//
//	private static Field transformLengthField(Field field, String s, Connection connection) {
//		LengthFieldDAO lengthfielddao = new LengthFieldDAO();
//		lengthfielddao.setConnection(connection);
//		LengthField lengthfield = lengthfielddao.selectByPK(s);
//		if (null != lengthfield.getDataType())
//			field.setDataType(Integer.parseInt(lengthfield.getDataType()));
//		field.setDataCharset(lengthfield.getDataCharset());
//		if (null != lengthfield.getPadding())
//			try {
//				field.setPadding(CodeUtil.HextoByte(lengthfield.getPadding().getBytes("utf-8"))[0]);
//			} catch (UnsupportedEncodingException unsupportedencodingexception) {
//				unsupportedencodingexception.printStackTrace();
//			}
//		if (null != lengthfield.getPaddingDirection())
//			field.setPaddingDirection(Integer.parseInt(lengthfield.getPaddingDirection()));
//		if (null != lengthfield.getPrefix()) {
//			field.setPrefix(lengthfield.getPrefix());
//			field.setPrefixString(new String(CodeUtil.BytetoHex(lengthfield.getPrefix())));
//		}
//		if (null != lengthfield.getSuffix()) {
//			field.setSuffix(lengthfield.getSuffix());
//			field.setSuffixString(new String(CodeUtil.BytetoHex(lengthfield.getSuffix())));
//		}
//		if (null != lengthfield.getDataEncoding())
//			field.setDataEncoding(Integer.parseInt(lengthfield.getDataEncoding()));
//		field.setPattern(lengthfield.getPattern());
//		field.setLength(lengthfield.getLength());
//		if ("0".equals(lengthfield.getStrictDataLength()))
//			field.setStrictDataLength(true);
//		else if ("1".equals(lengthfield.getStrictDataLength()))
//			field.setStrictDataLength(false);
//		field.setValue(lengthfield.getDefaultValue());
//		if (null != lengthfield.getExtendedAttributes()) {
//			field.setExtendedAttributeText(lengthfield.getExtendedAttributes());
//			field.setExtendedAttributes(parseExtendedAttributes(lengthfield.getExtendedAttributes()));
//		}
//		if ("0".equals(lengthfield.getHasValueRange()))
//			field.setValueRange(retrieveValueRange(s, lengthfield.getDefaultRefFieldId(), connection));
//		if ("0".equals(lengthfield.getIsRemoveUnwatchable()))
//			field.setRemoveUnwatchable(true);
//		else if ("1".equals(lengthfield.getIsRemoveUnwatchable()))
//			field.setRemoveUnwatchable(false);
//		field.setXpath(lengthfield.getXpath());
//		if (null != lengthfield.getStartFieldId()) {
//			field.setStartField(transformField(lengthfield.getStartFieldId(), connection));
//			field.setStartFieldName(field.getStartField().getName());
//		}
//		if (null != lengthfield.getEndFieldId()) {
//			field.setEndField(transformField(lengthfield.getEndFieldId(), connection));
//			field.setEndFieldName(field.getEndField().getName());
//		}
//		return field;
//	}

//	private static Field transformVarField(Field field, String s, Connection connection) {
//		VarFieldDAO varfielddao = new VarFieldDAO();
//		varfielddao.setConnection(connection);
//		VarField varfield = varfielddao.selectByPK(s);
//		if (null != varfield.getDataType())
//			field.setDataType(Integer.parseInt(varfield.getDataType()));
//		field.setDataCharset(varfield.getDataCharset());
//		if (null != varfield.getPrefix()) {
//			field.setPrefix(varfield.getPrefix());
//			field.setPrefixString(new String(CodeUtil.BytetoHex(varfield.getPrefix())));
//		}
//		if (null != varfield.getSuffix()) {
//			field.setSuffix(varfield.getSuffix());
//			field.setSuffixString(new String(CodeUtil.BytetoHex(varfield.getSuffix())));
//		}
//		if (null != varfield.getRefLengthFieldId()) {
//			field.setRefLengthField(transformField(varfield.getRefLengthFieldId(), connection));
//			field.setRefLengthFieldName(field.getRefLengthField().getName());
//		}
//		field.setRefLengthFieldOffset(varfield.getRefLengthFieldOffset());
//		if (null != varfield.getLengthFieldDataType())
//			field.setLengthFieldDataType(Integer.parseInt(varfield.getLengthFieldDataType()));
//		if (null != varfield.getLengthFieldDataEncoding())
//			field.setLengthFieldDataEncoding(Integer.parseInt(varfield.getLengthFieldDataEncoding()));
//		field.setLengthFieldLength(varfield.getLengthFieldLength());
//		field.setMinLength(varfield.getMinLength());
//		field.setMaxLength(varfield.getMaxLength());
//		if (null != varfield.getDataEncoding())
//			field.setDataEncoding(Integer.parseInt(varfield.getDataEncoding()));
//		field.setPattern(varfield.getPattern());
//		if (null != varfield.getExtendedAttributes()) {
//			field.setExtendedAttributeText(varfield.getExtendedAttributes());
//			field.setExtendedAttributes(parseExtendedAttributes(varfield.getExtendedAttributes()));
//		}
//		if ("0".equals(varfield.getHasValueRange()))
//			field.setValueRange(retrieveValueRange(s, varfield.getDefaultRefFieldId(), connection));
//		if ("0".equals(varfield.getIsRemoveUnwatchable()))
//			field.setRemoveUnwatchable(true);
//		else if ("1".equals(varfield.getIsRemoveUnwatchable()))
//			field.setRemoveUnwatchable(false);
//		return field;
//	}

//	private static Field transformFixedField(Field field, String s, Connection connection) {
//		FixedFieldDAO fixedfielddao = new FixedFieldDAO();
//		fixedfielddao.setConnection(connection);
//		FixedField fixedfield = fixedfielddao.selectByPK(s);
//		field.setDataCharset(fixedfield.getDataCharset());
//		if (null != fixedfield.getDataEncoding())
//			field.setDataEncoding(Integer.parseInt(fixedfield.getDataEncoding()));
//		if (null != fixedfield.getDataType())
//			field.setDataType(Integer.parseInt(fixedfield.getDataType()));
//		field.setValue(fixedfield.getDefaultValue());
//		if (null != fixedfield.getExtendedAttributes()) {
//			field.setExtendedAttributeText(fixedfield.getExtendedAttributes());
//			field.setExtendedAttributes(parseExtendedAttributes(fixedfield.getExtendedAttributes()));
//		}
//		if ("0".equals(fixedfield.getHasValueRange()))
//			field.setValueRange(retrieveValueRange(s, fixedfield.getDefaultRefFieldId(), connection));
//		if ("0".equals(fixedfield.getIsRemoveUnwatchable()))
//			field.setRemoveUnwatchable(true);
//		else if ("1".equals(fixedfield.getIsRemoveUnwatchable()))
//			field.setRemoveUnwatchable(false);
//		if (null != fixedfield.getPadding())
//			try {
//				field.setPadding(CodeUtil.HextoByte(fixedfield.getPadding().getBytes("utf-8"))[0]);
//			} catch (UnsupportedEncodingException unsupportedencodingexception) {
//				unsupportedencodingexception.printStackTrace();
//			}
//		if (null != fixedfield.getPaddingDirection())
//			field.setPaddingDirection(Integer.parseInt(fixedfield.getPaddingDirection()));
//		field.setPattern(fixedfield.getPattern());
//		field.setLength(fixedfield.getLength());
//		if (null != fixedfield.getPrefix()) {
//			field.setPrefix(fixedfield.getPrefix());
//			field.setPrefixString(new String(CodeUtil.BytetoHex(fixedfield.getPrefix())));
//		}
//		if (null != fixedfield.getSuffix()) {
//			field.setSuffix(fixedfield.getSuffix());
//			field.setSuffixString(new String(CodeUtil.BytetoHex(fixedfield.getSuffix())));
//		}
//		if ("0".equals(fixedfield.getStrictDataLength()))
//			field.setStrictDataLength(true);
//		else if ("1".equals(fixedfield.getStrictDataLength()))
//			field.setStrictDataLength(false);
//		field.setTag(fixedfield.getTag());
//		field.setXpath(fixedfield.getXpath());
//		return field;
//	}

//	private static Field transformCombineField(Field field, String s, Connection connection) {
//		CombineFieldDAO combinefielddao = new CombineFieldDAO();
//		combinefielddao.setConnection(connection);
//		CombineField combinefield = combinefielddao.selectByPK(s);
//		field.setDataCharset(combinefield.getDataCharset());
//		field.setExpression(combinefield.getExpression());
//		if ("0".equals(combinefield.getHasSubFields()))
//			field.setSubFields(retrieveSubFields(s, connection));
//		field.setCombineOrTableFieldClassName(combinefield.getReferenceMbClassName());
//		field.setReferenceType(combinefield.getReferenceType());
//		field.setTag(combinefield.getTag());
//		field.setXpath(combinefield.getXpath());
//		if (null != combinefield.getReferenceMessageId())
//			retrieveReferenceMessage(field, combinefield.getReferenceType(), combinefield.getReferenceMessageId(),
//					connection);
//		return field;
//	}

//	private static void retrieveReferenceMessage(Field field, String s, String s1, Connection connection) {
//		if ("dynamic".equals(s)) {
//			MessageFieldDAO messagefielddao = new MessageFieldDAO();
//			messagefielddao.setConnection(connection);
//			MessageField messagefield = messagefielddao.selectByPK(s1);
//			if (null == messagefield)
//				throw new RuntimeException((new StringBuilder()).append("field[name = ").append(field.getName())
//						.append("]'s reference field id : ").append(s1).append(" is not exist!").toString());
//			field.setReferenceId(messagefield.getName());
//		} else {
//			MessageDAO messagedao = new MessageDAO();
//			messagedao.setConnection(connection);
//			Message message = messagedao.selectByPK(s1);
//			if (null == message)
//				throw new RuntimeException((new StringBuilder()).append("field[name = ").append(field.getName())
//						.append("]'s reference message id : ").append(s1).append(" is not exist!").toString());
//			field.setReference(add2MessageCache(message, connection));
//			field.setReferenceId(field.getReference().getId());
//		}
//	}

//	private static SortHashMap retrieveSubFields(String s, Connection connection) {
//		SortHashMap sorthashmap = new SortHashMap();
//		FieldRelationDAO fieldrelationdao = new FieldRelationDAO();
//		fieldrelationdao.setConnection(connection);
//		List list = fieldrelationdao.getAllSubFieldsByFieldId(s);
//		MessageFieldDAO messagefielddao = new MessageFieldDAO();
//		messagefielddao.setConnection(connection);
//		Object obj = null;
//		Object obj1 = null;
//		for (int i = 0; i < list.size(); i++) {
//			FieldRelation fieldrelation = (FieldRelation) list.get(i);
//			MessageField messagefield = messagefielddao.selectByPK(fieldrelation.getSubFieldId());
//			sorthashmap.put(messagefield.getName(), transformField(messagefield.getId(), connection));
//		}
//
//		return sorthashmap;
//	}
//
//	private static Field transformBitmapField(Field field, String s, Connection connection) {
//		BitmapFieldDAO bitmapfielddao = new BitmapFieldDAO();
//		bitmapfielddao.setConnection(connection);
//		BitmapField bitmapfield = bitmapfielddao.selectByPK(s);
//		if (null != bitmapfield.getDataType())
//			field.setDataType(Integer.parseInt(bitmapfield.getDataType()));
//		field.setLength(bitmapfield.getLength());
//		field.setValue(bitmapfield.getDefaultValue());
//		return field;
//	}
//
//	private static Map retrieveValueRange(String s, String s1, Connection connection) {
//		FieldValueRelationDAO fieldvaluerelationdao = new FieldValueRelationDAO();
//		fieldvaluerelationdao.setConnection(connection);
//		List list = fieldvaluerelationdao.getAllValueId(s);
//		if (null != list) {
//			HashMap hashmap = new HashMap(list.size());
//			ValueTabDAO valuetabdao = new ValueTabDAO();
//			valuetabdao.setConnection(connection);
//			ValueTab valuetab = null;
//			for (int i = 0; i < list.size(); i++) {
//				valuetab = valuetabdao.selectByPK(((FieldValueRelation) list.get(i)).getValueId());
//				ValueRange valuerange1 = new ValueRange();
//				valuerange1.setShortText(valuetab.getDescription());
//				valuerange1.setValue(valuetab.getValue());
//				if (null != valuetab.getReferenceMessageId()) {
//					MessageDAO messagedao1 = new MessageDAO();
//					messagedao1.setConnection(connection);
//					valuerange1.setReference(
//							add2MessageCache(messagedao1.selectByPK(valuetab.getReferenceMessageId()), connection));
//					valuerange1.setReferenceId(valuerange1.getReference().getId());
//				}
//				hashmap.put(valuerange1.getValue(), valuerange1);
//			}
//
//			if (null != s1) {
//				ValueRange valuerange = new ValueRange();
//				valuerange.setValue("default-ref");
//				MessageDAO messagedao = new MessageDAO();
//				messagedao.setConnection(connection);
//				valuerange.setReference(
//						add2MessageCache(messagedao.selectByPK(valuetab.getReferenceMessageId()), connection));
//				valuerange.setReferenceId(valuerange.getReference().getId());
//				hashmap.put(valuerange.getValue(), valuerange);
//			}
//			return hashmap;
//		} else {
//			return null;
//		}
//	}

	private static Map<String, String> parseExtendedAttributes(String s) {
		HashMap<String, String> hashmap = new HashMap<>();
		String as[] = s.split(";");
		for (int i = 0; i < as.length; i++)
			hashmap.put(as[i].substring(0, as[i].indexOf(":")), as[i].substring(as[i].indexOf(":") + 1));

		return hashmap;
	}

	private static Map<String, String> parseMacField(String s) {
		String as[] = s.split(";");
		HashMap<String, String> hashmap = new HashMap<>();
		for (int i = 0; i < as.length; i++)
			hashmap.put(as[i], as[i]);

		return hashmap;
	}

	private static Message load(String s, File file) {
		_A _la = new _A();
		return _la.A(s, file);
	}

	static {
		keyWordSet = new HashSet<>();
		keyWordSet.add("parent");
		keyWordSet.add("Parent");
		keyWordSet.add("metadata");
		keyWordSet.add("Metadata");
		try {
			if (FileUtil.isExist(ConfigManager.getFullPathFileName("messagebean.properties"))) {
				Properties properties = ConfigManager.loadProperties("messagebean.properties");
				if (null != properties.getProperty("required") && 0 != properties.getProperty("required").length()
						&& "1".equalsIgnoreCase(properties.getProperty("required")))
					isRequired = true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			ExceptionUtil.throwActualException(exception);
		}
	}
}
