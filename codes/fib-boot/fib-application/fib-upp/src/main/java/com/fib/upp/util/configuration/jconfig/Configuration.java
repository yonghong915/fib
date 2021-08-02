package com.fib.upp.util.configuration.jconfig;

public interface Configuration {
	public abstract void setCategory(String s);

	public abstract void setCategory(Category category);

	public abstract void setCategory(String s, boolean flag);

	public abstract String getMainCategoryName();

	public abstract String[] getCategoryNames();

	public abstract String getProperty(String s);

	public abstract String getProperty(String s, String s1);

	public abstract String getProperty(String s, String s1, String s2);

	public abstract void setProperty(String s, String s1);

	public abstract void setProperty(String s, String s1, String s2);

	public abstract void addVariable(String variableName, Variable variable);

	String getVariable(String variableName, String propName);
}
