
package com.fib.upp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

import com.fib.upp.parse.Field;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;

public class TestParseMsg {
	private static final Logger logger = LoggerFactory.getLogger(TestParseMsg.class);
	private static final String DEFAULT_SAX_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
	private static final String DEFAULT_NAMESPACE_NAME = "default";
	private static final String EXPRESSION_WITH_NS = "/" + DEFAULT_NAMESPACE_NAME + ":";

	public static void main(String[] args) {
		parse();
	}

	public static void parse() {
		String msgContent = FileUtil.readUtf8String("data/cnaps/hvps.111.001.01.dat");
		logger.info("msgContent={}", msgContent);

		int idx = parseMsgHeader(msgContent);

		logger.info("digitalSignature,startIdx={}", idx);
		String digitalSignature = msgContent.substring(idx, idx + 100);
		logger.info("digitalSignature={}", digitalSignature);

		idx = idx + 100;

		logger.info("messageBeanOut,startIdx={}", idx);
		logger.info("messageBeanOut={}", msgContent.substring(idx));
		parseMessageBeanOut(msgContent.substring(idx));

	}

	private static void setXPathNamespaceURIs(String namespace) {
		Map<String, String> namespaces = new TreeMap<>();
		namespaces.put(DEFAULT_NAMESPACE_NAME, namespace);
		DocumentFactory.getInstance().setXPathNamespaceURIs(namespaces);
	}

	private static void parseMessageBeanOut(String msg) {

		InputStream is = new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8));
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			reader.setFeature(DEFAULT_SAX_FEATURE, true);
			doc = reader.read(is);
		} catch (DocumentException | SAXException e) {
			e.printStackTrace();
		}
		String namespaceURI = doc.getRootElement().getNamespaceURI();
		setXPathNamespaceURIs(namespaceURI);

		List<Field> fieldList = new ArrayList<>();
		Field field = new Field();
		field.setName("messageIdentification");
		field.setLength(35);
		field.setXpath("default:Document/default:FIToFICstmrCdtTrf/default:GrpHdr/default:MsgId/text()");
		fieldList.add(field);

		field = new Field();
		field.setName("creationDateTime");
		field.setLength(35);
		field.setXpath("default:Document/default:FIToFICstmrCdtTrf/default:GrpHdr/default:CreDtTm/text()");
		fieldList.add(field);

		field = new Field();
		field.setName("numberOfTransactions");
		field.setLength(35);
		field.setXpath("default:Document/default:FIToFICstmrCdtTrf/default:GrpHdr/default:NbOfTxs/text()");
		fieldList.add(field);

		field = new Field();
		field.setName("currencyType");
		field.setLength(35);
		field.setXpath("default:Document/default:FIToFICstmrCdtTrf/default:CdtTrfTxInf/default:IntrBkSttlmAmt/@Ccy");
		fieldList.add(field);

		field = new Field();
		field.setName("interbankSettlementAmount");
		field.setLength(35);
		field.setXpath("default:Document/default:FIToFICstmrCdtTrf/default:CdtTrfTxInf/default:IntrBkSttlmAmt/text()");
		fieldList.add(field);

		String className = "com.fib.upp.parse.Hvps111Out";
		Class<Object> clazz = ClassUtil.loadClass(className);
		Object obj = null;
		try {
			obj = clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}

		String fieldName = "";
		String value = "";
		for (int i = 0; i < fieldList.size(); i++) {
			Field fi = fieldList.get(i);
			fieldName = fi.getName();
			String dateType = fi.getDataType();
			String xpath = fi.getXpath();
			if ("datetime".equals(dateType)) {

			}
			int idx = xpath.indexOf("text()");

			String expression = xpath;
			if (idx != -1) {
				expression = xpath.substring(0, xpath.indexOf("text()") - 1);
			}

			Node node = doc.selectSingleNode(expression);
			if (node instanceof Element ele) {
				value = ele.getText();
				logger.info("value={}", value);
			} else if (node instanceof Attribute attr) {
				value = attr.getValue();
			}

			logger.info("name={},value={}", fieldName, value);
			final Method method = ClassUtil.getDeclaredMethod(clazz, "set" + StrUtil.upperFirst(fieldName), ClassUtil.getClasses(value));

			try {
				method.invoke(obj, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		String expression = "default:Document/default:FIToFICstmrCdtTrf/default:CdtTrfTxInf/default:RmtInf";
		Node node = doc.selectSingleNode(expression);
		List<Node> nodes = node.selectNodes("default:Ustrd");
		for (int i = 0; i < nodes.size(); i++) {
			Node subNode = nodes.get(i);
			if (subNode instanceof Element ele) {
				value = ele.getText();
				logger.info("value={}", value);
			} else if (subNode instanceof Attribute attr) {
				value = attr.getValue();
			}
		}
		logger.info("Hvps111Out={}", obj);

	}

	private static int parseMsgHeader(String msgContent) {
		int len = 0;
		String fieldName = "";
		int startIdx = 0;
		String value = "";
		// beginFlag
		List<Field> fieldList = new ArrayList<>();
		Field field = new Field();
		field.setName("beginFlag");
		field.setLength(3);
		fieldList.add(field);

		// version
		field = new Field();
		field.setName("version");
		field.setLength(2);
		fieldList.add(field);

		// origSender
		field = new Field();
		field.setName("origSender");
		field.setLength(14);
		fieldList.add(field);

		// origSenderSid
		field = new Field();
		field.setName("origSenderSid");
		field.setLength(4);
		fieldList.add(field);

		// origReceiver
		field = new Field();
		field.setName("origReceiver");
		field.setLength(14);
		fieldList.add(field);

		// origReceiverSid
		field = new Field();
		field.setName("origReceiverSid");
		field.setLength(4);
		fieldList.add(field);

		// origSendDate
		field = new Field();
		field.setName("origSendDate");
		field.setLength(8);
		fieldList.add(field);

		// origSendTime
		field = new Field();
		field.setName("origSendTime");
		field.setLength(6);
		fieldList.add(field);

		// structType
		field = new Field();
		field.setName("structType");
		field.setLength(3);
		fieldList.add(field);

		// mesgType
		field = new Field();
		field.setName("mesgType");
		field.setLength(20);
		fieldList.add(field);

		// mesgID
		field = new Field();
		field.setName("mesgID");
		field.setLength(20);
		fieldList.add(field);

		// mesgRefID
		field = new Field();
		field.setName("mesgRefID");
		field.setLength(20);
		fieldList.add(field);

		// mesgPriority
		field = new Field();
		field.setName("mesgPriority");
		field.setLength(1);
		fieldList.add(field);

		// mesgDirection
		field = new Field();
		field.setName("mesgDirection");
		field.setLength(1);
		fieldList.add(field);

		// reserve
		field = new Field();
		field.setName("reserve");
		field.setLength(9);
		fieldList.add(field);

		// endFlag
		field = new Field();
		field.setName("endFlag");
		field.setLength(3);
		fieldList.add(field);

		String className = "com.fib.upp.parse.MsgHeader";
		Class<Object> clazz = ClassUtil.loadClass(className);
		Object obj = null;
		try {
			obj = clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < fieldList.size(); i++) {
			Field fi = fieldList.get(i);
			len = fi.getLength();
			fieldName = fi.getName();
			String dateType = fi.getDataType();
			value = msgContent.substring(startIdx, len + startIdx);

			if ("datetime".equals(dateType)) {

			}

			logger.info("name={},value={}", fieldName, value);
			final Method method = ClassUtil.getDeclaredMethod(clazz, "set" + StrUtil.upperFirst(fieldName), ClassUtil.getClasses(value));

			try {
				method.invoke(obj, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			startIdx += len;
		}
		logger.info("msgHeader={}", obj);
		return startIdx;
	}
}