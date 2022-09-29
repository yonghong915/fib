package com.fib.autoconfigure.xml.dom4j;

import java.io.InputStream;
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
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.SAXException;

import com.fib.autoconfigure.xml.IXmlService;
import com.fib.commons.exception.CommonException;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;

public class Dom4jService implements IXmlService {
	private static final String DEFAULT_SAX_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
	private static final String DEFAULT_NAMESPACE_NAME = "default";
	private static final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	private static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
	private static final String ATTRIBUTE_LINE = "line";
	private Document document;

	public Dom4jService() {
		this.document = null;
	}

	@Override
	public Document getDocument(InputStream in) {
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			reader.setFeature(DEFAULT_SAX_FEATURE, true);
			doc = reader.read(in);
			document = doc;
		} catch (DocumentException | SAXException e) {
			throw new CommonException(e);
		}
		String namespaceURI = doc.getRootElement().getNamespaceURI();
		if (StrUtil.isEmptyIfStr(namespaceURI)) {
			setXPathNamespaceURIs(namespaceURI);
		}
		return doc;
	}

	@Override
	public Document getDocument(String xmlText) {
		try {
			Document doc = DocumentHelper.parseText(xmlText);
			String namespaceURI = doc.getRootElement().getNamespaceURI();
			if (CharSequenceUtil.isNotBlank(namespaceURI)) {
				setXPathNamespaceURIs(namespaceURI);
			}
			document = doc;
			return doc;
		} catch (DocumentException e) {
			throw new CommonException(e);
		}
	}

	@Override
	public List<Node> selectNodes(Node node, String xpathExpression) {
		return node.selectNodes(xpathExpression);
	}

	@Override
	public String getXPathValue(Node node, String xpathExpression) {
		Node childNode = node.selectSingleNode(xpathExpression);
		if (Objects.isNull(childNode)) {
			return CharSequenceUtil.EMPTY;
		}
		return childNode.getText();
	}

	@Override
	public boolean validate(String schemaURI) {
		Assert.notBlank(schemaURI, () -> new IllegalArgumentException("schemaURI must not be null."));
		Assert.notNull(document, () -> new IllegalArgumentException("document must not be null."));
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

		String xsdFileName = Dom4jService.class.getClassLoader().getResource(schemaURI).toString();

		try {
			SAXParser parser = factory.newSAXParser();

			parser.setProperty(SCHEMA_LANGUAGE, XML_SCHEMA);
			parser.setProperty(SCHEMA_SOURCE, xsdFileName);

			// 创建一个SAXValidator校验工具，并设置校验工具的属性
			SAXValidator validator = new SAXValidator(parser.getXMLReader());
			// 设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。
			validator.setErrorHandler(errorHandler);
			// 校验
			validator.validate(document);

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
				return true;
			}
		} catch (ParserConfigurationException | SAXException e) {
			return false;
		}
		return StrUtil.isEmptyIfStr(errorMsg.toString());
	}

	private void setXPathNamespaceURIs(String namespaceURI) {
		Map<String, String> namespaces = new TreeMap<>();
		namespaces.put(DEFAULT_NAMESPACE_NAME, namespaceURI);
		DocumentFactory.getInstance().setXPathNamespaceURIs(namespaces);
	}
}