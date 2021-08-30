package com.fib.msgconverter.commgateway.util;

import java.util.HashSet;
import java.util.Set;

import com.giantstone.common.util.CodeUtil;

/**
 * 敏感信息过滤器
 * 
 */
public class SensitiveInfoFilter {
	private static final String KEY_PREFIX = "k=\"";
	private static final String TYPE_PREFIX = "t=\"";
	private static final String VALUE_PREFIX = ">";
	private static final String KEY_TYPE_SUFFIX = "\"";
	private static final String VALUE_SUFFIX = "<";
	private static final String SEPARATOR = ",";

	/**
	 * 过滤指定格式字符串中的敏感信息
	 * 
	 * @param xml
	 *            字符串
	 * @param sensitiveFields
	 *            敏感域
	 * @param value4Show
	 *            用于替换的字符
	 * @return 过滤后的字符串
	 */
	public static String filtSensitiveInfo(String xml, String sensitiveFields,
			char value4Show) {
		int keyStartIndex = 0;
		int keyEndIndex = 0;
		int typeStartIndex = 0;
		int typeEndIndex = 0;
		int valueStartIndex = 0;
		int valueEndIndex = 0;

		Set fieldsSet = new HashSet();
		String[] fields = sensitiveFields.split(SEPARATOR);
		for (int i = 0; i < fields.length; i++) {
			fieldsSet.add(fields[i].trim());
		}

		String hexValue4Show = new String(CodeUtil.BytetoHex((byte) value4Show));
		StringBuffer buf = new StringBuffer();
		while (true) {
			keyStartIndex = xml.indexOf(KEY_PREFIX, typeEndIndex
					+ KEY_TYPE_SUFFIX.length());
			if (-1 == keyStartIndex) {
				buf.append(xml, valueEndIndex, xml.length());
				break;
			}

			int keyFrom = keyStartIndex + KEY_PREFIX.length();
			keyEndIndex = xml.indexOf(KEY_TYPE_SUFFIX, keyFrom);

			String key = xml.substring(keyFrom, keyEndIndex);

			typeStartIndex = xml.indexOf(TYPE_PREFIX, keyEndIndex);
			if (xml.indexOf(VALUE_PREFIX, keyEndIndex) < typeStartIndex
					|| -1 == typeStartIndex) {
				typeEndIndex = keyEndIndex + KEY_TYPE_SUFFIX.length();
				continue;
			}
			int typeFrom = typeStartIndex + TYPE_PREFIX.length();
			typeEndIndex = xml.indexOf(KEY_TYPE_SUFFIX, typeFrom);

			if (!fieldsSet.contains(key)) {
				continue;
			}

			String type = xml.substring(typeFrom, typeEndIndex);
			if ("S".equals(type)) {
				// 字符串
				valueStartIndex = xml.indexOf(VALUE_PREFIX, typeEndIndex);
				buf.append(xml, valueEndIndex, valueStartIndex + 1);
				if ('/' == xml.charAt(valueStartIndex - 1)) {
					valueEndIndex = valueStartIndex + 1;
				} else {
					valueEndIndex = xml.indexOf(VALUE_SUFFIX, valueStartIndex);
					String value = xml.substring(valueStartIndex
							+ VALUE_PREFIX.length(), valueEndIndex);
					for (int i = 0; i < value.length(); i++) {
						buf.append(value4Show);
					}
				}
			} else if ("b".equals(type)) {
				// 单字节
				valueStartIndex = xml.indexOf(VALUE_PREFIX, typeEndIndex);
				buf.append(xml, valueEndIndex, valueStartIndex + 1);
				if ('/' == xml.charAt(valueStartIndex - 1)) {
					valueEndIndex = valueStartIndex + 1;
				} else {
					buf.append(hexValue4Show);
				}
			} else if ("B".equals(type)) {
				// 字节数组
				valueStartIndex = xml.indexOf(VALUE_PREFIX, typeEndIndex);
				buf.append(xml, valueEndIndex, valueStartIndex + 1);
				if ('/' == xml.charAt(valueStartIndex - 1)) {
					valueEndIndex = valueStartIndex + 1;
				} else {
					valueEndIndex = xml.indexOf(VALUE_SUFFIX, valueStartIndex);
					String value = xml.substring(valueStartIndex
							+ VALUE_PREFIX.length(), valueEndIndex);
					for (int i = 0; i < value.length(); i += 2) {
						buf.append(hexValue4Show);
					}
				}
			} else if ("SS".equals(type)) {
				// 字符串数组
				valueStartIndex = xml.indexOf(VALUE_PREFIX, typeEndIndex);
				buf.append(xml, valueEndIndex, valueStartIndex + 1);
				if ('/' == xml.charAt(valueStartIndex - 1)) {
					valueEndIndex = valueStartIndex + 1;
				} else {
					valueEndIndex = xml.indexOf(VALUE_SUFFIX, valueStartIndex);
					String value = xml.substring(valueStartIndex
							+ VALUE_PREFIX.length(), valueEndIndex);

					String[] vals = value.split(SEPARATOR);
					for (int i = 0; i < vals.length; i++) {
						for (int j = 0; j < vals[i].length(); j++) {
							buf.append(value4Show);
						}
						if (i != (vals.length - 1)) {
							buf.append(SEPARATOR);
						}
					}
				}
			} else {
				continue;
			}

		}

		return buf.toString();
	}

}
