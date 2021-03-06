package com.fib.gateway.message.metadata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import lombok.Data;

/**
 * 字段域信息
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
@Data
public class Field {
	private String databaseFieldId;

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
	private Message reference;

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

	/** iso8583域编号 */
	private int iso8583_no = -1;

	public String getExtendAttribute(String var1) {
		return null == this.extendedAttributes ? null : (String) this.extendedAttributes.get(var1);
	}

	public Field getSubField(String var1) {
		return this.subFields.get(var1);
	}

	public void setSubField(String var1, Field var2) {
		this.subFields.put(var1, var2);
	}

	public void setExtendAttribute(String var1, String var2) {
		if (null == this.extendedAttributes) {
			this.extendedAttributes = new HashMap<>(3);
		}

		this.extendedAttributes.put(var1, var2);
	}

	public boolean equalTo(Field var1) {
		if (null == var1) {
			return false;
		} else if (!this.name.equalsIgnoreCase(var1.getName())) {
			return false;
		} else if (this.fieldType != var1.getFieldType()) {
			return false;
		} else if (this.padding != var1.getPadding()) {
			return false;
		} else if (this.paddingDirection != var1.getPaddingDirection()) {
			return false;
		} else if (this.required ^ var1.isRequired()) {
			return false;
		} else if (this.editable ^ var1.isEditable()) {
			return false;
		} else if (null == this.xpath ^ null == var1.getXpath()) {
			return false;
		} else if (null != this.xpath && null != var1.getXpath() && !this.xpath.equalsIgnoreCase(var1.getXpath())) {
			return false;
		} else if (this.iso8583_no != var1.getIso8583_no()) {
			return false;
		} else if (null == this.prefix ^ null == var1.getPrefix()) {
			return false;
		} else if (null != this.prefix && null != var1.getPrefix() && !Arrays.equals(this.prefix, var1.getPrefix())) {
			return false;
		} else if (this.firRowPrefix ^ var1.isFirRowPrefix()) {
			return false;
		} else if (null == this.suffix ^ null == var1.getSuffix()) {
			return false;
		} else if (null != this.suffix && null != var1.getSuffix() && !Arrays.equals(this.suffix, var1.getSuffix())) {
			return false;
		} else if (this.lastRowSuffix ^ var1.isLastRowSuffix()) {
			return false;
		} else if (null == this.extendedAttributeText ^ null == var1.getExtendedAttributeText()) {
			return false;
		} else if (null != this.extendedAttributeText && null != var1.getExtendedAttributeText()
				&& !this.extendedAttributeText.equalsIgnoreCase(var1.getExtendedAttributeText())) {
			return false;
		} else if (null == this.dataCharset ^ null == var1.getDataCharset()) {
			return false;
		} else if (null != this.dataCharset && null != var1.getDataCharset()
				&& !this.dataCharset.equalsIgnoreCase(var1.getDataCharset())) {
			return false;
		} else if (null == this.tag ^ null == var1.getTag()) {
			return false;
		} else if (null != this.tag && null != var1.getTag() && !this.tag.equalsIgnoreCase(var1.getTag())) {
			return false;
		} else if (this.isRemoveUnwatchable ^ var1.isRemoveUnwatchable()) {
			return false;
		} else {
			if (2001 == this.fieldType || 2003 == this.fieldType || 2009 == this.fieldType || 2011 == this.fieldType) {
				if (null == this.refLengthFieldName ^ null == var1.getRefLengthFieldName()) {
					return false;
				}

				if (null != this.refLengthFieldName && null != var1.getRefLengthFieldName()
						&& !this.refLengthFieldName.equalsIgnoreCase(var1.getRefLengthFieldName())) {
					return false;
				}

				if (this.refLengthFieldOffset != var1.getRefLengthFieldOffset()) {
					return false;
				}

				if (2011 == this.fieldType) {
					if (null == this.tabPrefix ^ null == var1.getTabPrefix()) {
						return false;
					}

					if (null != this.tabPrefix && null != var1.getTabPrefix()
							&& !Arrays.equals(this.tabPrefix, var1.getTabPrefix())) {
						return false;
					}

					if (null == this.tabSuffix ^ null == var1.getTabSuffix()) {
						return false;
					}

					if (null != this.tabSuffix && null != var1.getTabSuffix()
							&& !Arrays.equals(this.tabSuffix, var1.getTabSuffix())) {
						return false;
					}

					if (this.rowCut ^ var1.isRowCut()) {
						return false;
					}
				} else {
					if (this.lengthFieldDataType != var1.getLengthFieldDataType()) {
						return false;
					}

					if (this.lengthFieldDataEncoding != var1.getLengthFieldDataEncoding()) {
						return false;
					}

					if (this.lengthFieldLength != var1.getLengthFieldLength()) {
						return false;
					}
				}

				if (this.maxLength != var1.getMaxLength()) {
					return false;
				}

				if (this.minLength != var1.getMinLength()) {
					return false;
				}
			}

			Iterator<?> var2;
			String var3;
			if (2002 != this.fieldType && 2003 != this.fieldType && 2004 != this.fieldType && 2011 != this.fieldType
					&& 2008 != this.fieldType && 2009 != this.fieldType) {
				if (this.dataType != var1.getDataType()) {
					return false;
				}

				if (this.dataEncoding != var1.getDataEncoding()) {
					return false;
				}

				if (null == this.pattern ^ null == var1.getPattern()) {
					return false;
				}

				if (null != this.pattern && null != var1.getPattern()
						&& !this.pattern.equalsIgnoreCase(var1.getPattern())) {
					return false;
				}

				if (null == this.lengthScript ^ null == var1.getLengthScript()) {
					return false;
				}

				if (null != this.lengthScript && null != var1.getLengthScript()
						&& !this.lengthScript.equalsIgnoreCase(var1.getLengthScript())) {
					return false;
				}

				if (this.strictDataLength ^ var1.isStrictDataLength()) {
					return false;
				}

				if (2007 == this.fieldType) {
					if (!this.startFieldName.equalsIgnoreCase(var1.getStartFieldName())) {
						return false;
					}

					if (!this.endFieldName.equalsIgnoreCase(var1.getEndFieldName())) {
						return false;
					}
				}

				if (2010 == this.fieldType) {
					if (null == this.startFieldName ^ null == var1.getStartFieldName()) {
						return false;
					}

					if (null != this.startFieldName && null != var1.getStartFieldName()
							&& !this.startFieldName.equalsIgnoreCase(var1.getStartFieldName())) {
						return false;
					}

					if (null == this.endFieldName ^ null == var1.getEndFieldName()) {
						return false;
					}

					if (null != this.endFieldName && null != var1.getEndFieldName()
							&& !this.endFieldName.equalsIgnoreCase(var1.getEndFieldName())) {
						return false;
					}

					if (!this.calcType.equalsIgnoreCase(var1.getCalcType())) {
						return false;
					}

					if (null == this.macFldDataCache ^ null == var1.getMacFldDataCache()) {
						return false;
					}

					if (null != this.macFldDataCache && null != var1.getMacFldDataCache()) {
						var2 = var1.getMacFldDataCache().values().iterator();

						while (var2.hasNext()) {
							var3 = (String) var2.next();
							if (!var3.equalsIgnoreCase((String) this.macFldDataCache.get(var3))) {
								return false;
							}
						}
					}
				}

				if (2005 == this.fieldType) {
					if (null == this.tableName ^ null == var1.getTableName()) {
						return false;
					}

					if (null != this.tableName && null != var1.getTableName()
							&& !this.tableName.equalsIgnoreCase(var1.getTableName())) {
						return false;
					}
				}

				if (2001 != this.fieldType && this.length != var1.getLength()) {
					return false;
				}

				if (2000 == this.fieldType || 2005 == this.fieldType) {
					if (null == this.value ^ null == var1.getValue()) {
						return false;
					}

					if (null != this.value && null != var1.getValue()
							&& !this.value.equalsIgnoreCase(var1.getValue())) {
						return false;
					}
				}
			} else {
				if (null == this.reference ^ null == var1.getReference()) {
					return false;
				}

				if (null != this.reference && null != var1.getReference()
						&& !this.reference.equalTo(var1.getReference())) {
					return false;
				}

				if (null == this.referenceId ^ null == var1.getReferenceId()) {
					return false;
				}

				if (null != this.referenceId && null != var1.getReferenceId()
						&& !this.referenceId.equalsIgnoreCase(var1.getReferenceId())) {
					return false;
				}

				if (null == this.referenceType ^ null == var1.getReferenceType()) {
					return false;
				}

				if (null != this.referenceType && null != var1.getReferenceType()
						&& !this.referenceType.equalsIgnoreCase(var1.getReferenceType())) {
					return false;
				}

				if (this.dataEncoding != var1.getDataEncoding()) {
					return false;
				}

				if (2004 == this.fieldType) {
					if (null == this.rowNumFieldName ^ null == var1.getRowNumFieldName()) {
						return false;
					}

					if (null != this.rowNumFieldName && null != var1.getRowNumFieldName()
							&& !this.rowNumFieldName.equalsIgnoreCase(var1.getRowNumFieldName())) {
						return false;
					}

					if (null == this.rowXpath ^ null == var1.getRowXpath()) {
						return false;
					}

					if (null != this.rowXpath && null != var1.getRowXpath()
							&& !this.rowXpath.equalsIgnoreCase(var1.getRowXpath())) {
						return false;
					}
				}
			}

			if (null == this.combineOrTableFieldClassName ^ null == var1.getCombineOrTableFieldClassName()) {
				return false;
			} else if (null != this.combineOrTableFieldClassName && null != var1.getCombineOrTableFieldClassName()
					&& !this.combineOrTableFieldClassName.equalsIgnoreCase(var1.getCombineOrTableFieldClassName())) {
				return false;
			} else if (null == this.subFields ^ null == var1.getSubFields()) {
				return false;
			} else {
				if (null != this.subFields && null != var1.getSubFields()) {
					var2 = this.subFields.values().iterator();
					var3 = null;

					while (var2.hasNext()) {
						// Field var4 = (Field) var2.next();
//						if (!var4.equalTo(var1.getSubField(var4.getName()))) {
//							return false;
//						}
					}
				}

				if (null == this.valueRange ^ null == var1.getValueRange()) {
					return false;
				} else {
					if (null != this.valueRange && null != var1.getValueRange()) {
						var2 = var1.getValueRange().values().iterator();
						var3 = null;

						while (var2.hasNext()) {
//							ValueRange var5 = (ValueRange) var2.next();
//							if (!var5.equalsTo((ValueRange) this.valueRange.get(var5.getValue()))) {
//								return false;
//							}
						}
					}

					if (null == this.prePackEvent ^ null == var1.getPrePackEvent()) {
						return false;
					} else if (null != this.prePackEvent && null != var1.getPrePackEvent()
							&& !this.prePackEvent.equalsIgnoreCase(var1.getPrePackEvent())) {
						return false;
					} else if (null == this.postPackEvent ^ null == var1.getPostPackEvent()) {
						return false;
					} else if (null != this.postPackEvent && null != var1.getPostPackEvent()
							&& !this.postPackEvent.equalsIgnoreCase(var1.getPostPackEvent())) {
						return false;
					} else if (null == this.preParseEvent ^ null == var1.getPreParseEvent()) {
						return false;
					} else if (null != this.preParseEvent && null != var1.getPreParseEvent()
							&& !this.preParseEvent.equalsIgnoreCase(var1.getPreParseEvent())) {
						return false;
					} else if (null == this.postParseEvent ^ null == var1.getPostParseEvent()) {
						return false;
					} else if (null != this.postParseEvent && null != var1.getPostParseEvent()
							&& !this.postParseEvent.equalsIgnoreCase(var1.getPostParseEvent())) {
						return false;
					} else if (null == this.preRowPackEvent ^ null == var1.getPreRowPackEvent()) {
						return false;
					} else if (null != this.preRowPackEvent && null != var1.getPreRowPackEvent()
							&& !this.preRowPackEvent.equalsIgnoreCase(var1.getPreRowPackEvent())) {
						return false;
					} else if (null == this.postRowPackEvent ^ null == var1.getPostRowPackEvent()) {
						return false;
					} else if (null != this.postRowPackEvent && null != var1.getPostRowPackEvent()
							&& !this.postRowPackEvent.equalsIgnoreCase(var1.getPostRowPackEvent())) {
						return false;
					} else if (null == this.preRowParseEvent ^ null == var1.getPreRowParseEvent()) {
						return false;
					} else if (null != this.preRowParseEvent && null != var1.getPreRowParseEvent()
							&& !this.preRowParseEvent.equalsIgnoreCase(var1.getPreRowParseEvent())) {
						return false;
					} else if (null == this.postRowParseEvent ^ null == var1.getPostRowParseEvent()) {
						return false;
					} else if (null != this.postRowParseEvent && null != var1.getPostRowParseEvent()
							&& !this.postRowParseEvent.equalsIgnoreCase(var1.getPostRowParseEvent())) {
						return false;
					} else if (null == this.expression ^ null == var1.getExpression()) {
						return false;
					} else {
						return null == this.expression || null == var1.getExpression()
								|| this.expression.equalsIgnoreCase(var1.getExpression());
					}
				}
			}
		}
	}
	
	public Field copy() {
		Field var1 = new Field();
		var1.setName(this.name);
		var1.setFieldType(this.fieldType);
		var1.setIso8583_no(this.iso8583_no);
		var1.setReference(this.reference);
		var1.setReferenceId(this.referenceId);
		var1.setReferenceType(this.referenceType);
		var1.setXpath(this.xpath);
		var1.setRowXpath(this.rowXpath);
		var1.setEditable(this.editable);
		var1.setStrictDataLength(this.strictDataLength);
		var1.setRequired(this.required);
		var1.setDataType(this.dataType);
		var1.setDataEncoding(this.dataEncoding);
		var1.setLength(this.length);
		var1.setLengthScript(this.lengthScript);
		var1.setValue(this.value);
		var1.setPattern(this.pattern);
		var1.setLengthFieldDataType(this.lengthFieldDataType);
		var1.setLengthFieldDataEncoding(this.lengthFieldDataEncoding);
		var1.setLengthFieldLength(this.lengthFieldLength);
		var1.setMaxLength(this.maxLength);
		var1.setMinLength(this.minLength);
		var1.setRefLengthFieldName(this.refLengthFieldName);
		var1.setRefLengthField(this.refLengthField);
		var1.setRefLengthFieldOffset(this.refLengthFieldOffset);
		var1.setStartField(this.startField);
		var1.setStartFieldName(this.startFieldName);
		var1.setEndField(this.endField);
		var1.setEndFieldName(this.endFieldName);
		var1.setPrefix(this.prefix);
		var1.setTabPrefix(this.tabPrefix);
		var1.setFirRowPrefix(this.firRowPrefix);
		var1.setSuffix(this.suffix);
		var1.setTabSuffix(this.tabSuffix);
		var1.setLastRowSuffix(this.lastRowSuffix);
		var1.setRowCut(this.rowCut);
		var1.setPadding(this.padding);
		var1.setPaddingDirection(this.paddingDirection);
		var1.setSubFields(this.subFields);
		var1.setCombineOrTableFieldClassName(this.combineOrTableFieldClassName);
		var1.setValueRange(this.valueRange);
		var1.setRowNumFieldName(this.rowNumFieldName);
		var1.setRowNumField(this.rowNumField);
		var1.setTableName(this.tableName);
		var1.setTable(this.table);
		var1.setPrePackEvent(this.prePackEvent);
		var1.setPostPackEvent(this.postPackEvent);
		var1.setPreParseEvent(this.preParseEvent);
		var1.setPostParseEvent(this.postParseEvent);
		var1.setPreRowPackEvent(this.preRowPackEvent);
		var1.setPostRowPackEvent(this.postRowPackEvent);
		var1.setPreRowParseEvent(this.preRowParseEvent);
		var1.setPostRowParseEvent(this.postRowParseEvent);
		var1.setTag(this.tag);
		var1.setCalcType(this.calcType);
		var1.setExtendedAttributeText(this.extendedAttributeText);
		var1.setExtendedAttributes(this.extendedAttributes);
		var1.setMacFldDataCache(this.macFldDataCache);
		var1.setExpression(this.expression);
		var1.setDataCharset(this.dataCharset);
		var1.setRemoveUnwatchable(this.isRemoveUnwatchable);
		return var1;
	}
}
