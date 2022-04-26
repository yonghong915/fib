package com.fib.upp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.xml.Dom4jUtils;
import com.fib.upp.message.MessageParser;
import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.bean.impl.Ccms801Bean;
import com.fib.upp.message.metadata.Field;
import com.fib.upp.message.metadata.Message;
import com.fib.upp.message.parser.AbstractMessageParser;
import com.fib.upp.util.Constant;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

public class MessageParserTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageParserTest.class);

	public Message parseMessageBean(String channelId,File fileName) {
		
	}
	
	@Test
	public void testMessageParse() {
		String fileName = "data/cnaps/ccms.801.001.02.txt";
		String xmlData = FileUtil.readString(fileName, StandardCharsets.UTF_8);
		LOGGER.info("cnaps.801 message content [{}]", xmlData);

		AbstractMessageParser parser = new MessageParser();
		Message message = new Message();

		String ccms801BeanXml = "deploy/cnaps/CCMS_801_bean.xml";
		String ccms801XmlStr = FileUtil.readString(ccms801BeanXml, StandardCharsets.UTF_8);
		Document doc = Dom4jUtils.createDocument(new ByteArrayInputStream(ccms801XmlStr.getBytes(StandardCharsets.UTF_8)));
		Element rootELe = doc.getRootElement();
		String messageCharset = rootELe.attributeValue("message-charset");
		String id = rootELe.attributeValue("id");
		String shortText = rootELe.attributeValue("short-text");
		String className = rootELe.attributeValue("class");
		message.setId(id);
		message.setMsgCharset(messageCharset);
		message.setShortText(shortText);
		message.setClassName(className);

		List<Node> nodes = rootELe.selectNodes("field");
		Field field = null;
		for (Node node : nodes) {
			Element ele = (Element) node;
			field = new Field();
			String name = ele.attributeValue("name");
			String fShortText = ele.attributeValue("short-text");
			String fieldType = ele.attributeValue("field-type");
			String reference = ele.attributeValue("reference");
			String dataType = ele.attributeValue("data-type");
			String length = ele.attributeValue("length");
			String xpath = ele.attributeValue("xpath");
			field.setName(name);
			field.setShortText(fShortText);
			field.setDataType(0);

			if (StrUtil.isNotBlank(reference)) {
				field.setReferenceId(reference);
			}

			if (StrUtil.isNotBlank(length)) {
				field.setLength(Integer.parseInt(length));
			}
			if (StrUtil.isNotBlank(fieldType)) {
				field.setFieldType(Constant.getFieldTypeByText(fieldType));
			}

			field.setXpath(xpath);
			message.setField(name, field);
		}

		parser.setMessage(message);

		byte[] messageData = xmlData.getBytes(StandardCharsets.UTF_8);
		parser.setMessageData(messageData);

		MessageBean mb = new Ccms801Bean();
		parser.setMessageBean(mb);
		MessageBean meb = parser.parse();
		LOGGER.info("meb=[{}]",meb);
	}
}
