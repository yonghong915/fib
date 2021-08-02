package com.fib.upp.service.gateway.message.metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import lombok.Data;

@Data
public class Field {
	/** 域名 */
	private String name;

	/** 域类型 */
	private int fieldType;

	/** XPath表达式 */
	private String xpath;

	/** 表格行的XPath表达式 */
	private String rowXpath;

	/** 描述信息 */
	private String shortText;

	/** 域值的数据类型 */
	private int dataType;

	/** 域值的编码 */
	private int dataEncoding = 4000;

	/** 域值的长度 */
	private int length;

	/** 默认域值 */
	private String value;

	/** 模板 */
	private String pattern;

	/** 域值的最小长度 */
	private int maxLength = -1;

	/** 域值的最大长度 */
	private int minLength = -1;

	/** 是否必输域 */
	private boolean required;
	private String referenceId;

	/** 域值是否可修改 */
	private boolean editable = true;

	/** 引用类型,用于表示动态引用域 */
	private String referenceType;

	/** 引用,组合域、变长组合域、引用域、变长引用域、表格域专用 */
	private MessageMetaData reference;

	private String extendedAttributeText;
	private Map<String, String> extendedAttributes = new HashMap<>();

	private boolean isRemoveUnwatchable = true;

	/** 严格的值长度检查 */
	private boolean strictDataLength = false;

	private String lengthScript;

	/** 变长域的内嵌长度域的数据类型 */
	private int lengthFieldDataType = 0;

	/** 变长域的内嵌长度域值的编码 */
	private int lengthFieldDataEncoding = 4000;

	/** 变长域的内嵌长度域值的长度 */
	private int lengthFieldLength = 0;

	private String refLengthFieldName;

	/** 引用长度域,变长域、变长组合域、变长引用域专用 */
	private Field refLengthField;

	/** 引用长度域偏移量 */
	private int refLengthFieldOffset = 0;
	private String startFieldName;

	/** 开始域,长度域专用 */
	private Field startField;
	private String endFieldName;

	/** 结束域,长度域专用 */
	private Field endField;
	private String prefixString;

	/** 前缀字符串 */
	private byte[] prefix;
	private String tabPrefixString;
	private byte[] tabPrefix;
	private boolean firRowPrefix = false;
	private String suffixString;

	/** 后缀字符串 */
	private byte[] suffix;
	private String tabSuffixString;
	private byte[] tabSuffix;
	private boolean lastRowSuffix = false;
	private boolean rowCut = true;

	/** 填充字符,定长域专用 */
	private byte padding = 32;
	/** 填充字符填充方向 */
	private int paddingDirection = 5002;
	private String combineOrTableFieldClassName;
	private Map<String, Object> valueRange = new HashMap<>();
	private String rowNumFieldName;

	/** 表格行数域,表格域专用 */
	private Field rowNumField;
	private String tableName;

	/** 表格,表格行数域专用 */
	private Field table;
	private String prePackEvent;
	private String postPackEvent;
	private String preParseEvent;
	private String postParseEvent;
	private String preRowPackEvent;
	private String postRowPackEvent;
	private String preRowParseEvent;
	private String postRowParseEvent;

	/** Tag, */
	private String tag;

	/** Mac校验的计算方法 */
	private String calcType;
	private Map<String, String> macFldDataCache = new HashMap<>();
	private String expression;
	private String dataCharset;
	private String refValueRange;
	private boolean shield;
	private Map<String, Field> subFields = new TreeMap<>();

	public Field getSubField(String var1) {
		return this.subFields.get(var1);
	}

	public void setSubField(String var1, Field var2) {
		this.subFields.put(var1, var2);
	}

}
