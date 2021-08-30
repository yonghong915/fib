package com.fib.msgconverter.commgateway.mapping.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 值转换规则：如币种的值转换
 * 
 * @author 白帆
 * 
 */
public class ValueTranslateRule {
	/**
	 * id
	 */
	private String id = null;

	/**
	 * 转换关系
	 */
	private Map translateRelations = new HashMap(32);

	/**
	 * 名称
	 */
	private String name = null;

	/**
	 * 默认值
	 */
	private String defaultValue = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map getTranslateRelations() {
		return translateRelations;
	}

	public void setTranslateRelations(Map translateRelations) {
		this.translateRelations = translateRelations;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
