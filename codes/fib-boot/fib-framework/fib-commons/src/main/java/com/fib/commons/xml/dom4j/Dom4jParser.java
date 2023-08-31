package com.fib.commons.xml.dom4j;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import com.fib.commons.exception.CommonException;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;

public class Dom4jParser {
	private static final String DEFAULT_SAX_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
	private static final String DEFAULT_NAMESPACE_NAME = "default";
	private static final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	private static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
	private static final String ATTRIBUTE_LINE = "line";
	private String namespaceURI;

	public Dom4jParser() {
		this(new Builder());
	}

	public Dom4jParser(Builder builder) {
		this.namespaceURI = builder.namespaceURI;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public void parse(InputStream in) {
	}

	public Document getDocument(InputStream in) {
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			reader.setFeature(DEFAULT_SAX_FEATURE, true);
			doc = reader.read(in);
		} catch (DocumentException | SAXException e) {
			throw new CommonException(e);
		}
		String namespaceURI = doc.getRootElement().getNamespaceURI();
		if (StrUtil.isEmptyIfStr(namespaceURI)) {
			setXPathNamespaceURIs(namespaceURI);
		}
		return doc;
	}

	public Document getDocument(String xmlText) {
		try {
			Document doc = DocumentHelper.parseText(xmlText);
			String namespaceURI = doc.getRootElement().getNamespaceURI();
			if (StrUtil.isEmptyIfStr(namespaceURI)) {
				setXPathNamespaceURIs(namespaceURI);
			}
			return doc;
		} catch (DocumentException e) {
			throw new CommonException(e);
		}
	}

	public List<Node> selectNodes(Node node, String xpathExpression) {
		return node.selectNodes(xpathExpression);
	}

	public String getXPathValue(Node node, String xpathExpression) {
		Node childNode = node.selectSingleNode(xpathExpression);
		if (Objects.isNull(childNode)) {
			return CharSequenceUtil.EMPTY;
		}
		return childNode.getText();
	}

	public static final class Builder {
		private String namespaceURI;

		public Builder() {
			//
		}

		public Builder(Dom4jParser validator) {
			this.namespaceURI = validator.namespaceURI;
		}

		public Builder setNamespaceURI(String namespaceURI) {
			this.namespaceURI = namespaceURI;
			return this;
		}

		public Dom4jParser build() {
			return new Dom4jParser(this);
		}
	}

	private void setXPathNamespaceURIs(String namespaceURI) {
		Map<String, String> namespaces = new TreeMap<>();
		namespaces.put(DEFAULT_NAMESPACE_NAME, namespaceURI);
		DocumentFactory.getInstance().setXPathNamespaceURIs(namespaces);
	}
}
