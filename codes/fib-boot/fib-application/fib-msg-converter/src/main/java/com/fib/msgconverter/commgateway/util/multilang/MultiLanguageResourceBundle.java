package com.fib.msgconverter.commgateway.util.multilang;

import java.util.Locale;
import java.util.PropertyResourceBundle;

public class MultiLanguageResourceBundle {
	private static MultiLanguageResourceBundle instance = null;
	private PropertyResourceBundle resourceBundle = null;

	private MultiLanguageResourceBundle() {
		resourceBundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle("CommGateway_resource",
				Locale.getDefault());
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
		if (null == name) {
			throw new IllegalArgumentException(resourceBundle.getString("MultiLanguageResourceBundle.name.null"));
		} else {
			name = name.trim();
			if (0 == name.length()) {
				throw new IllegalArgumentException(resourceBundle.getString("MultiLanguageResourceBundle.name.empty"));
			}
		}
		String result = resourceBundle.getString(name);

//		if (Locale.CHINESE.getLanguage().equals(Locale.getDefault().getLanguage())) {
//			try {
//				result = new String(result.getBytes("ISO_8859_1"), "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// e.printStackTrace();
//			}
//		}

		if (null != params) {
			for (int i = 0; i < params.length; i++) {
				String tmp = "{" + i + "}";
				int index = result.indexOf(tmp);
				while (-1 < index) {
					result = result.substring(0, index) + params[i] + result.substring(index + tmp.length());
					index = result.indexOf(tmp, index + (null == params[i] ? 4 : params[i].length()));
				}
			}
		}
		return result;
	}

}
