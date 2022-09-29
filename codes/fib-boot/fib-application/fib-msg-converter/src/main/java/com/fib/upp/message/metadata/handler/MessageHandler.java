package com.fib.upp.message.metadata.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.fib.upp.message.metadata.Variable;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.common.util.SortHashMap;
import com.giantstone.common.util.StringUtil;
import com.giantstone.message.metadata.Constant;
import com.giantstone.message.metadata.Field;
import com.giantstone.message.metadata.Message;
import com.giantstone.message.metadata.ValueRange;

public class MessageHandler extends DefaultHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

	private static class _AField {
		private List<Field> A;

		public Object removeLast() {
			if (!A.isEmpty())
				return A.remove(A.size() - 1);
			else
				return null;
		}

		public void add(Field obj) {
			A.add(obj);
		}

		private _AField() {
			A = new ArrayList<>();
		}
	}

	private Message messge;
	private String fileAbsolutePath;
	private String D;
	_AField I;
	Map G;
	ValueRange A;
	Map<String, Variable> BMap;
	Variable H;
	Field J;
	String F;
	String E;

	public Message parseMessage(String s, File file) {
		LOGGER.info("fileName={}", file.getName());
		FileInputStream fileinputstream;
		fileAbsolutePath = file.getAbsolutePath();
		fileinputstream = null;
		Message message;
		try {
			fileinputstream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		message = parseSax(s, fileinputstream);
		if (fileinputstream != null)
			try {
				fileinputstream.close();
			} catch (Exception exception2) {
				exception2.printStackTrace(System.err);
			}
		return message;
	}

	public Message parseSax(String s, InputStream inputstream) {
		D = s;
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

		try {
			SAXParser saxparser = saxParserFactory.newSAXParser();
			saxparser.parse(inputstream, this);
		} catch (Exception exception) {
			exception.printStackTrace(System.err);
			ExceptionUtil.throwActualException(exception);
		}
		check();
		postParse(messge.getFields());
		A(messge.getClassName(), messge.getFields());
		return messge;
	}

	private void A(String s, SortHashMap sorthashmap) {
		Iterator<Field> iterator = sorthashmap.values().iterator();
		do {
			if (!iterator.hasNext())
				break;
			Field field = iterator.next();
			if ((2002 == field.getFieldType() || 2003 == field.getFieldType() || 2004 == field.getFieldType() || 2011 == field.getFieldType())
					&& !"dynamic".equalsIgnoreCase(field.getReferenceType()) && !"expression".equalsIgnoreCase(field.getReferenceType())
					&& null == field.getReference()) {
				Message message = new Message();
				message.setId((new StringBuilder()).append(messge.getId()).append("-").append(field.getName()).toString());
				message.setShortText(field.getShortText());
				message.setFields(field.getSubFields());
				String s1 = field.getCombineOrTableFieldClassName();
				if (null == s1)
					s1 = (new StringBuilder()).append(s).append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
				message.setClassName(s1);
				MessageMetadataManager.cacheByClass.put(message.getClassName(), message);
				A(message.getClassName(), message.getFields());
			}
		} while (true);
	}

	private void check() {
		label0: {
			if (1002 == messge.getType() || 1003 == messge.getType()) {
				Object obj = null;
				Iterator iterator = messge.getFields().values().iterator();
				HashSet hashset = new HashSet(messge.getFields().size());
				Field field;
				for (; iterator.hasNext(); hashset.add(field.getTag())) {
					field = (Field) iterator.next();
					if (null == field.getTag())
						throw new CommonException((new StringBuilder())
								.append(fileAbsolutePath).append(" :").append(MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.check.tag.noTag", new String[] { messge.getId(), field.getName() }))
								.toString());
					if (1002 == messge.getType() && 2004 != field.getFieldType() && 2005 != field.getFieldType() && 2000 != field.getFieldType()
							&& 2002 != field.getFieldType() && 2008 != field.getFieldType())
						throw new CommonException((new StringBuilder())
								.append(fileAbsolutePath).append(" :").append(MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.check.tag.illegalType", new String[] { messge.getId(), field.getName() }))
								.toString());
					if (hashset.contains(field.getTag()))
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.check.tag.reduplicate",
										new String[] { messge.getId(), field.getName(), field.getTag() }))
								.toString());
				}

			}
			if (1001 != messge.getType())
				break label0;
			Field field1 = null;
			Iterator iterator1 = messge.getFields().values().iterator();
			do {
				if (!iterator1.hasNext())
					break label0;
				field1 = (Field) iterator1.next();
			} while (2001 != field1.getFieldType() && 2003 != field1.getFieldType() && 2009 != field1.getFieldType());
			throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
					.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.check.xml.illegalFieldType")).toString());
		}
	}

	private void postParse(SortHashMap sorthashmap) {
		Iterator iterator = sorthashmap.values().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Field field = (Field) iterator.next();
			i++;
			if (("dynamic".equalsIgnoreCase(field.getReferenceType()) || "expression".equalsIgnoreCase(field.getReferenceType()))
					&& 2002 != field.getFieldType() && 2003 != field.getFieldType() && 2004 != field.getFieldType())
				throw new CommonException((new StringBuilder())
						.append(fileAbsolutePath).append(" :").append(MultiLanguageResourceBundle.getInstance()
								.getString("MessageMetadataManager.postParse.dynamicField.illegalType", new String[] { field.getName() }))
						.toString());
			if (2001 == field.getFieldType() || 2003 == field.getFieldType() || 2009 == field.getFieldType())
				if (field.getRefLengthFieldName() != null) {
					Field field1 = (Field) sorthashmap.get(field.getRefLengthFieldName());
					if (null == field1)
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.postParse.refLengthField.notExist",
										new String[] { field.getName(), field.getRefLengthFieldName() }))
								.toString());
					if (2007 != field1.getFieldType())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
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
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.postParse.lengthFieldLength.notEqual8", new String[] { field.getName() }))
									.toString());
						break;

					case 3003, 3007:
						if (field.getLengthFieldLength() != 4)
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.postParse.lengthFieldLength.notEqual4", new String[] { field.getName() }))
									.toString());
						break;

					case 3004:
						if (field.getLengthFieldLength() != 1)
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.postParse.lengthFieldLength.notEqual1", new String[] { field.getName() }))
									.toString());
						break;

					case 3005, 3008:
						if (field.getLengthFieldLength() != 2)
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.postParse.lengthFieldLength.notEqual2", new String[] { field.getName() }))
									.toString());
						break;

					case 3002, 3006:
					default:
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.postParse.lengthFieldLength.illegalDataType", new String[] { field.getName() }))
								.toString());
					}
				}
			if (2007 == field.getFieldType())
				checkLengthField(sorthashmap, field);
			if (2005 == field.getFieldType()) {
				if (3001 != field.getDataType() && 3003 != field.getDataType() && 3005 != field.getDataType() && 3004 != field.getDataType()
						&& 3007 != field.getDataType() && 3008 != field.getDataType())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
							.append(MultiLanguageResourceBundle.getInstance().getString(
									"MessageMetadataManager.postParse.tableRowNumberField.illegalDataType", new String[] { field.getName() }))
							.toString());
				Field field2 = (Field) sorthashmap.get(field.getTableName());
				if (null == field2)
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
							.append(MultiLanguageResourceBundle.getInstance().getString(
									"MessageMetadataManager.postParse.tableRowNumberField.tableField.null",
									new String[] { field.getName(), field.getTableName() }))
							.toString());
				if (field2.getFieldType() != 2004 && field2.getFieldType() != 2011)
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
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
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.postParse.tableField.tableRowNumberField.illegalType",
										new String[] { field.getName(), field.getRowNumFieldName() }))
								.toString());
					field.setRowNumField(field3);
				}
			}
//			if ((ConstantMB.FieldType.COMBINE_FIELD.getCode() == field.getFieldType()
//					|| ConstantMB.FieldType.REFERENCE_FIELD.getCode() == field.getFieldType())
//					&& messge.getType() != ConstantMB.MessageType.XML.getCode()) {
//				Message message = field.getReference();
//				LOGGER.info("filed:[{}],messsage:[{}]", field.getName(), message.getClassName());
//				if (message != null && ConstantMB.MessageType.XML.getCode() == message.getType())
//					throw new CommonException(new StringBuilder().append(fileAbsolutePath).append(" :")
//							.append("MessageMetadataManager.postParse.referenceField.xml.illegalFieldType").toString());
//			}
			if (1002 == messge.getType() && null == field.getSubFields() && null == field.getReference())
				throw new CommonException(
						(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field.getName()).append("']/@reference ")
								.append(MultiLanguageResourceBundle.getInstance().getString("null", new String[] { field.getName() })).toString());
			if (2010 == field.getFieldType()) {
				if (3002 != field.getDataType())
					throw new CommonException((new StringBuilder())
							.append(fileAbsolutePath).append(": ").append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.postParse.macField.illegalDataType", new String[] { field.getName() }))
							.toString());
				Field field4 = (Field) sorthashmap.get(field.getStartFieldName());
				if (null != field4) {
					field.setStartField(field4);
					if (field.getStartFieldName().equals(field.getEndFieldName())) {
						field.setEndField(field.getStartField());
					} else {
						Field field5 = (Field) sorthashmap.get(field.getEndFieldName());
						if (null == field5)
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.postParse.field.endField.null",
											new String[] { field.getName(), field.getEndFieldName() }))
									.toString());
						field.setEndField(field5);
					}
				}
			}
			if (2011 == field.getFieldType() && null == field.getRefLengthFieldName() && null == field.getTabSuffix()
					&& (null == field.getSuffix() || !field.isLastRowSuffix()) && i < sorthashmap.size())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :").append(MultiLanguageResourceBundle.getInstance()
						.getString("MessageMetadataManager.postParse.varField.error", new String[] { field.getName() })).toString());
		}
	}

	private void checkLengthField(SortHashMap sorthashmap, Field field) {
		if (3001 != field.getDataType() && 3003 != field.getDataType() && 3005 != field.getDataType() && 3004 != field.getDataType()
				&& 3007 != field.getDataType() && 3008 != field.getDataType() && 3009 != field.getDataType())
			throw new CommonException((new StringBuilder())
					.append(fileAbsolutePath).append(" :").append(MultiLanguageResourceBundle.getInstance()
							.getString("MessageMetadataManager.checkLengthField.lengthField.illegalDataType", new String[] { field.getName() }))
					.toString());
		Field field1 = (Field) sorthashmap.get(field.getStartFieldName());
		if (null == field1)
			throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
					.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.checkLengthField.lengthField.startField.null",
							new String[] { field.getName(), field.getStartFieldName() }))
					.toString());
		field.setStartField(field1);
		if (field.getStartFieldName().equals(field.getEndFieldName())) {
			field.setEndField(field.getStartField());
		} else {
			Field field2 = (Field) sorthashmap.get(field.getEndFieldName());
			if (null == field2)
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
						.append(MultiLanguageResourceBundle.getInstance().getString(
								"MessageMetadataManager.checkLengthField.lengthField.endField.null",
								new String[] { field.getName(), field.getEndFieldName() }))
						.toString());
			field.setEndField(field2);
		}
	}

	@Override
	public void endElement(String s, String s1, String s2) throws SAXException {
		if ("field".equals(s2)) {
			Field field = (Field) I.removeLast();
			if (field.getFieldType() == 2002 && field.getReference() == null && !"dynamic".equalsIgnoreCase(field.getReferenceType())
					&& !"expression".equalsIgnoreCase(field.getReferenceType())) {
				if (field.getReference() == null)
					postParse(field.getSubFields());
				if (1002 == messge.getType() && field.getFieldType() == 2002 && field.getReference() == null)
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field.getName())
							.append("']/@reference ")
							.append(MultiLanguageResourceBundle.getInstance().getString("null", new String[] { field.getName() })).toString());
			}
			if ((2002 == field.getFieldType() || 2003 == field.getFieldType() || 2004 == field.getFieldType() || 2011 == field.getFieldType())
					&& field.getReference() == null)
				postParse(field.getSubFields());
			if ((2000 == field.getFieldType() || 2001 == field.getFieldType()) && 3010 == field.getDataType()) {
				if (null == field.getPattern())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field.getName())
							.append("']/@pattern ")
							.append(MultiLanguageResourceBundle.getInstance().getString("null", new String[] { field.getName() })).toString());
				String as[] = field.getPattern().split(",");
				if (as.length > 2)
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field.getName())
							.append("']/@pattern ").append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.wrong", new String[] { field.getName() }))
							.toString());
				try {
					Integer.parseInt(as[0].trim());
				} catch (Exception exception) {
					throw new CommonException((new StringBuilder())
							.append(fileAbsolutePath).append(" :").append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.endElement.field.pattern.wrong", new String[] { field.getName() }))
							.append(exception.getMessage()).toString(), exception);
				}
				if (as.length == 2)
					try {
						Integer.parseInt(as[1].trim());
					} catch (Exception exception1) {
						throw new CommonException((new StringBuilder())
								.append(fileAbsolutePath).append(" :").append(MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.endElement.field.pattern.wrong", new String[] { field.getName() }))
								.append(exception1.getMessage()).toString(), exception1);
					}
			}
			if (0 != G.size()) {
				J.setValueRange(new HashMap<>());
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
				J = (Field) I.removeLast();
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
					messge.setPostPackEvent(s4);
			} else if ("pre-pack".equalsIgnoreCase(F)) {
				if (J != null)
					J.setPrePackEvent(s4);
				else
					messge.setPrePackEvent(s4);
			} else if ("post-parse".equalsIgnoreCase(F)) {
				if (J != null)
					J.setPostParseEvent(s4);
				else
					messge.setPostParseEvent(s4);
			} else if ("pre-parse".equalsIgnoreCase(F)) {
				if (J != null)
					J.setPreParseEvent(s4);
				else
					messge.setPreParseEvent(s4);
			} else {
				throw new CommonException(
						(new StringBuilder()).append(fileAbsolutePath).append(": Unsupport Event Type[").append(F).append("]!").toString());
			}
			if (flag && null != J)
				I.add(J);
			E = null;
		} else if ("template".equals(s2)) {
			String s5 = E;
			if (s5 != null) {
				s5 = s5.trim();
				if (0 == s5.length())
					s5 = null;
			}

			messge.setTemplate(s5);
			E = null;
		} else if ("message-bean".equals(s2) && BMap.size() != 0) {
			messge.setVariable(new HashMap<>());
			messge.getVariable().putAll(BMap);
			BMap.clear();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
//		if (null != E) goto _L2; else goto _L1
//_L1:
//		E = new String(ac, i, j);
//		  goto _L3
//_L2:
//		new StringBuilder();
//		this;
//		JVM INSTR dup_x1 ;
//		E;
//		append();
//		new String(ac, i, j);
//		append();
//		toString();
//		E;
//_L3:
	}

	@Override
	public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
		if ("message-bean".equals(s2)) {
			String s3 = attributes.getValue("id");
			if (null == s3)
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": message-bean/@id ")
						.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			s3 = s3.trim();
			if (0 == s3.length())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": message-bean/@id ")
						.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
			messge.setId(s3);
			s3 = attributes.getValue("type");
			if (null != s3) {
				s3 = s3.trim();
				if (0 == s3.length())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": message-bean/@type ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				messge.setType(Constant.getMessageTypeByText(s3));
			}
			messge.setShortText(attributes.getValue("short-text"));
			messge.setGroupId(D);
			s3 = attributes.getValue("class");
			if (null == s3)
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": message-bean/@class ")
						.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			s3 = s3.trim();
			if (0 == s3.length())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": message-bean/@class ")
						.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
			if (MessageMetadataManager.cacheByClass.containsKey(s3)) {
				Message message = (Message) MessageMetadataManager.cacheByClass.get(s3);
				if (D.equalsIgnoreCase(message.getGroupId()))
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ").append(
							MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.messageClass.reduplicate"))
							.toString());
			}
			messge.setClassName(s3);
			s3 = attributes.getValue("xpath");
			messge.setXpath(s3);
			s3 = attributes.getValue("message-charset");
			if (null != s3 && 0 != s3.trim().length())
				messge.setMsgCharset(s3);
			if (1002 == messge.getType() || 1003 == messge.getType()) {
				String s4 = attributes.getValue("prefix");
				if (null == s4)
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": Tag/Swift message-bean/@prefix ")
							.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s4 = s4.trim();
				if (0 == s4.length())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": Tag/Swift message-bean/@prefix ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				if (s4.length() > 2 && s4.startsWith("0x")) {
					String s9 = s4.substring(2);
					if (!CodeUtil.isHexString(s9))
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": Tag/Swift message-bean/@prefix ")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.notHexString"))
								.toString());
					messge.setPrefixString(s9);
					messge.setPrefix(CodeUtil.HextoByte(s9));
				} else {
					messge.setPrefixString(new String(CodeUtil.BCDtoASC(s4.getBytes())));
					messge.setPrefix(s4.getBytes());
				}
				s4 = attributes.getValue("suffix");
				if (null == s4)
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": Tag/Swift message-bean/@suffix ")
							.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s4 = s4.trim();
				if (0 == s4.length())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": Tag/Swift message-bean/@suffix ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				if (s4.length() > 2 && s4.startsWith("0x")) {
					String s10 = s4.substring(2);
					if (!CodeUtil.isHexString(s10))
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": Tag/Swift message-bean/@suffix ")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.notHexString"))
								.toString());
					messge.setSuffixString(s10);
					messge.setSuffix(CodeUtil.HextoByte(s10));
				} else {
					messge.setSuffixString(new String(CodeUtil.BCDtoASC(s4.getBytes())));
					messge.setSuffix(s4.getBytes());
				}
			}
		} else if ("field".equals(s2)) {
			Field field = (Field) I.removeLast();
			if (field != null && field.getFieldType() != 2002 && field.getFieldType() != 2003 && field.getFieldType() != 2004
					&& field.getFieldType() != 2011)
				throw new CommonException(
						(new StringBuilder())
								.append(fileAbsolutePath).append(": ").append(MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.fieldType.illegal", new String[] { field.getName() }))
								.toString());
			Field field1 = new Field();
			String s11 = attributes.getValue("name");
			if (null == s11)
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field/@name ")
						.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			s11 = s11.trim();
			if (0 == s11.length())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field/@name ")
						.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
			if (MessageMetadataManager.keyWordSet.contains(s11))
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field/@name [").append(s11).append("] ")
						.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.field.isReserved"))
						.toString());
			if (field != null) {
				if (field.getSubFields().containsKey(s11))
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
							.append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.field.subField.reduplicate", new String[] { field.getName() }))
							.append(":").append(s11).toString());
			} else if (messge.getFields().containsKey(s11))
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
						.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.field.reduplicate"))
						.append(":").append(s11).toString());
			field1.setName(s11);
			s11 = attributes.getValue("field-type");
			if (null == s11)
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
						.append("']/@field-type ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			s11 = s11.trim();
			if (0 == s11.length())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
						.append("']/@field-type ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
			try {
				field1.setFieldType(Constant.getFieldTypeByText(s11));
			} catch (Exception exception1) {
				throw new CommonException(
						(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName()).append("']/@field-type ")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong")).toString());
			}
			s11 = attributes.getValue("padding");
			if (null != s11) {
				s11 = s11.trim();
				if (0 != s11.length()) {
					byte abyte0[] = s11.getBytes();
					if (1 == abyte0.length)
						field1.setPadding(abyte0[0]);
					else if (4 == abyte0.length && s11.startsWith("0x")) {
						byte[] abyte1 = CodeUtil.HextoByte(s11.substring(2));
						field1.setPadding(abyte1[0]);
					} else {
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@padding ").append(MultiLanguageResourceBundle.getInstance()
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
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@padding-direction ").append(MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.paddingDirection.format.wrong"))
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
				field1.setRequired(MessageMetadataManager.IS_REQUIRED);
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
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
							.append("']/@iso8583-no ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				try {
					field1.setIso8583_no(Integer.parseInt(s11));
				} catch (Exception exception2) {
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
							.append("']/@iso8583-no ")
							.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong")).toString());
				}
			}
			s11 = attributes.getValue("prefix");
			if (null != s11 && s11.length() > 0) {
				if (s11.length() > 2 && s11.startsWith("0x")) {
					String s12 = s11.substring(2);
					if (!CodeUtil.isHexString(s12))
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@prefix ")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.notHexString"))
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
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@suffix ")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.notHexString"))
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
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
							.append("']/@extended-attributes ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				try {
					field1.setExtendedAttributeText(s11);
					String as[] = s11.split(";");
					for (int j = 0; j < as.length; j++)
						field1.setExtendAttribute(as[j].substring(0, as[j].indexOf(":")), as[j].substring(as[j].indexOf(":") + 1));

				} catch (Exception exception3) {
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
							.append("']/@extended-attributes ")
							.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong")).toString());
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
			if (2001 == field1.getFieldType() || 2003 == field1.getFieldType() || 2009 == field1.getFieldType() || 2011 == field1.getFieldType()) {
				s11 = attributes.getValue("ref-length-field");
				if (null != s11) {
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@ref-length-field ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					field1.setRefLengthFieldName(s11);
					s11 = attributes.getValue("ref-length-field-offset");
					if (null != s11)
						try {
							field1.setRefLengthFieldOffset(Integer.parseInt(s11));
						} catch (Exception exception4) {
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
									.append(field1.getName()).append("']/@ref-length-field-offset ")
									.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong"))
									.append(exception4.getMessage()).toString(), exception4);
						}
				} else if (2011 == field1.getFieldType()) {
					s11 = attributes.getValue("tab-prefix");
					if (null != s11) {
						s11 = s11.trim();
						if (0 != s11.length())
							if (s11.length() > 2 && s11.startsWith("0x")) {
								String s14 = s11.substring(2);
								if (!CodeUtil.isHexString(s14))
									throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
											.append(field1.getName()).append("']/@tab-suffix ").append(MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.notHexString"))
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
									throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
											.append(field1.getName()).append("']/@tab-suffix ").append(MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.notHexString"))
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
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@length-field-data-type ").append(MultiLanguageResourceBundle.getInstance().getString("null"))
								.toString());
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@length-field-data-type ").append(MultiLanguageResourceBundle.getInstance().getString("blank"))
								.toString());
					try {
						field1.setLengthFieldDataType(Constant.getDataTypeByText(s11));
					} catch (Exception exception5) {
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@length-field-data-type ")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong")).toString());
					}
					s11 = attributes.getValue("length-field-data-encoding");
					if (null != s11) {
						s11 = s11.trim();
						if ("bcd".equalsIgnoreCase(s11))
							field1.setLengthFieldDataEncoding(4001);
					}
					s11 = attributes.getValue("length-field-length");
					if (null == s11)
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@length-field-length ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
								.append(field1.getCalcType()).append("']/@length-field-length ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					try {
						field1.setLengthFieldLength(Integer.parseInt(s11));
					} catch (Exception exception6) {
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@length-field-length ")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong")).toString());
					}
				}
				s11 = attributes.getValue("max-length");
				if (null != s11) {
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@max-length ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					try {
						field1.setMaxLength(Integer.parseInt(s11));
					} catch (Exception exception7) {
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@max-length ")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong")).toString());
					}
				}
				s11 = attributes.getValue("min-length");
				if (null != s11) {
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@min-length ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					try {
						field1.setMinLength(Integer.parseInt(s11));
					} catch (Exception exception8) {
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@min-length ")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong"))
								.append(exception8.getMessage()).toString(), exception8);
					}
				}
			}
			if (1002 == messge.getType() || 1003 == messge.getType()) {
				s11 = attributes.getValue("tag");
				if (null != s11) {
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@tag ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					field1.setTag(s11);
				}
			}
			if (2002 == field1.getFieldType() || 2003 == field1.getFieldType() || 2004 == field1.getFieldType() || 2011 == field1.getFieldType()
					|| 2008 == field1.getFieldType() || 2009 == field1.getFieldType()) {
				s11 = attributes.getValue("reference-type");
				if (null != s11) {
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@reference-type ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					field1.setReferenceType(s11);
				}
				s11 = attributes.getValue("reference");
				if (null == s11 && (2008 == field1.getFieldType() || 2009 == field1.getFieldType()))
					throw new CommonException(
							(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName()).append("'] ")
									.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.reference.null"))
									.toString());
				if (s11 != null) {
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@reference ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					if (!"dynamic".equalsIgnoreCase(field1.getReferenceType()) && !"expression".equalsIgnoreCase(field1.getReferenceType())) {
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
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
									.append(field1.getName()).append("']/@row-num-field ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						field1.setRowNumFieldName(s11);
					}
					s11 = attributes.getValue("row-xpath");
					if (s11 != null) {
						s11 = s11.trim();
						if (0 == s11.length())
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
									.append(field1.getName()).append("']/@row-xpath ")
									.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
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
						throw new CommonException(
								(new StringBuilder())
										.append(fileAbsolutePath).append(": field[@name='").append("'] ").append(MultiLanguageResourceBundle
												.getInstance().getString("MessageMetadataManager.startElement.lengthField.startField.null"))
										.toString());
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("'] ").append(MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.lengthField.startField.blank"))
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
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
									.append(field1.getName()).append("'] ").append(MultiLanguageResourceBundle.getInstance()
											.getString("MessageMetadataManager.startElement.lengthField.startField.blank"))
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
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@table ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@table ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					field1.setTableName(s11);
				}
				s11 = attributes.getValue("data-type");
				if (null == s11)
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
							.append("']/@data-type ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s11 = s11.trim();
				if (0 == s11.length())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
							.append("']/@data-type ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				try {
					field1.setDataType(Constant.getDataTypeByText(s11));
				} catch (Exception exception9) {
					throw new CommonException(
							(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName()).append("']/@data-type ")
									.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong"))
									.append(exception9.getMessage()).toString(),
							exception9);
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
							throw new CommonException(
									(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
											.append("']/@length ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
						s11 = s11.trim();
						if (0 == s11.length())
							throw new CommonException(
									(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
											.append("']/@length ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
						try {
							field1.setLength(Integer.parseInt(s11));
						} catch (Exception exception10) {
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
									.append(field1.getName()).append("']/@length[").append(s11).append("] ")
									.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.format.wrong"))
									.toString());
						}
					} else if (3006 == field1.getDataType()) {
						if (null == field1.getPattern())
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
									.append(field1.getName()).append("'] ").append(MultiLanguageResourceBundle.getInstance()
											.getString("MessageMetadataManager.startElement.datatime.pattern.null"))
									.toString());
						SimpleDateFormat simpledateformat;
						try {
							simpledateformat = new SimpleDateFormat(field1.getPattern());
						} catch (Exception exception11) {
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
									.append(field1.getName()).append("'] ").append(MultiLanguageResourceBundle.getInstance()
											.getString("MessageMetadataManager.startElement.datatime.pattern.wrong"))
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
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
									.append(field1.getName()).append("']/@length[ ").append(s11).append("] ")
									.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.format.wrong"))
									.toString());
						}
						switch (field1.getDataType()) {
						case 3003, 3007:
							if (i != 4)
								throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
										.append(MultiLanguageResourceBundle.getInstance()
												.getString("MessageMetadataManager.startElement.length.notEqual4", new String[] { field1.getName() }))
										.toString());
							break;

						case 3004:
							if (i != 1)
								throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ").append(
										MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.length.notEqual1"))
										.toString());
							break;

						case 3005:
						case 3008:
							if (i != 2)
								throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ").append(
										MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.length.notEqual2"))
										.toString());
							break;

						case 3009:
							if (i != 8)
								throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ").append(
										MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.length.notEqual8"))
										.toString());
							break;

						case 3006:
						default:
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.field.unsupportDataType", new String[] { field1.getName() }))
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
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.startElement.field.defaultValue.null", new String[] { field1.getName(), s11 }))
								.toString());
					if (2000 == field1.getFieldType() || 2005 == field1.getFieldType()) {
						if (3000 == field1.getDataType() || 3001 == field1.getDataType() || 3010 == field1.getDataType()) {
							if (s11.length() > field1.getLength())
								throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.defaultValue.tooLong",
												new String[] { field1.getName(), s11 }))
										.toString());
							if (field1.isStrictDataLength() && s11.length() < field1.getLength())
								throw new CommonException(
										(new StringBuilder()).append(fileAbsolutePath).append(": ")
												.append(MultiLanguageResourceBundle.getInstance().getString(
														"MessageMetadataManager.startElement.field.defaultValue.tooShort",
														new String[] { field1.getName(), s11,
																(new StringBuilder()).append("").append(field1.getLength()).toString() }))
												.toString());
							BigDecimal bigdecimal;
							if (3001 == field1.getDataType())
								try {
									Integer.parseInt(s11);
								} catch (Exception exception12) {
									throw new CommonException(
											(new StringBuilder()).append(fileAbsolutePath).append(": ")
													.append(MultiLanguageResourceBundle.getInstance().getString(
															"MessageMetadataManager.startElement.field.defaultValue.notNumber",
															new String[] { field1.getName(), s11 }))
													.append(exception12.getMessage()).toString(),
											exception12);
								}
							else if (3010 == field1.getDataType())
								try {
									bigdecimal = new BigDecimal(s11);
								} catch (Exception exception13) {
									throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
											.append(MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.notDecimalNumber",
													new String[] { field1.getName(), s11 }))
											.toString());
								}
						} else if (3003 == field1.getDataType() || 3007 == field1.getDataType())
							try {
								Integer.parseInt(s11);
							} catch (Exception exception14) {
								throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.defaultValue.notIntNumber",
												new String[] { field1.getName(), s11 }))
										.toString());
							}
						else if (3005 == field1.getDataType() || 3008 == field1.getDataType())
							try {
								Short.parseShort(s11);
							} catch (Exception exception15) {
								throw new CommonException(
										(new StringBuilder()).append(fileAbsolutePath).append(": ")
												.append(MultiLanguageResourceBundle.getInstance().getString(
														"MessageMetadataManager.startElement.field.defaultValue.notShortNumber",
														new String[] { field1.getName(), s11 }))
												.append(exception15.getMessage()).toString(),
										exception15);
							}
						else if (3004 == field1.getDataType())
							try {
								if (0 != s11.length()) {
									byte abyte2[] = s11.getBytes();
									if (1 != abyte2.length)
										if (4 == abyte2.length && s11.startsWith("0x"))
											abyte2 = CodeUtil.HextoByte(s11.substring(2));
										else
											throw new CommonException(
													(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
															.append("']/@padding ")
															.append(MultiLanguageResourceBundle.getInstance()
																	.getString("MessageMetadataManager.startElement.padding.format.wrong"))
															.toString());
								}
							} catch (Exception exception16) {
								throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
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
								throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.datetime.illegalPattern",
												new String[] { field1.getName(), s11, field1.getPattern() }))
										.toString());
							}
						} else if (3009 == field1.getDataType())
							try {
								Long.parseLong(s11);
							} catch (Exception exception17) {
								throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.defaultValue.notLongNumber",
												new String[] { field1.getName(), s11 }))
										.toString(), exception17);
							}
						else if (3002 == field1.getDataType()) {
							byte abyte3[] = s11.trim().getBytes();
							if (abyte3.length % 2 != 0)
								throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.defaultValue.notHexString",
												new String[] { field1.getName(), Constant.getFieldTypeText(field1.getFieldType()) }))
										.toString());
							if (abyte3.length / 2 != field1.getLength())
								throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
										.append(MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.defaultValue.hexString.illegalLength",
												new String[] { field1.getName(), Constant.getFieldTypeText(field1.getFieldType()) }))
										.append(" ").append(field1.getLength() * 2).toString());
							for (int k = 0; k < abyte3.length; k++)
								if ((abyte3[k] < 48 || abyte3[k] > 57) && (abyte3[k] < 97 || abyte3[k] > 102) && (abyte3[k] < 65 || abyte3[k] > 70))
									throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
											.append(MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.notHexString",
													new String[] { field1.getName(), Constant.getFieldTypeText(field1.getFieldType()) }))
											.toString());

						} else {
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.field.canNotHave.defaultValue.1",
											new String[] { field1.getName(), Constant.getDataTypeText(field1.getDataType()) }))
									.toString());
						}
						field1.setValue(s11);
					} else {
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
								.append(MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.startElement.field.canNotHave.defaultValue.2",
										new String[] { field1.getName(), Constant.getFieldTypeText(field1.getFieldType()) }))
								.toString());
					}
				}
				s11 = attributes.getValue("ref-value-range");
				if (null != s11) {
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@ref-value-range ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					field1.setRefValueRange(s11);
					File file = (File) MessageMetadataManager.groupPathCache.get(D);
					String s17 = file.getAbsolutePath();
					if (!s17.endsWith(System.getProperty("file.separator")))
						s17 = (new StringBuilder()).append(s17).append(System.getProperty("file.separator")).toString();
					String s18 = (new StringBuilder()).append(s17).append("value-range").append(System.getProperty("file.separator")).append(s11)
							.append(".xml").toString();
					File file1 = new File(s18);
					ValueRangeHalder _lb = new ValueRangeHalder();
					G = _lb.A(D, file1);
				}
			}
			s11 = attributes.getValue("class");
			if (field1.getFieldType() == 2010) {
				String s16 = attributes.getValue("calc-type");
				if (null == s16)
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": mac-field/@calc-type ")
							.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s16 = s16.trim();
				if (0 == s16.length())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": mac-field/@calc-type ")
							.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
				field1.setCalcType(s16);
				if ("java".equalsIgnoreCase(field1.getCalcType())) {
					if (null == s11)
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": mac-field/@class ")
								.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": mac-field/@class ")
								.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					field1.setCombineOrTableFieldClassName(s11);
				}
			} else if (null != s11) {
				if (field1.getFieldType() != 2002 && field1.getFieldType() != 2003 && field1.getFieldType() != 2004 && field1.getFieldType() != 2011
						&& field1.getFieldType() != 2010 && field1.getFieldType() != 2008 && field1.getFieldType() != 2001)
					throw new CommonException((new StringBuilder())
							.append(fileAbsolutePath).append(": ").append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.field.canNotHave.class", new String[] { field1.getName() }))
							.toString());
				s11 = s11.trim();
				if (s11.length() > 0)
					field1.setCombineOrTableFieldClassName(s11);
			}
			if (field != null) {
				field.setSubField(field1.getName(), field1);
				I.add(field);
			} else {
				messge.setField(field1.getName(), field1);
			}
			I.add(field1);
			J = field1;
		} else if ("value-range".equals(s2)) {
			G.clear();
			A = new ValueRange();
			String s5 = attributes.getValue("default-ref");
			if (null != s5) {
				s5 = s5.trim();
				if (0 == s5.trim().length())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(J.getName())
							.append("']/value-range/value-ref ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
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
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(J.getName())
						.append("']/value-range/value/@value ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			s6 = s6.trim();
			if (0 == s6.length())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(J.getName())
						.append("']/value-range/value/@value ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
			if (G.containsKey(s6))
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(J.getName())
						.append("']/value-range/value[@value='").append(s6).append("'] ")
						.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.reduplicate")).toString());
			A.setValue(s6);
			A.setShortText(attributes.getValue("short-text"));
			s6 = attributes.getValue("reference");
			if (s6 != null) {
				s6 = s6.trim();
				if (0 == s6.length())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": fiedl[@name='").append(J.getName())
							.append("']/value-range/value/@reference ").append(MultiLanguageResourceBundle.getInstance().getString("blank"))
							.toString());
				A.setReferenceId(s6);
				Message message2 = MessageMetadataManager.getMessage(D, s6);
				if (null == message2)
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
							.append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.field.valueRange.reference.notExist", new String[] { s6 }))
							.toString());
				A.setReference(message2);
				if (null == G.get("default-ref"))
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(J.getName())
							.append("']/@default-ref ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			}
			G.put(A.getValue(), A);
		} else if ("event".equals(s2)) {
			String s7 = attributes.getValue("type");
			if (null == s7)
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": event/@type ")
						.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			s7 = s7.trim();
			if (0 == s7.length())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": event/@type ")
						.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
			F = s7;
		} else if ("variable".equals(s2)) {
			H = new Variable();
			String s8 = attributes.getValue("name");
			if (null == s8)
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": variable/@name ")
						.append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			s8 = s8.trim();
			if (0 == s8.length())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": variable/@name ")
						.append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
			H.setName(s8);
			s8 = attributes.getValue("data-type");
			if (null == s8)
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": variable[@name='").append(H.getName())
						.append("']/@data-type ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			s8 = s8.trim();
			if (0 == s8.length())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": variable[@name='").append(H.getName())
						.append("']/@data-type ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
			try {
				H.setDataType(Constant.getDataTypeByText(s8));
			} catch (Exception exception) {
				throw new CommonException(
						(new StringBuilder()).append(fileAbsolutePath).append(": variable[@name='").append(H.getName()).append("']/@data-type ")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong"))
								.append(exception.getMessage()).toString(),
						exception);
			}
			s8 = attributes.getValue("value");
			if (null == s8)
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": variable[@name='").append(H.getName())
						.append("']/@value ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			s8 = s8.trim();
			if (0 == s8.length())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": variable[@name='").append(H.getName())
						.append("']/@value ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
			H.setValue(s8);
			BMap.put(H.getName(), H);
		}
	}

	public MessageHandler() {
		messge = new Message();
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
