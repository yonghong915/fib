package com.fib.gateway.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.config.base.DynamicObjectConfig;
import com.fib.gateway.config.base.TypedDynamicObjectConfig;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

import cn.hutool.core.util.StrUtil;

public class GatewayConfigParser extends DefaultHandler {
	private GatewayConfig config;
	private String fileName;
	private String currParaName;
	private DynamicObjectConfig currParaOwner;
	private ChannelMainConfig channel;
	private String elementValue;

	public GatewayConfig parse(InputStream in) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newDefaultInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse(in, this);
		} catch (Exception e) {
			throw new CommonException("Failed to read file", e);
		}
		// 解析后检查
		configCheck();
		return config;
	}

	public GatewayConfig parse(String fileName) {
		this.fileName = fileName;
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("config/upp/deploy/config/" + fileName);
		return this.parse(is);
	}

	private void configCheck() {
		if (null != fileName) {
			String gwId = fileName.substring(fileName.indexOf("gateway_") + "gateway_".length(),
					fileName.indexOf(".xml"));
			if (!gwId.equals(config.getId())) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"CommGateway.loadConfig.configFileName.notEqual.id",
						new String[] { config.getId(), fileName }));
			}
		}

//		if (null != config.getVariableFileName()) {
//			try {
//				config.setVariableConfig(new Properties(ConfigManager.loadProperties(config.getVariableFileName())));
//			} catch (Exception e) {
//				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
//						"CommGateway.loadVariable.error", new String[] { ExceptionUtil.getExceptionDetail(e) }));
//			}
//		}
		int monitorPort = getMonitorPort();
		config.setMonitorPort(monitorPort);
	}

	private int getMonitorPort() {
		// StrUtil.strip(str, StrUtil.C_DELIM_START, StrUtil.C_DELIM_END);
		return 0;
	}

	public static void main(String[] args) {
		String str = "${cnaps2.upp.cnaps2.monitor_port}";
		System.out.println(
				StrUtil.strip(str, "${", "}"));
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		String attrValue = "";
		if ("gateway".equals(name)) {
			config = new GatewayConfig();
			attrValue = attributes.getValue("id");
			ExceptionUtil.requireNotEmpty(attrValue, "id .....");
			config.setId(attrValue);

			attrValue = attributes.getValue("name");
			ExceptionUtil.requireNotEmpty(attrValue, "gateway/@name....");
			config.setName(attrValue);

			attrValue = attributes.getValue("monitor-port");
			ExceptionUtil.requireNotEmpty(attrValue, "gateway/@monitor-port....");
			config.setMonitorPortString(attrValue);
		} else if ("module".equals(name)) {
			ModuleConfig module = new ModuleConfig();
			module.setParameters(new HashMap<>());
			currParaOwner = module;
			config.getModules().add(module);

			// type 优先于 class
			attrValue = attributes.getValue("type");
			if (StrUtil.isNotBlank(attrValue)
					&& !TypedDynamicObjectConfig.USER_DEFINED.equalsIgnoreCase(attrValue.trim())) {
				module.setType(attrValue);
			} else {
				attrValue = attributes.getValue("class");
				ExceptionUtil.requireNotEmpty(attrValue, "module/@class....");
				module.setClassName(attrValue);
			}
		} else if ("parameter".equals(name)) {
			attrValue = attributes.getValue("name");
			ExceptionUtil.requireNotEmpty(attrValue, "parameter/@name....");
			currParaName = attrValue;
		} else if ("channel".equals(name)) {
			channel = new ChannelMainConfig();
			attrValue = attributes.getValue("id");
			ExceptionUtil.requireNotEmpty(attrValue, "channel/@id....");
			if (config.getChannels().containsKey(attrValue)) {
				throw new CommonException("reduplicate channel/@id=");
			}
			channel.setId(attrValue);

			attrValue = attributes.getValue("name");
			ExceptionUtil.requireNotEmpty(attrValue, "channel/@name....");
			channel.setName(attrValue);

			// @startup
			attrValue = attributes.getValue("startup");
			ExceptionUtil.requireNotEmpty(attrValue, "channel/@startup....");
			channel.setStartup(!"false".equalsIgnoreCase(attrValue));

			// @deploy
			attrValue = attributes.getValue("deploy");
			ExceptionUtil.requireNotEmpty(attrValue, "channel/@deploy....");
			channel.setDeploy(attrValue);

			config.setChannel(channel.getId(), channel);
		} else if ("event-handler".equals(name)) {
			attrValue = attributes.getValue("max-handler-number");
			ExceptionUtil.requireNotEmpty(attrValue, "event-handler/@max-handler-number....");
			config.setEventHandlerNumber(Integer.parseInt(attrValue));
		} else if ("logger".equals(name)) {
			attrValue = attributes.getValue("name");
			if (StrUtil.isNotBlank(attrValue)) {
				config.setLoggerName(attrValue);
			}
		} else if ("session".equals(name)) {
			attrValue = attributes.getValue("timeout");

		} else if ("variable-file".equals(name)) {
			attrValue = attributes.getValue("name");
			if (StrUtil.isNotBlank(attrValue)) {
				config.setVariableFileName(attrValue);
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (null == elementValue) {
			elementValue = new String(ch, start, length);
		} else {
			elementValue += new String(ch, start, length);
		}
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if ("parameter".equals(name)) {
			ExceptionUtil.requireNotEmpty(elementValue, "parameter/text()");
			currParaOwner.getParameters().put(currParaName, elementValue);
		}
		elementValue = null;
	}
}