package com.fib.gateway.message.xml.message;


public class MultiLanguageResourceBundle {
	private static MultiLanguageResourceBundle instance = null;

	private MultiLanguageResourceBundle() {
		
	}

	public static MultiLanguageResourceBundle getInstance() {
		if (null == instance) {
			instance = new MultiLanguageResourceBundle();
		}

		return instance;
	}

	public String getString(String name) {
		return getString(name, null);
	}

	public String getString(String name, String[] params) {
	
		return name;
	}

}
