package com.fib.upp.util.configuration.jconfig;

public interface Category {
	public abstract Category setProperty(String s, String s1);

	public abstract String getProperty(String s);

	public abstract String getProperty(String s, String s1);

	public abstract String getCategoryName();
	
	public abstract void setConfigurationName(String s);
}
