package com.fib.gateway.mapping.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.util.ConfigManager;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

/**
 * MB映射元数据管理器
 * 
 * 
 * 
 */
public class MappingRuleManager {
	private static Map mappingRuleGroup = new HashMap(64);

	public static final String DB_TRUE = "0";
	public static final String DB_FALSE = "1";

	public static void clear() {
		mappingRuleGroup.clear();
	}

	/**
	 * 解析path路径下所有以.xml结尾的文件，加载MappingRule配置
	 * 
	 * @param path
	 */
	public static void loadMappingConfg(String groupId, String path) {
		if (null == path) {
			// throw new IllegalArgumentException("path is null");
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("inputParameter.null", new String[] { "path" }));
		}

		File dir = new File(ConfigManager.getFullPathFileName(path));
		loadMappingConfig(groupId, dir);
	}

	// public static void reloadMappingConfg(String groupId, String path) {
	// if (null == path) {
	// // throw new IllegalArgumentException("path is null");
	// throw new IllegalArgumentException(MultiLanguageResourceBundle
	// .getInstance().getString("inputParameter.null",
	// new String[] { "path" }));
	// }
	//
	// File dir = new File(ConfigManager.getFullPathFileName(path));
	// reloadMappingConfig(groupId, dir);
	// }

	// public static void reloadMappingConfig(String groupId, File dir) {
	// if (!dir.isDirectory()) {
	// // throw new IllegalArgumentException("path[" +
	// // dir.getAbsolutePath()
	// // + "] must be a directory!");
	// throw new IllegalArgumentException(
	// MultiLanguageResourceBundle
	// .getInstance()
	// .getString(
	// "MappingRuleManager.loadMappingConfig.path.notDirectory",
	// new String[] { dir.getAbsolutePath() }));
	// }
	// if (!dir.canRead()) {
	// // throw new IllegalArgumentException("path[" +
	// // dir.getAbsolutePath()
	// // + "] can not be read!");
	// throw new IllegalArgumentException(
	// MultiLanguageResourceBundle
	// .getInstance()
	// .getString(
	// "MappingRuleManager.loadMappingConfig.path.canNotRead",
	// new String[] { dir.getAbsolutePath() }));
	// }
	// mappingRuleGroup.remove(groupId);
	// loadMappingConfig(groupId, dir);
	// }

	/**
	 * 解析dir路径下所有以.xml结尾的文件，加载MappingRule配置
	 * 
	 * @param dir
	 */
	public static void loadMappingConfig(String groupId, File dir) {
		if (!dir.isDirectory()) {
			// throw new IllegalArgumentException("path[" +
			// dir.getAbsolutePath()
			// + "] must be a directory!");
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString(
					"MappingRuleManager.loadMappingConfig.path.notDirectory", new String[] { dir.getAbsolutePath() }));
		}
		if (!dir.canRead()) {
			// throw new IllegalArgumentException("path[" +
			// dir.getAbsolutePath()
			// + "] can not be read!");
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString(
					"MappingRuleManager.loadMappingConfig.path.canNotRead", new String[] { dir.getAbsolutePath() }));
		}

		// Map mappingRules = (Map) mappingRuleGroup.get(groupId);
		// 外部需要重新加载,因此修改为每次加载都将之前的清空
		// if (null == mappingRules) {
		// mappingRules = new HashMap(1024);
		// mappingRuleGroup.put(groupId, mappingRules);
		// }
		Map mappingRules = new HashMap(1024);
		mappingRuleGroup.put(groupId, mappingRules);

		File[] allFile = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.endsWith(".xml")) {
					return true;
				}
				return false;
			}
		});

		for (int i = 0; i < allFile.length; i++) {
			// 1. 解析映射规则
			MappingRule rule = null;
			try {
				rule = load(allFile[i]);
			} catch (Exception e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"CommGateway.loadChannel.loadMapping.failed",
						new String[] { groupId, allFile[i].getAbsolutePath(), e.getMessage() }), e);
			}
			List includeFiles = rule.getIncludes();
			if (null != includeFiles) {
				String path = dir.getAbsolutePath();
				if (!path.endsWith(System.getProperty("file.separator"))) {
					path += System.getProperty("file.separator");
				}

				File includeFile = null;
				for (int j = 0; j < includeFiles.size(); j++) {
					List includeRule = loadIncludes(new File(path + includeFiles.get(j)));
					rule.getFieldMappingRules().addAll(includeRule);
					rule.getIncludeRuleMap().put(includeFiles.get(j), includeRule);
				}
			}
			if (mappingRules.containsKey(rule.getId())) {
				// throw new RuntimeException(allFile[i].getAbsolutePath()
				// + ": Mapping Rule Id is reduplicated! Id="
				// + rule.getId());
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"MappingRuleManager.loadMappingConfig.id.reduplicate",
						new String[] { allFile[i].getAbsolutePath(), rule.getId() }));
			}
			mappingRules.put(rule.getId(), rule);
		}
	}

	/**
	 * 取得映射规则
	 * 
	 * @param groupId
	 * @param mappingRuleId
	 * @return
	 */
	public static MappingRule getMappingRule(String groupId, String mappingRuleId) {
		if (null == groupId) {
			// throw new IllegalArgumentException("groupId is NULL");
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("inputParameter.null", new String[] { "groupId" }));
		}
		if (null == mappingRuleId) {
			// throw new IllegalArgumentException("mappingRuleId is NULL");
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("inputParameter.null", new String[] { "mappingRuleId" }));
		}

		Map mappingRules = (Map) mappingRuleGroup.get(groupId);
		if (null == mappingRules) {
			// throw new IllegalArgumentException("group[" + groupId
			// + "] is null! please load it first!");
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingRuleManager.getMappingRule.group.null", new String[] { groupId }));
		}

		return (MappingRule) mappingRules.get(mappingRuleId);
	}

	public static Map getAllMappingRule() {
		return mappingRuleGroup;
	}

	private static MappingRule load(File file) {
		MappingRuleParser parser = new MappingRuleParser();
		return parser.parse(file);
	}

	// private static MappingRule load(String fileName) {
	// return load(new File(ConfigManager.getFullPathFileName(fileName)));
	// }

	private static List loadIncludes(File file) {
		IncludeRuleParser parser = new IncludeRuleParser();
		return parser.parse(file);
	}

	private static class MappingRuleParser extends DefaultHandler {

		private MappingRule rule = null;

		private String fileName = null;

		private File file = null;

		private MappingRule parse(File file) {
			this.file = file;
			fileName = file.getAbsolutePath();

			SAXParserFactory saxParserFactory = SAXParserFactory.newDefaultInstance();
			try {
				SAXParser saxParser = saxParserFactory.newSAXParser();
				saxParser.parse(new FileInputStream(file), this);
			} catch (Exception e) {
				throw new CommonException("Failed to read file", e);
			}
			// 解析后检查
			checkConfig();
			return rule;
		}

		String elementValue = null;
		FieldMappingRule fieldMapingRule = null;
		boolean isInclude = false;
		int position = 0;

		public void characters(char[] ch, int start, int length) throws SAXException {
			if (null == elementValue) {
				elementValue = new String(ch, start, length);
			} else {
				elementValue += new String(ch, start, length);
			}
		}

		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
			String attr = null;
			if ("message-bean-mapping".equalsIgnoreCase(name)) {
				rule = new MappingRule();
				// @id
				attr = attributes.getValue("id");
				if (null == attr) {
					// throw new RuntimeException(fileName
					// + ": message-bean-mapping/@id is NULL");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
							new String[] { fileName, "message-bean-mapping/@id" }));
				} else {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(fileName
						// + ": message-bean-mapping/@id is Blank");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
								new String[] { fileName, "message-bean-mapping/@id" }));
					}
				}
				rule.setId(attr);

				// @name
				attr = attributes.getValue("name");
				if (attr != null) {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(fileName
						// + ": message-bean-mapping/@name is Blank");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
								new String[] { fileName, "message-bean-mapping/@name" }));
					}
					rule.setName(attr);
				}

				// //@is-request-bean
				// attr = attributes.getValue("is-request-bean");
				// if(null != attr){
				// if(!"false".equals(attr) && !"true".equals(attr)){
				// throw new RuntimeException(this.fileName
				// + ": message-bean-mapping/@is-request-bean must be \"true\"
				// or \"false\"");
				// }
				// if("false".equals(attr)){
				// rule.setRequest(false);
				// }
				// }

				// @target-type
				attr = attributes.getValue("target-type");
				if (null == attr) {
					// throw new RuntimeException(fileName
					// + ": message-bean-mapping/@target-type is NULL");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
							new String[] { fileName, "message-bean-mapping/@target-type" }));
				} else {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(
						// fileName
						// + ": message-bean-mapping/@target-type is Blank");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
								new String[] { fileName, "message-bean-mapping/@target-type" }));
					}
				}
				rule.setTargetType(MappingRule.getClassTypeByText(attr));

				if (MappingRule.MB == rule.getTargetType()) {
					// @target-bean-class
					attr = attributes.getValue("target-bean-class");
					if (null == attr) {
						// throw new RuntimeException(
						// fileName
						// +
						// ": message-bean-mapping/@target-bean-class is NULL");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
								new String[] { fileName, "message-bean-mapping/@target-bean-class" }));
					} else {
						attr = attr.trim();
						if (0 == attr.length()) {
							// throw new RuntimeException(
							// fileName
							// +
							// ": message-bean-mapping/@target-bean-class is Blank");
							throw new RuntimeException(
									MultiLanguageResourceBundle.getInstance().getString("config.blank",
											new String[] { fileName, "message-bean-mapping/@target-bean-class" }));
						}
					}
					rule.setTargetClass(attr);
				}

				// // @source-type
				// attr = attributes.getValue("source-type");
				// if (null == attr) {
				// throw new RuntimeException(this.fileName
				// + ": message-bean-mapping/@source-type is NULL");
				// } else {
				// attr = attr.trim();
				// if (0 == attr.length()) {
				// throw new RuntimeException(
				// this.fileName
				// + ": message-bean-mapping/@source-type is Blank");
				// }
				// }
				// rule.setSourceType(MappingRule.getClassTypeByText(attr));

			} else if ("mapping".equalsIgnoreCase(name)) {
				isInclude = false;
				fieldMapingRule = new FieldMappingRule();
				// @type
				attr = attributes.getValue("type");
				if (null == attr) {
					// throw new RuntimeException(fileName
					// + ": message-bean-mapping[" + position
					// + "]/mapping/@type is NULL");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
							new String[] { fileName, "message-bean-mapping[" + position + "]/mapping/@type" }));
				} else {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(fileName
						// + ": message-bean-mapping/mapping[" + position
						// + "]/@type is Blank");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
								new String[] { fileName, "message-bean-mapping[" + position + "]/mapping/@type" }));
					}
				}
				fieldMapingRule.setType(FieldMappingRule.getMappingTypeByText(attr));

				// @translate-rule-id
				if (FieldMappingRule.VALUE_TRANSLATE_MAPPING == fieldMapingRule.getType()) {
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
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
									.getString("MappingRuleManager.translateRuleId.blank", new String[] { fileName
											+ ": message-bean-mapping/mapping[" + position + "]/@translate-rule-id" }));
						}
					}
					fieldMapingRule.setTranslateRuleId(attr);
				}

				if (FieldMappingRule.VALUE_TRANSLATE_MAPPING == fieldMapingRule.getType()
						&& fieldMapingRule.getTranslateRuleId() != null) {
					// 加载引用的值转换规则文件
					File vtrPath = new File(file.getParentFile(), "value-translate-rule");
					File vtrfile = new File(vtrPath, fieldMapingRule.getTranslateRuleId() + ".xml");
					ValueTranslateRuleParser vParser = new ValueTranslateRuleParser();
					ValueTranslateRule vtRule = vParser.parse(vtrfile);
					if (null == vtRule) {
						// throw new RuntimeException(vtrfile
						// + "is not a value-translate-rule perhaps!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("MappingRuleManager.notTranslateRule", new String[] { "" + vtrfile }));
					}
					if (!fieldMapingRule.getTranslateRuleId().equals(vtRule.getId())) {
						// throw new RuntimeException(vtrfile + "'s id["
						// + vtRule.getId() + "] != "
						// + fieldMapingRule.getTranslateRuleId());
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"MappingRuleManager.id.notEqual",
								new String[] { "" + vtrfile, vtRule.getId(), fieldMapingRule.getTranslateRuleId() }));
					}
					// 将值转换规则加入到字段规则中
					fieldMapingRule.setTranslateRules(vtRule.getTranslateRelations());
					fieldMapingRule.setDefaultValue(vtRule.getDefaultValue());
				}

				// @from
				if (FieldMappingRule.VALUE_MAPPING == fieldMapingRule.getType()
						|| FieldMappingRule.VALUE_TRANSLATE_MAPPING == fieldMapingRule.getType()) {
					// 当映射类型为值对值映射或数据值映射时，必须有来源属性
					attr = attributes.getValue("from");
					if (null == attr) {
						// throw new RuntimeException(fileName
						// + ": message-bean-mapping/mapping[" + position
						// + "]/@from is NULL,type=value-mapping");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"MappingRuleManager.from.null",
								new String[] { fileName + ": message-bean-mapping/mapping[" + position + "]/@from" }));
					} else {
						attr = attr.trim();
						if (0 == attr.length()) {
							// throw new RuntimeException(fileName
							// + ": message-bean-mapping/mapping["
							// + position
							// + "]/@from is Blank,type=value-mapping");
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
									.getString("MappingRuleManager.from.blank", new String[] {
											fileName + ": message-bean-mapping/mapping[" + position + "]/@from" }));
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
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
									.getString("config.blank", new String[] { fileName,
											"message-bean-mapping/mapping[" + position + "]/@source-parameter" }));
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
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
							new String[] { fileName, "message-bean-mapping/mapping[" + position + "]/@to" }));
				} else {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(fileName
						// + ": message-bean-mapping/mapping[" + position
						// + "]/@to is Blank");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
								new String[] { fileName, "message-bean-mapping/mapping[" + position + "]/@to" }));
					}
				}
				fieldMapingRule.setTo(attr);

				// @target-data-type
				// 只有目标对象为Map时，才有必要设置目标字段的数据类型
				if (rule.getTargetType() == MappingRule.MAP) {
					attr = attributes.getValue("target-data-type");
					if (null != attr) {
						attr = attr.trim();
						if (0 == attr.length()) {
							// throw new RuntimeException(fileName
							// + ": message-bean-mapping/mapping["
							// + position + "]/@target-data-type is Blank");
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
									.getString("config.blank", new String[] { fileName,
											"message-bean-mapping/mapping[" + position + "]/@target-data-type" }));
						}
					}
					fieldMapingRule.setTargetDataType(attr);
					if (fieldMapingRule.getTargetDataType() != null) {
						fieldMapingRule.setTargetClass(FieldMappingRule.getClassByDataType(attr));
					}
				}

				// @force-type-conversion
				attr = attributes.getValue("force-type-conversion");
				if (null != attr) {
					if ("true".equals(attr.trim())) {
						fieldMapingRule.setForcibleTypeConversion(true);
					}
				}

				rule.getFieldMappingRules().add(fieldMapingRule);
				rule.getFieldMappingRulesWithoutInclude().add(fieldMapingRule);
			} else if ("include".equals(name)) {
				isInclude = true;
			} else if ("value".equals(name)) {
				if (null != fieldMapingRule.getTranslateRuleId()) {
					// throw new
					// RuntimeException("Translate Rule Id is NOT NULL!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("MappingRuleManager.translateRuleId.notNull"));
				}
				if (fieldMapingRule.getTranslateRules() == null) {
					fieldMapingRule.setTranslateRules(new HashMap());
				}

				// @is-default
				attr = attributes.getValue("is-default");
				if (null != attr && "true".equals(attr.trim())) {
					// @value
					attr = attributes.getValue("value");
					if (null == attr) {
						throw new RuntimeException(
								MultiLanguageResourceBundle.getInstance().getString("config.null", new String[] {
										fileName, "message-bean-mapping/mapping[" + position + "]/value/@value" }));
					} else {
						attr = attr.trim();
						if (0 == attr.length()) {
							throw new RuntimeException(
									MultiLanguageResourceBundle.getInstance().getString("config.blank", new String[] {
											fileName, "message-bean-mapping/mapping[" + position + "]/value/@value" }));
						} else {
							fieldMapingRule.setDefaultValue(attr);
						}
					}
				} else {

					// @from
					String from = attributes.getValue("from");
					if (null == from) {
						// throw new RuntimeException(fileName
						// + ": message-bean-mapping/mapping[" + position
						// + "]/@from is NULL!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
								new String[] { fileName, "message-bean-mapping/mapping[" + position + "]/@from" }));
					} else {
						from = from.trim();
						if (0 == from.length()) {
							// throw new RuntimeException(fileName
							// + ": message-bean-mapping/mapping[" + position
							// + "]/@from is Blank!");
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
									"config.blank",
									new String[] { fileName, "message-bean-mapping/mapping[" + position + "]/@from" }));
						}
					}

					// @to
					String to = attributes.getValue("to");
					if (null == to) {
						// throw new RuntimeException(fileName
						// + ": message-bean-mapping/mapping[" + position
						// + "]/@to is NULL!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
								new String[] { fileName, "message-bean-mapping/mapping[" + position + "]/@to" }));
					} else {
						to = to.trim();
						if (0 == to.length()) {
							// throw new RuntimeException(fileName
							// + ": message-bean-mapping/mapping[" + position
							// + "]/@to is Blank!");
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
									"config.blank",
									new String[] { fileName, "message-bean-mapping/mapping[" + position + "]/@to" }));
						}
					}

					fieldMapingRule.getTranslateRules().put(from, to);
				}
			}
		}

		public void endElement(String uri, String localName, String name) throws SAXException {
			if ("mapping".equalsIgnoreCase(name)) {
				if (FieldMappingRule.VALUE == fieldMapingRule.getType()
						|| FieldMappingRule.SCRIPT == fieldMapingRule.getType()) {
					if (null == elementValue) {
						// throw new RuntimeException(
						// fileName
						// + ": mapping/text() must be NOT NULL when Type is \""
						// + fieldMapingRule.getType() + "\"");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"MappingRuleManager.mapping.text.null",
								new String[] { fileName, fieldMapingRule.getType() + "" }));
					}
				}
				if (!isInclude && null != elementValue) {
					fieldMapingRule.setValue(elementValue.trim());
				}
				position++;
			} else if ("include".equals(name)) {
				if (null == elementValue) {
					// throw new RuntimeException(fileName
					// + ": include/text() must be NOT NULL");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
							new String[] { fileName, "include/text()" }));
				}
				if (isInclude) {
					rule.getIncludes().add(elementValue.trim());
				}
			} else if ("manual-mapping".equals(name)) {
				String script = elementValue;
				if (null == script) {
					// throw new RuntimeException(fileName
					// + ": manual-mapping/text() is Null!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
							new String[] { fileName, "manual-mapping/text()" }));
				} else {
					script = script.trim();
					if (0 == script.length()) {
						// throw new RuntimeException(fileName
						// + ": manual-mapping/text() is Blank!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
								new String[] { fileName, "manual-mapping/text()" }));
					}
				}
				rule.getScripts().add(script);
			}
			elementValue = null;
		}

		private void checkConfig() {

		}
	}

}
