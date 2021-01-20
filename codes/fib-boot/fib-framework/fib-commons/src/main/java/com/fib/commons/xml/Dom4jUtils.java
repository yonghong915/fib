package com.fib.commons.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import com.fib.commons.exception.CommonException;

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
	private static final String DEFAULT_NAMESPACE = "http://www.fib.com/schema";
	private static final String DEFAULT_SAX_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";

	private Dom4jUtils() {
	}

	public static Document getDocument(String file) {
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			reader.setFeature(DEFAULT_SAX_FEATURE, true);
			doc = reader.read(file);
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
		reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		try {
			return reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException | DocumentException e) {
			throw new CommonException("Failed to read xml.", e);
		}
	}

	public static String getXPathValue(Document doc, String xpath) {
		Node node = doc.selectSingleNode(xpath);
		if (Objects.isNull(node)) {
			return StrUtil.EMPTY;
		}
		return node.getText();
	}

	public static String getXPathValue(Node node, String xpath) {
		Node childNode = node.selectSingleNode(xpath);
		if (Objects.isNull(childNode)) {
			return StrUtil.EMPTY;
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
}
