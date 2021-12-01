package com.fib.commons.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.text.CharSequenceUtil;

public class DefaultConfiguration implements Serializable, Configuration {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2518352263009624230L;

	protected String configName;
	private transient Map<String, Category> categories = new HashMap<>();
	private transient Map<String, Variable> variables = new HashMap<>();

	public DefaultConfiguration() {

	}

	public DefaultConfiguration(String configName) {
		this.configName = configName;

	}

	@Override
	public void setCategory(Category category) {
		categories.put(category.getName(), category);
	}

	@Override
	public void setVariable(Variable variable) {
		variables.put(variable.getName(), variable);
	}

	@Override
	public Map<String, Variable> getVariables() {
		return variables;
	}

	@Override
	public Map<String, Category> getCategories() {
		return categories;
	}

	@Override
	public String getProperty(String key) {
		return getProperty(key, null, null);
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		return getProperty(key, defaultValue, null);
	}

	@Override
	public String getProperty(String key, String defaultValue, String categoryName) {
		if (null == categoryName || "".equals(categoryName)) {
			categoryName = "general";
		}
		Category cat = categories.get(categoryName);
		List<Property> propList = cat.getProperties();
		for (Property prp : propList) {
			if (key.equals(prp.getName())) {
				return replaceVar(prp.getValue(), variables, "");
			}
		}
		return CharSequenceUtil.EMPTY;
	}

	private static String replaceVar(String text, Map<String, Variable> vars, String configName) {
		if (text == null)
			return text;
		int startPosition = text.indexOf("${");
		if (startPosition == -1)
			return text;
		StringBuilder buffer = new StringBuilder();
		int textLength = text.length();
		int lastIndex = 0;
		for (; startPosition > -1 && startPosition + 2 < textLength; startPosition = text.indexOf("${", lastIndex)) {
			int endPosition = text.indexOf("}", startPosition + 2);
			if (endPosition == -1)
				break;
			buffer.append(text.substring(lastIndex, startPosition));
			String currentKey = text.substring(startPosition + 2, endPosition);
			String value = getVariable(vars, currentKey);
			if (value != null) {
				value = replaceVar(value, vars, configName);
				buffer.append(value);
				lastIndex = endPosition + 1;
			} else {
				buffer.append(text.charAt(startPosition));
				buffer.append(text.charAt(startPosition + 1));
				lastIndex = startPosition + 2;
			}
		}

		buffer.append(text.substring(lastIndex, textLength));
		return buffer.toString();
	}

	private static String getVariable(Map<String, Variable> vars, String currentKey) {
		Variable varV = vars.get(currentKey);
		return varV.getValue();
	}
}