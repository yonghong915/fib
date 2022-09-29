package com.fib.upp.parse;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import cn.hutool.core.io.FileUtil;

public class MessageBeanParser {
	private static final Logger logger = LoggerFactory.getLogger(MessageBeanParser.class);
	private static final String DEFAULT_SAX_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
	private static final String DEFAULT_NAMESPACE_NAME = "default";

	public static void main(String[] args) {
		parse();
	}

	public static void parse() {
		File file = FileUtil.file("deploy/cnaps/hvps_111_bean.xml");
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			reader.setFeature(DEFAULT_SAX_FEATURE, true);
			doc = reader.read(FileUtil.getInputStream(file));
		} catch (DocumentException | SAXException e) {
			e.printStackTrace();
		}
		String namespaceURI = doc.getRootElement().getNamespaceURI();
		setXPathNamespaceURIs(namespaceURI);

		Element rootEle = doc.getRootElement();

		// id
		String attrVal = rootEle.attributeValue("id");
		Message message = new Message();
		message.setId(attrVal);

		// short-text
		attrVal = rootEle.attributeValue("short-text");
		message.setShortText(attrVal);

		// class
		attrVal = rootEle.attributeValue("class");
		message.setClassName(attrVal);

		// message-charset
		attrVal = rootEle.attributeValue("message-charset");
		message.setMsgCharset(attrVal);

		Field field = null;
		String value = "";
		String text = "";
		List<Node> nodeList = doc.selectNodes("default:message-bean/default:field");
		for (int i = 0; i < nodeList.size(); i++) {
			field = new Field();
			Node node = nodeList.get(i);
			if (node instanceof Element ele) {
				// name
				attrVal = ele.attributeValue("name");
				field.setName(attrVal);

				// short-text
				attrVal = ele.attributeValue("short-text");
				field.setShortText(attrVal);

				// field-type
				attrVal = ele.attributeValue("field-type");
				field.setFieldType(attrVal);

				// reference
				attrVal = ele.attributeValue("reference");
				field.setReference(attrVal);

			} else if (node instanceof Attribute attr) {
				value = attr.getValue();
				text = attr.getText();
			}
			message.setField(field.getName(), field);
		}

		//
		logger.info("message={}", message);

	}

	private static void setXPathNamespaceURIs(String namespace) {
		Map<String, String> namespaces = new TreeMap<>();
		namespaces.put(DEFAULT_NAMESPACE_NAME, namespace);
		DocumentFactory.getInstance().setXPathNamespaceURIs(namespaces);
	}
}
