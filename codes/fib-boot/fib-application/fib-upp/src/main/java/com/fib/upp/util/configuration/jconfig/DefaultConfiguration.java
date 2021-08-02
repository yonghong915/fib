package com.fib.upp.util.configuration.jconfig;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultConfiguration implements Serializable, Configuration {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8021129867018338372L;
	protected String configName;
	protected String mainCategory;
	protected Map<String, Category> categories;
	protected Map<String, Variable> variables;

	public DefaultConfiguration(String configName) {
		this.configName = configName;
		categories = new HashMap<>();
		variables = new HashMap<>();
		setCategory("general", true);
	}

	@Override
	public void setCategory(String name) {
		setCategory(name, false);
	}

	@Override
	public void setCategory(Category category) {
		if (!categories.containsKey(category.getCategoryName())) {
			category.setConfigurationName(configName);
			categories.put(category.getCategoryName(), category);
		}
	}

	@Override
	public void setCategory(String categoryName, boolean main) {
		if (null == categoryName) {
			return;
		}
		mainCategory = main ? categoryName : mainCategory;
		if (!categories.containsKey(categoryName)) {
			Category category = new DefaultCategory(categoryName);
			category.setConfigurationName(configName);
			// category.addCategoryListener(new MyCategoryListener());
			categories.put(categoryName, category);
			// markDirty();
			// category.fireCategoryChangedEvent(new ConfigurationChangedEventImpl(4,
			// category, null, null, null));
			// fireConfigurationChangedEvent(new ConfigurationChangedEventImpl(4, category,
			// null, null, null));
		}

	}

	@Override
	public String getMainCategoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getCategoryNames() {
		// TODO Auto-generated method stub
		return null;
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
		if (categoryName == null) {
			categoryName = mainCategory;
		}
		boolean isMainCat = false;
		if (key == null) {
			return defaultValue;
		}
		Category category = getCategory(categoryName);
		String tmp = category.getProperty(key);
		return tmp;
	}

	@Override
	public void setProperty(String name, String value) {
		setProperty(name, value, null);
	}

	@Override
	public void setProperty(String name, String value, String categoryName) {
		if (null == name) {
			return;
		}
		if (categoryName == null) {
			categoryName = mainCategory;
		}
		Category category = getCategory(categoryName);
		category.setProperty(name, value);
		// markDirty();
		// fireConfigurationChangedEvent(new ConfigurationChangedEventImpl(3, category,
		// name, value, null));
	}

	public Category getCategory() {
		return getCategory(mainCategory);
	}

	public Category getCategory(String categoryName) {
		if (categoryName == null) {
			categoryName = mainCategory;
		}
		Category category = (Category) categories.get(categoryName);
		if (category == null) {
//			if (baseConfigName != null) {
//				Configuration cfg = ConfigurationManager.getConfiguration(baseConfigName);
//				return cfg.getCategory(categoryName);
//			}
			category = new DefaultCategory(categoryName);
			categories.put(categoryName, category);
		}
//		else if (baseConfigName != null && !"base".equals(baseConfigName)) {
//			Configuration parentCfg = ConfigurationManager.getConfiguration(baseConfigName);
//			Category parentCategory = parentCfg.getCategory(categoryName);
//			Properties props = parentCategory.getProperties();
//			Iterator iter = props.keySet().iterator();
//			do {
//				if (!iter.hasNext())
//					break;
//				String element = (String) iter.next();
//				if (category.getProperty(element) == null)
//					category.setProperty(element, parentCategory.getProperty(element));
//			} while (true);
//		}
		return category;
	}

	public void getVariables() {

	}

	public String getVariable(String variableName, String propName) {
		if (null == variableName || "".equals(variableName)) {
			return "";
		}
		if (null == propName || "".equals(propName)) {
			return "";
		}
		Variable variable = variables.get(variableName);
		if (null == variable) {
			return "";
		}
		List<Property> props = variable.getProperties();
		for (Property prop : props) {
			if (propName.equals(prop.getName())) {
				return prop.getValue();
			}
		}
		return "";
	}

	@Override
	public void addVariable(String variableName, Variable variable) {
		variables.put(variableName, variable);
	}
}
