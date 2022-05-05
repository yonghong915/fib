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
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.commons.util.SortHashMap;
import com.fib.upp.message.metadata.Constant;
import com.fib.upp.message.metadata.ConstantMB;
import com.fib.upp.message.metadata.Field;
import com.fib.upp.message.metadata.Message;
import com.fib.upp.message.metadata.ValueRange;
import com.fib.upp.message.metadata.Variable;
import com.fib.upp.util.CodeUtil;
import com.fib.upp.util.MultiLanguageResourceBundle;
import com.fib.upp.util.StringUtil;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;

public class MessageHandler extends DefaultHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

	private static class FieldList {
		private List<Field> fieldLst;

		private FieldList() {
			fieldLst = new ArrayList<>();
		}

		public Object remove() {
			if (!fieldLst.isEmpty())
				return fieldLst.remove(fieldLst.size() - 1);
			else
				return null;
		}

		public void add(Field field) {
			fieldLst.add(field);
		}
	}

	private Message message;
	private String fileAbsolutePath;
	private String groupId;
	FieldList fieldList;
	Map<String, ValueRange> valueRangeMap;
	ValueRange valueRange;
	Map<String, Variable> variableMap;
	Variable variable;
	Field field;
	String tmpFieldAttr;
	String nodeValue;

	public Message parseMessage(String groupId, File file) {
		LOGGER.info("fileName={}", file.getName());
		fileAbsolutePath = file.getAbsolutePath();
		try {
			return parseMessage(groupId, new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new CommonException(e);
		}
	}

	private Message parseMessage(String groupId, InputStream is) {
		this.groupId = groupId;
		XmlUtil.readBySax(is, this);

		check();
		postParse(this.message.getFields());
		A(this.message.getClassName(), this.message.getFields());
		return this.message;
	}

	private void A(String messageBeanClassName, Map<String, Field> sorthashmap) {
		Iterator<Field> iterator = sorthashmap.values().iterator();
		while (iterator.hasNext()) {
			Field field = iterator.next();
			int fieldType = field.getFieldType();
			String referenceType = field.getReferenceType();
			if ((ConstantMB.FieldType.COMBINE_FIELD.getCode() == fieldType || ConstantMB.FieldType.VAR_COMBINE_FIELD.getCode() == fieldType
					|| ConstantMB.FieldType.TABLE.getCode() == fieldType || ConstantMB.FieldType.VAR_TABLE.getCode() == fieldType)
					&& !ConstantMB.ReferenceType.DYNAMIC.getCode().equalsIgnoreCase(referenceType)
					&& !ConstantMB.ReferenceType.EXPRESSION.getCode().equalsIgnoreCase(referenceType) && null == referenceType) {
				Message message = new Message();
				message.setId((new StringBuilder()).append(this.message.getId()).append("-").append(field.getName()).toString());
				message.setShortText(field.getShortText());
				message.setFields(field.getSubFields());
				String s1 = field.getCombineOrTableFieldClassName();
				if (null == s1) {
					s1 = (new StringBuilder()).append(messageBeanClassName).append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
				}
				message.setClassName(s1);
				MessageMetadataManager.cacheByClass.put(message.getClassName(), message);
				A(message.getClassName(), message.getFields());
			}
		}
	}

	private void check() {
		label0: {
			if (1002 == this.message.getType() || 1003 == this.message.getType()) {
				Iterator<Field> iterator = this.message.getFields().values().iterator();
				HashSet<String> hashset = new HashSet<>(this.message.getFields().size());
				Field field;
				for (; iterator.hasNext(); hashset.add(field.getTag())) {
					field = iterator.next();
					if (null == field.getTag())
						throw new CommonException((new StringBuilder())
								.append(fileAbsolutePath).append(" :").append(MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.check.tag.noTag", new String[] { this.message.getId(), field.getName() }))
								.toString());
					if (1002 == this.message.getType() && 2004 != field.getFieldType() && 2005 != field.getFieldType() && 2000 != field.getFieldType()
							&& 2002 != field.getFieldType() && 2008 != field.getFieldType())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.check.tag.illegalType",
										new String[] { this.message.getId(), field.getName() }))
								.toString());
					if (hashset.contains(field.getTag()))
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.check.tag.reduplicate",
										new String[] { this.message.getId(), field.getName(), field.getTag() }))
								.toString());
				}

			}
			if (1001 != this.message.getType())
				break label0;
			Field field1 = null;
			Iterator<Field> iterator1 = this.message.getFields().values().iterator();
			do {
				if (!iterator1.hasNext())
					break label0;
				field1 = (Field) iterator1.next();
			} while (2001 != field1.getFieldType() && 2003 != field1.getFieldType() && 2009 != field1.getFieldType());
			throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
					.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.check.xml.illegalFieldType")).toString());
		}
	}

	private void postParse(Map<String, Field> sorthashmap) {
		Iterator<Field> iterator = sorthashmap.values().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Field field = iterator.next();
			i++;

			String refType = field.getReferenceType();
			int fieldType = field.getFieldType();
			if ((ConstantMB.ReferenceType.DYNAMIC.getCode().equalsIgnoreCase(refType)
					|| ConstantMB.ReferenceType.EXPRESSION.getCode().equalsIgnoreCase(refType))
					&& ConstantMB.FieldType.COMBINE_FIELD.getCode() != fieldType && ConstantMB.FieldType.VAR_COMBINE_FIELD.getCode() != fieldType
					&& ConstantMB.FieldType.TABLE.getCode() != fieldType) {
				throw new CommonException("MessageMetadataManager.postParse.dynamicField.illegalType");
			}

			if (ConstantMB.FieldType.VAR_FIELD.getCode() == fieldType || ConstantMB.FieldType.VAR_COMBINE_FIELD.getCode() == fieldType
					|| ConstantMB.FieldType.VAR_REFERENCE_FIELD.getCode() == fieldType) {

				if (field.getRefLengthFieldName() != null) {
					Field field1 = sorthashmap.get(field.getRefLengthFieldName());
					Assert.notNull(field1, () -> new CommonException("MessageMetadataManager.postParse.refLengthField.notExist"));

					Assert.isTrue(ConstantMB.FieldType.LENGTH_FIELD.getCode() == fieldType,
							() -> new CommonException("MessageMetadataManager.postParse.refLengthField.notLengthField"));

					field.setRefLengthField(field1);
				} else {
					ConstantMB.DataType dt = ConstantMB.DataType.getDataTypeByCode(field.getLengthFieldDataType());
					switch (dt) {
					case NUM:
						break;

					case LONG:
						Assert.isTrue(field.getLengthFieldLength() == 8,
								() -> new CommonException("MessageMetadataManager.postParse.lengthFieldLength.notEqual8"));

						break;

					case INT, NET_INT:
						Assert.isTrue(field.getLengthFieldLength() == 4,
								() -> new CommonException("MessageMetadataManager.postParse.lengthFieldLength.notEqual4"));
						break;

					case BYTE:
						Assert.isTrue(field.getLengthFieldLength() == 1,
								() -> new CommonException("MessageMetadataManager.postParse.lengthFieldLength.notEqual1"));

						break;

					case SHORT, NET_SHORT:
						Assert.isTrue(field.getLengthFieldLength() == 2,
								() -> new CommonException("MessageMetadataManager.postParse.lengthFieldLength.notEqual2"));

						break;

					case BIN, DATETIME:
					default:
						throw new CommonException("MessageMetadataManager.postParse.lengthFieldLength.illegalDataType");
					}
				}
			}
			if (ConstantMB.FieldType.LENGTH_FIELD.getCode() == field.getFieldType()) {
				checkLengthField(sorthashmap, field);
			}
			if (ConstantMB.FieldType.TABLE_ROW_NUM_FIELD.getCode() == field.getFieldType()) {
				if (3001 != field.getDataType() && 3003 != field.getDataType() && 3005 != field.getDataType() && 3004 != field.getDataType()
						&& 3007 != field.getDataType() && 3008 != field.getDataType())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(" :")
							.append(MultiLanguageResourceBundle.getInstance().getString(
									"MessageMetadataManager.postParse.tableRowNumberField.illegalDataType", new String[] { field.getName() }))
							.toString());
				Field field2 = sorthashmap.get(field.getTableName());

				Assert.notNull(field2, () -> new CommonException("MessageMetadataManager.postParse.tableRowNumberField.tableField.null"));

				Assert.isTrue(field2.getFieldType() == 2004 || field2.getFieldType() == 2011,
						() -> new CommonException("MessageMetadataManager.postParse.tableRowNumberField.tableField.illegalType"));

				field.setTable(field2);
			}
			if (2004 == field.getFieldType() && field.getRowNumFieldName() != null) {
				Field field3 = sorthashmap.get(field.getRowNumFieldName());
				if (null != field3) {
					Assert.isTrue(field3.getFieldType() == 2005,
							() -> new CommonException("MessageMetadataManager.postParse.tableField.tableRowNumberField.illegalType"));

					field.setRowNumField(field3);
				}
			}
//			if ((ConstantMB.FieldType.COMBINE_FIELD.getCode() == field.getFieldType()
//					|| ConstantMB.FieldType.REFERENCE_FIELD.getCode() == field.getFieldType())
//					&& this.message.getType() != ConstantMB.MessageType.XML.getCode()) {
//				Message message = field.getReference();
//				LOGGER.info("filed:[{}],messsage:[{}]", field.getName(), message.getClassName());
//				if (message != null && ConstantMB.MessageType.XML.getCode() == message.getType())
//					throw new CommonException(new StringBuilder().append(fileAbsolutePath).append(" :")
//							.append("MessageMetadataManager.postParse.referenceField.xml.illegalFieldType").toString());
//			}
			if (1002 == this.message.getType() && null == field.getSubFields() && null == field.getReference())
				throw new CommonException(
						(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field.getName()).append("']/@reference ")
								.append(MultiLanguageResourceBundle.getInstance().getString("null", new String[] { field.getName() })).toString());
			if (2010 == field.getFieldType()) {
				if (3002 != field.getDataType())
					throw new CommonException((new StringBuilder())
							.append(fileAbsolutePath).append(": ").append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.postParse.macField.illegalDataType", new String[] { field.getName() }))
							.toString());
				Field field4 = sorthashmap.get(field.getStartFieldName());
				if (null != field4) {
					field.setStartField(field4);
					if (field.getStartFieldName().equals(field.getEndFieldName())) {
						field.setEndField(field.getStartField());
					} else {
						Field field5 = sorthashmap.get(field.getEndFieldName());
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

	private void checkLengthField(Map<String, Field> sorthashmap, Field field) {
		List<ConstantMB.DataType> lengthFieldsLst = new ArrayList<>();
		lengthFieldsLst.add(ConstantMB.DataType.NUM);
		lengthFieldsLst.add(ConstantMB.DataType.INT);
		lengthFieldsLst.add(ConstantMB.DataType.BYTE);
		lengthFieldsLst.add(ConstantMB.DataType.SHORT);
		lengthFieldsLst.add(ConstantMB.DataType.NET_INT);
		lengthFieldsLst.add(ConstantMB.DataType.NET_SHORT);
		lengthFieldsLst.add(ConstantMB.DataType.LONG);

		ConstantMB.DataType dataType = ConstantMB.DataType.getDataTypeByCode(field.getDataType());

		Assert.isTrue(lengthFieldsLst.contains(dataType),
				() -> new CommonException("MessageMetadataManager.checkLengthField.lengthField.illegalDataType"));

		Field field1 = sorthashmap.get(field.getStartFieldName());
		Assert.notNull(field1, () -> new CommonException("MessageMetadataManager.checkLengthField.lengthField.startField.null"));

		field.setStartField(field1);
		if (field.getStartFieldName().equals(field.getEndFieldName())) {
			field.setEndField(field.getStartField());
		} else {
			Field field2 = sorthashmap.get(field.getEndFieldName());
			Assert.notNull(field2, () -> new CommonException("MessageMetadataManager.checkLengthField.lengthField.endField.null"));
			field.setEndField(field2);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("field".equals(qName)) {
			Field field = (Field) fieldList.remove();
			if (field.getFieldType() == 2002 && field.getReference() == null && !"dynamic".equalsIgnoreCase(field.getReferenceType())
					&& !"expression".equalsIgnoreCase(field.getReferenceType())) {
				if (field.getReference() == null)
					postParse(field.getSubFields());
				if (1002 == this.message.getType() && field.getFieldType() == 2002 && field.getReference() == null)
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
				String[] as = field.getPattern().split(",");
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
			if (0 != valueRangeMap.size()) {
				this.field.setValueRange(new HashMap<>());
				this.field.getValueRange().putAll(valueRangeMap);
				valueRangeMap.clear();
			}
			this.field = null;
		} else if ("value-range".equals(qName)) {
			this.field.setValueRange(new HashMap<>());
			this.field.getValueRange().putAll(valueRangeMap);
			valueRangeMap.clear();
		} else if ("expression".equals(qName)) {
			String s3 = nodeValue;
			if (null != s3)
				s3 = s3.trim();
			if (0 == s3.length())
				s3 = null;
			if (this.field != null)
				this.field.setExpression(s3);
			nodeValue = null;
		} else if ("event".equals(qName)) {
			String s4 = nodeValue;
			if (s4 != null)
				s4 = s4.trim();
			if (0 == s4.length())
				s4 = null;
			boolean flag = false;
			if (null == this.field) {
				this.field = (Field) fieldList.remove();
				flag = true;
			}
			if ("row-post-pack".equalsIgnoreCase(tmpFieldAttr)) {
				if (this.field != null)
					this.field.setPostRowPackEvent(s4);
			} else if ("row-pre-pack".equalsIgnoreCase(tmpFieldAttr)) {
				if (this.field != null)
					this.field.setPreRowPackEvent(s4);
			} else if ("row-post-parse".equalsIgnoreCase(tmpFieldAttr)) {
				if (this.field != null)
					this.field.setPostRowParseEvent(s4);
			} else if ("row-pre-parse".equalsIgnoreCase(tmpFieldAttr)) {
				if (this.field != null)
					this.field.setPreRowParseEvent(s4);
			} else if ("post-pack".equalsIgnoreCase(tmpFieldAttr)) {

				if (this.field != null)
					this.field.setPostPackEvent(s4);
				else
					this.message.setPostPackEvent(s4);
			} else if ("pre-pack".equalsIgnoreCase(tmpFieldAttr)) {
				if (this.field != null)
					this.field.setPrePackEvent(s4);
				else
					this.message.setPrePackEvent(s4);
			} else if ("post-parse".equalsIgnoreCase(tmpFieldAttr)) {
				if (this.field != null)
					this.field.setPostParseEvent(s4);
				else
					this.message.setPostParseEvent(s4);
			} else if ("pre-parse".equalsIgnoreCase(tmpFieldAttr)) {

				if (this.field != null)
					this.field.setPreParseEvent(s4);
				else
					this.message.setPreParseEvent(s4);
			} else {
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": Unsupport Event Type[").append(tmpFieldAttr)
						.append("]!").toString());
			}
			if (flag && null != this.field)
				fieldList.add(this.field);
			nodeValue = null;
		} else if ("template".equals(qName)) {
			String s5 = nodeValue;
			if (s5 != null) {
				s5 = s5.trim();
				if (0 == s5.length())
					s5 = null;
			}

			this.message.setTemplate(s5);
			nodeValue = null;
		} else if ("message-bean".equals(qName) && MapUtil.isNotEmpty(variableMap)) {
			this.message.setVariable(new HashMap<>());
			this.message.getVariable().putAll(variableMap);
			variableMap.clear();
		}
	}

	public static void main(String[] args) {
		String str = "abcd";
		Map<String, String> map = new HashMap<>();
		defaultIfEmpty(str, a -> map.put(str, a + "edfg"), b -> map.put(str, b + "123445"));

		System.out.println(map);

	}

	public static void defaultIfEmpty(String str, Consumer<String> handle, Consumer<String> other) {
		if (StrUtil.isEmptyIfStr(str)) {
			handle.accept(str);
		} else {
			other.accept(str);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (null == this.nodeValue) {
			this.nodeValue = new String(ch, start, length);
		} else {
			this.nodeValue += new String(ch, start, length);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (ConstantMB.MessageTag.MESSAGE_BEAN.getName().equals(qName)) {
			buildMessageBean(attributes);
		} else if (ConstantMB.MessageTag.FIELD.getName().equals(qName)) {
			buildField(attributes);
		} else if (ConstantMB.MessageTag.VALUE_RANGE.getName().equals(qName)) {
			buildValueRange(attributes);
		} else if (ConstantMB.MessageTag.VALUE.getName().equals(qName)) {
			this.valueRange = new ValueRange();
			String attrVal = attributes.getValue("value");

			Assert.notBlank(attrVal, () -> new CommonException("field[@name=/value-range/value/@value"));

			if (valueRangeMap.containsKey(attrVal))
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(this.field.getName())
						.append("']/value-range/value[@value='").append(attrVal).append("'] ")
						.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.reduplicate")).toString());
			this.valueRange.setValue(attrVal);

			this.valueRange.setShortText(attributes.getValue("short-text"));

			attrVal = attributes.getValue("reference");
			if (attrVal != null) {
				attrVal = attrVal.trim();
				if (0 == attrVal.length())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": fiedl[@name='").append(this.field.getName())
							.append("']/value-range/value/@reference ").append(MultiLanguageResourceBundle.getInstance().getString("blank"))
							.toString());
				this.valueRange.setReferenceId(attrVal);
				Message message2 = MessageMetadataManager.getMessage(this.groupId, attrVal);
				if (null == message2)
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
							.append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.field.valueRange.reference.notExist", new String[] { attrVal }))
							.toString());
				this.valueRange.setReference(message2);
				if (null == valueRangeMap.get("default-ref"))
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(this.field.getName())
							.append("']/@default-ref ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
			}
			valueRangeMap.put(this.valueRange.getValue(), this.valueRange);
		} else if ("event".equals(qName)) {
			String attrVal = attributes.getValue("type");
			Assert.notBlank(attrVal, () -> new CommonException(": event/@type.blank"));
			tmpFieldAttr = attrVal;
		} else if ("variable".equals(qName)) {
			this.variable = new Variable();
			String attrVal = attributes.getValue("name");
			Assert.notBlank(attrVal, () -> new CommonException(": variable/@name.null"));
			this.variable.setName(attrVal);

			attrVal = attributes.getValue("data-type");
			Assert.notBlank(attrVal, () -> new CommonException("variable[@name=@data-type.blank"));
			try {
				this.variable.setDataType(Constant.getDataTypeByText(attrVal));
			} catch (Exception exception) {
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": variable[@name='").append(this.variable.getName())
						.append("']/@data-type ")
						.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong"))
						.append(exception.getMessage()).toString(), exception);
			}

			attrVal = attributes.getValue("value");
			Assert.notBlank(attrVal, () -> new CommonException("variable[@name=&value.null"));

			this.variable.setValue(attrVal);
			variableMap.put(this.variable.getName(), this.variable);
		}
	}

	private void buildValueRange(Attributes attributes) {
		valueRangeMap.clear();
		this.valueRange = new ValueRange();
		String s5 = attributes.getValue("default-ref");
		if (null != s5) {
			s5 = s5.trim();
			if (0 == s5.trim().length())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(this.field.getName())
						.append("']/value-range/value-ref ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
			this.valueRange.setValue("default-ref");
			this.valueRange.setReferenceId(s5);
			Message message1 = MessageMetadataManager.getMessage(this.groupId, s5);
			this.valueRange.setReference(message1);
			valueRangeMap.put("default-ref", this.valueRange);
		}
	}

	private void buildField(Attributes attributes) {
		Field field = (Field) fieldList.remove();
		if (field != null && field.getFieldType() != 2002 && field.getFieldType() != 2003 && field.getFieldType() != 2004
				&& field.getFieldType() != 2011)
			throw new CommonException(
					(new StringBuilder())
							.append(fileAbsolutePath).append(": ").append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.fieldType.illegal", new String[] { field.getName() }))
							.toString());
		Field field1 = new Field();
		String s11 = attributes.getValue(ConstantMB.FieldAttr.NAME.getName());
		Assert.notBlank(s11, () -> new CommonException("field/@name null"));

		if (MessageMetadataManager.keyWordSet.contains(s11))
			throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field/@name [").append(s11).append("] ")
					.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.field.isReserved")).toString());
		if (field != null) {
			if (field.getSubFields().containsKey(s11))
				throw new CommonException((new StringBuilder())
						.append(fileAbsolutePath).append(": ").append(MultiLanguageResourceBundle.getInstance()
								.getString("MessageMetadataManager.startElement.field.subField.reduplicate", new String[] { field.getName() }))
						.append(":").append(s11).toString());
		} else if (this.message.getFields().containsKey(s11))
			throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
					.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.field.reduplicate")).append(":")
					.append(s11).toString());
		field1.setName(s11);

		s11 = attributes.getValue(ConstantMB.FieldAttr.FIELD_TYPE.getName());
		Assert.notBlank(s11, () -> new CommonException("field/@field-type null"));
		try {
			field1.setFieldType(Constant.getFieldTypeByText(s11));
		} catch (Exception exception1) {
			throw new CommonException(
					(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName()).append("']/@field-type ")
							.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong")).toString());
		}

		s11 = attributes.getValue(ConstantMB.FieldAttr.PADDING.getName());
		if (null != s11) {
			s11 = s11.trim();
			if (0 != s11.length()) {
				byte[] abyte0 = s11.getBytes();
				if (1 == abyte0.length)
					field1.setPadding(abyte0[0]);
				else if (4 == abyte0.length && s11.startsWith("0x")) {
					byte[] abyte1 = CodeUtil.HextoByte(s11.substring(2));
					field1.setPadding(abyte1[0]);
				} else {
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
							.append("']/@padding ")
							.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.padding.format.wrong"))
							.toString());
				}
			}
		}
		s11 = attributes.getValue(ConstantMB.FieldAttr.PADDING_DIRECTION.getName());
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
		s11 = attributes.getValue(ConstantMB.FieldAttr.REQUIRED.getName());
		if (null != s11) {
			s11 = s11.trim();
			if ("true".equalsIgnoreCase(s11))
				field1.setRequired(true);
			else
				field1.setRequired(false);
		} else {
			field1.setRequired(MessageMetadataManager.IS_REQUIRED);
		}
		s11 = attributes.getValue(ConstantMB.FieldAttr.EDITABLE.getName());
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
			s11 = attributes.getValue(ConstantMB.FieldAttr.XPATH.getName());
			field1.setXpath(s11);
		}
		field1.setShortText(attributes.getValue(ConstantMB.FieldAttr.SHORT_TEXT.getName()));
		s11 = attributes.getValue("iso8583-no");
		if (null != s11) {
			s11 = s11.trim();
			if (0 == s11.length())
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
						.append("']/@iso8583-no ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
			try {
				field1.setIso8583_no(Integer.parseInt(s11));
			} catch (Exception exception2) {
				throw new CommonException(
						(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName()).append("']/@iso8583-no ")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.wrong")).toString());
			}
		}
		s11 = attributes.getValue("prefix");
		if (null != s11 && s11.length() > 0) {
			if (s11.length() > 2 && s11.startsWith("0x")) {
				String s12 = s11.substring(2);
				if (!CodeUtil.isHexString(s12))
					throw new CommonException(
							(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName()).append("']/@prefix ")
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
					throw new CommonException(
							(new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName()).append("']/@suffix ")
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
				String[] as = s11.split(";");
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
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@ref-length-field-offset ")
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
							.append("']/@length-field-data-type ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
				s11 = s11.trim();
				if (0 == s11.length())
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
							.append("']/@length-field-data-type ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
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
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getCalcType())
							.append("']/@length-field-length ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
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
		if (1002 == this.message.getType() || 1003 == this.message.getType()) {
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
					Message message3 = MessageMetadataManager.getMessage(this.groupId, s11);
					field1.setReference(message3);
				}
				field1.setReferenceId(s11);
			}
			field1.setSubFields(new SortHashMap<>(32));
			if (2004 == field1.getFieldType()) {
				s11 = attributes.getValue("row-num-field");
				if (s11 != null) {
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@row-num-field ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					field1.setRowNumFieldName(s11);
				}
				s11 = attributes.getValue("row-xpath");
				if (s11 != null) {
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@row-xpath ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
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
					throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append("'] ").append(
							MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.lengthField.startField.null"))
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
				s11 = attributes.getValue("data");
				if (null != s11) {
					s11 = s11.trim();
					if (0 != s11.length()) {
						String[] as1 = s11.split(";");
						HashMap<String, String> hashmap = new HashMap<>();
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
				if (3000 == field1.getDataType() || 3001 == field1.getDataType() || 3002 == field1.getDataType() || 3010 == field1.getDataType()) {
					s11 = attributes.getValue("length");
					if (null == s11)
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@length ").append(MultiLanguageResourceBundle.getInstance().getString("null")).toString());
					s11 = s11.trim();
					if (0 == s11.length())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@length ").append(MultiLanguageResourceBundle.getInstance().getString("blank")).toString());
					try {
						field1.setLength(Integer.parseInt(s11));
					} catch (Exception exception10) {
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@length[").append(s11).append("] ")
								.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.format.wrong"))
								.toString());
					}
				} else if (3006 == field1.getDataType()) {
					if (null == field1.getPattern())
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("'] ").append(MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.datatime.pattern.null"))
								.toString());
					try {
						new SimpleDateFormat(field1.getPattern());
					} catch (Exception exception11) {
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("'] ").append(MultiLanguageResourceBundle.getInstance()
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
						throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='").append(field1.getName())
								.append("']/@length[ ").append(s11).append("] ")
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

					case 3005, 3008:
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
								.append(MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.field.unsupportDataType", new String[] { field1.getName() }))
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
					throw new CommonException((new StringBuilder())
							.append(fileAbsolutePath).append(": ").append(MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.field.defaultValue.null", new String[] { field1.getName(), s11 }))
							.toString());
				if (2000 == field1.getFieldType() || 2005 == field1.getFieldType()) {
					if (3000 == field1.getDataType() || 3001 == field1.getDataType() || 3010 == field1.getDataType()) {
						if (s11.length() > field1.getLength())
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
									.append(MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.field.defaultValue.tooLong", new String[] { field1.getName(), s11 }))
									.toString());
						if (field1.isStrictDataLength() && s11.length() < field1.getLength())
							throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
									.append(MultiLanguageResourceBundle.getInstance()
											.getString("MessageMetadataManager.startElement.field.defaultValue.tooShort", new String[] {
													field1.getName(), s11, (new StringBuilder()).append("").append(field1.getLength()).toString() }))
									.toString());
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
								new BigDecimal(s11);
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
								byte[] abyte2 = s11.getBytes();
								if (1 != abyte2.length)
									if (4 == abyte2.length && s11.startsWith("0x"))
										abyte2 = CodeUtil.HextoByte(s11.substring(2));
									else
										throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": field[@name='")
												.append(field1.getName()).append("']/@padding ").append(MultiLanguageResourceBundle.getInstance()
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
						byte[] abyte3 = s11.trim().getBytes();
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
				File file = (File) MessageMetadataManager.groupPathCache.get(this.groupId);
				String s17 = file.getAbsolutePath();
				if (!s17.endsWith(System.getProperty("file.separator")))
					s17 = (new StringBuilder()).append(s17).append(System.getProperty("file.separator")).toString();
				String s18 = (new StringBuilder()).append(s17).append("value-range").append(System.getProperty("file.separator")).append(s11)
						.append(".xml").toString();
				File file1 = new File(s18);
				ValueRangeHandler vrHandler = new ValueRangeHandler();
				valueRangeMap = vrHandler.parseValueRange(this.groupId, file1);
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
			fieldList.add(field);
		} else {
			this.message.setField(field1.getName(), field1);
		}
		fieldList.add(field1);
		this.field = field1;
	}

	private void buildMessageBean(Attributes attributes) {
		String attrVal = attributes.getValue(ConstantMB.MessageBeanAttr.ID.getName());
		Assert.notBlank(attrVal, () -> new CommonException("message-bean/@id null"));
		this.message.setId(attrVal);

		attrVal = attributes.getValue(ConstantMB.MessageBeanAttr.TYPE.getName());
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			this.message.setType(Constant.getMessageTypeByText(attrVal));
		}

		this.message.setShortText(attributes.getValue(ConstantMB.MessageBeanAttr.SHORT_TEXT.getName()));
		this.message.setGroupId(this.groupId);

		attrVal = attributes.getValue(ConstantMB.MessageBeanAttr.CLASS.getName());
		Assert.notBlank(attrVal, () -> new CommonException("message-bean/@class null"));

		if (MessageMetadataManager.cacheByClass.containsKey(attrVal)) {
			Message message = MessageMetadataManager.cacheByClass.get(attrVal);
			if (this.groupId.equalsIgnoreCase(message.getGroupId()))
				throw new CommonException((new StringBuilder()).append(fileAbsolutePath).append(": ")
						.append(MultiLanguageResourceBundle.getInstance().getString("MessageMetadataManager.startElement.messageClass.reduplicate"))
						.toString());
		}
		this.message.setClassName(attrVal);

		attrVal = attributes.getValue(ConstantMB.MessageBeanAttr.XPATH.getName());
		this.message.setXpath(attrVal);

		attrVal = attributes.getValue(ConstantMB.MessageBeanAttr.MESSAGE_CHARSET.getName());
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			this.message.setMsgCharset(attrVal);
		}

		if (ConstantMB.MessageType.TAG.getCode() == this.message.getType() || ConstantMB.MessageType.SWIFT.getCode() == this.message.getType()) {
			attrVal = attributes.getValue(ConstantMB.MessageBeanAttr.PREFIX.getName());
			Assert.notBlank(attrVal, () -> new CommonException("Tag/Swift message-bean/@prefix null"));

			if (attrVal.length() > 2 && attrVal.startsWith("0x")) {
				String s9 = attrVal.substring(2);
				Assert.isTrue(CodeUtil.isHexString(s9),
						() -> new CommonException("MessageMetadataManager.startElement.notHexString Tag/Swift message-bean/@prefix"));

				this.message.setPrefixString(s9);
				this.message.setPrefix(CodeUtil.HextoByte(s9));
			} else {
				this.message.setPrefixString(new String(CodeUtil.BCDtoASC(attrVal.getBytes())));
				this.message.setPrefix(attrVal.getBytes());
			}

			Assert.notBlank(attrVal, () -> new CommonException("Tag/Swift message-bean/@suffix null"));

			if (attrVal.length() > 2 && attrVal.startsWith("0x")) {
				String s10 = attrVal.substring(2);
				Assert.isTrue(CodeUtil.isHexString(s10),
						() -> new CommonException("MessageMetadataManager.startElement.notHexString Tag/Swift message-bean/@suffix"));

				this.message.setSuffixString(s10);
				this.message.setSuffix(CodeUtil.HextoByte(s10));
			} else {
				this.message.setSuffixString(new String(CodeUtil.BCDtoASC(attrVal.getBytes())));
				this.message.setSuffix(attrVal.getBytes());
			}
		}
	}

	public MessageHandler() {
		this.message = new Message();
		fileAbsolutePath = null;
		this.groupId = null;
		fieldList = new FieldList();
		this.valueRangeMap = new HashMap<>();
		this.valueRange = null;
		variableMap = new HashMap<>();
		this.variable = null;
		this.field = null;
		tmpFieldAttr = null;
		nodeValue = null;
	}

}
