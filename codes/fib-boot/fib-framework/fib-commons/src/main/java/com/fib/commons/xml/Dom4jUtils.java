package com.fib.commons.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.util.XMLErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.fib.commons.exception.CommonException;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-06
 */
public class Dom4jUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(Dom4jUtils.class);
	private static final String DEFAULT_NAMESPACE = "http://www.fib.com/schema";
	private static final String DEFAULT_SAX_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
	private static final String DEFAULT_NAMESPACE_NAME = "default";
	private static final String EXPRESSION_WITH_NS = "/" + DEFAULT_NAMESPACE_NAME + ":";

	private Dom4jUtils() {
	}

	public static Document createDocument(String fileName) {
		if (StrUtil.isEmptyIfStr(fileName)) {
			throw new CommonException("fileName must be not empty.");
		}
		InputStream is = FileUtil.getInputStream(fileName);
		return createDocument(is);
	}

	public static Document createDocument(InputStream is) {
		Document doc = null;
		SAXReader xmlReader = new SAXReader();
		try {
			xmlReader.setFeature(DEFAULT_SAX_FEATURE, true);
			doc = xmlReader.read(is);
		} catch (SAXException | DocumentException e1) {
			throw new CommonException("Failed to create xml document.");
		}
		Element rootElement = doc.getRootElement();
		String namespace = rootElement.getNamespaceURI();

		if (namespace != null) {
			Map<String, String> namespaces = new TreeMap<>();
			namespaces.put(DEFAULT_NAMESPACE_NAME, namespace);
			DocumentFactory.getInstance().setXPathNamespaceURIs(namespaces);
		}
		return doc;
	}

	public static List<Node> selectNodes(Document doc, String xpathExpression) {
		Element rootElement = doc.getRootElement();
		String namespace = rootElement.getNamespaceURI();
		if (namespace != null) {
			xpathExpression = xpathExpression.replace("/", EXPRESSION_WITH_NS);
		}
		return doc.selectNodes(xpathExpression);
	}

	public static Node selectSingleNode(Document doc, String xpathExpression) {
		Element rootElement = doc.getRootElement();
		String namespace = rootElement.getNamespaceURI();
		if (namespace != null) {
			xpathExpression = xpathExpression.replace("/", EXPRESSION_WITH_NS);
		}
		return doc.selectSingleNode(xpathExpression);
	}

	private static void setXPathNamespaceURIs(String namespace) {
		Map<String, String> namespaces = new TreeMap<>();
		namespaces.put(DEFAULT_NAMESPACE_NAME, namespace);
		DocumentFactory.getInstance().setXPathNamespaceURIs(namespaces);
	}

	public static Node getSingleNode(String xpathExpression, Element rootEle) {
		String namespace = rootEle.getNamespaceURI();
		if (namespace != null) {
			setXPathNamespaceURIs(namespace);
			xpathExpression = xpathExpression.replace("/", EXPRESSION_WITH_NS);
		}
		XPath xpath = rootEle.createXPath(xpathExpression);
		return xpath.selectSingleNode(rootEle);
	}

	public static String getSingleNodeText(String xpathExpression, Element rootEle) {
		Element element = (Element) getSingleNode(xpathExpression, rootEle);
		String returnstr = "";
		if (element != null) {
			returnstr = element.getText();
		}
		return returnstr;
	}

	public static Document getDocument(String filePath) {
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			reader.setFeature(DEFAULT_SAX_FEATURE, true);
			doc = reader.read(filePath);
		} catch (DocumentException | SAXException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static Document getDocument(InputStream is) {
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			reader.setFeature(DEFAULT_SAX_FEATURE, true);
			doc = reader.read(is);
		} catch (DocumentException | SAXException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static Document getXPathDocument(InputStream is, String... namespaces)
			throws DocumentException, SAXException {
		Map<String, String> map = new HashMap<>();
		map.put("ns", (ArrayUtil.isEmpty(namespaces)) ? DEFAULT_NAMESPACE : namespaces[0]);
		DocumentFactory factory = new DocumentFactory();
		factory.setXPathNamespaceURIs(map);
		SAXReader reader = new SAXReader(factory);
		reader.setFeature(DEFAULT_SAX_FEATURE, true);
		return reader.read(is);
	}

	public static Document getXPathDocument(String xml, String... namespaces) throws DocumentException, SAXException {
		Map<String, String> map = new HashMap<>();
		map.put("ns", (ArrayUtil.isEmpty(namespaces)) ? DEFAULT_NAMESPACE : namespaces[0]);
		DocumentFactory factory = new DocumentFactory();
		factory.setXPathNamespaceURIs(map);
		SAXReader reader = new SAXReader(factory);
		reader.setFeature(DEFAULT_SAX_FEATURE, true);
		try {
			return reader.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
		} catch (DocumentException e) {
			throw new CommonException("Failed to read xml.", e);
		}
	}

	public static XPath getXPath(Element rootEle, String xpath) {
		Map<String, String> nameSpaceMap = new HashMap<>();
		nameSpaceMap.put(DEFAULT_NAMESPACE_NAME, rootEle.getNamespaceURI());
		xpath = xpath.replace(File.separator, File.separator + DEFAULT_NAMESPACE_NAME + ":");
		XPath xItemName = rootEle.createXPath(xpath);
		xItemName.setNamespaceURIs(nameSpaceMap);
		return xItemName;
	}

	public static XPath getXPath(String xpathExpression, String... namespaces) {
		Map<String, String> nameSpaceMap = new HashMap<>();

		String namespaceKey = "";
		String namespace = "";
		if (ArrayUtil.isEmpty(namespaces)) {
			namespaceKey = DEFAULT_NAMESPACE_NAME;
			namespace = DEFAULT_NAMESPACE;
		} else if (namespaces.length == 1) {
			namespaceKey = DEFAULT_NAMESPACE_NAME;
			namespace = namespaces[0];
		} else {
			namespace = namespaces[0];
			namespaceKey = namespaces[1];
		}
		nameSpaceMap.put(namespaceKey, namespace);
		DocumentFactory.getInstance().setXPathNamespaceURIs(nameSpaceMap);

		xpathExpression = xpathExpression.replace(File.separator, File.separator + DEFAULT_NAMESPACE_NAME + ":");
		return DocumentFactory.getInstance().createXPath(xpathExpression);
	}

	public static String getXPathValue(Document doc, String xpath) {
		Node node = doc.selectSingleNode(xpath);
		if (Objects.isNull(node)) {
			return CharSequenceUtil.EMPTY;
		}
		return node.getText();
	}

	public static String getXPathValue(Node node, String xpath) {
		Node childNode = node.selectSingleNode(xpath);
		if (Objects.isNull(childNode)) {
			return CharSequenceUtil.EMPTY;
		}
		return childNode.getText();
	}

	public static Node getNode(Document doc, String xpath) {
		return doc.selectSingleNode(xpath);
	}

	public static Node getNode(Node node, String xpath) {
		return node.selectSingleNode(xpath);
	}

	public static List<Node> getNodes(Document doc, String xpath) {
		return doc.selectNodes(xpath);
	}

	public static List<Node> getNodes(Node node, String xpath) {
		return node.selectNodes(xpath);
	}

	public static Object getObject(Document doc, String xpath) {
		return doc.selectObject(xpath);
	}

	public static Object getObject(Node node, String xpath) {
		return node.selectObject(xpath);
	}

	public static int numberValueOf(Document doc, String xpath) {
		Number number = doc.numberValueOf(xpath);
		try {
			return number.intValue();
		} catch (Exception e) {
			return 0;
		}
	}

	public static int numberValueOf(Node node, String xpath) {
		Number number = node.numberValueOf(xpath);
		try {
			return number.intValue();
		} catch (Exception e) {
			return 0;
		}
	}

	static final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

	static final String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	static final String ATTRIBUTE_LINE = "line";

	public static boolean check(Document xmlDocument, String xsdFileName) {
		StringBuilder errorMsg = new StringBuilder();

		// 创建默认的XML错误处理器
		XMLErrorHandler errorHandler = new XMLErrorHandler();
		// 获取基于 SAX 的解析器的实例
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// 解析器在解析时验证 XML 内容。
		factory.setValidating(true);
		// 指定由此代码生成的解析器将提供对 XML 名称空间的支持。
		factory.setNamespaceAware(true);
		// 使用当前配置的工厂参数创建 SAXParser 的一个新实例。
		try {
			SAXParser parser = factory.newSAXParser();

			parser.setProperty(SCHEMA_LANGUAGE, XML_SCHEMA);
			parser.setProperty(SCHEMA_SOURCE, xsdFileName);

			// 创建一个SAXValidator校验工具，并设置校验工具的属性
			SAXValidator validator = new SAXValidator(parser.getXMLReader());
			// 设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。
			validator.setErrorHandler(errorHandler);
			// 校验
			validator.validate(xmlDocument);

			// 如果错误信息不为空，说明校验失败，打印错误信息
			if (errorHandler.getErrors().hasContent()) {
				List<Element> elements = errorHandler.getErrors().elements();
				for (Element element : elements) {
					String line = String.valueOf(Integer.parseInt(element.attributeValue(ATTRIBUTE_LINE)) - 1);
					String text = element.getText();
					errorMsg.append("(第 " + line + "行出现错误) " + text);
				}
				errorMsg.append("XML文件通过XSD文件校验失败！");
			} else {
				LOGGER.error("Good! XML文件通过XSD文件校验成功！");
				return true;
			}
		} catch (ParserConfigurationException | SAXException e) {
			LOGGER.error("XML文件通过XSD文件校验失败", e);
			return false;
		}
		return StrUtil.isEmptyIfStr(errorMsg.toString());
	}
}
