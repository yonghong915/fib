package com.fib.msgconverter.commgateway.channel.http.config;

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

public class HTTPClientChannelConfigParser extends DefaultHandler {
	private HTTPClientChannelConfig config = new HTTPClientChannelConfig();
	private String fileName;

	public HTTPClientChannelConfig parse(InputStream in) {
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
				config.setMaxHandlerNumberString(attr);
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
				// throw new RuntimeException(fileName + ": url is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("config.null",
								new String[] { fileName, "url" }));
			}
			url = url.trim();
			if (0 == url.length()) {
				// throw new RuntimeException(fileName + ": url is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("config.blank",
								new String[] { fileName, "url" }));
			}
			config.setUrlString(url);
			url = getRealValue(url);
			try {
				new URL(url);
			} catch (MalformedURLException e) {
				// e.printStackTrace();
				// throw new RuntimeException(fileName + ": url[" + url
				// + "]'format is not valid!" + e.getMessage(), e);
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"HTTPClientHandler.endElement.url.format.invalidate",
										new String[] { fileName, url,
												e.getMessage() }), e);
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
				config.setContentCharsetString(contentCharset);
				contentCharset = getRealValue(contentCharset);
			}
			config.setContentCharset(contentCharset);

			elementValue = null;
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

	private String elementValue = null;
}
