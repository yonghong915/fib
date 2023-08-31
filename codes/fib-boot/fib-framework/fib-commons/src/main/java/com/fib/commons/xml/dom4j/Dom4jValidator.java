package com.fib.commons.xml.dom4j;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXValidator;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.SAXException;

import com.fib.commons.xml.XMLValidator;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

/**
 * Dom4j XML 验证器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2023-07-12 10:13:22
 */
public class Dom4jValidator implements XMLValidator {

	private final String schemaURI;
	private final Document document;
	private static final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	private static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
	private static final String ATTRIBUTE_LINE = "line";

	public Dom4jValidator() {
		this(new Builder());
	}

	public Dom4jValidator(Builder builder) {
		this.schemaURI = builder.schemaURI;
		this.document = builder.document;
	}

	@Override
	public boolean validate() {
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
		try {
			SAXParser parser = factory.newSAXParser();

			parser.setProperty(SCHEMA_LANGUAGE, XML_SCHEMA);
			parser.setProperty(SCHEMA_SOURCE, schemaURI);

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

	public Builder newBuilder() {
		return new Builder(this);
	}

	public static final class Builder {
		private String schemaURI;
		private Document document;

		public Builder() {
			//
		}

		Builder(Dom4jValidator validator) {
			this.schemaURI = validator.schemaURI;
			this.document = validator.document;
		}

		public Builder setSchemaURI(String schemaURI) {
			this.schemaURI = schemaURI;
			return this;
		}

		public Builder setDocument(Document document) {
			this.document = document;
			return this;
		}

		public XMLValidator build() {
			return new Dom4jValidator(this);
		}
	}

}
