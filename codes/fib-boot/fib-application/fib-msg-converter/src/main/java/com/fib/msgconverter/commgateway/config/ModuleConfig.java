/**
 * 北京长信通信息技术有限公司
 * 2008-11-22 上午09:25:04
 */
package com.fib.msgconverter.commgateway.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fib.msgconverter.commgateway.config.base.TypedDynamicObjectConfig;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 组件配置
 * 
 * @author 刘恭亮
 * 
 */
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
