package com.fib.msgconverter.commgateway.channel.nio.config;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.config.base.ConnectionConfig;
import com.fib.msgconverter.commgateway.config.base.DynamicObjectConfig;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ExceptionUtil;

public abstract class SocketChannelConfigParser extends DefaultHandler {
	protected SocketChannelConfig config = null;

	/**
	 * 解析后检查
	 */
	protected abstract void configCheck();

	public SocketChannelConfig parse(InputStream in) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			parser.parse(in, this);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		}
		// 解析后检查
		configCheck();
		return config;
	}

	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		// System.out.println(name);
		if ("connector".equals(name)) {
			config = new SocketChannelConfig();
		} else if ("connection".equals(name)) {
			ConnectionConfig conn = new ConnectionConfig();
			config.setConnectionConfig(conn);

			// @type
			String attr = attributes.getValue("type");
			if (null == attr) {
				// throw new RuntimeException("connection/@type is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("null",
								new String[] { "connection/@type" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException("connection/@type is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("blank",
									new String[] { "connection/@type" }));
				}
				attr = getRealValue(attr);
			}
			conn.setType(ConnectionConfig.getTypeByText(attr));

			// @server-address
			attr = attributes.getValue("server-address");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(
					// "connection/@server-address is Blank!");
					throw new RuntimeException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"blank",
											new String[] { "connection/@server-address" }));
				}
				conn.setServerAddressString(attr);
				attr = getRealValue(attr);
				conn.setServerAddress(attr);
			}

			// @port
			attr = attributes.getValue("port");
			if (null == attr) {
				// throw new RuntimeException("connection/@port is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("null",
								new String[] { "connection/@port" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException("connection/@port is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("blank",
									new String[] { "connection/@port" }));
				}
				conn.setPortString(attr);
				attr = getRealValue(attr);
			}
			conn.setPort(Integer.parseInt(attr));

			// @backlog
			attr = attributes.getValue("backlog");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new
					// RuntimeException("connection/@backlog is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("blank",
									new String[] { "connection/@backlog" }));
				}
				attr = getRealValue(attr);
				conn.setBacklog(Integer.parseInt(attr));
			}

			// @direction
			attr = attributes.getValue("direction");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(
					// "connection/@direction is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("blank",
									new String[] { "connection/@direction" }));
				}
				attr = getRealValue(attr);
				conn.setDirection(ConnectionConfig.getDirectionByText(attr));
			}

			// @local-port
			attr = attributes.getValue("local-port");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(
					// "connection/@direction is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("blank",
									new String[] { "connection/@local-port" }));
				}
				conn.setLocalPortString(attr);
				attr = getRealValue(attr);
				conn.setLocalPort(Integer.parseInt(attr));
			}

			// @local-server-address
			attr = attributes.getValue("local-server-address");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(
					// "connection/@direction is Blank!");
					throw new RuntimeException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"blank",
											new String[] { "connection/@local-server-address" }));
				}
				conn.setLocalServerAddressString(attr);
				attr = getRealValue(attr);
				conn.setLocalServerAddress(attr);
			}

			// @comm-buffer-size
			attr = attributes.getValue("comm-buffer-size");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(
					// "connection/@comm-buffer-size is Blank!");
					throw new RuntimeException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"blank",
											new String[] { "connection/@comm-buffer-size" }));
				}
				attr = getRealValue(attr);
				conn.setCommBufferSize(Integer.parseInt(attr));
				if (ConnectionConfig.DEFAULT_COMM_BUFFER_SIZE > conn
						.getCommBufferSize()) {
					conn
							.setCommBufferSize(ConnectionConfig.DEFAULT_COMM_BUFFER_SIZE);
				}
			}

		} else if ("reader".equals(name)) {
			ReaderConfig readerConfig = new ReaderConfig();
			config.setReaderConfig(readerConfig);

			currentParamOwner = readerConfig;

			// @class
			String attr = attributes.getValue("class");
			if (null == attr) {
				// throw new RuntimeException("reader/@class is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("null",
								new String[] { "reader/@class" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException("reader/@class is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("blank",
									new String[] { "reader/@class" }));
				}
				attr = getRealValue(attr);
			}
			readerConfig.setClassName(attr);
		} else if ("writer".equals(name)) {
			WriterConfig writerConfig = new WriterConfig();
			config.setWriterConfig(writerConfig);

			currentParamOwner = writerConfig;

			// @class
			String attr = attributes.getValue("class");
			if (null == attr) {
				// throw new RuntimeException("writer/@class is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("null",
								new String[] { "writer/@class" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException("writer/@class is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("blank",
									new String[] { "writer/@class" }));
				}
				attr = getRealValue(attr);
			}
			writerConfig.setClassName(attr);
		} else if ("parameter".equals(name)) {
			// @name
			String attr = attributes.getValue("name");
			if (null == attr) {
				// throw new RuntimeException("parameter/@name is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("null",
								new String[] { "parameter/@name" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException("parameter/@name is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("blank",
									new String[] { "parameter/@name" }));
				}
				attr = getRealValue(attr);
			}
			currentParameter = attr;

			// currentParamOwner.setParameter(attr, currentParameter);
		} else if ("filter".equals(name)) {
			FilterConfig filterConfig = new FilterConfig();
			// @class
			String attr = attributes.getValue("class");
			if (null == attr) {
				// throw new RuntimeException("filter/@class is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("null",
								new String[] { "filter/@class" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException("filter/@class is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("blank",
									new String[] { "filter/@class" }));
				}
				attr = getRealValue(attr);
			}
			filterConfig.setClassName(attr);
			if (currentParamOwner instanceof ReaderConfig) {
				((ReaderConfig) currentParamOwner).getFilterConfigList().add(
						filterConfig);
			} else {
				((WriterConfig) currentParamOwner).getFilterConfigList().add(
						filterConfig);
			}
		}
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if ("reader".equals(name)) {
			currentParamOwner = null;
		} else if ("writer".equals(name)) {
			currentParamOwner = null;
		} else if ("parameter".equals(name)) {
			// currentParameter.setValue(elementValue);
			currentParamOwner.setParameter(currentParameter, elementValue);
			elementValue = null;
			currentParameter = null;
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (null == elementValue) {
			elementValue = new String(ch, start, length).trim();
		} else {
			elementValue += new String(ch, start, length).trim();
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
	private DynamicObjectConfig currentParamOwner = null;
	private String currentParameter = null;

}