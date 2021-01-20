package com.fib.gateway.mapping.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

/**
 * 映射规则：MB<->Map Map<->Map MB<->MB
 * 
 * @author 白帆
 * 
 */
public class MappingRule {
	public static final int MAP = 9000;
	public static final int MB = 9001;

	public static final String MAP_TXT = "Map";
	public static final String MB_TXT = "Message-Bean";

	public static int getClassTypeByText(String text) {
		if (MAP_TXT.equalsIgnoreCase(text)) {
			return MAP;
		} else if (MB_TXT.equalsIgnoreCase(text)) {
			return MB;
		} else {
			// throw new IllegalArgumentException(
			// "Unkown Source/Target Object Type[" + text + "]!");
			throw new IllegalArgumentException(MultiLanguageResourceBundle
					.getInstance().getString(
							"MappingRule.getClassTypeByText.unkownSource",
							new String[] { text }));
		}
	}

	public static String getClassTypeText(int classType) {
		if (classType == MAP) {
			return MAP_TXT;
		} else if (classType == MB) {
			return MB_TXT;
		} else {
			throw new IllegalArgumentException(MultiLanguageResourceBundle
					.getInstance().getString(
							"MappingRule.getClassTypeByText.unkownSource",
							new String[] { classType + "" }));
		}
	}

	/**
	 * id
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	// /**
	// * 源类型：Map/MessageBean
	// */
	// private int sourceType;

	/**
	 * 目的类型：Map/MessageBean
	 */
	private int targetType;

	/**
	 * 目的对象类名。当目的类型为MessageBean时有效。
	 */
	private String targetClass;

	// private boolean isRequest = true;
	/**
	 * 引用的映射规则文件名
	 */
	private List includes = new ArrayList();

	/**
	 * 引用的映射规则表 key:文件名 value:映射规则列表
	 */
	private Map includeRuleMap = new HashMap();
	/**
	 * 字段映射规则
	 */
	private List fieldMappingRules = new ArrayList();

	private List fieldMappingRulesWithoutInclude = new ArrayList();

	/**
	 * 手动映射脚本
	 */
	private List scripts = new ArrayList();

	public List getScripts() {
		return scripts;
	}

	public void setScripts(List scripts) {
		this.scripts = scripts;
	}

	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	public List getFieldMappingRules() {
		return fieldMappingRules;
	}

	public void setFieldMappingRules(List fieldMappingRules) {
		this.fieldMappingRules = fieldMappingRules;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List getIncludes() {
		return includes;
	}

	public void setIncludes(List includes) {
		this.includes = includes;
	}

	// public boolean isRequest() {
	// return isRequest;
	// }
	// public void setRequest(boolean isRequest) {
	// this.isRequest = isRequest;
	// }
	// public int getSourceType() {
	// return sourceType;
	// }
	//
	// public void setSourceType(int sourceType) {
	// this.sourceType = sourceType;
	// }

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map getIncludeRuleMap() {
		return includeRuleMap;
	}

	public void setIncludeRuleMap(Map includeRuleMap) {
		this.includeRuleMap = includeRuleMap;
	}

	public List getFieldMappingRulesWithoutInclude() {
		return fieldMappingRulesWithoutInclude;
	}

	public void setFieldMappingRulesWithoutInclude(
			List fieldMappingRulesWithoutInclude) {
		this.fieldMappingRulesWithoutInclude = fieldMappingRulesWithoutInclude;
	}

}
