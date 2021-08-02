package com.fib.upp.util.configuration.jconfig.parser;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.fib.upp.util.configuration.jconfig.Configuration;
import com.fib.upp.util.configuration.jconfig.DefaultConfiguration;

public class DefaultConfigParser extends AbstractConfigParser {
	public DefaultConfigParser() {
	}

	public Configuration parse(Document doc, String configName) {
		Configuration configuration = new DefaultConfiguration(configName);

		getBaseConfigName(doc, configuration);
		getVariables(doc, configuration);
		getIncludeProperties(doc, configuration);

		Element rootEle = doc.getRootElement();
		List<Element> categoryEles = rootEle.elements("category");
		for (int i = 0, len = categoryEles.size(); i < len; i++) {
			Element ele = categoryEles.get(i);
			String currentCategory = ele.attributeValue("name");
			configuration.setCategory(currentCategory);

			List<Element> propertyEles = ele.elements("property");
			for (int j = 0, propLen = propertyEles.size(); j < propLen; j++) {
				Element propEle = propertyEles.get(j);
				String propName = propEle.attributeValue("name");
				String propValue = propEle.attributeValue("value");
				String propText = propEle.attributeValue("text");
				configuration.setProperty(propName, propValue, currentCategory);
			}
		}
		return configuration;
	}
}