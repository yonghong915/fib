package com.fib.msgconverter.commgateway.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.msgconverter.commgateway.config.base.DynamicObjectConfig;
import com.fib.msgconverter.commgateway.config.base.TypedDynamicObjectConfig;
import com.fib.msgconverter.commgateway.session.config.SessionConfig;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.config.ConfigManager;
import com.giantstone.common.util.ExceptionUtil;

public class GatewayConfigParser extends DefaultHandler {
	private GatewayConfig config;
	private String fileName;
	private String currParaName;
	private DynamicObjectConfig currParaOwner;
	private ChannelMainConfig channel;
	private String elementValue;

	public GatewayConfig parse(InputStream in) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			parser.parse(in, this);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {
					ex.printStackTrace(System.err);
				}
			}
		}
		// 解析后检查
		configCheck();
		return config;
	}

	public GatewayConfig parse(String fileName) {
		this.fileName = fileName;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		InputStream in = null;
		try {
			in = ConfigManager.getInputStream(this.fileName);
			parser = factory.newSAXParser();
			parser.parse(in, this);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {
					ex.printStackTrace(System.err);
				}
			}
		}
		// 解析后检查
		configCheck();
		return config;
	}

	private void configCheck() {
		if (null != fileName) {
			String gwId = fileName.substring(fileName.indexOf("gateway_")
					+ "gateway_".length(), fileName.indexOf(".xml"));
			if (!gwId.equals(config.getId())) {
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"CommGateway.loadConfig.configFileName.notEqual.id",
										new String[] { config.getId(), fileName }));
			}
		}
		if (null != config.getVariableFileName()) {
			try {
				config.setVariableConfig(new Properties(ConfigManager
						.loadProperties(config.getVariableFileName())));
			} catch (Exception e) {
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"CommGateway.loadVariable.error",
								new String[] { ExceptionUtil
										.getExceptionDetail(e) }));
			}
		}

		config.setMonitorPort(Integer.parseInt(getRealValue(config
				.getMonitorPortString())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		// System.out.println(name);
		if ("gateway".equals(name)) {
			config = new GatewayConfig();
			// @id
			String attr = attributes.getValue("id");
			if (null == attr) {
				// throw new RuntimeException(this.fileName
				// + ": gateway/@id is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("config.null",
								new String[] { fileName, "gateway/@id" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": gateway/@id is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("config.blank",
									new String[] { fileName, "gateway/@id" }));
				}
			}
			config.setId(attr);

			// @name
			attr = attributes.getValue("name");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": gateway/@name is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("config.null",
								new String[] { fileName, "gateway/@name" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": gateway/@name is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("config.blank",
									new String[] { fileName, "gateway/@name" }));
				}
			}
			config.setName(attr);

			// @monitor-port
			attr = attributes.getValue("monitor-port");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": gateway/@monitor-port is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"config.null",
								new String[] { fileName,
										"gateway/@monitor-port" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": gateway/@monitor-port is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] { fileName,
											"gateway/@monitor-port" }));
				}
			}
			config.setMonitorPortString(attr);
		} else if ("module".equals(name)) {
			ModuleConfig module = new ModuleConfig();
			module.setParameters(new HashMap());
			currParaOwner = module;
			config.getModules().add(module);

			// type 优先于 class
			// @type
			String attr = attributes.getValue("type");
			if (attr != null
					&& attr.trim().length() != 0
					&& !TypedDynamicObjectConfig.USER_DEFINED
							.equalsIgnoreCase(attr.trim())) {
				module.setType(attr.trim());
			} else {
				// @class
				attr = attributes.getValue("class");
				if (null == attr) {
					// throw new RuntimeException(fileName
					// + ": module/@class is NULL");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("config.null",
									new String[] { fileName, "module/@class" }));
				} else {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(fileName
						// + ": module/@class is Blank");
						throw new RuntimeException(MultiLanguageResourceBundle
								.getInstance().getString(
										"config.blank",
										new String[] { fileName,
												"module/@class" }));
					}
				}
				module.setClassName(attr);
			}
		} else if ("parameter".equals(name)) {
			// @name
			String attr = attributes.getValue("name");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": parameter/@name is NULL");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("config.null",
								new String[] { fileName, "parameter/@name" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": parameter/@name is Blank");
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance()
									.getString(
											"config.blank",
											new String[] { fileName,
													"parameter/@name" }));
				}
			}
			currParaName = attr;
		} else if ("channel".equals(name)) {
			channel = new ChannelMainConfig();

			// @id
			String attr = attributes.getValue("id");
			if (null == attr) {
				// throw new RuntimeException(fileName +
				// ": channel/@id is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("config.null",
								new String[] { fileName, "channel/@id" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": channel/@id is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("config.blank",
									new String[] { fileName, "channel/@id" }));
				}
			}
			if (config.getChannels().containsKey(attr)) {
				// throw new RuntimeException(fileName + ": channel/@id=" + attr
				// + " is reduplicate!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"reduplicate",
								new String[] { fileName + ": channel/@id="
										+ attr }));
			}
			channel.setId(attr);

			// @name
			attr = attributes.getValue("name");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": channel/@name is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("config.null",
								new String[] { fileName, "channel/@name" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": channel/@name is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("config.blank",
									new String[] { fileName, "channel/@name" }));
				}
			}
			channel.setName(attr);

			// @startup
			attr = attributes.getValue("startup");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": channel/@startup is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("config.null",
								new String[] { fileName, "channel/@startup" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": channel/@startup is Blank!");
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance()
									.getString(
											"config.blank",
											new String[] { fileName,
													"channel/@startup" }));
				}
			}
			if ("false".equalsIgnoreCase(attr)) {
				channel.setStartup(false);
			} else {
				channel.setStartup(true);
			}

			// @deploy
			attr = attributes.getValue("deploy");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": channel/@deploy is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("config.null",
								new String[] { fileName, "channel/@deploy" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": channel/@deploy is Blank!");
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance()
									.getString(
											"config.blank",
											new String[] { fileName,
													"channel/@deploy" }));
				}
			}
			channel.setDeploy(attr);

			config.setChannel(channel.getId(), channel);

		} else if ("event-handler".equals(name)) {
			// @max-handler-number
			String attr = attributes.getValue("max-handler-number");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": event-handler/@max-handler-number is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"config.null",
								new String[] { fileName,
										"event-handler/@max-handler-number" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": event-handler/@max-handler-number is Blank!");
					throw new RuntimeException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"config.blank",
											new String[] { fileName,
													"event-handler/@max-handler-number" }));
				}
			}
			config.setEventHandlerNumber(Integer.parseInt(attr));

		} else if ("logger".equals(name)) {
			String attr = attributes.getValue("name");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": logger/@name is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString("config.blank",
									new String[] { fileName, "logger/@name" }));
				}
				config.setLoggerName(attr);
			}

		} else if ("session".equals(name)) {
			String attr = attributes.getValue("timeout");
			SessionConfig sessionConfig = new SessionConfig();
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": session/@timeout is Blank!");
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance()
									.getString(
											"config.blank",
											new String[] { fileName,
													"session/@timeout" }));
				}

				sessionConfig.setTimeText(attr);
				sessionConfig.setTimeout(SessionConfig.getTimeByText(attr));

			}

			attr = attributes.getValue("level-into-db");
			if (null != attr) {
				attr = attr.trim();
				if (0 != attr.length()) {
					sessionConfig.setLevelIntoDBText(attr);
				}
			}

			attr = attributes.getValue("shield-sensitive-field");
			if (null != attr) {
				if ("true".equals(attr.trim())) {
					sessionConfig.setShieldSensitiveField(true);
				}
			}

			attr = attributes.getValue("sensitive-fields-in-map");
			if (sessionConfig.isShieldSensitiveField()) {
				if (null == attr) {
					throw new RuntimeException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"config.null",
											new String[] { fileName,
													"session/@sensitive-fields-in-map" }));
				} else {
					attr = attr.trim();
					if (0 == attr.length()) {
						throw new RuntimeException(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"config.blank",
												new String[] { fileName,
														"session/@sensitive-fields-in-map" }));
					} else {
						sessionConfig.setSensitiveFieldsInMap(attr);
					}
				}
			}

			attr = attributes.getValue("sensitive-fields-replace");
			if (null != attr) {
				attr = attr.trim();
				if (0 < attr.length()) {
					sessionConfig.setSensitiveFieldsRelpace(attr.charAt(0));
				}
			}

			config.setSessionConfig(sessionConfig);
		} else if ("variable-file".equals(name)) {
			String attr = attributes.getValue("name");

			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] { fileName,
											"variable-file/@name" }));
				}
				config.setVariableFileName(attr);
			}
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

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if ("parameter".equals(name)) {
			if (null == elementValue) {
				// throw new RuntimeException(fileName
				// + ": parameter/text() is NULL");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("config.null",
								new String[] { fileName, "parameter/text()" }));
			} else {
				elementValue = elementValue.trim();
				if (0 == elementValue.length()) {
					// throw new RuntimeException(fileName
					// + ": parameter/text() is Blank");
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance()
									.getString(
											"config.blank",
											new String[] { fileName,
													"parameter/text()" }));
				}
			}
			currParaOwner.getParameters().put(currParaName, elementValue);
		}

		elementValue = null;
	}

	private String getRealValue(String value) {
		if (null == value || null == config.getVariableConfig()) {
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
					String realValue = config.getVariableConfig().getProperty(
							value);
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
}