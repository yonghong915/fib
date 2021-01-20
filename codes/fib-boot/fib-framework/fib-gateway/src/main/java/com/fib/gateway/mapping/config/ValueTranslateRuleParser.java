package com.fib.gateway.mapping.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class ValueTranslateRuleParser extends DefaultHandler {

	private ValueTranslateRule rule = null;

	private String fileName = null;

	private String key;

	private int position = 0;

	public ValueTranslateRule parse(File file) {
		fileName = file.getAbsolutePath();

		SAXParserFactory saxParserFactory = SAXParserFactory.newDefaultInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse(new FileInputStream(file), this);
		} catch (Exception e) {
			throw new CommonException("Failed to read file", e);
		}
		return rule;
	}

	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		String attr = null;

		if ("value-translate-rule".equalsIgnoreCase(name)) {
			rule = new ValueTranslateRule();
			rule.setTranslateRelations(new HashMap(32));
			// @value-translate-rule /id
			attr = attributes.getValue("id");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": value-translate-rule /@id is null");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "value-translate-rule/@id" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": value-translate-rule /@id is blank");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "value-translate-rule/@id" }));
				}
			}
			rule.setId(attr);
			// @name
			attr = attributes.getValue("name");
			rule.setName(attr);

		} else if ("value".equalsIgnoreCase(name)) {
			// @is-default
			attr = attributes.getValue("is-default");
			if (null != attr && "true".equals(attr.trim())) {
				// @value
				attr = attributes.getValue("value");
				if (null == attr) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
							new String[] { fileName, "message-bean-mapping/mapping[" + position + "]/value/@value" }));
				} else {
					attr = attr.trim();
					if (0 == attr.length()) {
						throw new RuntimeException(
								MultiLanguageResourceBundle.getInstance().getString("config.blank", new String[] {
										fileName, "message-bean-mapping/mapping[" + position + "]/value/@value" }));
					} else {
						rule.setDefaultValue(attr);
					}
				}
			} else {

				// @from
				attr = attributes.getValue("from");
				if (null == attr) {
					// throw new RuntimeException(fileName
					// + ": value-translate-rule [" + position
					// + "]/value/@from is null");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
							new String[] { fileName, "value-translate-rule[" + position + "]/value/@from" }));
				} else {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(fileName
						// + ": value-translate-rule [" + position
						// + "]/value/@from is blank");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
								new String[] { fileName, "value-translate-rule[" + position + "]/value/@from" }));
					}
				}
				key = attr;

				// @to
				attr = attributes.getValue("to");
				if (null == attr) {
					// throw new RuntimeException(fileName
					// + ": value-translate-rule [" + position
					// + "]/value/@to is null");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
							new String[] { fileName, "value-translate-rule[" + position + "]/value/@to" }));
				} else {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(fileName
						// + ": value-translate-rule [" + position
						// + "]/value/@to is blank");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
								new String[] { fileName, "value-translate-rule[" + position + "]/value/@to" }));
					}
				}
				rule.getTranslateRelations().put(key, attr);
				position++;
			}
		}
	}
}
