package com.fib.msgconverter.commgateway.mapping.config;

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

import com.fib.msgconverter.commgateway.dao.fieldmappingrule.dao.FieldMappingRuleDAO;
import com.fib.msgconverter.commgateway.dao.mapping.dao.Mapping;
import com.fib.msgconverter.commgateway.dao.mapping.dao.MappingDAO;
import com.fib.msgconverter.commgateway.dao.mappingfieldrelation.dao.MappingFieldRelation;
import com.fib.msgconverter.commgateway.dao.mappingfieldrelation.dao.MappingFieldRelationDAO;
import com.fib.msgconverter.commgateway.dao.mappingscriptrelation.dao.MappingScriptRelation;
import com.fib.msgconverter.commgateway.dao.mappingscriptrelation.dao.MappingScriptRelationDAO;
import com.fib.msgconverter.commgateway.dao.rulescript.dao.RuleScript;
import com.fib.msgconverter.commgateway.dao.rulescript.dao.RuleScriptDAO;
import com.fib.msgconverter.commgateway.dao.rulevalue.dao.RuleValue;
import com.fib.msgconverter.commgateway.dao.rulevalue.dao.RuleValueDAO;
import com.fib.msgconverter.commgateway.dao.rulevaluemapping.dao.RuleValueMapping;
import com.fib.msgconverter.commgateway.dao.rulevaluemapping.dao.RuleValueMappingDAO;
import com.fib.msgconverter.commgateway.dao.rulevaluetranslatemapping.dao.RuleValueTranslateMapping;
import com.fib.msgconverter.commgateway.dao.rulevaluetranslatemapping.dao.RuleValueTranslateMappingDAO;
import com.fib.msgconverter.commgateway.dao.script.dao.Script;
import com.fib.msgconverter.commgateway.dao.script.dao.ScriptDAO;
import com.fib.msgconverter.commgateway.dao.valuetranslaterule.dao.ValueTranslateRuleDAO;
import com.fib.msgconverter.commgateway.dao.valuetranslaterulegroup.dao.ValueTranslateRuleGroup;
import com.fib.msgconverter.commgateway.dao.valuetranslaterulegroup.dao.ValueTranslateRuleGroupDAO;
import com.fib.msgconverter.commgateway.dao.valuetranslaterulerelation.dao.ValueTranslateRuleRelation;
import com.fib.msgconverter.commgateway.dao.valuetranslaterulerelation.dao.ValueTranslateRuleRelationDAO;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.config.ConfigManager;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ExceptionUtil;

/**
 * MB映射元数据管理器
 * 
 * @author 白帆
 * 
 */
public class MappingRuleManager {
	private static Map mappingRuleGroup = new HashMap(64);

	public static final String DB_TRUE = "0";
	public static final String DB_FALSE = "1";

	public static void clear() {
		mappingRuleGroup.clear();
	}

	public static void loadMappingConfigByDB(String groupId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			MappingDAO mappingDao = new MappingDAO();
			mappingDao.setConnection(conn);
			List<Mapping> mappingList = mappingDao
					.getAllMappingInGroup(groupId);
			for (int i = 0; i < mappingList.size(); i++) {
				addMapping2Cache(mappingList.get(i), groupId, conn);
			}
			conn.commit();
		} catch (Exception e) {
			if (null != conn) {
				try {
					conn.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void addMapping2Cache(Mapping mapping, String groupId,
			Connection conn) {
		MappingRule mappingRule = new MappingRule();
		mappingRule.setId(mapping.getMappingId());
		mappingRule.setTargetClass(mapping.getTargetBeanClass());
		if (null != mapping.getTargetType()) {
			mappingRule
					.setTargetType(Integer.parseInt(mapping.getTargetType()));
		}
		if (DB_TRUE.equals(mapping.getHasScript())) {
			MappingScriptRelationDAO msrDao = new MappingScriptRelationDAO();
			msrDao.setConnection(conn);

			List scriptList = new ArrayList();
			List<MappingScriptRelation> msrList = msrDao
					.getAllScriptByMappingId(mapping.getId());
			Script script = null;
			ScriptDAO scriptDao = new ScriptDAO();
			scriptDao.setConnection(conn);
			for (int i = 0; i < msrList.size(); i++) {
				script = scriptDao.selectByPK(msrList.get(i).getScriptId());
				try {
					scriptList.add(new String(script.getScript(), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			mappingRule.setScripts(scriptList);
		}

		MappingFieldRelationDAO mfrDao = new MappingFieldRelationDAO();
		mfrDao.setConnection(conn);

		List<MappingFieldRelation> mfrList = mfrDao
				.getAllFieldRuleByMappingId(mapping.getId());
		List fieldMappingRuleList = new ArrayList();
		for (int i = 0; i < mfrList.size(); i++) {
			fieldMappingRuleList.add(exchange2FieldMappingRule(mfrList.get(i)
					.getFieldMappingRuleId(), conn));
		}
		mappingRule.setFieldMappingRules(fieldMappingRuleList);
		Map group = (Map) mappingRuleGroup.get(groupId);
		if (null == group) {
			group = new HashMap();
			mappingRuleGroup.put(groupId, group);
		}
		group.put(mappingRule.getId(), mappingRule);
	}

	private static FieldMappingRule exchange2FieldMappingRule(
			String fieldMappingRuleId, Connection conn) {
		FieldMappingRuleDAO fmrDao = new FieldMappingRuleDAO();
		fmrDao.setConnection(conn);

		com.fib.msgconverter.commgateway.dao.fieldmappingrule.dao.FieldMappingRule fmrDto = fmrDao
				.selectByPK(fieldMappingRuleId);
		FieldMappingRule fmr = new FieldMappingRule();
		fmr.setType(Integer.parseInt(fmrDto.getMappingType()));
		if (DB_TRUE.equals(fmrDto.getForceTypeConversion())) {
			fmr.setForcibleTypeConversion(true);
		} else {
			fmr.setForcibleTypeConversion(false);
		}
		if (null != fmrDto.getTargetDataType()) {
			fmr.setTargetDataType(fmrDto.getTargetDataType());
			fmr.setTargetClass(FieldMappingRule.getClassByDataType(fmrDto
					.getTargetDataType()));
		}
		fmr.setTo(fmrDto.getAttrTo());
		if (null != fmrDto.getMappingType()) {
			fmr.setType(Integer.parseInt(fmrDto.getMappingType()));
		}
		switch (fmr.getType()) {
		case FieldMappingRule.VALUE_MAPPING:
			loadValueMappingRuleByDB(fmr, fmrDto.getId(), conn);
			break;
		case FieldMappingRule.VALUE_TRANSLATE_MAPPING:
			loadValueTranslateMappingRuleByDB(fmr, fmrDto.getId(), conn);
			break;
		case FieldMappingRule.VALUE:
			loadValueRuleByDB(fmr, fmrDto.getId(), conn);
			break;
		case FieldMappingRule.SCRIPT:
			loadScriptRuleByDB(fmr, fmrDto.getId(), conn);
			break;
		default:
			throw new RuntimeException("fieldMappingRule's type["
					+ fmr.getType() + "] is unkown!");
		}

		return fmr;
	}

	private static void loadScriptRuleByDB(FieldMappingRule field,
			String fieldId, Connection conn) {
		RuleScriptDAO rsDao = new RuleScriptDAO();
		rsDao.setConnection(conn);

		RuleScript rs = rsDao.selectByPK(fieldId);
		if (null == rs) {
			throw new RuntimeException(
					"there is no record in table rule_script!id = " + fieldId);
		}

		field.setFrom(rs.getAttrFrom());
		try {
			field.setValue(new String(rs.getScript(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static void loadValueRuleByDB(FieldMappingRule field,
			String fieldId, Connection conn) {
		RuleValueDAO rvDao = new RuleValueDAO();
		rvDao.setConnection(conn);

		RuleValue rv = rvDao.selectByPK(fieldId);
		if (null == rv) {
			throw new RuntimeException(
					"there is no record in table rule_value!id = " + fieldId);
		}
		field.setValue(rv.getValue());
	}

	private static void loadValueTranslateMappingRuleByDB(
			FieldMappingRule field, String fieldId, Connection conn) {
		RuleValueTranslateMappingDAO rvtmDao = new RuleValueTranslateMappingDAO();
		rvtmDao.setConnection(conn);

		RuleValueTranslateMapping rvtm = rvtmDao.selectByPK(fieldId);
		if (null == rvtm) {
			throw new RuntimeException(
					"there is no record in table rule_value_translate_mapping!id = "
							+ fieldId);
		}

		field.setFrom(rvtm.getAttrFrom());

		ValueTranslateRuleGroupDAO vtrgDao = new ValueTranslateRuleGroupDAO();
		vtrgDao.setConnection(conn);
		ValueTranslateRuleGroup vtrg = vtrgDao.selectByPK(rvtm
				.getValueTranslateRuleGroupId());
		if (null != vtrg.getDefaultValue()) {
			try {
				field.setDefaultValue(new String(vtrg.getDefaultValue(),
						"utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		ValueTranslateRuleRelationDAO vtrrDao = new ValueTranslateRuleRelationDAO();
		vtrrDao.setConnection(conn);
		List<ValueTranslateRuleRelation> vtrrList = vtrrDao
				.getAllTranslateRuleByGroupId(vtrg.getId());
		com.fib.msgconverter.commgateway.dao.valuetranslaterule.dao.ValueTranslateRule vtr = null;
		ValueTranslateRuleDAO vtrDao = new ValueTranslateRuleDAO();
		vtrDao.setConnection(conn);
		field.setTranslateRules(new HashMap());
		for (int i = 0; i < vtrrList.size(); i++) {
			vtr = vtrDao.selectByPK(vtrrList.get(i).getRuleId());
			field.getTranslateRules().put(vtr.getValueFrom(), vtr.getValueTo());
		}
	}

	private static void loadValueMappingRuleByDB(FieldMappingRule field,
			String fieldId, Connection conn) {
		RuleValueMappingDAO rvmDao = new RuleValueMappingDAO();
		rvmDao.setConnection(conn);

		RuleValueMapping rvm = rvmDao.selectByPK(fieldId);
		if (null == rvm) {
			throw new RuntimeException(
					"there is no record in table rule_value_mapping!id = "
							+ fieldId);
		}
		field.setFrom(rvm.getAttrFrom());
	}

	/**
	 * 解析path路径下所有以.xml结尾的文件，加载MappingRule配置
	 * 
	 * @param path
	 */
	public static void loadMappingConfg(String groupId, String path) {
		if (null == path) {
			// throw new IllegalArgumentException("path is null");
			throw new IllegalArgumentException(MultiLanguageResourceBundle
					.getInstance().getString("inputParameter.null",
							new String[] { "path" }));
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
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"MappingRuleManager.loadMappingConfig.path.notDirectory",
									new String[] { dir.getAbsolutePath() }));
		}
		if (!dir.canRead()) {
			// throw new IllegalArgumentException("path[" +
			// dir.getAbsolutePath()
			// + "] can not be read!");
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"MappingRuleManager.loadMappingConfig.path.canNotRead",
									new String[] { dir.getAbsolutePath() }));
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
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"CommGateway.loadChannel.loadMapping.failed",
								new String[] { groupId,
										allFile[i].getAbsolutePath(),
										e.getMessage() }), e);
			}
			List includeFiles = rule.getIncludes();
			if (null != includeFiles) {
				String path = dir.getAbsolutePath();
				if (!path.endsWith(System.getProperty("file.separator"))) {
					path += System.getProperty("file.separator");
				}

				File includeFile = null;
				for (int j = 0; j < includeFiles.size(); j++) {
					List includeRule = loadIncludes(new File(path
							+ includeFiles.get(j)));
					rule.getFieldMappingRules().addAll(includeRule);
					rule.getIncludeRuleMap().put(includeFiles.get(j),
							includeRule);
				}
			}
			if (mappingRules.containsKey(rule.getId())) {
				// throw new RuntimeException(allFile[i].getAbsolutePath()
				// + ": Mapping Rule Id is reduplicated! Id="
				// + rule.getId());
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"MappingRuleManager.loadMappingConfig.id.reduplicate",
										new String[] {
												allFile[i].getAbsolutePath(),
												rule.getId() }));
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
	public static MappingRule getMappingRule(String groupId,
			String mappingRuleId) {
		if (null == groupId) {
			// throw new IllegalArgumentException("groupId is NULL");
			throw new IllegalArgumentException(MultiLanguageResourceBundle
					.getInstance().getString("inputParameter.null",
							new String[] { "groupId" }));
		}
		if (null == mappingRuleId) {
			// throw new IllegalArgumentException("mappingRuleId is NULL");
			throw new IllegalArgumentException(MultiLanguageResourceBundle
					.getInstance().getString("inputParameter.null",
							new String[] { "mappingRuleId" }));
		}

		Map mappingRules = (Map) mappingRuleGroup.get(groupId);
		if (null == mappingRules) {
			// throw new IllegalArgumentException("group[" + groupId
			// + "] is null! please load it first!");
			throw new IllegalArgumentException(MultiLanguageResourceBundle
					.getInstance().getString(
							"MappingRuleManager.getMappingRule.group.null",
							new String[] { groupId }));
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

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser;
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				parser = factory.newSAXParser();
				parser.parse(in, this);
			} catch (Exception e) {
				e.printStackTrace(System.err);
				ExceptionUtil.throwActualException(e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception ex) {
						ex.printStackTrace(System.err);
					}
				}
			}
			// 解析后检查
			checkConfig();
			return rule;
		}

		String elementValue = null;
		FieldMappingRule fieldMapingRule = null;
		boolean isInclude = false;
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
			if ("message-bean-mapping".equalsIgnoreCase(name)) {
				rule = new MappingRule();
				// @id
				attr = attributes.getValue("id");
				if (null == attr) {
					// throw new RuntimeException(fileName
					// + ": message-bean-mapping/@id is NULL");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.null",
									new String[] { fileName,
											"message-bean-mapping/@id" }));
				} else {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(fileName
						// + ": message-bean-mapping/@id is Blank");
						throw new RuntimeException(MultiLanguageResourceBundle
								.getInstance().getString(
										"config.blank",
										new String[] { fileName,
												"message-bean-mapping/@id" }));
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
						throw new RuntimeException(MultiLanguageResourceBundle
								.getInstance().getString(
										"config.blank",
										new String[] { fileName,
												"message-bean-mapping/@name" }));
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
					throw new RuntimeException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"config.null",
											new String[] { fileName,
													"message-bean-mapping/@target-type" }));
				} else {
					attr = attr.trim();
					if (0 == attr.length()) {
						// throw new RuntimeException(
						// fileName
						// + ": message-bean-mapping/@target-type is Blank");
						throw new RuntimeException(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"config.blank",
												new String[] { fileName,
														"message-bean-mapping/@target-type" }));
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
						throw new RuntimeException(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"config.null",
												new String[] { fileName,
														"message-bean-mapping/@target-bean-class" }));
					} else {
						attr = attr.trim();
						if (0 == attr.length()) {
							// throw new RuntimeException(
							// fileName
							// +
							// ": message-bean-mapping/@target-bean-class is Blank");
							throw new RuntimeException(
									MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"config.blank",
													new String[] { fileName,
															"message-bean-mapping/@target-bean-class" }));
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
												"message-bean-mapping["
														+ position
														+ "]/mapping/@type" }));
					}
				}
				fieldMapingRule.setType(FieldMappingRule
						.getMappingTypeByText(attr));

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
										new String[] { "" + vtrfile }));
					}
					if (!fieldMapingRule.getTranslateRuleId().equals(
							vtRule.getId())) {
						// throw new RuntimeException(vtrfile + "'s id["
						// + vtRule.getId() + "] != "
						// + fieldMapingRule.getTranslateRuleId());
						throw new RuntimeException(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
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
					fieldMapingRule.setDefaultValue(vtRule.getDefaultValue());
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
						throw new RuntimeException(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
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
															+ position
															+ "]/@from" }));
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
				// 只有目标对象为Map时，才有必要设置目标字段的数据类型
				if (rule.getTargetType() == MappingRule.MAP) {
					attr = attributes.getValue("target-data-type");
					if (null != attr) {
						attr = attr.trim();
						if (0 == attr.length()) {
							// throw new RuntimeException(fileName
							// + ": message-bean-mapping/mapping["
							// + position + "]/@target-data-type is Blank");
							throw new RuntimeException(
									MultiLanguageResourceBundle
											.getInstance()
											.getString(
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
					throw new RuntimeException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"MappingRuleManager.translateRuleId.notNull"));
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
						throw new RuntimeException(MultiLanguageResourceBundle
								.getInstance().getString(
										"config.null",
										new String[] {
												fileName,
												"message-bean-mapping/mapping["
														+ position
														+ "]/value/@value" }));
					} else {
						attr = attr.trim();
						if (0 == attr.length()) {
							throw new RuntimeException(
									MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"config.blank",
													new String[] {
															fileName,
															"message-bean-mapping/mapping["
																	+ position
																	+ "]/value/@value" }));
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
						throw new RuntimeException(
								MultiLanguageResourceBundle.getInstance()
										.getString(
												"config.null",
												new String[] {
														fileName,
														"message-bean-mapping/mapping["
																+ position
																+ "]/@from" }));
					} else {
						from = from.trim();
						if (0 == from.length()) {
							// throw new RuntimeException(fileName
							// + ": message-bean-mapping/mapping[" + position
							// + "]/@from is Blank!");
							throw new RuntimeException(
									MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"config.blank",
													new String[] {
															fileName,
															"message-bean-mapping/mapping["
																	+ position
																	+ "]/@from" }));
						}
					}

					// @to
					String to = attributes.getValue("to");
					if (null == to) {
						// throw new RuntimeException(fileName
						// + ": message-bean-mapping/mapping[" + position
						// + "]/@to is NULL!");
						throw new RuntimeException(MultiLanguageResourceBundle
								.getInstance().getString(
										"config.null",
										new String[] {
												fileName,
												"message-bean-mapping/mapping["
														+ position + "]/@to" }));
					} else {
						to = to.trim();
						if (0 == to.length()) {
							// throw new RuntimeException(fileName
							// + ": message-bean-mapping/mapping[" + position
							// + "]/@to is Blank!");
							throw new RuntimeException(
									MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"config.blank",
													new String[] {
															fileName,
															"message-bean-mapping/mapping["
																	+ position
																	+ "]/@to" }));
						}
					}

					fieldMapingRule.getTranslateRules().put(from, to);
				}
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
						throw new RuntimeException(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"MappingRuleManager.mapping.text.null",
												new String[] {
														fileName,
														fieldMapingRule
																.getType()
																+ "" }));
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
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance()
									.getString(
											"config.null",
											new String[] { fileName,
													"include/text()" }));
				}
				if (isInclude) {
					rule.getIncludes().add(elementValue.trim());
				}
			} else if ("manual-mapping".equals(name)) {
				String script = elementValue;
				if (null == script) {
					// throw new RuntimeException(fileName
					// + ": manual-mapping/text() is Null!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.null",
									new String[] { fileName,
											"manual-mapping/text()" }));
				} else {
					script = script.trim();
					if (0 == script.length()) {
						// throw new RuntimeException(fileName
						// + ": manual-mapping/text() is Blank!");
						throw new RuntimeException(MultiLanguageResourceBundle
								.getInstance().getString(
										"config.blank",
										new String[] { fileName,
												"manual-mapping/text()" }));
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
