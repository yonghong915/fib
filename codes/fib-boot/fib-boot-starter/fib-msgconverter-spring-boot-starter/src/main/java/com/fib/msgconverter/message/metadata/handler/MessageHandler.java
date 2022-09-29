package com.fib.msgconverter.message.metadata.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.exception.CommonException;
import com.fib.commons.xml.Dom4jUtils;
import com.fib.msgconverter.message.metadata.Field;
import com.fib.msgconverter.message.metadata.Message;
import com.fib.msgconverter.message.metadata.MessageMetadataManager;
import com.fib.msgconverter.util.constant.EnumConstant.DataType;
import com.fib.msgconverter.util.constant.EnumConstant.FieldType;
import com.fib.msgconverter.util.constant.EnumConstant.MessageType;
import com.fib.msgconverter.util.constant.EnumConstant.PaddingDirection;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 解析message-bean
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-07-08 16:00:04
 */
public class MessageHandler implements ElementHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);
	private List<Field> tmpFieldLst = new ArrayList<>();
	private Message message;
	private String groupId;

	public MessageHandler() {
		this.message = new Message();
	}

	public Message parse(File file) {
		LOGGER.info("parse filePath={}", file.getAbsolutePath());
		try (InputStream in = new FileInputStream(file)) {
			return parse(in);
		} catch (IOException e) {
			throw new CommonException(e);
		}
	}

	private Message parse(InputStream in) {
		Dom4jUtils.parseXmlBySax(in, this);
		return this.message;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public void onStart(ElementPath elementPath) {
		Element row = elementPath.getCurrent();
		String name = row.getName();
		if ("message-bean".equals(name)) {
			buildMessageBean(row);
		} else if ("field".equals(name)) {
			buildField(row);
		}
	}

	private void buildField(Element row) {
		String attrVal = "";
		Field tmpField = null;
		if (CollUtil.isNotEmpty(tmpFieldLst)) {
			tmpField = tmpFieldLst.remove(tmpFieldLst.size() - 1);
		}

		if (null != tmpField && tmpField.getFieldType() != FieldType.TABLE.getCode() && tmpField.getFieldType() != FieldType.COMBINE_FIELD.getCode()
				&& tmpField.getFieldType() != FieldType.VAR_COMBINE_FIELD.getCode() && tmpField.getFieldType() != FieldType.VAR_TABLE.getCode()) {
			throw new CommonException("E900002", "fieldType.illegal");
		}

		Field field = new Field();

		/* field/@name */
		attrVal = row.attributeValue("name");
		Assert.isFalse(StrUtil.isBlankIfStr(attrVal), () -> new CommonException("E9999", "field/@name must not be null."));
		Assert.isFalse(MessageMetadataManager.keyWordSet().contains(attrVal),
				() -> new CommonException("E9999", "field/@name  must not be Reserved."));

		if (null != tmpField) {
			if (tmpField.getSubFields().containsKey(attrVal)) {
				throw new CommonException("subField.reduplicate");
			}
		} else if (this.message.getFields().containsKey(attrVal)) {
			throw new CommonException("field.reduplicate");
		}
		field.setName(attrVal);

		/* field/@name */
		attrVal = row.attributeValue("short-text");
		field.setShortText(attrVal);

		/* field/@field-type */
		attrVal = row.attributeValue("field-type");
		Assert.isFalse(StrUtil.isBlankIfStr(attrVal), () -> new CommonException("E9999", "field/@field-type must not be null."));
		field.setFieldType(FieldType.getFieldTypeByName(attrVal).getCode());

		/* field/@padding */
		attrVal = row.attributeValue("padding");
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			byte[] bytes = attrVal.getBytes();
			if (1 == bytes.length) {
				field.setPadding(bytes[0]);
			} else if (4 == bytes.length && attrVal.startsWith("0x")) {
				// TODO CodeUtil.HextoByte(s11.substring(2));
				attrVal = attrVal.substring(2);
				field.setPadding(attrVal.getBytes()[0]);
			} else {
				throw new CommonException("E900001", "field/@padding format.wrong");
			}
		}

		/* field/@padding-direction */
		attrVal = row.attributeValue("padding-direction");
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			field.setPaddingDirection(PaddingDirection.getDataTypeByName(attrVal).getCode());
		}

		/* field/@required */
		attrVal = row.attributeValue("required");
		if (CharSequenceUtil.isBlank(attrVal)) {
			field.setRequired(true);
		} else {
			field.setRequired("true".equalsIgnoreCase(attrVal));
		}

		/* field/@editable */
		attrVal = row.attributeValue("editable");
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			field.setEditable(!"false".equalsIgnoreCase(attrVal));
		}

		/* field/@isRemoveUnwatchable */
		attrVal = row.attributeValue("isRemoveUnwatchable");
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			field.setEditable(!"false".equalsIgnoreCase(attrVal));
		}

		if (field.isEditable()) {
			field.setXpath(row.attributeValue("xpath"));
		}

		/* field/@prefix */
		attrVal = row.attributeValue("prefix");
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			if (attrVal.length() > 2 && attrVal.startsWith("0x")) {
				String hexStr = attrVal.substring(2);
				Assert.isFalse(!HexUtil.isHexNumber(hexStr), () -> new CommonException("E9999", "field/@prefix notHexString."));
				field.setPrefixString(hexStr);
				// TODO CodeUtil.HextoByte(s9)
				field.setPrefix(hexStr.getBytes());
			} else {
				// TODO CodeUtil.BCDtoASC(s4.getBytes())
				field.setPrefixString(attrVal);
				field.setPrefix(attrVal.getBytes());
			}
			attrVal = row.attributeValue("fir-row-prefix");
			if (CharSequenceUtil.isNotBlank(attrVal) && "true".equals(attrVal)) {
				field.setFirRowPrefix(true);
			}
		}

		/* field/@prefix */
		attrVal = row.attributeValue("suffix");
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			if (attrVal.length() > 2 && attrVal.startsWith("0x")) {
				String hexStr = attrVal.substring(2);
				Assert.isFalse(!HexUtil.isHexNumber(attrVal), () -> new CommonException("E9999", "field/@suffix notHexString."));
				field.setSuffixString(hexStr);
				// TODO CodeUtil.HextoByte(s9)
				field.setSuffix(hexStr.getBytes());
			} else {
				// TODO CodeUtil.BCDtoASC(s4.getBytes())
				field.setSuffixString(attrVal);
				field.setSuffix(attrVal.getBytes());
			}

			attrVal = row.attributeValue("last-row-suffix");
			if (CharSequenceUtil.isNotBlank(attrVal) && "true".equals(attrVal)) {
				field.setFirRowPrefix(true);
			}
		}

		/* field/@data-charset */
		attrVal = row.attributeValue("data-charset");
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			field.setDataCharset(attrVal);
		}

		/* field/@shield */
		attrVal = row.attributeValue("shield");
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			field.setShield("true".equals(attrVal));
		}

		/* field/@max-length */
		attrVal = row.attributeValue("max-length");
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			field.setMaxLength(Integer.parseInt(attrVal));
		}

		/* field/@min-length */
		attrVal = row.attributeValue("min-length");
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			field.setMinLength(Integer.parseInt(attrVal));
		}
		if (2002 == field.getFieldType() || 2003 == field.getFieldType() || 2004 == field.getFieldType() || 2011 == field.getFieldType()
				|| 2008 == field.getFieldType() || 2009 == field.getFieldType()) {
			attrVal = row.attributeValue("reference-type");
			if (CharSequenceUtil.isNotBlank(attrVal)) {
				field.setReferenceType(attrVal);
			}

			attrVal = row.attributeValue("reference");
			if (null == attrVal && (2008 == field.getFieldType() || 2009 == field.getFieldType())) {
				throw new CommonException("reference.null");
			}
			// Assert.isFalse(StrUtil.isBlankIfStr(attrVal), () -> new
			// CommonException("E9999", "field/@reference must not be null."));
			if (CharSequenceUtil.isNotBlank(attrVal)) {
				if (!"dynamic".equalsIgnoreCase(field.getReferenceType()) && !"expression".equalsIgnoreCase(field.getReferenceType())) {
					Message message3 = MessageMetadataManager.getMessage(groupId, attrVal);
					field.setReference(message3);
				}
				field.setReferenceId(attrVal);
			}

		} else {
			/* field/@name */
			attrVal = row.attributeValue("data-type");
			Assert.isFalse(StrUtil.isBlankIfStr(attrVal), () -> new CommonException("E9999", "field/@data-type must not be null."));
			field.setDataType(DataType.getDataTypeByName(attrVal).getCode());
		}

		if (null != tmpField) {
			tmpField.setSubField(field.getName(), field);
			tmpFieldLst.add(tmpField);
		}

		this.message.setField(field.getName(), field);
		tmpFieldLst.add(field);

	}

	private void buildMessageBean(Element row) {
		this.message.setGroupId(this.groupId);
		String attrVal = "";
		/* message-bean/@id */
		attrVal = row.attributeValue("id");
		Assert.isFalse(StrUtil.isBlankIfStr(attrVal), () -> new CommonException("E9999", "message-bean/@id must not be null."));
		this.message.setId(attrVal);

		/* message-bean/@type */
		attrVal = row.attributeValue("type");
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			this.message.setType(MessageType.getMessageTypeByName(attrVal));
		}

		/* message-bean/@short-text */
		attrVal = row.attributeValue("short-text");
		this.message.setShortText(attrVal);

		/* message-bean/@message-charset */
		attrVal = row.attributeValue("message-charset");
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			this.message.setMsgCharset(attrVal);
		}

		/* message-bean/@class */
		attrVal = row.attributeValue("class");
		Assert.isFalse(StrUtil.isBlankIfStr(attrVal), () -> new CommonException("E9999", "message-bean/@class must not be null."));
		if (MessageMetadataManager.cacheClass().containsKey(attrVal)) {
			Message cacheMessage = MessageMetadataManager.cacheClass().get(attrVal);
			if (this.groupId.equals(cacheMessage.getGroupId())) {
				throw new CommonException("E90001", "messageClass.reduplicate");
			}
		}
		this.message.setClassName(attrVal);

		/* message-bean/@xpath */
		attrVal = row.attributeValue("xpath");
		this.message.setXpath(attrVal);

		if (this.message.getType() == MessageType.TAG || this.message.getType() == MessageType.SWIFT) {// tag/swift
			/* message-bean/@prefix */
			attrVal = row.attributeValue("prefix");
			Assert.isFalse(StrUtil.isBlankIfStr(attrVal), () -> new CommonException("E9999", "Tag/Swift message-bean/@prefix must not be null."));
			if (attrVal.length() > 2 && attrVal.startsWith("0x")) {
				String hexStr = attrVal.substring(2);
				Assert.isFalse(!HexUtil.isHexNumber(hexStr), () -> new CommonException("E9999", "Tag/Swift message-bean/@prefix notHexString."));
				this.message.setPrefixString(hexStr);
				// TODO CodeUtil.HextoByte(s9)
				this.message.setPrefix(hexStr.getBytes());
			} else {
				// TODO CodeUtil.BCDtoASC(s4.getBytes())
				this.message.setPrefixString(attrVal);
				this.message.setPrefix(attrVal.getBytes());
			}

			/* message-bean/@prefix */
			attrVal = row.attributeValue("suffix");
			Assert.isFalse(StrUtil.isBlankIfStr(attrVal), () -> new CommonException("E9999", "Tag/Swift message-bean/@suffix must not be null."));
			if (attrVal.length() > 2 && attrVal.startsWith("0x")) {
				String hexStr = attrVal.substring(2);
				Assert.isFalse(!HexUtil.isHexNumber(hexStr), () -> new CommonException("E9999", "Tag/Swift message-bean/@suffix notHexString."));
				this.message.setSuffixString(hexStr);
				// TODO CodeUtil.HextoByte(s9)
				this.message.setSuffix(hexStr.getBytes());
			} else {
				// TODO CodeUtil.BCDtoASC(s4.getBytes())
				this.message.setSuffixString(attrVal);
				this.message.setSuffix(attrVal.getBytes());
			}
		}
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		Field tmpField = null;
		if (CollUtil.isNotEmpty(tmpFieldLst)) {
			tmpField = tmpFieldLst.remove(tmpFieldLst.size() - 1);
		}

		Element row = elementPath.getCurrent();
		String name = row.getName();
		String attrVal = "";
		if ("message-bean".equals(name)) {
			attrVal = row.attributeValue("id");

			attrVal = row.attributeValue("short-text");

			attrVal = row.attributeValue("message-charset");

			attrVal = row.attributeValue("class");

		} else if ("field".equals(name)) {
			attrVal = row.attributeValue("name");

			attrVal = row.attributeValue("short-text");

			attrVal = row.attributeValue("field-type");

			attrVal = row.attributeValue("data-type");

		}
	}

}
