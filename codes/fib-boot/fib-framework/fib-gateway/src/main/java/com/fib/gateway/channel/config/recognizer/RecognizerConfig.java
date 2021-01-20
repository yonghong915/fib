package com.fib.gateway.channel.config.recognizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.fib.gateway.config.base.TypedDynamicObjectConfig;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class RecognizerConfig extends TypedDynamicObjectConfig {

	private static Properties prop;

	static {
		String propFileName = "recognizerType.properties";
		InputStream in = RecognizerConfig.class.getResourceAsStream(propFileName);
		if (null == in) {
			// throw new RuntimeException("Can't load " + propFileName);
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("propery.canNotLoadFile",
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

	/**
	 * ID
	 */
	private String id;

	/**
	 * 内嵌识别器组件配置的列表
	 */
	private List<RecognizerConfig> componentList = null;

	/**
	 * 默认值
	 */
	private String defaultValue = null;

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the componentList
	 */
	public List<RecognizerConfig> getComponentList() {
		return componentList;
	}

	/**
	 * @param componentList the componentList to set
	 */
	public void setComponentList(List componentList) {
		this.componentList = componentList;
	}

	protected Properties getProperties() {
		return prop;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}
