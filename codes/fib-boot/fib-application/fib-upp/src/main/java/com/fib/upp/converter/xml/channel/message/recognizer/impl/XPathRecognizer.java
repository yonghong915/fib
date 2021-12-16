/**
 * 北京长信通信息技术有限公司
 * 2008-8-29 下午12:04:58
 */
package com.fib.upp.converter.xml.channel.message.recognizer.impl;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fib.upp.converter.xml.channel.message.recognizer.AbstractMessageRecognizer;

/**
 * XPath表达式取ID的识别器。仅用于XML格式的消息。
 * 
 * @author 刘恭亮
 * 
 */
public class XPathRecognizer extends AbstractMessageRecognizer {
	/**
	 * 参数名：XPATH表达式
	 */
	public static final String PARAM_TEXT_XPATH = "xpathExpression";
	/**
	 * 参数名：XML开始位置
	 */
	public static final String PARAM_TEXT_XML_START_AT = "xmlStartAt";
	/**
	 * 参数名：XML数据结束点偏移量
	 */
	public static final String PARAM_TEXT_XML_END_OFFSET = "xmlEndOffset";
	/**
	 * 参数名：XML数据编码类型
	 */
	public static final String PARAM_TEXT_MSG_CHARSET = "xmlMessageCharset";

	private String xpathExpression;
	private int xmlStartAt = -1;
	private int xmlEndOffset = 0;
	private String charset = System.getProperty("file.encoding");

	public String recognize(byte[] message) {
		if (null == message) {
			 throw new IllegalArgumentException("message is null");
//			throw new IllegalArgumentException(MultiLanguageResourceBundle
//					.getInstance().getString("inputParameter.null",
//							new String[] { "message" }));
		}

		// 1. 从报文中截取XML数据
		if (xmlStartAt >= message.length) {
			 throw new RuntimeException("message too short! xmlStartAt["
			 + xmlStartAt + "] >= message.length[" + message.length
			 + "]");
//			throw new RuntimeException(
//					MultiLanguageResourceBundle
//							.getInstance()
//							.getString(
//									"XPathRecognizer.recognize.message.tooShort.1",
//									new String[] { "" + xmlStartAt,
//											"" + message.length }));
		}
		int xmlLength = message.length - xmlStartAt - xmlEndOffset;
		if (xmlLength <= 0) {
			 throw new RuntimeException(
			 "message too short! xmlLength = message.length - xmlStartAt - xmlEndOffset = "
			 + xmlLength);
//			throw new RuntimeException(MultiLanguageResourceBundle
//					.getInstance().getString(
//							"XPathRecognizer.recognize.message.tooShort.2",
//							new String[] { "" + xmlLength }));
		}
		String xml = null;
		try {
			xml = new String(message, xmlStartAt, xmlLength, charset);
		} catch (Exception e) {
			// e.printStackTrace();
			//ExceptionUtil.throwActualException(e);
		}

		// 2. 解析XML数据，准备XPATH
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		// domFactory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		XPath xpath = null;
		Document doc = null;
		try {
			builder = domFactory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(xml)));
		} catch (ParserConfigurationException e) {
			// e.printStackTrace();
			//ExceptionUtil.throwActualException(e);
		} catch (SAXException e) {
			// e.printStackTrace();
			//ExceptionUtil.throwActualException(e);
		} catch (IOException e) {
			// e.printStackTrace();
			//ExceptionUtil.throwActualException(e);
		}
		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();

		// 3. 按表达式取得域数据
		String value = null;
		try {
			value = xpath.evaluate(xpathExpression, doc);
		} catch (XPathExpressionException e) {
			// e.printStackTrace();
			//ExceptionUtil.throwActualException(e);
		}
		return value;
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);
		parseParameters();
	}

	private void parseParameters() {
		// xpathExpression
		String tmp = (String) parameters.get(PARAM_TEXT_XPATH);
		if (null == tmp) {
			 throw new RuntimeException("parameter " + PARAM_TEXT_XPATH
			 + " is null!");
//			throw new RuntimeException(MultiLanguageResourceBundle
//					.getInstance().getString("parameter.null",
//							new String[] { PARAM_TEXT_XPATH }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				 throw new RuntimeException("parameter " + PARAM_TEXT_XPATH
				 + " is blank!");
//				throw new RuntimeException(MultiLanguageResourceBundle
//						.getInstance().getString("parameter.blank",
//								new String[] { PARAM_TEXT_XPATH }));
			}
		}
		xpathExpression = tmp;

		// xmlStartAt
		tmp = (String) parameters.get(PARAM_TEXT_XML_START_AT);
		if (null == tmp) {
			 throw new RuntimeException("parameter " + PARAM_TEXT_XML_START_AT
			 + " is null!");
//			throw new RuntimeException(MultiLanguageResourceBundle
//					.getInstance().getString("parameter.null",
//							new String[] { PARAM_TEXT_XML_START_AT }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				 throw new RuntimeException("parameter "
				 + PARAM_TEXT_XML_START_AT + " is blank!");
//				throw new RuntimeException(MultiLanguageResourceBundle
//						.getInstance().getString("parameter.blank",
//								new String[] { PARAM_TEXT_XML_START_AT }));
			}
		}
		xmlStartAt = Integer.parseInt(tmp);
		if (xmlStartAt < 0) {
			 throw new RuntimeException("parameter " + PARAM_TEXT_XML_START_AT
			 + " must be >=0!");
//			throw new RuntimeException(MultiLanguageResourceBundle
//					.getInstance().getString("index.lessThan0",
//							new String[] { PARAM_TEXT_XML_START_AT }));
		}

		// xmlEndOffset
		tmp = (String) parameters.get(PARAM_TEXT_XML_END_OFFSET);
		if (tmp != null) {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				 throw new RuntimeException("parameter "
				 + PARAM_TEXT_XML_END_OFFSET + " is blank!");
//				throw new RuntimeException(MultiLanguageResourceBundle
//						.getInstance().getString("parameter.blank",
//								new String[] { PARAM_TEXT_XML_END_OFFSET }));
			}
			xmlEndOffset = Integer.parseInt(tmp);
			if (xmlEndOffset < 0) {
				xmlEndOffset = -xmlEndOffset;
			}
		}

		// xmlMessageCharset
		tmp = (String) parameters.get(PARAM_TEXT_MSG_CHARSET);
		if (null != tmp) {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				throw new RuntimeException("parameter.blank");
			}

			try {
				Charset.isSupported(tmp);
			} catch (IllegalCharsetNameException e) {
				throw new RuntimeException(
								"XPathRecognizer.charset.illegal", e);
			}

			charset = tmp;
		}
	}
}
