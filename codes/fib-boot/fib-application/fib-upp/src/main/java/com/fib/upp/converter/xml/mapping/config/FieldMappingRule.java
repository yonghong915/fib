package com.fib.upp.converter.xml.mapping.config;

import java.util.Map;

import lombok.Data;

@Data
public class FieldMappingRule {
	/**
	 * 映射类型：值映射
	 */
	public static final int VALUE_MAPPING = 1000;

	/**
	 * 映射类型：直接给目标赋值
	 */
	public static final int VALUE = 1001;

	/**
	 * 映射类型：值转换
	 */
	public static final int VALUE_TRANSLATE_MAPPING = 1002;

	/**
	 * 映射类型：脚本
	 */
	public static final int SCRIPT = 1003;

	public static String getMappingTypeText(int mappingType) {
		switch (mappingType) {
		case VALUE_MAPPING:
			return "value-mapping";
		case VALUE:
			return "value";
		case VALUE_TRANSLATE_MAPPING:
			return "value-translate";
		case SCRIPT:
			return "script";
		default:
			throw new IllegalArgumentException("Unkown Mapping Type[" + mappingType + "]!");
//			throw new IllegalArgumentException(MultiLanguageResourceBundle
//					.getInstance().getString("type.unsupport",
//							new String[] { "" + mappingType }));
		}
	}

	public static int getMappingTypeByText(String text) {
		if ("value-mapping".equalsIgnoreCase(text)) {
			return VALUE_MAPPING;
		} else if ("value".equalsIgnoreCase(text)) {
			return VALUE;
		} else if ("value-translate".equalsIgnoreCase(text)) {
			return VALUE_TRANSLATE_MAPPING;
		} else if ("script".equalsIgnoreCase(text)) {
			return SCRIPT;
		} else {
			throw new IllegalArgumentException("Unkown Mapping Type[" + text + "]!");
//			throw new IllegalArgumentException(MultiLanguageResourceBundle
//					.getInstance().getString("type.unsupport",
//							new String[] { text }));
		}
	}

	private static byte[] byteArray = new byte[0];

	public static Class<?> getClassByDataType(String dataType) {
		if ("string".equalsIgnoreCase(dataType)) {
			return java.lang.String.class;
		} else if ("int".equalsIgnoreCase(dataType) || "integer".equalsIgnoreCase(dataType)) {
			return java.lang.Integer.class;
		} else if ("long".equalsIgnoreCase(dataType)) {
			return java.lang.Long.class;
		} else if ("short".equalsIgnoreCase(dataType)) {
			return java.lang.Short.class;
		} else if ("byte".equalsIgnoreCase(dataType)) {
			return java.lang.Byte.class;
		} else if ("byte[]".equalsIgnoreCase(dataType)) {
			return byteArray.getClass();
		} else if ("float".equalsIgnoreCase(dataType)) {
			return java.lang.Float.class;
		} else if ("double".equalsIgnoreCase(dataType)) {
			return java.lang.Double.class;
		} else {
			throw new RuntimeException("Unsupport dataType:" + dataType);
//			throw new IllegalArgumentException(MultiLanguageResourceBundle
//					.getInstance().getString("type.unsupport",
//							new String[] { dataType }));
		}
	}

	/**
	 * 类型：值映射、值转换、赋值、脚本
	 */
	private int type;

	/**
	 * 源字段路径
	 */
	private String from;

	/**
	 * 目的字段路径
	 */
	private String to;

	/**
	 * 目标字段数据类型：当目标对象为Map时才有效，可选，默认不转数据类型
	 */
	private String targetDataType = null;

	/**
	 * 目标字段类：当目标对象为Map时才有效，可选，默认不转数据类型
	 */
	private Class<?> targetClass = null;

	/**
	 * 强制类型转换，例如 long->int, double->int
	 */
	private boolean forcibleTypeConversion = false;

	/**
	 * 值或脚本
	 */
	private String value;

	/**
	 * 引用的值转换规则id
	 */
	private String translateRuleId;

	/**
	 * 值转换规则
	 */
	private Map<String,Object> translateRules = null;

	/**
	 * 映射数据来源
	 */
	private String sourceParameter = null;

	/**
	 * 默认值
	 */
	private String defaultValue = null;

	public String toString() {
		StringBuffer buf = new StringBuffer(128);
		// buf.append("FieldMappingRule[type=");
		// buf.append(getMappingTypeText(getType()));
		// buf.append(",from=");
		// buf.append(getFrom());
		// buf.append(",to=");
		// buf.append(getTo());
		// if (getTargetDataType() != null) {
		// buf.append(",targetType=");
		// buf.append(getTargetDataType());
		// }
		// if (isForcibleTypeConversion()) {
		// buf.append(",forceTypeConversion");
		// }
		// buf.append("]");
//		buf.append(MultiLanguageResourceBundle.getInstance().getString(
//				"FieldMappingRule.toString.1",
//				new String[] { getMappingTypeText(getType()), getFrom(),
//						getTo() }));
//		if (getTargetDataType() != null) {
//			buf.append(MultiLanguageResourceBundle.getInstance().getString(
//					"FieldMappingRule.toString.2",
//					new String[] { getTargetDataType() }));
//		}
//
//		if (isForcibleTypeConversion()) {
//			buf.append(MultiLanguageResourceBundle.getInstance().getString(
//					"FieldMappingRule.toString.3"));
//		}
		buf.append("]");
		return buf.toString();
	}

	public static byte[] getByteArray() {
		return byteArray;
	}

	public static void setByteArray(byte[] byteArray) {
		FieldMappingRule.byteArray = byteArray;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTargetDataType() {
		return targetDataType;
	}

	public void setTargetDataType(String targetDataType) {
		this.targetDataType = targetDataType;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public boolean isForcibleTypeConversion() {
		return forcibleTypeConversion;
	}

	public void setForcibleTypeConversion(boolean forcibleTypeConversion) {
		this.forcibleTypeConversion = forcibleTypeConversion;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTranslateRuleId() {
		return translateRuleId;
	}

	public void setTranslateRuleId(String translateRuleId) {
		this.translateRuleId = translateRuleId;
	}

	public Map<String, Object> getTranslateRules() {
		return translateRules;
	}

	public void setTranslateRules(Map<String, Object> translateRules) {
		this.translateRules = translateRules;
	}

	public String getSourceParameter() {
		return sourceParameter;
	}

	public void setSourceParameter(String sourceParameter) {
		this.sourceParameter = sourceParameter;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	
}
