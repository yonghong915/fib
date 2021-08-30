package com.fib.msgconverter.commgateway.channel.simulate;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.msgconverter.commgateway.channel.simulate.config.BusinessSimulateClientChannelConfig;
import com.giantstone.common.util.ExceptionUtil;

public class BusinessSimulateClientChannelConfigParser extends DefaultHandler {
	private BusinessSimulateClientChannelConfig config = null;
	private static final int DEFAULT_HANDLER_NUM = 3;

	public BusinessSimulateClientChannelConfig parse(InputStream in) {
		config = new BusinessSimulateClientChannelConfig();
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
		return config;
	}

	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		String attr = null;
		if ("request-handler".equalsIgnoreCase(name)) {
			attr = attributes.getValue("max-number");
			config.setMaxNumber(DEFAULT_HANDLER_NUM);
			if (null != attr) {
				attr = attr.trim();
				if (0 < attr.length()) {
					config.setMaxNumber(Integer.parseInt(attr));
				}
			}
		}
	}
}
