package com.fib.msgconverter.message.metadata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fib.commons.util.SortHashMap;
import com.fib.msgconverter.message.metadata.Message;

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
	private Map<String, ValueRange> valueRange;
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

	public Field() {
		iso8583_no = -1;
		editable = true;
		isRemoveUnwatchable = true;
		strictDataLength = false;
		required = false;
		dataEncoding = 4000;
		lengthFieldDataType = 0;
		lengthFieldDataEncoding = 4000;
		lengthFieldLength = 0;
		maxLength = -1;
		minLength = -1;
		refLengthFieldOffset = 0;
		firRowPrefix = false;
		lastRowSuffix = false;
		rowCut = true;
		padding = 32;
		paddingDirection = 5002;
		subFields = new SortHashMap(32);
		valueRange = new HashMap();
		extendedAttributes = new HashMap();
		macFldDataCache = new HashMap();
	}

	public boolean isShield() {
		return shield;
	}

	public void setShield(boolean flag) {
		shield = flag;
	}

	public String getRefValueRange() {
		return refValueRange;
	}

	public void setRefValueRange(String s) {
		refValueRange = s;
	}

	public String getName() {
		return name;
	}

	public void setName(String s) {
		name = s;
	}

	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int i) {
		fieldType = i;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean flag) {
		editable = flag;
	}

	public boolean isRemoveUnwatchable() {
		return isRemoveUnwatchable;
	}

	public void setRemoveUnwatchable(boolean flag) {
		isRemoveUnwatchable = flag;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean flag) {
		required = flag;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String s) {
		shortText = s;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int i) {
		dataType = i;
	}

	public int getDataEncoding() {
		return dataEncoding;
	}

	public void setDataEncoding(int i) {
		dataEncoding = i;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int i) {
		length = i;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String s) {
		value = s;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String s) {
		pattern = s;
	}

	public int getLengthFieldDataType() {
		return lengthFieldDataType;
	}

	public void setLengthFieldDataType(int i) {
		lengthFieldDataType = i;
	}

	public int getLengthFieldDataEncoding() {
		return lengthFieldDataEncoding;
	}

	public void setLengthFieldDataEncoding(int i) {
		lengthFieldDataEncoding = i;
	}

	public int getLengthFieldLength() {
		return lengthFieldLength;
	}

	public void setLengthFieldLength(int i) {
		lengthFieldLength = i;
	}

	public Field getRefLengthField() {
		return refLengthField;
	}

	public void setRefLengthField(Field field) {
		refLengthField = field;
	}

	public int getRefLengthFieldOffset() {
		return refLengthFieldOffset;
	}

	public void setRefLengthFieldOffset(int i) {
		refLengthFieldOffset = i;
	}

	public byte[] getPrefix() {
		return prefix;
	}

	public void setPrefix(byte abyte0[]) {
		prefix = abyte0;
	}

	public byte[] getSuffix() {
		return suffix;
	}

	public void setSuffix(byte abyte0[]) {
		suffix = abyte0;
	}

	public byte getPadding() {
		return padding;
	}

	public void setPadding(byte byte0) {
		padding = byte0;
	}

	public int getPaddingDirection() {
		return paddingDirection;
	}

	public void setPaddingDirection(int i) {
		paddingDirection = i;
	}

	public SortHashMap<String,Field> getSubFields() {
		return subFields;
	}

	public void setSubFields(SortHashMap sorthashmap) {
		subFields = sorthashmap;
	}

	public int getIso8583_no() {
		return iso8583_no;
	}

	public void setIso8583_no(int i) {
		iso8583_no = i;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String s) {
		xpath = s;
	}

	public String getRefLengthFieldName() {
		return refLengthFieldName;
	}

	public void setRefLengthFieldName(String s) {
		refLengthFieldName = s;
	}

	public Map<String, ValueRange> getValueRange() {
		return valueRange;
	}

	public void setValueRange(Map<String, ValueRange> map) {
		valueRange = map;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String s) {
		referenceId = s;
	}

	public void setReference(Message message) {
		reference = message;
	}

	public String getCombineOrTableFieldClassName() {
		return combineOrTableFieldClassName;
	}

	public void setCombineOrTableFieldClassName(String s) {
		combineOrTableFieldClassName = s;
	}

	public Message getReference() {
		return reference;
	}

	public boolean isStrictDataLength() {
		return strictDataLength;
	}

	public void setStrictDataLength(boolean flag) {
		strictDataLength = flag;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int i) {
		maxLength = i;
	}

	public String getStartFieldName() {
		return startFieldName;
	}

	public void setStartFieldName(String s) {
		startFieldName = s;
	}

	public Field getStartField() {
		return startField;
	}

	public void setStartField(Field field) {
		startField = field;
	}

	public String getEndFieldName() {
		return endFieldName;
	}

	public void setEndFieldName(String s) {
		endFieldName = s;
	}

	public Field getEndField() {
		return endField;
	}

	public void setEndField(Field field) {
		endField = field;
	}

	public Field getSubField(String s) {
		return (Field) subFields.get(s);
	}

	public void setSubField(String s, Field field) {
		subFields.put(s, field);
	}

	public String getRowNumFieldName() {
		return rowNumFieldName;
	}

	public void setRowNumFieldName(String s) {
		rowNumFieldName = s;
	}

	public Field getRowNumField() {
		return rowNumField;
	}

	public void setRowNumField(Field field) {
		rowNumField = field;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String s) {
		tableName = s;
	}

	public Field getTable() {
		return table;
	}

	public void setTable(Field field) {
		table = field;
	}

	public String getPrePackEvent() {
		return prePackEvent;
	}

	public void setPrePackEvent(String s) {
		prePackEvent = s;
	}

	public String getPostPackEvent() {
		return postPackEvent;
	}

	public void setPostPackEvent(String s) {
		postPackEvent = s;
	}

	public String getPreParseEvent() {
		return preParseEvent;
	}

	public void setPreParseEvent(String s) {
		preParseEvent = s;
	}

	public String getPostParseEvent() {
		return postParseEvent;
	}

	public void setPostParseEvent(String s) {
		postParseEvent = s;
	}

	public String getRowXpath() {
		return rowXpath;
	}

	public void setRowXpath(String s) {
		rowXpath = s;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String s) {
		tag = s;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int i) {
		minLength = i;
	}

	public String getCalcType() {
		return calcType;
	}

	public void setCalcType(String s) {
		calcType = s;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String s) {
		referenceType = s;
	}

	public String getPostRowPackEvent() {
		return postRowPackEvent;
	}

	public void setPostRowPackEvent(String s) {
		postRowPackEvent = s;
	}

	public String getPostRowParseEvent() {
		return postRowParseEvent;
	}

	public void setPostRowParseEvent(String s) {
		postRowParseEvent = s;
	}

	public String getPreRowPackEvent() {
		return preRowPackEvent;
	}

	public void setPreRowPackEvent(String s) {
		preRowPackEvent = s;
	}

	public String getPreRowParseEvent() {
		return preRowParseEvent;
	}

	public void setPreRowParseEvent(String s) {
		preRowParseEvent = s;
	}

	public boolean isFirRowPrefix() {
		return firRowPrefix;
	}

	public void setFirRowPrefix(boolean flag) {
		firRowPrefix = flag;
	}

	public boolean isLastRowSuffix() {
		return lastRowSuffix;
	}

	public void setLastRowSuffix(boolean flag) {
		lastRowSuffix = flag;
	}

	public Map getExtendedAttributes() {
		return extendedAttributes;
	}

	public void setExtendedAttributes(Map map) {
		extendedAttributes = map;
	}

	public String getExtendedAttributeText() {
		return extendedAttributeText;
	}

	public void setExtendedAttributeText(String s) {
		extendedAttributeText = s;
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

	public String getLengthScript() {
		return lengthScript;
	}

	public void setLengthScript(String s) {
		lengthScript = s;
	}

	public Map getMacFldDataCache() {
		return macFldDataCache;
	}

	public void setMacFldDataCache(Map map) {
		macFldDataCache = map;
	}

	public byte[] getTabPrefix() {
		return tabPrefix;
	}

	public void setTabPrefix(byte abyte0[]) {
		tabPrefix = abyte0;
	}

	public byte[] getTabSuffix() {
		return tabSuffix;
	}

	public void setTabSuffix(byte abyte0[]) {
		tabSuffix = abyte0;
	}

	public boolean isRowCut() {
		return rowCut;
	}

	public void setRowCut(boolean flag) {
		rowCut = flag;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String s) {
		expression = s;
	}

	public String getDataCharset() {
		return dataCharset;
	}

	public void setDataCharset(String s) {
		dataCharset = s;
	}

	public String getPrefixString() {
		return prefixString;
	}

	public void setPrefixString(String s) {
		prefixString = s;
	}

	public String getTabPrefixString() {
		return tabPrefixString;
	}

	public void setTabPrefixString(String s) {
		tabPrefixString = s;
	}

	public String getSuffixString() {
		return suffixString;
	}

	public void setSuffixString(String s) {
		suffixString = s;
	}

	public String getTabSuffixString() {
		return tabSuffixString;
	}

	public void setTabSuffixString(String s) {
		tabSuffixString = s;
	}

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
