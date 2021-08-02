package com.fib.upp.service.gateway.message.metadata;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;

import com.fib.commons.xml.Dom4jUtils;
import com.fib.upp.service.gateway.constant.EnumConstants;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

public class MessageHandler {
	private String beanFilePath;
	private String channelId;
	private MessageMetaData message;
	private List<Field> fields;

	MessageHandler() {
		this.message = new MessageMetaData();
	}

	public MessageMetaData parseFile(String channelId, File beanFile) {
		this.beanFilePath = beanFile.getAbsolutePath();
		InputStream is = FileUtil.getInputStream(beanFile);
		return this.parseFile(channelId, is);
	}

	public MessageMetaData parseFile(String channelId, InputStream is) {
		this.channelId = channelId;

		Document doc = Dom4jUtils.getDocument(is);
		Element rootEle = doc.getRootElement();

		buildMessageBean(rootEle);

		XPath xpath = Dom4jUtils.getXPath(rootEle, "/message-bean/field");
		List<Node> nodes = xpath.selectNodes(rootEle);
		for (Node node : nodes) {
			Element element = (Element) node;
			buildField(element);
			System.out.println(
					"returnstr=" + element.attributeValue("name") + "  " + element.attributeValue("short-text"));
		}
		this.check();
		this.checkFields(this.message.getFields());

		return message;
	}

	private void checkFields(Map<String, Field> fields) {
		// TODO Auto-generated method stub

	}

	private void check() {
		// TODO Auto-generated method stub

	}

	public void startElement(String eleName, Element ele) {
		if (EnumConstants.MESSAGE_BEAN_NAME.equals(eleName)) {
			buildMessageBean(ele);
		} else if (EnumConstants.FIELD_BEAN_NAME.equals(eleName)) {
			buildField(ele);
		}
	}

	private void buildMessageBean(Element ele) {
		String attrValue = ele.attributeValue(EnumConstants.MessageAttr.ID.code());
		Assert.notBlank(attrValue, "message-bean/id must not be blank");
		message.setId(attrValue);

		attrValue = ele.attributeValue(EnumConstants.MessageAttr.TYPE.code());
		if (StrUtil.isNotBlank(attrValue)) {
			this.message.setType(EnumConstants.MsgType.getMessageTypeCode(attrValue));
		}

		attrValue = ele.attributeValue(EnumConstants.MessageAttr.SHORT_TEXT.code());
		message.setShortText(attrValue);

		attrValue = ele.attributeValue(EnumConstants.MessageAttr.CLASS.code());
		Assert.notBlank(attrValue, "message-bean/class must not be blank");
		message.setClassName(attrValue);

		attrValue = ele.attributeValue(EnumConstants.MessageAttr.XPATH.code());
		message.setXpath(attrValue);
	}

	private void buildField(Element ele) {
		Field field = new Field();
		String attrValue = ele.attributeValue(EnumConstants.FieldAttr.NAME.code());
		Assert.notBlank(attrValue, "field/name must not be blank");
		field.setName(attrValue);

		attrValue = ele.attributeValue(EnumConstants.FieldAttr.FIELD_TYPE.code());
		field.setFieldType(EnumConstants.FieldType.getFieldTypeCode(attrValue));

		attrValue = ele.attributeValue(EnumConstants.FieldAttr.REQUIRED.code());
		field.setRequired("true".equalsIgnoreCase(attrValue));

		attrValue = ele.attributeValue(EnumConstants.FieldAttr.XPATH.code());
		field.setXpath(attrValue);

		attrValue = ele.attributeValue(EnumConstants.FieldAttr.SHORT_TEXT.code());
		field.setShortText(attrValue);

		attrValue = ele.attributeValue(EnumConstants.FieldAttr.MAX_LENGTH.code());
		if (StrUtil.isNotEmpty(attrValue)) {
			field.setMaxLength(Integer.parseInt(attrValue));
		}

		attrValue = ele.attributeValue(EnumConstants.FieldAttr.MIN_LENGTH.code());
		if (StrUtil.isNotEmpty(attrValue)) {
			field.setMinLength(Integer.parseInt(attrValue));
		}

		attrValue = ele.attributeValue(EnumConstants.FieldAttr.DATA_TYPE.code());
		field.setDataType(EnumConstants.DataType.getDataTypeCode(attrValue));

		attrValue = ele.attributeValue(EnumConstants.FieldAttr.VALUE.code());
		field.setValue(attrValue);

		attrValue = ele.attributeValue(EnumConstants.FieldAttr.REFERENCE.code());
		field.setReferenceId(attrValue);

		this.message.setField(field.getName(), field);
	}
}
