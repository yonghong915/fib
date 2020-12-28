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
	private String name;
	private int fieldType;
	private String xpath;
	private String rowXpath;
	private String shortText;
	private int dataType;
	private int dataEncoding = 4000;
	private int length;
	private String value;
	private String pattern;
	private int maxLength = -1;
	private int minLength = -1;
	private boolean required;
	private String referenceId;
	private boolean editable = true;
	private String referenceType;
	private Message reference;

	private String extendedAttributeText;
	private Map<String, String> extendedAttributes = new HashMap<>();

	private boolean isRemoveUnwatchable = true;
	private boolean strictDataLength = false;

	private String lengthScript;

	private int lengthFieldDataType = 0;
	private int lengthFieldDataEncoding = 4000;
	private int lengthFieldLength = 0;

	private String refLengthFieldName;
	private Field refLengthField;
	private int refLengthFieldOffset = 0;
	private String startFieldName;
	private Field startField;
	private String endFieldName;
	private Field endField;
	private String prefixString;
	private byte[] prefix;
	private String tabPrefixString;
	private byte[] tabPrefix;
	private boolean firRowPrefix = false;
	private String suffixString;
	private byte[] suffix;
	private String tabSuffixString;
	private byte[] tabSuffix;
	private boolean lastRowSuffix = false;
	private boolean rowCut = true;
	private byte padding = 32;
	private int paddingDirection = 5002;
	private String combineOrTableFieldClassName;
	private Map valueRange = new HashMap();
	private String rowNumFieldName;
	private Field rowNumField;
	private String tableName;
	private Field table;
	private String prePackEvent;
	private String postPackEvent;
	private String preParseEvent;
	private String postParseEvent;
	private String preRowPackEvent;
	private String postRowPackEvent;
	private String preRowParseEvent;
	private String postRowParseEvent;
	private String tag;
	private String calcType;
	private Map macFldDataCache = new HashMap();
	private String expression;
	private String dataCharset;
	private String refValueRange;
	private boolean shield;
	private Map<String, Field> subFields = new TreeMap<>();
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

			Iterator var2;
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
						var3 = null;

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
						Field var4 = (Field) var2.next();
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
}
