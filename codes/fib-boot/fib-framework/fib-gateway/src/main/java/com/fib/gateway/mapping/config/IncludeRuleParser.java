package com.fib.gateway.mapping.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class IncludeRuleParser extends DefaultHandler {
	private List rules = new ArrayList();

	private String fileName = null;

	private File file = null;

	public List parse(File file) {
		this.file = file;
		fileName = file.getAbsolutePath();

		SAXParserFactory saxParserFactory = SAXParserFactory.newDefaultInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse(new FileInputStream(file), this);
		} catch (Exception e) {
			throw new CommonException("Failed to read file", e);
		}
		return rules;
	}

	String elementValue = null;
	FieldMappingRule fieldMapingRule = null;
	int position = 0;

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (null == elementValue) {
			elementValue = new String(ch, start, length);
		} else {
			elementValue += new String(ch, start, length);
		}
	}

	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		String attr = null;

		if ("mapping".equalsIgnoreCase(name)) {
			fieldMapingRule = new FieldMappingRule();
			// @type
			attr = attributes.getValue("type");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": message-bean-mapping[" + position
				// + "]/mapping/@type is NULL");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"config.null",
								new String[] {
										fileName,
										"message-bean-mapping[" + position
												+ "]/mapping/@type" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": message-bean-mapping/mapping[" + position
					// + "]/@type is Blank");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] {
											fileName,
											"message-bean-mapping[" + position
													+ "]/mapping/@type" }));
				}
			}
			fieldMapingRule
					.setType(FieldMappingRule.getMappingTypeByText(attr));

			// @translate-rule-id
			if (FieldMappingRule.VALUE_TRANSLATE_MAPPING == fieldMapingRule
					.getType()) {
				attr = attributes.getValue("translate-rule-id");
				if (null != attr) {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(
						// fileName
						// + ": message-bean-mapping/mapping["
						// + position
						// +
						// "]/@translate-rule-id is Blank,type=value-mapping-translate");
						throw new RuntimeException(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"MappingRuleManager.translateRuleId.blank",
												new String[] { fileName
														+ ": message-bean-mapping/mapping["
														+ position
														+ "]/@translate-rule-id" }));
					}
				}
				fieldMapingRule.setTranslateRuleId(attr);
			}

			if (FieldMappingRule.VALUE_TRANSLATE_MAPPING == fieldMapingRule
					.getType()
					&& fieldMapingRule.getTranslateRuleId() != null) {
				// 加载引用的值转换规则文件
				File vtrPath = new File(file.getParentFile(),
						"value-translate-rule");
				File vtrfile = new File(vtrPath, fieldMapingRule
						.getTranslateRuleId()
						+ ".xml");
				ValueTranslateRuleParser vParser = new ValueTranslateRuleParser();
				ValueTranslateRule vtRule = vParser.parse(vtrfile);
				if (null == vtRule) {
					// throw new RuntimeException(vtrfile
					// + "is not a value-translate-rule perhaps!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"MappingRuleManager.notTranslateRule",
									new String[] { vtrfile + "" }));
				}
				if (!fieldMapingRule.getTranslateRuleId()
						.equals(vtRule.getId())) {
					// throw new RuntimeException(vtrfile + "'s id["
					// + vtRule.getId() + "] != "
					// + fieldMapingRule.getTranslateRuleId());
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"MappingRuleManager.id.notEqual",
									new String[] {
											"" + vtrfile,
											vtRule.getId(),
											fieldMapingRule
													.getTranslateRuleId() }));
				}
				// 将值转换规则加入到字段规则中
				fieldMapingRule.setTranslateRules(vtRule
						.getTranslateRelations());
			}

			// @from
			if (FieldMappingRule.VALUE_MAPPING == fieldMapingRule.getType()
					|| FieldMappingRule.VALUE_TRANSLATE_MAPPING == fieldMapingRule
							.getType()) {
				// 当映射类型为值对值映射或数据值映射时，必须有来源属性
				attr = attributes.getValue("from");
				if (null == attr) {
					// throw new RuntimeException(fileName
					// + ": message-bean-mapping/mapping[" + position
					// + "]/@from is NULL,type=value-mapping");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"MappingRuleManager.from.null",
									new String[] { fileName
											+ ": message-bean-mapping/mapping["
											+ position + "]/@from" }));
				} else {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(fileName
						// + ": message-bean-mapping/mapping["
						// + position
						// + "]/@from is Blank,type=value-mapping");
						throw new RuntimeException(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"MappingRuleManager.from.blank",
												new String[] { fileName
														+ ": message-bean-mapping/mapping["
														+ position + "]/@from" }));
					}
				}
				fieldMapingRule.setFrom(attr);

				// @source-parameter
				attr = attributes.getValue("source-parameter");
				if (null != attr) {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(fileName
						// + ": message-bean-mapping/mapping["
						// + position
						// + "]/@source-parameter is Blank!");
						throw new RuntimeException(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"config.blank",
												new String[] {
														fileName,
														"message-bean-mapping/mapping["
																+ position
																+ "]/@source-parameter" }));
					}
					fieldMapingRule.setSourceParameter(attr);
				}
			}

			// @to
			attr = attributes.getValue("to");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": message-bean-mapping/mapping[" + position
				// + "]/@to is NULL");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"config.null",
								new String[] {
										fileName,
										"message-bean-mapping/mapping["
												+ position + "]/@to" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": message-bean-mapping/mapping[" + position
					// + "]/@to is Blank");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] {
											fileName,
											"message-bean-mapping/mapping["
													+ position + "]/@to" }));
				}
			}
			fieldMapingRule.setTo(attr);

			// @target-data-type
			attr = attributes.getValue("target-data-type");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": message-bean-mapping/mapping[" + position
					// + "]/@target-data-type is Blank");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] {
											fileName,
											"message-bean-mapping/mapping["
													+ position
													+ "]/@target-data-type" }));
				}
			}
			fieldMapingRule.setTargetDataType(attr);
			if (fieldMapingRule.getTargetDataType() != null) {
				fieldMapingRule.setTargetClass(FieldMappingRule
						.getClassByDataType(attr));
			}

			rules.add(fieldMapingRule);
		}
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if ("mapping".equalsIgnoreCase(name)) {
			if (FieldMappingRule.VALUE == fieldMapingRule.getType()
					|| FieldMappingRule.SCRIPT == fieldMapingRule.getType()) {
				if (null == elementValue) {
					// throw new RuntimeException(
					// fileName
					// + ": mapping/text() must be NOT NULL when Type is \""
					// + fieldMapingRule.getType() + "\"");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"MappingRuleManager.mapping.text.null",
									new String[] { fileName,
											fieldMapingRule.getType() + "" }));
				}

				fieldMapingRule.setValue(elementValue.trim());
			}
			position++;
		}
	}
}
