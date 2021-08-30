/**
 * 北京长信通信息技术有限公司
 * 2008-11-22 上午09:41:24
 */
package com.fib.msgconverter.commgateway.config.base;

import java.util.Properties;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

/**
 * 带类型的动态对象配置。
 * 
 * 为避免配置时填写较长的类名，提供了一个type属性，其值较短，可以自定义，一个type值为唯一对应一个类名。
 * type与类名的映射关系配置在properties文件中。
 * 
 * @author 刘恭亮
 * 
 */
public abstract class TypedDynamicObjectConfig extends DynamicObjectConfig {
	public static final String USER_DEFINED = "USER_DEFINED";

	protected String type;

	/**
	 * 取得type与类名的映射关系。由子类实现。
	 * 
	 * @return
	 */
	protected abstract Properties getProperties();

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;

		if (type != null && !USER_DEFINED.equalsIgnoreCase(type)) {
			String className = getProperties().getProperty(type);
			if (null == className) {
				// throw new RuntimeException("Undefined type [" + type + "]");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("type.unsupport",
								new String[] { type }));
			}
			setClassName(className);
		}
	}

}
