package com.fib.gateway.channel.http.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fib.gateway.CommGateway;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

/**
 * HTTPServerChannel配置文件解析器
 * 
 * @author BaiFan
 * 
 */
public class HTTPServerChannelConfigParser {
	private HTTPServerChannelConfig config = null;
	private Document doc = null;
	private XPath xpath = null;

	public HTTPServerChannelConfig parse(InputStream in) {
		// 1.准备XPATH
		prepareXPath(in);

		// 2. 提取配置数据
		try {
			parseAll();
		} catch (XPathExpressionException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		}

		// 3. 检查配置合法性、关联合法性等
		// checkAll();

		return config;
	}

	private void prepareXPath(InputStream in) {

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			// domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			doc = builder.parse(new InputSource(in));
		} catch (ParserConfigurationException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} catch (SAXException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} catch (IOException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} catch (FactoryConfigurationError e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		}
		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
	}

	private void parseAll() throws XPathExpressionException {
		config = new HTTPServerChannelConfig();
		// http-server-channel/port/text()
		String expression = "http-server-channel/port/text()";
		String value = xpath.evaluate(expression, doc);

		if (null == value) {
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
					new String[] { "connector.xml", expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
						new String[] { "connector.xml", expression }));
			}
			config.setPortString(value);
			value = getRealValue(value);
		}
		config.setPort(Integer.parseInt(value));
		// params
		expression = "http-server-channel/params";
		Node node = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);
		if (null != node) {
			parseParams(node);
		}
		// verifier
		expression = "verifier";
		node = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);
		if (null != node) {
			parseVerifier(node);
		}
	}

	private void parseVerifier(Node node) throws XPathExpressionException {
		config.setVerifierConfig(new VerifierConfig());
		// @class
		String expression = "@class";
		String value = xpath.evaluate(expression, node);
		if (null == value) {
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
					new String[] { "connector.xml", node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
						new String[] { "connector.xml", node.getNodeName() + "/" + expression }));
			}
			config.getVerifierConfig().setClassNameString(value);
			value = getRealValue(value);
		}
		config.getVerifierConfig().setClassName(value);
	}

	private void parseParams(Node node) throws XPathExpressionException {

		// @timeout
		String expression = "@timeout";
		String value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
//				config.setTimeoutString(value);
				value = getRealValue(value);
				config.setTimeout(Integer.parseInt(value));
			}
		}
		// @socket-buffer-size
		expression = "@socket-buffer-size";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
//				config.setSocketBufferSizeString(value);
				value = getRealValue(value);
				config.setSocketBufferSize(Integer.parseInt(value));
			}
		}
		// @stale-connection-check
		expression = "@stale-connection-check";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
//				config.setStaleConnectionCheckString(value);
				value = getRealValue(value);
				if ("true".equalsIgnoreCase(value)) {
					config.setStaleConnectionCheck(true);
				} else {
					config.setStaleConnectionCheck(false);
				}
			}
		}
		// @tcp-nodelay
		expression = "@tcp-nodelay";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
//				config.setTcpNoDelayString(value);
				value = getRealValue(value);
				if ("false".equalsIgnoreCase(value)) {
					config.setTcpNoDelay(false);
				} else {
					config.setTcpNoDelay(true);
				}
			}
		}
		// @element-charset
		expression = "@element-charset";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
//				config.setElementCharsetString(value);
				value = getRealValue(value);
				try {
					Charset.forName(value);
				} catch (Exception e) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("XPathRecognizer.charset.illegal", new String[] { value, e.getMessage() }), e);
				}
			}
			config.setElementCharset(value);
		}
		// @content-charset
		expression = "@content-charset";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
//				config.setContentCharsetString(value);
				value = getRealValue(value);
				try {
					Charset.forName(value);
				} catch (Exception e) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("XPathRecognizer.charset.illegal", new String[] { value, e.getMessage() }), e);
				}
			}
			config.setContentCharset(value);
		}
		// @backlog
		expression = "@backlog";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
//				config.setBacklogString(value);
				value = getRealValue(value);
				config.setBacklog(Integer.parseInt(value));
			}
		}

	}

	private String getRealValue(String value) {
		return value;
	}
}