package com.fib.commons.config;

import java.util.Map;

public interface Configuration {

	void setVariable(Variable variable);

	void setCategory(Category category);

	Map<String, Variable> getVariables();

	Map<String, Category> getCategories();

	public String getProperty(String key);

	public String getProperty(String key, String categoryName);

}
