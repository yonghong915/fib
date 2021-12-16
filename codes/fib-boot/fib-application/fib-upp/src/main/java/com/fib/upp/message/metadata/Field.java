package com.fib.upp.message.metadata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import com.fib.upp.util.SortHashMap;

import lombok.Data;

@Data
public class Field {
	private String name;
	private int fieldType;
	private int iso8583_no;
	private Message reference;
	private String referenceId;
	private String referenceType;
	private String xpath;
	private String rowXpath;
	private boolean editable;
	private boolean isRemoveUnwatchable;
	private boolean strictDataLength;
	private boolean required;
	private String shortText;
	private int dataType;
	private int dataEncoding;
	private int length;
	private String lengthScript;
	private String value;
	private String pattern;
	private int lengthFieldDataType;
	private int lengthFieldDataEncoding;
	private int lengthFieldLength;
	private int maxLength;
	private int minLength;
	private String refLengthFieldName;
	private Field refLengthField;
	private int refLengthFieldOffset;
	private String startFieldName;
	private Field startField;
	private String endFieldName;
	private Field endField;
	private String prefixString;
	private byte prefix[];
	private String tabPrefixString;
	private byte tabPrefix[];
	private boolean firRowPrefix;
	private String suffixString;
	private byte suffix[];
	private String tabSuffixString;
	private byte tabSuffix[];
	private boolean lastRowSuffix;
	private boolean rowCut;
	private byte padding;
	private int paddingDirection;
	private SortHashMap subFields;
	private String combineOrTableFieldClassName;
	private Map valueRange;
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
	private String extendedAttributeText;
	private Map extendedAttributes;
	private Map macFldDataCache;
	private String expression;
	private String dataCharset;
	private String refValueRange;
	private boolean shield;

	public int getIso8583_no() {
		return iso8583_no;
	}

	public void setIso8583_no(int iso8583_no) {
		this.iso8583_no = iso8583_no;
	}

	public boolean isRemoveUnwatchable() {
		return isRemoveUnwatchable;
	}

	public void setRemoveUnwatchable(boolean isRemoveUnwatchable) {
		this.isRemoveUnwatchable = isRemoveUnwatchable;
	}

	public String getTabPrefixString() {
		return tabPrefixString;
	}

	public void setTabPrefixString(String tabPrefixString) {
		this.tabPrefixString = tabPrefixString;
	}

	public byte[] getTabPrefix() {
		return tabPrefix;
	}

	public void setTabPrefix(byte[] tabPrefix) {
		this.tabPrefix = tabPrefix;
	}

	public boolean isFirRowPrefix() {
		return firRowPrefix;
	}

	public void setFirRowPrefix(boolean firRowPrefix) {
		this.firRowPrefix = firRowPrefix;
	}

	public String getTabSuffixString() {
		return tabSuffixString;
	}

	public void setTabSuffixString(String tabSuffixString) {
		this.tabSuffixString = tabSuffixString;
	}

	public byte[] getTabSuffix() {
		return tabSuffix;
	}

	public void setTabSuffix(byte[] tabSuffix) {
		this.tabSuffix = tabSuffix;
	}

	public boolean isLastRowSuffix() {
		return lastRowSuffix;
	}

	public void setLastRowSuffix(boolean lastRowSuffix) {
		this.lastRowSuffix = lastRowSuffix;
	}

	public boolean isRowCut() {
		return rowCut;
	}

	public void setRowCut(boolean rowCut) {
		this.rowCut = rowCut;
	}

	public String getPrePackEvent() {
		return prePackEvent;
	}

	public void setPrePackEvent(String prePackEvent) {
		this.prePackEvent = prePackEvent;
	}

	public String getPostPackEvent() {
		return postPackEvent;
	}

	public void setPostPackEvent(String postPackEvent) {
		this.postPackEvent = postPackEvent;
	}

	public String getPreParseEvent() {
		return preParseEvent;
	}

	public void setPreParseEvent(String preParseEvent) {
		this.preParseEvent = preParseEvent;
	}

	public String getPostParseEvent() {
		return postParseEvent;
	}

	public void setPostParseEvent(String postParseEvent) {
		this.postParseEvent = postParseEvent;
	}

	public String getPreRowPackEvent() {
		return preRowPackEvent;
	}

	public void setPreRowPackEvent(String preRowPackEvent) {
		this.preRowPackEvent = preRowPackEvent;
	}

	public String getPostRowPackEvent() {
		return postRowPackEvent;
	}

	public void setPostRowPackEvent(String postRowPackEvent) {
		this.postRowPackEvent = postRowPackEvent;
	}

	public String getPreRowParseEvent() {
		return preRowParseEvent;
	}

	public void setPreRowParseEvent(String preRowParseEvent) {
		this.preRowParseEvent = preRowParseEvent;
	}

	public String getPostRowParseEvent() {
		return postRowParseEvent;
	}

	public void setPostRowParseEvent(String postRowParseEvent) {
		this.postRowParseEvent = postRowParseEvent;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCalcType() {
		return calcType;
	}

	public void setCalcType(String calcType) {
		this.calcType = calcType;
	}

	public String getExtendedAttributeText() {
		return extendedAttributeText;
	}

	public void setExtendedAttributeText(String extendedAttributeText) {
		this.extendedAttributeText = extendedAttributeText;
	}

	public Map getMacFldDataCache() {
		return macFldDataCache;
	}

	public void setMacFldDataCache(Map macFldDataCache) {
		this.macFldDataCache = macFldDataCache;
	}

	public String getRefValueRange() {
		return refValueRange;
	}

	public void setRefValueRange(String refValueRange) {
		this.refValueRange = refValueRange;
	}

//	public void setSubFields(SortedMap subFields) {
//		this.subFields = subFields;
//	}

	public void setValueRange(Map<String, ValueRange> valueRange) {
		this.valueRange = valueRange;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getExtendAttribute(String s) {
		if (null == extendedAttributes)
			return null;
		else
			return (String) extendedAttributes.get(s);
	}

	public void setExtendAttribute(String s, String s1) {
		if (null == extendedAttributes)
			extendedAttributes = new HashMap(3);
		extendedAttributes.put(s, s1);
	}

	public Map getExtendedAttributes() {
		return extendedAttributes;
	}

	public void setExtendedAttributes(Map extendedAttributes) {
		this.extendedAttributes = extendedAttributes;
	}

	public byte getPadding() {
		return padding;
	}

	public void setPadding(byte padding) {
		this.padding = padding;
	}

	public void setSubField(String fieldName, Field field) {
		subFields.put(fieldName, field);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getRowXpath() {
		return rowXpath;
	}

	public void setRowXpath(String rowXpath) {
		this.rowXpath = rowXpath;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public int getDataEncoding() {
		return dataEncoding;
	}

	public void setDataEncoding(int dataEncoding) {
		this.dataEncoding = dataEncoding;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getLengthScript() {
		return lengthScript;
	}

	public void setLengthScript(String lengthScript) {
		this.lengthScript = lengthScript;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getPaddingDirection() {
		return paddingDirection;
	}

	public void setPaddingDirection(int paddingDirection) {
		this.paddingDirection = paddingDirection;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public Message getReference() {
		return reference;
	}

	public void setReference(Message reference) {
		this.reference = reference;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	public String getPrefixString() {
		return prefixString;
	}

	public void setPrefixString(String prefixString) {
		this.prefixString = prefixString;
	}

	public byte[] getPrefix() {
		return prefix;
	}

	public void setPrefix(byte[] prefix) {
		this.prefix = prefix;
	}

	public String getSuffixString() {
		return suffixString;
	}

	public void setSuffixString(String suffixString) {
		this.suffixString = suffixString;
	}

	public byte[] getSuffix() {
		return suffix;
	}

	public void setSuffix(byte[] suffix) {
		this.suffix = suffix;
	}

	public SortHashMap getSubFields() {
		return subFields;
	}

	public void setSubFields(SortHashMap subFields) {
		this.subFields = subFields;
	}

	public String getCombineOrTableFieldClassName() {
		return combineOrTableFieldClassName;
	}

	public void setCombineOrTableFieldClassName(String combineOrTableFieldClassName) {
		this.combineOrTableFieldClassName = combineOrTableFieldClassName;
	}

	public boolean isShield() {
		return shield;
	}

	public void setShield(boolean shield) {
		this.shield = shield;
	}

	public boolean isStrictDataLength() {
		return strictDataLength;
	}

	public void setStrictDataLength(boolean strictDataLength) {
		this.strictDataLength = strictDataLength;
	}

	public String getDataCharset() {
		return dataCharset;
	}

	public void setDataCharset(String dataCharset) {
		this.dataCharset = dataCharset;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getLengthFieldDataType() {
		return lengthFieldDataType;
	}

	public void setLengthFieldDataType(int lengthFieldDataType) {
		this.lengthFieldDataType = lengthFieldDataType;
	}

	public int getLengthFieldDataEncoding() {
		return lengthFieldDataEncoding;
	}

	public void setLengthFieldDataEncoding(int lengthFieldDataEncoding) {
		this.lengthFieldDataEncoding = lengthFieldDataEncoding;
	}

	public int getLengthFieldLength() {
		return lengthFieldLength;
	}

	public void setLengthFieldLength(int lengthFieldLength) {
		this.lengthFieldLength = lengthFieldLength;
	}

	public String getRefLengthFieldName() {
		return refLengthFieldName;
	}

	public void setRefLengthFieldName(String refLengthFieldName) {
		this.refLengthFieldName = refLengthFieldName;
	}

	public Field getRefLengthField() {
		return refLengthField;
	}

	public void setRefLengthField(Field refLengthField) {
		this.refLengthField = refLengthField;
	}

	public int getRefLengthFieldOffset() {
		return refLengthFieldOffset;
	}

	public void setRefLengthFieldOffset(int refLengthFieldOffset) {
		this.refLengthFieldOffset = refLengthFieldOffset;
	}

	public String getStartFieldName() {
		return startFieldName;
	}

	public void setStartFieldName(String startFieldName) {
		this.startFieldName = startFieldName;
	}

	public Field getStartField() {
		return startField;
	}

	public void setStartField(Field startField) {
		this.startField = startField;
	}

	public String getEndFieldName() {
		return endFieldName;
	}

	public void setEndFieldName(String endFieldName) {
		this.endFieldName = endFieldName;
	}

	public Field getEndField() {
		return endField;
	}

	public void setEndField(Field endField) {
		this.endField = endField;
	}

	public Map getValueRange() {
		return valueRange;
	}

//	public void setValueRange(Map valueRange) {
//		this.valueRange = valueRange;
//	}

	public String getRowNumFieldName() {
		return rowNumFieldName;
	}

	public void setRowNumFieldName(String rowNumFieldName) {
		this.rowNumFieldName = rowNumFieldName;
	}

	public Field getRowNumField() {
		return rowNumField;
	}

	public void setRowNumField(Field rowNumField) {
		this.rowNumField = rowNumField;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Field getTable() {
		return table;
	}

	public void setTable(Field table) {
		this.table = table;
	}

	public Field getSubField(String s) {
		return (Field) subFields.get(s);
	}

//	public void setSubField(String s, Field field) {
//		subFields.put(s, field);
//	}

	public boolean equalTo(Field field) {
		label0: {
			label1: {
				if (null == field)
					return false;
				if (!name.equalsIgnoreCase(field.getName()))
					return false;
				if (fieldType != field.getFieldType())
					return false;
				if (padding != field.getPadding())
					return false;
				if (paddingDirection != field.getPaddingDirection())
					return false;
				if (required ^ field.isRequired())
					return false;
				if (editable ^ field.isEditable())
					return false;
				if ((null == xpath) ^ (null == field.getXpath()))
					return false;
				if (null != xpath && null != field.getXpath() && !xpath.equalsIgnoreCase(field.getXpath()))
					return false;
				if (iso8583_no != field.getIso8583_no())
					return false;
				if ((null == prefix) ^ (null == field.getPrefix()))
					return false;
				if (null != prefix && null != field.getPrefix() && !Arrays.equals(prefix, field.getPrefix()))
					return false;
				if (firRowPrefix ^ field.isFirRowPrefix())
					return false;
				if ((null == suffix) ^ (null == field.getSuffix()))
					return false;
				if (null != suffix && null != field.getSuffix() && !Arrays.equals(suffix, field.getSuffix()))
					return false;
				if (lastRowSuffix ^ field.isLastRowSuffix())
					return false;
				if ((null == extendedAttributeText) ^ (null == field.getExtendedAttributeText()))
					return false;
				if (null != extendedAttributeText && null != field.getExtendedAttributeText()
						&& !extendedAttributeText.equalsIgnoreCase(field.getExtendedAttributeText()))
					return false;
				if ((null == dataCharset) ^ (null == field.getDataCharset()))
					return false;
				if (null != dataCharset && null != field.getDataCharset()
						&& !dataCharset.equalsIgnoreCase(field.getDataCharset()))
					return false;
				if ((null == tag) ^ (null == field.getTag()))
					return false;
				if (null != tag && null != field.getTag() && !tag.equalsIgnoreCase(field.getTag()))
					return false;
				if (isRemoveUnwatchable ^ field.isRemoveUnwatchable())
					return false;
				if (2001 == fieldType || 2003 == fieldType || 2009 == fieldType || 2011 == fieldType) {
					if ((null == refLengthFieldName) ^ (null == field.getRefLengthFieldName()))
						return false;
					if (null != refLengthFieldName && null != field.getRefLengthFieldName()
							&& !refLengthFieldName.equalsIgnoreCase(field.getRefLengthFieldName()))
						return false;
					if (refLengthFieldOffset != field.getRefLengthFieldOffset())
						return false;
					if (2011 == fieldType) {
						if ((null == tabPrefix) ^ (null == field.getTabPrefix()))
							return false;
						if (null != tabPrefix && null != field.getTabPrefix()
								&& !Arrays.equals(tabPrefix, field.getTabPrefix()))
							return false;
						if ((null == tabSuffix) ^ (null == field.getTabSuffix()))
							return false;
						if (null != tabSuffix && null != field.getTabSuffix()
								&& !Arrays.equals(tabSuffix, field.getTabSuffix()))
							return false;
						if (rowCut ^ field.isRowCut())
							return false;
					} else {
						if (lengthFieldDataType != field.getLengthFieldDataType())
							return false;
						if (lengthFieldDataEncoding != field.getLengthFieldDataEncoding())
							return false;
						if (lengthFieldLength != field.getLengthFieldLength())
							return false;
					}
					if (maxLength != field.getMaxLength())
						return false;
					if (minLength != field.getMinLength())
						return false;
				}
				if (2002 == fieldType || 2003 == fieldType || 2004 == fieldType || 2011 == fieldType
						|| 2008 == fieldType || 2009 == fieldType) {
					if ((null == reference) ^ (null == field.getReference()))
						return false;
					if (null != reference && null != field.getReference() && !reference.equalTo(field.getReference()))
						return false;
					if ((null == referenceId) ^ (null == field.getReferenceId()))
						return false;
					if (null != referenceId && null != field.getReferenceId()
							&& !referenceId.equalsIgnoreCase(field.getReferenceId()))
						return false;
					if ((null == referenceType) ^ (null == field.getReferenceType()))
						return false;
					if (null != referenceType && null != field.getReferenceType()
							&& !referenceType.equalsIgnoreCase(field.getReferenceType()))
						return false;
					if (dataEncoding != field.getDataEncoding())
						return false;
					if (2004 == fieldType) {
						if ((null == rowNumFieldName) ^ (null == field.getRowNumFieldName()))
							return false;
						if (null != rowNumFieldName && null != field.getRowNumFieldName()
								&& !rowNumFieldName.equalsIgnoreCase(field.getRowNumFieldName()))
							return false;
						if ((null == rowXpath) ^ (null == field.getRowXpath()))
							return false;
						if (null != rowXpath && null != field.getRowXpath()
								&& !rowXpath.equalsIgnoreCase(field.getRowXpath()))
							return false;
					}
					break label0;
				}
				if (dataType != field.getDataType())
					return false;
				if (dataEncoding != field.getDataEncoding())
					return false;
				if ((null == pattern) ^ (null == field.getPattern()))
					return false;
				if (null != pattern && null != field.getPattern() && !pattern.equalsIgnoreCase(field.getPattern()))
					return false;
				if ((null == lengthScript) ^ (null == field.getLengthScript()))
					return false;
				if (null != lengthScript && null != field.getLengthScript()
						&& !lengthScript.equalsIgnoreCase(field.getLengthScript()))
					return false;
				if (strictDataLength ^ field.isStrictDataLength())
					return false;
				if (2007 == fieldType) {
					if (!startFieldName.equalsIgnoreCase(field.getStartFieldName()))
						return false;
					if (!endFieldName.equalsIgnoreCase(field.getEndFieldName()))
						return false;
				}
				if (2010 != fieldType)
					break label1;
				if ((null == startFieldName) ^ (null == field.getStartFieldName()))
					return false;
				if (null != startFieldName && null != field.getStartFieldName()
						&& !startFieldName.equalsIgnoreCase(field.getStartFieldName()))
					return false;
				if ((null == endFieldName) ^ (null == field.getEndFieldName()))
					return false;
				if (null != endFieldName && null != field.getEndFieldName()
						&& !endFieldName.equalsIgnoreCase(field.getEndFieldName()))
					return false;
				if (!calcType.equalsIgnoreCase(field.getCalcType()))
					return false;
				if ((null == macFldDataCache) ^ (null == field.getMacFldDataCache()))
					return false;
				if (null == macFldDataCache || null == field.getMacFldDataCache())
					break label1;
				Iterator iterator = field.getMacFldDataCache().values().iterator();
				String s = null;
				do {
					if (!iterator.hasNext())
						break label1;
					s = (String) iterator.next();
				} while (s.equalsIgnoreCase((String) macFldDataCache.get(s)));
				return false;
			}
			if (2005 == fieldType) {
				if ((null == tableName) ^ (null == field.getTableName()))
					return false;
				if (null != tableName && null != field.getTableName()
						&& !tableName.equalsIgnoreCase(field.getTableName()))
					return false;
			}
			if (2001 != fieldType && length != field.getLength())
				return false;
			if (2000 == fieldType || 2005 == fieldType) {
				if ((null == value) ^ (null == field.getValue()))
					return false;
				if (null != value && null != field.getValue() && !value.equalsIgnoreCase(field.getValue()))
					return false;
			}
		}
		label2: {
			if ((null == combineOrTableFieldClassName) ^ (null == field.getCombineOrTableFieldClassName()))
				return false;
			if (null != combineOrTableFieldClassName && null != field.getCombineOrTableFieldClassName()
					&& !combineOrTableFieldClassName.equalsIgnoreCase(field.getCombineOrTableFieldClassName()))
				return false;
			if ((null == subFields) ^ (null == field.getSubFields()))
				return false;
			if (null == subFields || null == field.getSubFields())
				break label2;
			Iterator iterator1 = subFields.values().iterator();
			Field field1 = null;
			do {
				if (!iterator1.hasNext())
					break label2;
				field1 = (Field) iterator1.next();
			} while (field1.equalTo(field.getSubField(field1.getName())));
			return false;
		}
		label3: {
			if ((null == valueRange) ^ (null == field.getValueRange()))
				return false;
			if (null == valueRange || null == field.getValueRange())
				break label3;
			Iterator iterator2 = field.getValueRange().values().iterator();
			ValueRange valuerange = null;
			do {
				if (!iterator2.hasNext())
					break label3;
				valuerange = (ValueRange) iterator2.next();
			} while (valuerange.equalsTo((ValueRange) valueRange.get(valuerange.getValue())));
			return false;
		}
		if ((null == prePackEvent) ^ (null == field.getPrePackEvent()))
			return false;
		if (null != prePackEvent && null != field.getPrePackEvent()
				&& !prePackEvent.equalsIgnoreCase(field.getPrePackEvent()))
			return false;
		if ((null == postPackEvent) ^ (null == field.getPostPackEvent()))
			return false;
		if (null != postPackEvent && null != field.getPostPackEvent()
				&& !postPackEvent.equalsIgnoreCase(field.getPostPackEvent()))
			return false;
		if ((null == preParseEvent) ^ (null == field.getPreParseEvent()))
			return false;
		if (null != preParseEvent && null != field.getPreParseEvent()
				&& !preParseEvent.equalsIgnoreCase(field.getPreParseEvent()))
			return false;
		if ((null == postParseEvent) ^ (null == field.getPostParseEvent()))
			return false;
		if (null != postParseEvent && null != field.getPostParseEvent()
				&& !postParseEvent.equalsIgnoreCase(field.getPostParseEvent()))
			return false;
		if ((null == preRowPackEvent) ^ (null == field.getPreRowPackEvent()))
			return false;
		if (null != preRowPackEvent && null != field.getPreRowPackEvent()
				&& !preRowPackEvent.equalsIgnoreCase(field.getPreRowPackEvent()))
			return false;
		if ((null == postRowPackEvent) ^ (null == field.getPostRowPackEvent()))
			return false;
		if (null != postRowPackEvent && null != field.getPostRowPackEvent()
				&& !postRowPackEvent.equalsIgnoreCase(field.getPostRowPackEvent()))
			return false;
		if ((null == preRowParseEvent) ^ (null == field.getPreRowParseEvent()))
			return false;
		if (null != preRowParseEvent && null != field.getPreRowParseEvent()
				&& !preRowParseEvent.equalsIgnoreCase(field.getPreRowParseEvent()))
			return false;
		if ((null == postRowParseEvent) ^ (null == field.getPostRowParseEvent()))
			return false;
		if (null != postRowParseEvent && null != field.getPostRowParseEvent()
				&& !postRowParseEvent.equalsIgnoreCase(field.getPostRowParseEvent()))
			return false;
		if ((null == expression) ^ (null == field.getExpression()))
			return false;
		return null == expression || null == field.getExpression()
				|| expression.equalsIgnoreCase(field.getExpression());
	}

	public Field copy() {
		Field field = new Field();
		field.setName(name);
		field.setFieldType(fieldType);
		field.setIso8583_no(iso8583_no);
		field.setReference(reference);
		field.setReferenceId(referenceId);
		field.setReferenceType(referenceType);
		field.setXpath(xpath);
		field.setRowXpath(rowXpath);
		field.setEditable(editable);
		field.setStrictDataLength(strictDataLength);
		field.setRequired(required);
		field.setDataType(dataType);
		field.setDataEncoding(dataEncoding);
		field.setLength(length);
		field.setLengthScript(lengthScript);
		field.setValue(value);
		field.setPattern(pattern);
		field.setLengthFieldDataType(lengthFieldDataType);
		field.setLengthFieldDataEncoding(lengthFieldDataEncoding);
		field.setLengthFieldLength(lengthFieldLength);
		field.setMaxLength(maxLength);
		field.setMinLength(minLength);
		field.setRefLengthFieldName(refLengthFieldName);
		field.setRefLengthField(refLengthField);
		field.setRefLengthFieldOffset(refLengthFieldOffset);
		field.setStartField(startField);
		field.setStartFieldName(startFieldName);
		field.setEndField(endField);
		field.setEndFieldName(endFieldName);
		field.setPrefix(prefix);
		field.setTabPrefix(tabPrefix);
		field.setFirRowPrefix(firRowPrefix);
		field.setSuffix(suffix);
		field.setTabSuffix(tabSuffix);
		field.setLastRowSuffix(lastRowSuffix);
		field.setRowCut(rowCut);
		field.setPadding(padding);
		field.setPaddingDirection(paddingDirection);
		field.setSubFields(subFields);
		field.setCombineOrTableFieldClassName(combineOrTableFieldClassName);
		field.setValueRange(valueRange);
		field.setRowNumFieldName(rowNumFieldName);
		field.setRowNumField(rowNumField);
		field.setTableName(tableName);
		field.setTable(table);
		field.setPrePackEvent(prePackEvent);
		field.setPostPackEvent(postPackEvent);
		field.setPreParseEvent(preParseEvent);
		field.setPostParseEvent(postParseEvent);
		field.setPreRowPackEvent(preRowPackEvent);
		field.setPostRowPackEvent(postRowPackEvent);
		field.setPreRowParseEvent(preRowParseEvent);
		field.setPostRowParseEvent(postRowParseEvent);
		field.setTag(tag);
		field.setCalcType(calcType);
		field.setExtendedAttributeText(extendedAttributeText);
		field.setExtendedAttributes(extendedAttributes);
		field.setMacFldDataCache(macFldDataCache);
		field.setExpression(expression);
		field.setDataCharset(dataCharset);
		field.setRemoveUnwatchable(isRemoveUnwatchable);
		return field;
	}
}
