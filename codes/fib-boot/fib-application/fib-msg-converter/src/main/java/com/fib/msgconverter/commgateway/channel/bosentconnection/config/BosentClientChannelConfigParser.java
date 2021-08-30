package com.fib.msgconverter.commgateway.channel.bosentconnection.config;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ExceptionUtil;

public class BosentClientChannelConfigParser extends DefaultHandler {
	private BosentClientChannelConfig config = new BosentClientChannelConfig();
	private String fileName;
	private String elementValue = null;

	public BosentClientChannelConfig parse(InputStream in) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			parser.parse(in, this);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		}
		return config;
	}

	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		// 任何元素开始都将elementValue设置为空
		elementValue = null;

		if ("handler".equals(name)) {
			// @max-handler-number
			String attr = attributes.getValue("max-handler-number");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": handler/@max-handler-number is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"config.null",
								new String[] { fileName,
										"handler/@max-handler-number" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": handler/@max-handler-number is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] { fileName,
											"handler/@max-handler-number" }));
				}
				attr = getRealValue(attr);
			}
			config.setMaxHandlerNumber(Integer.parseInt(attr));
		}
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if ("url".equals(name)) {
			String url = elementValue;
			if (null == url) {
				throw new RuntimeException(fileName + ": url is Null!");
			}
			url = url.trim();
			if (0 == url.length()) {
				throw new RuntimeException(fileName + ": url is Blank!");
			}
			url = getRealValue(url);
			try {
				new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new RuntimeException(fileName + ": url[" + url
						+ "]'format is not valid!" + e.getMessage(), e);
			}

			config.setUrl(url);

			elementValue = null;
		} else if ("content-charset".equals(name)) {
			String contentCharset = elementValue;
			if (null == contentCharset) {
				return;
			}
			contentCharset = contentCharset.trim();
			if (0 == contentCharset.length()) {
				return;
			} else {
				contentCharset = getRealValue(contentCharset);
			}
			config.setContentCharset(contentCharset);

			elementValue = null;
		} else if ("user-name".equals(name)) {
			String userName = elementValue;
			if (null == userName) {
				return;
			}
			userName = userName.trim();
			if (0 == userName.length()) {
				return;
			} else {
				userName = getRealValue(userName);
			}
			config.setUserName(userName);
		} else if ("password".equals(name)) {
			String password = elementValue;
			if (null == password) {
				return;
			}
			password = password.trim();
			if (0 == password.length()) {
				return;
			} else {
				password = getRealValue(password);
			}
			config.setPassword(password);
		} else if ("server-id".equals(name)) {
			String serverId = elementValue;
			if (null == serverId) {
				throw new RuntimeException(fileName + ": server-id is NULL!");
			}
			serverId = serverId.trim();
			if (0 == serverId.length()) {
				throw new RuntimeException(fileName + ": server-id is Blank!");
			} else {
				serverId = getRealValue(serverId);
			}
			config.setServerId(serverId);
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (null == elementValue) {
			elementValue = new String(ch, start, length);
		} else {
			elementValue += new String(ch, start, length);
		}
	}

	private String getRealValue(String value) {
		if (null == value || null == CommGateway.getVariableConfig()) {
			return value;
		} else {
			int startIndex = value.indexOf("${");

			if (-1 == startIndex) {
				return value;
			} else {
				startIndex += 2;
				int endIndex = value.indexOf("}", startIndex);
				if (-1 == endIndex) {
					return value;
				} else {
					value = value.substring(startIndex, endIndex);
					String realValue = CommGateway.getVariableConfig()
							.getProperty(value);
					if (null == realValue || 0 == realValue.trim().length()) {
						throw new RuntimeException(MultiLanguageResourceBundle
								.getInstance().getString(
										"CommGateway.variable.notFound",
										new String[] { value }));

					} else {
						return realValue;
					}

				}
			}
		}
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
