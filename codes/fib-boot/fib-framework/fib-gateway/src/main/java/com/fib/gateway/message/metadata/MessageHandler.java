package com.fib.gateway.message.metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.util.EnumConstants;
import com.fib.gateway.message.util.EnumConstants.MsgTypeList;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.message.Constant;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

/**
 * MessageBean处理器
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-30
 */
public class MessageHandler extends DefaultHandler {
	public class FieldHandle {
		private List<Field> fields;

		private FieldHandle() {
			this.fields = new ArrayList<>();
		}

		// 移除最后一个元素并返回
		public Field pop() {
			return this.fields.isEmpty() ? null : this.fields.remove(this.fields.size() - 1);
		}

		public void addField(Field field) {
			this.fields.add(field);
		}
	}

	private Message message;
	private String beanFilePath;
	private String channelId;
	FieldHandle fieldHandle;
	private Map<String, Object> valueRangeMap;
	private ValueRange valueRange;
	private Map<String, Variable> variableMap;
	private Variable variable;
	private Field valueRangeField;
	private String eventValue;
	private String currentValue;

	MessageHandler() {
		this.message = new Message();
		this.beanFilePath = null;
		this.channelId = null;
		this.fieldHandle = new MessageHandler.FieldHandle();
		this.valueRangeMap = new HashMap<>();
		this.valueRange = null;
		this.variableMap = new HashMap<>();
		this.variable = null;
		this.valueRangeField = null;
		this.eventValue = null;
		this.currentValue = null;
	}

	public Message parseFile(String channelId, File beanFile) {
		this.beanFilePath = beanFile.getAbsolutePath();
		try (FileInputStream fis = new FileInputStream(beanFile)) {
			return this.parseFile(channelId, fis);
		} catch (Exception e) {
			throw new CommonException("Failed to read file", e);
		}
	}

	public Message parseFile(String channelId, InputStream is) {
		this.channelId = channelId;
		SAXParserFactory saxParserFactory = SAXParserFactory.newDefaultInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse(is, this);
		} catch (Exception e) {
			throw new CommonException("Failed to read file", e);
		}
		this.check();
		this.checkFields(this.message.getFields());
		this.buildClassCache(this.message.getClassName(), this.message.getFields());
		return this.message;
	}

	private void check() {
		Field field;
		Iterator<Field> fieldIter;
		if (EnumConstants.MsgType.TAG.getCode() == this.message.getType()
				|| EnumConstants.MsgType.SWIFT.getCode() == this.message.getType()) {
			fieldIter = this.message.getFields().values().iterator();
			Set<String> var3 = new HashSet<>(this.message.getFields().size());
			while (fieldIter.hasNext()) {
				field = fieldIter.next();
				if (null == field.getTag()) {
					throw new CommonException("check.tag.noTag");
				}

				if (EnumConstants.MsgType.TAG.getCode() == this.message.getType()
						&& !MsgTypeList.TAG_LIST.getArrayList().contains(field.getFieldType())) {
					throw new CommonException("check.tag.illegalType");
				}

				if (var3.contains(field.getTag())) {
					throw new CommonException("check.tag.reduplicate");
				}
				var3.add(field.getTag());
			}
		} else if (EnumConstants.MsgType.XML.getCode() == this.message.getType()) {
			fieldIter = this.message.getFields().values().iterator();
			while (fieldIter.hasNext()) {
				field = fieldIter.next();
				if (EnumConstants.FieldType.VAR_FIELD.getCode() == field.getFieldType()
						|| EnumConstants.FieldType.VAR_COMBINE_FIELD.getCode() == field.getFieldType()
						|| EnumConstants.FieldType.VAR_REFERENCE_FIELD.getCode() == field.getFieldType()) {
					throw new CommonException("check.xml.illegalFieldType");
				}
			}
		}
	}

	private void getA(Map<String, Field> var1, Field field) {
		if (3001 != field.getDataType() && 3003 != field.getDataType() && 3005 != field.getDataType()
				&& 3004 != field.getDataType() && 3007 != field.getDataType() && 3008 != field.getDataType()
				&& 3009 != field.getDataType()) {
			throw new CommonException("");
		} else {
			Field var3 = var1.get(field.getStartFieldName());
			if (null == var3) {
				throw new CommonException("");
			} else {
				field.setStartField(var3);
				if (field.getStartFieldName().equals(field.getEndFieldName())) {
					field.setEndField(field.getStartField());
				} else {
					Field var4 = var1.get(field.getEndFieldName());
					if (null == var4) {
						throw new CommonException("");
					}
					field.setEndField(var4);
				}

			}
		}
	}

	private void checkFields(Map<String, Field> sorthashmap) {
		Iterator<Field> iterator = sorthashmap.values().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Field field = iterator.next();
			i++;
			if ((EnumConstants.ReferenceType.DYNAMIC.code().equalsIgnoreCase(field.getReferenceType())
					||EnumConstants.ReferenceType.EXPRESSION.code().equalsIgnoreCase(field.getReferenceType())) && 2002 != field.getFieldType()
					&& 2003 != field.getFieldType() && 2004 != field.getFieldType())
				throw new CommonException("MessageMetadataManager.postParse.dynamicField.illegalType");
			if (2001 == field.getFieldType() || 2003 == field.getFieldType() || 2009 == field.getFieldType())
				if (field.getRefLengthFieldName() != null) {
					Field field1 = sorthashmap.get(field.getRefLengthFieldName());
					if (null == field1)
						throw new CommonException("MessageMetadataManager.postParse.refLengthField.notExist");
					if (2007 != field1.getFieldType())
						throw new CommonException("MessageMetadataManager.postParse.refLengthField.notLengthField");
					field.setRefLengthField(field1);
				} else {
					switch (field.getLengthFieldDataType()) {
					case 3001:
						break;

					case 3009:
						if (field.getLengthFieldLength() != 8)
							throw new CommonException("MessageMetadataManager.postParse.lengthFieldLength.notEqual8");
						break;

					case 3003:
					case 3007:
						if (field.getLengthFieldLength() != 4)
							throw new CommonException("MessageMetadataManager.postParse.lengthFieldLength.notEqual4");
						break;

					case 3004:
						if (field.getLengthFieldLength() != 1)
							throw new CommonException("MessageMetadataManager.postParse.lengthFieldLength.notEqual1");
						break;

					case 3005:
					case 3008:
						if (field.getLengthFieldLength() != 2)
							throw new CommonException("MessageMetadataManager.postParse.lengthFieldLength.notEqual2");
						break;

					case 3002:
					case 3006:
					default:
						throw new CommonException("MessageMetadataManager.postParse.lengthFieldLength.illegalDataType");
					}
				}
			if (2007 == field.getFieldType())
				getA(sorthashmap, field);
			if (2005 == field.getFieldType()) {
				if (3001 != field.getDataType() && 3003 != field.getDataType() && 3005 != field.getDataType()
						&& 3004 != field.getDataType() && 3007 != field.getDataType() && 3008 != field.getDataType())
					throw new CommonException("MessageMetadataManager.postParse.tableRowNumberField.illegalDataType");
				Field field2 = sorthashmap.get(field.getTableName());
				if (null == field2)
					throw new CommonException("MessageMetadataManager.postParse.tableRowNumberField.tableField.null");
				if (field2.getFieldType() != 2004 && field2.getFieldType() != 2011)
					throw new CommonException(
							"MessageMetadataManager.postParse.tableRowNumberField.tableField.illegalType");
				field.setTable(field2);
			}
			if (2004 == field.getFieldType() && field.getRowNumFieldName() != null) {
				Field field3 = sorthashmap.get(field.getRowNumFieldName());
				if (null != field3) {
					if (field3.getFieldType() != 2005)
						throw new CommonException(
								"MessageMetadataManager.postParse.tableField.tableRowNumberField.illegalType");
					field.setRowNumField(field3);
				}
			}
			if (1002 == message.getType() && null == field.getSubFields() && null == field.getReference())
				throw new CommonException("']/@reference ");
			if (2010 == field.getFieldType()) {
				if (3002 != field.getDataType())
					throw new CommonException("MessageMetadataManager.postParse.macField.illegalDataType");
				Field field4 = sorthashmap.get(field.getStartFieldName());
				if (null != field4) {
					field.setStartField(field4);
					if (field.getStartFieldName().equals(field.getEndFieldName())) {
						field.setEndField(field.getStartField());
					} else {
						Field field5 = sorthashmap.get(field.getEndFieldName());
						if (null == field5)
							throw new CommonException("MessageMetadataManager.postParse.field.endField.null");
						field.setEndField(field5);
					}
				}
			}
			if (2011 == field.getFieldType() && null == field.getRefLengthFieldName() && null == field.getTabSuffix()
					&& (null == field.getSuffix() || !field.isLastRowSuffix()) && i < sorthashmap.size())
				throw new CommonException("MessageMetadataManager.postParse.varField.error");
		}
	}

	private void buildClassCache(String className, Map<String, Field> fields) {
		Field var3 = null;
		Iterator<Field> var4 = fields.values().iterator();

		while (true) {
			do {
				if (!var4.hasNext()) {
					return;
				}
				var3 = var4.next();
			} while (2002 != var3.getFieldType() && 2003 != var3.getFieldType() && 2004 != var3.getFieldType()
					&& 2011 != var3.getFieldType());

			if (!EnumConstants.ReferenceType.DYNAMIC.code().equalsIgnoreCase(var3.getReferenceType())
					&& !EnumConstants.ReferenceType.EXPRESSION.code().equalsIgnoreCase(var3.getReferenceType()) && null == var3.getReference()) {
				Message var5 = new Message();
				var5.setId(this.message.getId() + "-" + var3.getName());
				var5.setShortText(var3.getShortText());
				var5.setFields(var3.getSubFields());
				if (MessageBean.COMMON_MESSAGEBEAN_CLASS.equals(this.message.getClassName())) {
					var5.setClassName(MessageBean.COMMON_MESSAGEBEAN_CLASS);
				} else {
					String var6 = var3.getCombineOrTableFieldClassName();
					if (null == var6) {
						var6 = className + StrUtil.upperFirst(var3.getName());
					}

					var5.setClassName(var6);
					MessageMetadataManager.getClassCache().put(var5.getClassName(), var5);
				}

				this.buildClassCache(var5.getClassName(), var5.getFields());
			}
		}
	}

	@Override
	public void endElement(String var1, String var2, String var3) throws SAXException {
		if ("field".equals(var3)) {
			Field var4 = this.fieldHandle.pop();
			if (var4.getFieldType() == 2002 && var4.getReference() == null
					&& !EnumConstants.ReferenceType.DYNAMIC.code().equalsIgnoreCase(var4.getReferenceType())
					&& !EnumConstants.ReferenceType.EXPRESSION.code().equalsIgnoreCase(var4.getReferenceType())) {
				if (var4.getReference() == null) {
					this.checkFields(var4.getSubFields());
				}

				if (1002 == this.message.getType() && var4.getFieldType() == 2002 && var4.getReference() == null) {
					throw new CommonException("");
				}
			}

			if ((2002 == var4.getFieldType() || 2003 == var4.getFieldType() || 2004 == var4.getFieldType()
					|| 2011 == var4.getFieldType()) && var4.getReference() == null) {
				this.checkFields(var4.getSubFields());
			}

			if ((2000 == var4.getFieldType() || 2001 == var4.getFieldType()) && 3010 == var4.getDataType()) {
				if (null == var4.getPattern()) {
					throw new CommonException("");
				}

				String[] var5 = var4.getPattern().split(",");
				if (var5.length > 2) {
					throw new CommonException(this.beanFilePath);
				}

				try {
					Integer.parseInt(var5[0].trim());
				} catch (Exception var8) {
					throw new CommonException(this.beanFilePath + " :");
				}

				if (var5.length == 2) {
					try {
						Integer.parseInt(var5[1].trim());
					} catch (Exception var7) {
						throw new CommonException(this.beanFilePath + " :");
					}
				}
			}

			if (0 != this.valueRangeMap.size()) {
				this.valueRangeField.setValueRange(new HashMap<>());
				this.valueRangeField.getValueRange().putAll(this.valueRangeMap);
				this.valueRangeMap.clear();
			}

			this.valueRangeField = null;
		} else if ("value-range".equals(var3)) {
			this.valueRangeField.setValueRange(new HashMap<>());
			this.valueRangeField.getValueRange().putAll(this.valueRangeMap);
			this.valueRangeMap.clear();
		} else {
			String var9;
			if ("expression".equals(var3)) {
				var9 = this.currentValue;
				if (null != var9) {
					var9 = var9.trim();
				}

				if (0 == var9.length()) {
					var9 = null;
				}

				if (this.valueRangeField != null) {
					this.valueRangeField.setExpression(var9);
				}

				this.currentValue = null;
			} else if ("event".equals(var3)) {
				var9 = this.currentValue;
				if (var9 != null) {
					var9 = var9.trim();
				}

				if (0 == var9.length()) {
					var9 = null;
				}

				boolean var10 = false;
				if (null == this.valueRangeField) {
					this.valueRangeField = this.fieldHandle.pop();
					var10 = true;
				}

				if ("row-post-pack".equalsIgnoreCase(this.eventValue)) {
					if (this.valueRangeField != null) {
						this.valueRangeField.setPostRowPackEvent(var9);
					}
				} else if ("row-pre-pack".equalsIgnoreCase(this.eventValue)) {
					if (this.valueRangeField != null) {
						this.valueRangeField.setPreRowPackEvent(var9);
					}
				} else if ("row-post-parse".equalsIgnoreCase(this.eventValue)) {
					if (this.valueRangeField != null) {
						this.valueRangeField.setPostRowParseEvent(var9);
					}
				} else if ("row-pre-parse".equalsIgnoreCase(this.eventValue)) {
					if (this.valueRangeField != null) {
						this.valueRangeField.setPreRowParseEvent(var9);
					}
				} else if ("post-pack".equalsIgnoreCase(this.eventValue)) {
					if (this.valueRangeField != null) {
						this.valueRangeField.setPostPackEvent(var9);
					} else {
						this.message.setPostPackEvent(var9);
					}
				} else if ("pre-pack".equalsIgnoreCase(this.eventValue)) {
					if (this.valueRangeField != null) {
						this.valueRangeField.setPrePackEvent(var9);
					} else {
						this.message.setPrePackEvent(var9);
					}
				} else if ("post-parse".equalsIgnoreCase(this.eventValue)) {
					if (this.valueRangeField != null) {
						this.valueRangeField.setPostParseEvent(var9);
					} else {
						this.message.setPostParseEvent(var9);
					}
				} else {
					if (!"pre-parse".equalsIgnoreCase(this.eventValue)) {
						throw new CommonException(
								this.beanFilePath + ": Unsupport Event Type[" + this.eventValue + "]!");
					}

					if (this.valueRangeField != null) {
						this.valueRangeField.setPreParseEvent(var9);
					} else {
						this.message.setPreParseEvent(var9);
					}
				}

				if (var10 && null != this.valueRangeField) {
					this.fieldHandle.addField(this.valueRangeField);
				}

				this.currentValue = null;
			} else if ("template".equals(var3)) {
				var9 = this.currentValue;
				if (var9 != null) {
					var9 = var9.trim();
				}

				if (0 == var9.length()) {
					var9 = null;
				}

				this.message.setTemplate(var9);
				this.currentValue = null;
			} else if ("message-bean".equals(var3) && this.variableMap.size() != 0) {
				this.message.setVariable(new HashMap<>());
				this.message.getVariable().putAll(this.variableMap);
				this.variableMap.clear();
			}
		}

	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (null == this.currentValue) {
			this.currentValue = new String(ch, start, length);
		} else {
			this.currentValue = this.currentValue + new String(ch, start, length);
		}

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		String attrValue;
		if (EnumConstants.MESSAGE_BEAN_NAME.equals(qName)) {
			buildMessageBean(attrs);
		} else if (EnumConstants.FIELD_BEAN_NAME.equals(qName)) {
			buildField(attrs);
		} else if (EnumConstants.VALUE_RANGE_NAME.equals(qName)) {
			buildValueRange(attrs);
		} else if (EnumConstants.VALUE_NAME.equals(qName)) {
			buildValue(attrs);
		} else if (EnumConstants.EVENT.equals(qName)) {
			attrValue = attrs.getValue(EnumConstants.FieldAttr.TYPE.code());
			ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": event/@type ");
			this.eventValue = attrValue;
		} else if (EnumConstants.VARIABLE.equals(qName)) {
			buildVariable(attrs);
		}
	}

	private void buildMessageBean(Attributes attrs) {
		String attrValue = attrs.getValue(EnumConstants.MessageAttr.ID.code());
		ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": message-bean/@id ");
		this.message.setId(attrValue);

		attrValue = attrs.getValue(EnumConstants.MessageAttr.TYPE.code());
		if (StrUtil.isNotBlank(attrValue)) {
			this.message.setType(Constant.getMessageTypeByText(attrValue));
		}

		this.message.setShortText(attrs.getValue(EnumConstants.MessageAttr.SHORT_TEXT.code()));
		this.message.setGroupId(this.channelId);
		attrValue = attrs.getValue(EnumConstants.MessageAttr.CLASS.code());
		ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": message-bean/@class ");

		if (!MessageBean.COMMON_MESSAGEBEAN_CLASS.equals(attrValue)
				&& MessageMetadataManager.getClassCache().containsKey(attrValue)) {
			Message messageTmp = MessageMetadataManager.getClassCache().get(attrValue);
			if (this.channelId.equalsIgnoreCase(messageTmp.getGroupId())) {
				throw new CommonException(this.beanFilePath + ": " + MultiLanguageResourceBundle.getInstance()
						.getString("MessageMetadataManager.startElement.messageClass.reduplicate"));
			}
		}

		this.message.setClassName(attrValue);

		attrValue = attrs.getValue(EnumConstants.MessageAttr.XPATH.code());
		this.message.setXpath(attrValue);

		attrValue = attrs.getValue(EnumConstants.MessageAttr.SCHEMA_VALID.code());
		if ("true".equals(attrValue)) {
			this.message.setSchemaValid(true);
		}

		attrValue = attrs.getValue(EnumConstants.MessageAttr.SCHEMA_VALID_TYPE.code());

		if (StrUtil.isNotBlank(attrValue)) {
			this.message.setSchemaValidType(Constant.getSchemaTypeByTxt(attrValue));
		}

		attrValue = attrs.getValue(EnumConstants.MessageAttr.SCHEMA_VALID_PATH.code());
		this.message.setSchemaValidPath(attrValue);

		attrValue = attrs.getValue(EnumConstants.MessageAttr.MESSAGE_CHARSET.code());
		if (StrUtil.isNotBlank(attrValue)) {
			this.message.setMsgCharset(attrValue);
		}

		if (EnumConstants.MsgType.TAG.getCode() == this.message.getType()
				|| EnumConstants.MsgType.SWIFT.getCode() == this.message.getType()) {
			attrValue = attrs.getValue(EnumConstants.MessageAttr.PREFIX.code());
			ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": Tag/Swift message-bean/@prefix");

			//
			if (HexUtil.isHexNumber(attrValue)) {
				String prefixTmp = attrValue.substring(2);
				this.message.setPrefixString(prefixTmp);
				this.message.setPrefix(HexUtil.decodeHex(prefixTmp));
			} else {
				this.message.setPrefixString(new String(HexUtil.encodeHex(attrValue.getBytes())));
				this.message.setPrefix(attrValue.getBytes());
			}

			attrValue = attrs.getValue(EnumConstants.MessageAttr.SUFFIX.code());
			ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": Tag/Swift message-bean/@suffix ");

			if (HexUtil.isHexNumber(attrValue)) {
				String suffixTmp = attrValue.substring(2);
				this.message.setSuffixString(suffixTmp);
				this.message.setSuffix(HexUtil.decodeHex(suffixTmp));
			} else {
				this.message.setSuffixString(new String(HexUtil.encodeHex(attrValue.getBytes())));
				this.message.setSuffix(attrValue.getBytes());
			}
		}

		attrValue = attrs.getValue(EnumConstants.MessageAttr.EXTENDED_ATTRIBUTES.code());
		if (StrUtil.isNotBlank(attrValue)) {
			this.message.setExtendedAttributeText(attrValue);
			String[] extendedAttrs = attrValue.split(";");
			for (int i = 0, len = extendedAttrs.length; i < len; ++i) {
				this.message.setExtendAttribute(extendedAttrs[i].substring(0, extendedAttrs[i].indexOf(":")),
						extendedAttrs[i].substring(extendedAttrs[i].indexOf(":") + 1));
			}
		}

		attrValue = attrs.getValue(EnumConstants.MessageAttr.NAME_SPACE.code());
		if (StrUtil.isNotBlank(attrValue)) {
			String[] namespaceAttrs = attrValue.split("\\|");
			Map<String, String> tmpMap = new HashMap<>();
			for (int i = 0, len = namespaceAttrs.length; i < len; ++i) {
				int var50 = namespaceAttrs[i].indexOf("=");
				tmpMap.put(namespaceAttrs[i].substring(0, var50), namespaceAttrs[i].substring(var50 + 1));
			}
			this.message.setNameSpaces(tmpMap);
		}

		attrValue = attrs.getValue(EnumConstants.MessageAttr.REMOVE_BLANK_NODE.code());
		if (StrUtil.isNotBlank(attrValue) && "true".equals(attrValue)) {
			this.message.setRemoveBlankNode(true);
		}
	}

	private void buildField(Attributes attrs) {
		Field var40 = this.fieldHandle.pop();
		if (var40 != null && var40.getFieldType() != 2002 && var40.getFieldType() != 2003
				&& var40.getFieldType() != 2004 && var40.getFieldType() != 2011) {
			throw new CommonException(this.beanFilePath + ": " + MultiLanguageResourceBundle.getInstance().getString(
					"MessageMetadataManager.startElement.fieldType.illegal", new String[] { var40.getName() }));
		}

		Field field = new Field();
		String attrValue = attrs.getValue(EnumConstants.FieldAttr.NAME.code());
		ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": field/@name ");

		if (MessageMetadataManager.keyWordSet.contains(attrValue)) {
			throw new CommonException(
					this.beanFilePath + ": field/@name [" + attrValue + "] " + MultiLanguageResourceBundle.getInstance()
							.getString("MessageMetadataManager.startElement.field.isReserved"));
		}

		if (var40 != null) {
			if (var40.getSubFields().containsKey(attrValue)) {
				throw new CommonException(this.beanFilePath + ": "
						+ MultiLanguageResourceBundle.getInstance().getString(
								"MessageMetadataManager.startElement.field.subField.reduplicate",
								new String[] { var40.getName() })
						+ ":" + attrValue);
			}
		} else if (this.message.getFields().containsKey(attrValue)) {
			throw new CommonException(this.beanFilePath + ": " + MultiLanguageResourceBundle.getInstance()
					.getString("MessageMetadataManager.startElement.field.reduplicate") + ":" + attrValue);
		}

		field.setName(attrValue);

		attrValue = attrs.getValue(EnumConstants.FieldAttr.FIELD_TYPE.code());
		ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + field.getName() + "']/@field-type ");
		field.setFieldType(Constant.getFieldTypeByText(attrValue));

		attrValue = attrs.getValue("padding");
		if (StrUtil.isNotBlank(attrValue)) {
			byte[] paddingByte = attrValue.getBytes();
			if (1 == paddingByte.length) {
				field.setPadding(paddingByte[0]);
			} else {
				if (4 != paddingByte.length || !attrValue.startsWith("0x")) {
					throw new CommonException(this.beanFilePath + field.getName() + "']/@padding "
							+ MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.padding.format.wrong"));
				}
				paddingByte = HexUtil.decodeHex(attrValue.substring(2));
				field.setPadding(paddingByte[0]);
			}
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.PADDING_DIRECTION.code());
		if (StrUtil.isNotBlank(attrValue)) {
			if (EnumConstants.PaddingDirection.RIGHT.getName().equalsIgnoreCase(attrValue)) {
				field.setPaddingDirection(EnumConstants.PaddingDirection.RIGHT.getCode());
			} else if (EnumConstants.PaddingDirection.LEFT.getName().equalsIgnoreCase(attrValue)) {
				field.setPaddingDirection(EnumConstants.PaddingDirection.LEFT.getCode());
			} else {
				if (!EnumConstants.PaddingDirection.NONE.getName().equalsIgnoreCase(attrValue)) {
					throw new CommonException(this.beanFilePath + field.getName() + "']/@padding-direction "
							+ MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.paddingDirection.format.wrong"));
				}
				field.setPaddingDirection(EnumConstants.PaddingDirection.NONE.getCode());
			}
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.REQUIRED.code());
		if (StrUtil.isBlank(attrValue))

		{
			field.setRequired(false);
		} else {
			field.setRequired(EnumConstants.LoginFlag.TRUE.getName().equalsIgnoreCase(attrValue));
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.EDITABLE.code());

		if (StrUtil.isNotBlank(attrValue)) {
			field.setEditable(!EnumConstants.LoginFlag.FALSE.getName().equalsIgnoreCase(attrValue));
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.IS_REMOVE_UNWATCHABLE.code());
		if (StrUtil.isNotBlank(attrValue)) {
			field.setEditable(!EnumConstants.LoginFlag.FALSE.getName().equalsIgnoreCase(attrValue));
		}

		if (field.isEditable()) {
			attrValue = attrs.getValue(EnumConstants.FieldAttr.XPATH.code());
			field.setXpath(attrValue);
		}

		field.setShortText(attrs.getValue(EnumConstants.FieldAttr.SHORT_TEXT.code()));

		attrValue = attrs.getValue(EnumConstants.FieldAttr.ISO8583_NO.code());
		if (StrUtil.isNotBlank(attrValue)) {
			field.setIso8583_no(Integer.parseInt(attrValue));
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.PREFIX.code());
		if (StrUtil.isNotBlank(attrValue)) {
			if (HexUtil.isHexNumber(attrValue)) {
				String prefixTmp = attrValue.substring(2);
				field.setPrefixString(prefixTmp);
				field.setPrefix(HexUtil.decodeHex(prefixTmp));
			} else {
				field.setPrefixString(new String(HexUtil.encodeHex(attrValue.getBytes())));
				field.setPrefix(attrValue.getBytes());
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.FIR_ROW_PREFIX.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setFirRowPrefix("true".equalsIgnoreCase(attrValue));
			}
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.SUFFIX.code());
		if (StrUtil.isNotBlank(attrValue)) {
			if (HexUtil.isHexNumber(attrValue)) {
				String prefixTmp = attrValue.substring(2);
				field.setSuffixString(prefixTmp);
				field.setSuffix(HexUtil.decodeHex(prefixTmp));
			} else {
				field.setSuffixString(new String(HexUtil.encodeHex(attrValue.getBytes())));
				field.setSuffix(attrValue.getBytes());
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.LAST_ROW_SUFFIX.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setLastRowSuffix("true".equalsIgnoreCase(attrValue));
			}
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.EXTENDED_ATTRIBUTES.code());
		if (StrUtil.isNotBlank(attrValue)) {
			field.setExtendedAttributeText(attrValue);
			String[] extendedAttrs = attrValue.split(";");
			for (int i = 0, len = extendedAttrs.length; i < len; ++i) {
				field.setExtendAttribute(extendedAttrs[i].substring(0, extendedAttrs[i].indexOf(":")),
						extendedAttrs[i].substring(extendedAttrs[i].indexOf(":") + 1));
			}
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.DATA_CHARSET.code());
		if (StrUtil.isNotBlank(attrValue)) {
			field.setDataCharset(attrValue);
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.SHIELD.code());
		if (StrUtil.isNotBlank(attrValue)) {
			field.setShield("true".equals(attrValue));
		}

		if (EnumConstants.FieldType.VAR_TABLE.getCode() == field.getFieldType()) {
			attrValue = attrs.getValue(EnumConstants.FieldAttr.ROW_CUT.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setRowCut(!"false".equalsIgnoreCase(attrValue));
			}

		}

		if (EnumConstants.FieldType.VAR_FIELD.getCode() == field.getFieldType()
				|| EnumConstants.FieldType.VAR_COMBINE_FIELD.getCode() == field.getFieldType()
				|| EnumConstants.FieldType.VAR_REFERENCE_FIELD.getCode() == field.getFieldType()
				|| EnumConstants.FieldType.VAR_TABLE.getCode() == field.getFieldType()) {
			attrValue = attrs.getValue(EnumConstants.FieldAttr.REF_LENGTH_FIELD.code());

			// TODO if else 判断????
			if (StrUtil.isNotBlank(attrValue)) {
				field.setRefLengthFieldName(attrValue);
				attrValue = attrs.getValue(EnumConstants.FieldAttr.REF_LENGTH_FIELD_OFFSET.code());
				if (StrUtil.isNotBlank(attrValue)) {
					field.setRefLengthFieldOffset(Integer.parseInt(attrValue));
				}
			} else if (EnumConstants.FieldType.VAR_TABLE.getCode() == field.getFieldType()) {
				attrValue = attrs.getValue(EnumConstants.FieldAttr.LENGTH_FIELD_DATA_TYPE.code());
				ExceptionUtil.requireNotEmpty(attrValue,
						this.beanFilePath + field.getName() + "']/@length-field-data-type ");
				field.setLengthFieldDataType(Constant.getDataTypeByText(attrValue));

				attrValue = attrs.getValue(EnumConstants.FieldAttr.LENGTH_FIELD_DATA_ENCODING.code());
				if (StrUtil.isNotBlank(attrValue) && "bcd".equals(attrValue)) {
					field.setLengthFieldDataEncoding(EnumConstants.DTA_ENC_TYP_BCD);
				}

				attrValue = attrs.getValue(EnumConstants.FieldAttr.LENGTH_FIELD_LENGTH.code());
				ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath);
				field.setLengthFieldLength(Integer.parseInt(attrValue));
			} else {
				attrValue = attrs.getValue(EnumConstants.FieldAttr.TAB_PREFIX.code());
				if (StrUtil.isNotBlank(attrValue)) {
					if (HexUtil.isHexNumber(attrValue)) {
						String prefixTmp = attrValue.substring(2);
						field.setTabPrefixString(prefixTmp);
						field.setTabPrefix(HexUtil.decodeHex(prefixTmp));
					} else {
						field.setTabPrefixString(new String(HexUtil.encodeHex(attrValue.getBytes())));
						field.setTabPrefix(attrValue.getBytes());
					}
				}

				attrValue = attrs.getValue(EnumConstants.FieldAttr.TAB_SUFFIX.code());
				if (StrUtil.isNotBlank(attrValue)) {
					if (HexUtil.isHexNumber(attrValue)) {
						String suffixTmp = attrValue.substring(2);
						field.setTabSuffixString(suffixTmp);
						field.setTabSuffix(HexUtil.decodeHex(suffixTmp));
					} else {
						field.setTabSuffixString(new String(HexUtil.encodeHex(attrValue.getBytes())));
						field.setTabSuffix(attrValue.getBytes());
					}
				}
			}
			attrValue = attrs.getValue(EnumConstants.FieldAttr.MAX_LENGTH.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setMaxLength(Integer.parseInt(attrValue));
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.MIN_LENGTH.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setMinLength(Integer.parseInt(attrValue));
			}
		}

		if (EnumConstants.MsgType.TAG.getCode() == this.message.getType()
				|| EnumConstants.MsgType.SWIFT.getCode() == this.message.getType()) {
			attrValue = attrs.getValue(EnumConstants.FieldAttr.TAG.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setTag(attrValue);
			}
		}

		if (EnumConstants.FieldType.COMBINE_FIELD.getCode() != field.getFieldType()
				&& EnumConstants.FieldType.VAR_COMBINE_FIELD.getCode() != field.getFieldType()
				&& EnumConstants.FieldType.TABLE.getCode() != field.getFieldType()
				&& EnumConstants.FieldType.VAR_TABLE.getCode() != field.getFieldType()
				&& EnumConstants.FieldType.REFERENCE_FIELD.getCode() != field.getFieldType()
				&& EnumConstants.FieldType.VAR_REFERENCE_FIELD.getCode() != field.getFieldType()) {
			if (EnumConstants.FieldType.LENGTH_FIELD.getCode() == field.getFieldType()
					|| EnumConstants.FieldType.PBOC_LENGTH_FIELD.getCode() == field.getFieldType()) {
				attrValue = attrs.getValue(EnumConstants.FieldAttr.START_FIELD.code());
				ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + "'] ");
				field.setStartFieldName(attrValue);

				attrValue = attrs.getValue(EnumConstants.FieldAttr.END_FIELD.code());
				if (StrUtil.isNotBlank(attrValue)) {
					field.setEndFieldName(attrValue.trim());
				} else {
					field.setEndFieldName(field.getStartFieldName());
				}
			}

			if (EnumConstants.FieldType.MAC_FIELD.getCode() == field.getFieldType()) {
				attrValue = attrs.getValue(EnumConstants.FieldAttr.START_FIELD.code());
				if (StrUtil.isNotBlank(attrValue)) {
					field.setStartFieldName(attrValue);

					attrValue = attrs.getValue(EnumConstants.FieldAttr.END_FIELD.code());
					if (StrUtil.isNotBlank(attrValue)) {
						field.setEndFieldName(attrValue.trim());
					} else {
						field.setEndFieldName(field.getStartFieldName());
					}
				}

				attrValue = attrs.getValue(EnumConstants.FieldAttr.DATA.code());
				if (StrUtil.isNotBlank(attrValue)) {
					String[] dataArr = attrValue.split(";");
					Map<String, String> dataTmp = new HashMap<>();
					for (int i = 0, len = dataArr.length; i < len; ++i) {
						dataTmp.put(dataArr[i], dataArr[i]);
					}
					field.setMacFldDataCache(dataTmp);
				}
			}

			if (EnumConstants.FieldType.TABLE_ROW_NUM_FIELD.getCode() == field.getFieldType()) {
				attrValue = attrs.getValue(EnumConstants.FieldAttr.TABLE.code());
				ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath);
				field.setTableName(attrValue);
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.DATA_TYPE.code());
			ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath);
			field.setDataType(EnumConstants.DataType.getDataTypeCode(attrValue));

			attrValue = attrs.getValue(EnumConstants.FieldAttr.DATA_ENCODING.code());
			if (StrUtil.isNotBlank(attrValue) && "bcd".equalsIgnoreCase(attrValue)) {
				field.setDataEncoding(EnumConstants.DTA_ENC_TYP_BCD);
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.PATTERN.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setPattern(attrValue);
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.LENGTH_SCRIPT.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setLengthScript(attrValue);
			}

			if (EnumConstants.FieldType.VAR_FIELD.getCode() != field.getFieldType()) {
				if (EnumConstants.DataType.STR.getCode() != field.getDataType()
						&& EnumConstants.DataType.NUM.getCode() != field.getDataType()
						&& EnumConstants.DataType.BIN.getCode() != field.getDataType()
						&& EnumConstants.DataType.DECIMAL.getCode() != field.getDataType()
						&& EnumConstants.DataType.DOUBLE.getCode() != field.getDataType()) {
					if (EnumConstants.DataType.DATETIME.getCode() == field.getDataType()) {
						ExceptionUtil.requireNotEmpty(field.getPattern(), this.beanFilePath);
						field.setLength(field.getPattern().length());
					} else {
						attrValue = attrs.getValue(EnumConstants.FieldAttr.LENGTH.code());
						int attrValInt = Integer.parseInt(StrUtil.isBlank(attrValue) ? "0" : attrValue.trim());

						checkDataTypeLength(field.getDataType(), attrValInt);
						field.setLength(attrValInt);
					}
				} else {
					attrValue = attrs.getValue(EnumConstants.FieldAttr.LENGTH.code());
					ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath);
					field.setLength(Integer.parseInt(attrValue));
				}
			}

			attrValue = attrs.getValue("strict-data-length");
			if (StrUtil.isNotBlank(attrValue)) {
				field.setStrictDataLength(EnumConstants.LoginFlag.TRUE.getName().equalsIgnoreCase(attrValue));
			}

			if (EnumConstants.FieldType.VAR_FIELD.getCode() == field.getFieldType()) {
				field.setPaddingDirection(EnumConstants.PaddingDirection.NONE.getCode());
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.VALUE.code());
			if (StrUtil.isNotBlank(attrValue)) {
				if (EnumConstants.FieldType.FIXED_FIELD.getCode() != field.getFieldType()
						&& EnumConstants.FieldType.TABLE_ROW_NUM_FIELD.getCode() != field.getFieldType()) {
					throw new CommonException(this.beanFilePath + ": ");
				}

				if (EnumConstants.DataType.STR.getCode() != field.getDataType()
						&& EnumConstants.DataType.NUM.getCode() != field.getDataType()
						&& EnumConstants.DataType.DECIMAL.getCode() != field.getDataType()
						&& EnumConstants.DataType.DOUBLE.getCode() != field.getDataType()) {

					if (EnumConstants.DataType.INT.getCode() != field.getDataType()
							&& EnumConstants.DataType.NETINT.getCode() != field.getDataType()) {
						if (EnumConstants.DataType.SHORT.getCode() != field.getDataType()
								&& EnumConstants.DataType.NETSHORT.getCode() != field.getDataType()) {

							if (EnumConstants.DataType.BYTE.getCode() == field.getDataType()) {
								try {
									if (0 != attrValue.length()) {
										byte[] tmpByte = attrValue.getBytes();
										if (1 != tmpByte.length) {
											if (4 != tmpByte.length || !attrValue.startsWith("0x")) {
												throw new CommonException(this.beanFilePath + "format.wrong");
											}

										}
									}
								} catch (Exception e) {
									throw new CommonException(this.beanFilePath + "defaultValue.notByteNumber");
								}
							} else if (EnumConstants.DataType.DATETIME.getCode() == field.getDataType()) {
								SimpleDateFormat var47 = new SimpleDateFormat(field.getPattern());
								var47.setLenient(false);
								try {
									var47.parse(attrValue);
								} catch (Exception var15) {
									throw new CommonException(this.beanFilePath + ".datetime.illegalPattern");
								}
							} else if (EnumConstants.DataType.LONG.getCode() == field.getDataType()) {
								try {
									Long.parseLong(attrValue);
								} catch (Exception var14) {
									throw new CommonException(this.beanFilePath + ".defaultValue.notLongNumber");
								}
							} else {
								if (EnumConstants.DataType.BIN.getCode() != field.getDataType()) {
									throw new CommonException(this.beanFilePath + ".canNotHave.defaultValue.1");
								}
								byte[] tmpBype = attrValue.trim().getBytes();
								if (tmpBype.length % 2 != 0) {
									throw new CommonException(this.beanFilePath + ".defaultValue.notHexString");
								}

								if (tmpBype.length / 2 != field.getLength()) {
									throw new CommonException(
											this.beanFilePath + "defaultValue.hexString.illegalLength");
								}

								for (int i = 0; i < tmpBype.length; ++i) {
									if ((tmpBype[i] < 48 || tmpBype[i] > 57) && (tmpBype[i] < 97 || tmpBype[i] > 102)
											&& (tmpBype[i] < 65 || tmpBype[i] > 70)) {
										throw new CommonException(this.beanFilePath + "defaultValue.notHexString");
									}
								}
							}
						} else {
							try {
								Short.parseShort(attrValue);
							} catch (Exception e) {
								throw new CommonException(this.beanFilePath + "defaultValue.notShortNumber");
							}
						}
					} else {
						try {
							Integer.parseInt(attrValue);
						} catch (Exception e) {
							throw new CommonException(this.beanFilePath + "defaultValue.notIntNumber");
						}
					}
				} else {
					if (attrValue.length() > field.getLength()) {
						throw new CommonException(this.beanFilePath + ".defaultValue.tooLong");
					}

					if (field.isStrictDataLength() && attrValue.length() < field.getLength()) {
						throw new CommonException(this.beanFilePath + "defaultValue.tooShort");
					}

					if (EnumConstants.DataType.NUM.getCode() == field.getDataType()) {
						try {
							Integer.parseInt(attrValue);
						} catch (Exception var19) {
							throw new CommonException(this.beanFilePath + "defaultValue.notNumber");
						}
					} else if (EnumConstants.DataType.DECIMAL.getCode() == field.getDataType()) {
						try {
							new BigDecimal(attrValue);
						} catch (Exception e) {
							throw new CommonException(this.beanFilePath + "defaultValue.notDecimalNumber");
						}
					}
				}
				field.setValue(attrValue);
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.REF_VALUE_RANGE.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setRefValueRange(attrValue);
				File file = MessageMetadataManager.getGroupPathCache().get(this.channelId);
				String fileAbsolutePath = file.getAbsolutePath();
				if (!fileAbsolutePath.endsWith(File.separator)) {
					fileAbsolutePath = fileAbsolutePath + File.separator;
				}
				String valueRangePath = fileAbsolutePath + "value-range" + File.separator + attrValue + ".xml";
				ValueRangeHandler valueRangeHandler = new ValueRangeHandler();
				this.valueRangeMap = valueRangeHandler.getValueRangeMap(this.channelId, new File(valueRangePath));
			}
		} else {
			attrValue = attrs.getValue(EnumConstants.FieldAttr.REFERENCE_TYPE.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setReferenceType(attrValue);
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.REFERENCE.code());
			if (StrUtil.isBlank(attrValue) && (EnumConstants.FieldType.REFERENCE_FIELD.getCode() == field.getFieldType()
					|| EnumConstants.FieldType.VAR_REFERENCE_FIELD.getCode() == field.getFieldType())) {
				throw new CommonException(this.beanFilePath + field.getName() + "'] " + MultiLanguageResourceBundle
						.getInstance().getString("MessageMetadataManager.startElement.reference.null"));
			}
			if (StrUtil.isNotBlank(attrValue)) {
				if (!"dynamic".equalsIgnoreCase(field.getReferenceType())
						&& !"expression".equalsIgnoreCase(field.getReferenceType())) {
					Message refMessage = MessageMetadataManager.getMessage(this.channelId, attrValue);
					field.setReference(refMessage);
				}
				field.setReferenceId(attrValue);
			}

			field.setSubFields(new TreeMap<>());

			if (EnumConstants.FieldType.TABLE.getCode() == field.getFieldType()) {
				attrValue = attrs.getValue(EnumConstants.FieldAttr.ROW_NUM_FIELD.code());
				if (StrUtil.isNotBlank(attrValue)) {
					field.setRowNumFieldName(attrValue);
				}

				attrValue = attrs.getValue(EnumConstants.FieldAttr.ROW_XPATH.code());
				if (StrUtil.isNotBlank(attrValue)) {
					field.setRowXpath(attrValue);
				}
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.DATA_ENCODING.code());
			if (StrUtil.isNotBlank(attrValue) && "bcd".equalsIgnoreCase(attrValue)) {
				field.setDataEncoding(EnumConstants.DTA_ENC_TYP_BCD);
			}
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.CLASS.code());
		if (EnumConstants.FieldType.MAC_FIELD.getCode() == field.getFieldType()) {
			String calcType = attrs.getValue(EnumConstants.FieldAttr.CALC_TYPE.code());
			ExceptionUtil.requireNotEmpty(calcType, this.beanFilePath + ": mac-field/@calc-type ");
			field.setCalcType(calcType);

			if ("java".equalsIgnoreCase(field.getCalcType())) {
				ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": mac-field/@class ");
				field.setCombineOrTableFieldClassName(attrValue);
			}
		} else if (StrUtil.isNotBlank(attrValue)) {
			if (field.getFieldType() != EnumConstants.FieldType.COMBINE_FIELD.getCode()
					&& field.getFieldType() != EnumConstants.FieldType.VAR_COMBINE_FIELD.getCode()
					&& field.getFieldType() != EnumConstants.FieldType.TABLE.getCode()
					&& field.getFieldType() != EnumConstants.FieldType.VAR_TABLE.getCode()
					&& field.getFieldType() != EnumConstants.FieldType.MAC_FIELD.getCode()
					&& field.getFieldType() != EnumConstants.FieldType.REFERENCE_FIELD.getCode()
					&& field.getFieldType() != EnumConstants.FieldType.VAR_FIELD.getCode()) {
				throw new CommonException(this.beanFilePath + "field.canNotHave.class");
			}
			field.setCombineOrTableFieldClassName(attrValue);
		}

		if (var40 != null) {
			var40.setSubField(field.getName(), field);
			this.fieldHandle.addField(var40);
		} else {
			this.message.setField(field.getName(), field);
		}

		this.fieldHandle.addField(field);
		this.valueRangeField = field;
	}

	private void buildValueRange(Attributes attrs) {
		this.valueRangeMap.clear();
		this.valueRange = new ValueRange();
		String attrValue = attrs.getValue(EnumConstants.FieldAttr.DEFAULT_REF.code());
		if (StrUtil.isNotBlank(attrValue)) {
			this.valueRange.setValue(EnumConstants.ValueRangeAttr.DEFAULT_REF.code());
			this.valueRange.setReferenceId(attrValue);
			Message referenceMessage = MessageMetadataManager.getMessage(this.channelId, attrValue);
			this.valueRange.setReference(referenceMessage);
			this.valueRangeMap.put(EnumConstants.ValueRangeAttr.DEFAULT_REF.code(), this.valueRange);
		}
	}

	private void buildValue(Attributes attrs) {
		this.valueRange = new ValueRange();
		String attrValue = attrs.getValue(EnumConstants.FieldAttr.VALUE.code());
		ExceptionUtil.requireNotEmpty(attrValue,
				this.beanFilePath + this.valueRangeField.getName() + "value-range/value/@value ");

		if (this.valueRangeMap.containsKey(attrValue)) {
			throw new CommonException(this.beanFilePath + this.valueRangeField.getName()
					+ "']/value-range/value[@value='" + attrValue + "'] " + MultiLanguageResourceBundle.getInstance()
							.getString("MessageMetadataManager.startElement.reduplicate"));
		}

		this.valueRange.setValue(attrValue);
		this.valueRange.setShortText(attrs.getValue(EnumConstants.FieldAttr.SHORT_TEXT.code()));
		attrValue = attrs.getValue(EnumConstants.FieldAttr.REFERENCE.code());
		if (StrUtil.isNotBlank(attrValue)) {
			this.valueRange.setReferenceId(attrValue);
			Message referenceMessage = MessageMetadataManager.getMessage(this.channelId, attrValue);
			if (null == referenceMessage) {
				throw new CommonException(this.beanFilePath + ": "
						+ MultiLanguageResourceBundle.getInstance().getString(
								"MessageMetadataManager.startElement.field.valueRange.reference.notExist",
								new String[] { attrValue }));
			}

			this.valueRange.setReference(referenceMessage);
			if (null == this.valueRangeMap.get("default-ref")) {
				throw new CommonException(this.beanFilePath + this.valueRangeField.getName() + "']/@default-ref "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}
		}

		this.valueRangeMap.put(this.valueRange.getValue(), this.valueRange);
	}

	private void buildVariable(Attributes attrs) {
		this.variable = new Variable();
		String attrValue = attrs.getValue(EnumConstants.FieldAttr.NAME.code());
		ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": variable/@name ");
		this.variable.setName(attrValue);

		attrValue = attrs.getValue(EnumConstants.FieldAttr.DATA_TYPE.code());
		ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": variable/@data-type ");
		this.variable.setDataType(Constant.getDataTypeByText(attrValue));

		attrValue = attrs.getValue(EnumConstants.FieldAttr.VALUE.code());
		ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": variable/@value ");
		this.variable.setValue(attrValue);
		this.variableMap.put(this.variable.getName(), this.variable);
	}

	private void checkDataTypeLength(int dataType, int dataTypeLength) {
		if (EnumConstants.DataType.INT.getCode() == dataType || EnumConstants.DataType.NETINT.getCode() == dataType) {
			if (dataTypeLength != 4) {
				throw new CommonException(this.beanFilePath + ": ");
			}
		} else if (EnumConstants.DataType.BYTE.getCode() == dataType) {
			if (dataTypeLength != 1) {
				throw new CommonException(this.beanFilePath + ": ");
			}
		} else if (EnumConstants.DataType.SHORT.getCode() == dataType
				|| EnumConstants.DataType.NETSHORT.getCode() == dataType) {
			if (dataTypeLength != 2) {
				throw new CommonException(this.beanFilePath + ": ");
			}
		} else if (EnumConstants.DataType.DATETIME.getCode() == dataType
				|| EnumConstants.DataType.LONG.getCode() == dataType) {
			if (dataTypeLength != 8) {
				throw new CommonException(this.beanFilePath + ": ");
			}
		} else {
			throw new CommonException(this.beanFilePath + ": ");
		}
	}
}
