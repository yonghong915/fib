/**
 * 北京长信通信息技术有限公司
 * 2008-11-21 下午09:05:20
 */
package com.fib.msgconverter.commgateway.config;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.giantstone.common.config.ConfigManager;
import com.giantstone.common.util.ExceptionUtil;

/**
 * @author 刘恭亮
 * 
 */
public class GatewayConfigParser2 {

	private XPath xpath = null;

	private Document doc = null;

	private GatewayConfig config = null;

	public GatewayConfig parse(String fileName) {
		// 1. 加载XML
		loadXmlFile(fileName);

		// 2. 解析配置
		config = new GatewayConfig();
		try {
			parseGateway();
		} catch (XPathExpressionException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		}

		return config;
	}

	private void parseGateway() throws XPathExpressionException {
		String value = xpath.evaluate("gateway/@id", doc);
		if (null == value) {
			throw new RuntimeException("gateway/@id is null");
		} else {
			value = value.trim();
			if (0 == value.length()) {
				throw new RuntimeException("gateway/@id is blank");
			}
		}
		config.setId(value);

	}

	private void loadXmlFile(String fileName) {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		// domFactory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		try {
			builder = domFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		}

		InputStream in = ConfigManager.getInputStream(fileName);
		try {
			doc = builder.parse(in);
		} catch (SAXException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} catch (IOException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}

		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
	}

}
