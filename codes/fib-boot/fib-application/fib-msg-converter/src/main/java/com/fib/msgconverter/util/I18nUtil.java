package com.fib.msgconverter.util;

import java.util.ResourceBundle;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @date 2023-08-31 15:18:24
 */
public enum I18nUtil {
	INSTANCE;

	private ResourceBundle resourceBundle = null;

	private I18nUtil() {
		this.resourceBundle = ResourceBundle.getBundle("config/i18n/messages");
	}

	public String getString(String key) {
		return getString(key, "");
	}

	public String getString(String key, String... params) {
		if (null == key || key.trim().length() == 0) {
			throw new IllegalArgumentException(resourceBundle.getString("MultiLanguageResourceBundle.name.null"));
		}
		String result = resourceBundle.getString(key);
		if (null == params) {
			return result;
		}

		for (int i = 0; i < params.length; i++) {
			String tmp = "{" + i + "}";
			int index = result.indexOf(tmp);
			while (-1 < index) {
				result = result.substring(0, index) + params[i] + result.substring(index + tmp.length());
				index = result.indexOf(tmp, index + (null == params[i] ? 4 : params[i].length()));
			}
		}
		return result;
	}
}