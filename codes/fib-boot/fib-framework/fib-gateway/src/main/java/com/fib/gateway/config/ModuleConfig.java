
package com.fib.gateway.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fib.gateway.config.base.TypedDynamicObjectConfig;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;


public class ModuleConfig extends TypedDynamicObjectConfig {
	private static Properties prop;

	static {
		String propFileName = "moduleType.properties";
		InputStream in = ModuleConfig.class.getResourceAsStream(propFileName);
		if (null == in) {
			// throw new RuntimeException("Can't load " + propFileName);
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("propery.canNotLoadFile",
							new String[] { propFileName }));
		}
		prop = new Properties();
		try {
			prop.load(in);
		} catch (IOException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
	}

	protected Properties getProperties() {
		return prop;
	}

}
