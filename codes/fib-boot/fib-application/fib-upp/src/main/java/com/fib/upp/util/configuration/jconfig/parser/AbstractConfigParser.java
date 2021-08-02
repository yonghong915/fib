package com.fib.upp.util.configuration.jconfig.parser;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.fib.upp.util.configuration.jconfig.Configuration;
import com.fib.upp.util.configuration.jconfig.Property;
import com.fib.upp.util.configuration.jconfig.Variable;

public abstract class AbstractConfigParser implements ConfigurationParser {
	protected void getBaseConfigName(Document doc, Configuration configuration) {
	}

	protected void getVariables(Document doc, Configuration configuration) {
		Element rootEle = doc.getRootElement();

		Element variablesEle = rootEle.element("variables");
		if (null == variablesEle) {
			return;
		}

		List<Element> variableEles = variablesEle.elements("variable");
		Variable variable = null;
		Property property = null;
		for (int i = 0, len = variableEles.size(); i < len; i++) {
			variable = new Variable();
			Element ele = variableEles.get(i);
			String variableName = ele.attributeValue("name");
			String variableText = ele.attributeValue("text");
			variable.setName(variableName);
			variable.setText(variableText);

			List<Element> propertyEles = ele.elements("property");
			for (int j = 0, propLen = propertyEles.size(); j < propLen; j++) {
				Element propEle = propertyEles.get(j);
				property = new Property();
				String propName = propEle.attributeValue("name");
				String propValue = propEle.attributeValue("value");
				String propText = propEle.attributeValue("text");
				property.setName(propName);
				property.setValue(propValue);
				property.setText(propText);
				variable.addProperty(property);
			}
			configuration.addVariable(variableName, variable);
		}
	}

	protected void getIncludeProperties(Document doc, Configuration configuration) {
	}
}
