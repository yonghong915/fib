package com.fib.commons.config.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.fib.commons.config.Category;
import com.fib.commons.config.Configuration;
import com.fib.commons.config.DefaultConfiguration;
import com.fib.commons.config.Property;
import com.fib.commons.xml.Dom4jUtils;
import com.fib.commons.config.Variable;

public class DefaultConfigParser implements BaseXMLConfigParser {

	public DefaultConfigParser() {
		// nothing to do
	}

	@Override
	public Configuration parse(Document doc, String configName) {
		Configuration configuration = new DefaultConfiguration(configName);

		getVariables(doc, configuration);

		List<Node> nodes = Dom4jUtils.selectNodes(doc, "configuration/category");
		Category category = null;
		Property property = null;
		Element ele = null;
		String attrValue = null;
		for (Node node : nodes) {
			ele = (Element) node;

			category = new Category();
			attrValue = ele.attributeValue("name");
			category.setName(attrValue);
			List<Node> propList = Dom4jUtils.selectNodes(doc, "configuration/category/property");

			for (Node propNode : propList) {
				Element propEle = (Element) propNode;
				property = new Property();
				attrValue = propEle.attributeValue("name");
				property.setName(attrValue);

				attrValue = propEle.attributeValue("value");
				property.setValue(attrValue);

				attrValue = propEle.attributeValue("text");
				property.setText(attrValue);

				category.add(property);
			}
			configuration.setCategory(category);
		}
		return configuration;
	}

	private void getVariables(Document doc, Configuration configuration) {
		String xpathExpression = "configuration/variables/variable";
		List<Node> nodes = Dom4jUtils.selectNodes(doc, xpathExpression);
		Element ele = null;
		String attrValue = "";
		Variable variable = null;

		Map<String, Variable> varMap = new HashMap<>();
		for (Node node : nodes) {
			ele = (Element) node;
			variable = new Variable();
			attrValue = ele.attributeValue("name");

			variable.setName(attrValue);
			attrValue = ele.attributeValue("value");

			variable.setValue(attrValue);
			attrValue = ele.attributeValue("text");

			variable.setText(attrValue);
			varMap.put(variable.getName(), variable);
			configuration.setVariable(variable);
		}
	}
}