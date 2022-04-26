package com.fib.upp.message.metadata;

import java.util.LinkedHashMap;
import java.util.Map;

import org.checkerframework.checker.units.qual.K;

public class Field {
	private String name;
	private int fieldType;
	private String xpath;
	private String shortText;
	private int dataType;
	private int dataEncoding;
	private int length;

	private Message reference;
	private String referenceId;
	private String referenceType;
	private Map<String, Field> subFields;

	public Field() {
		this.subFields = new LinkedHashMap<>(32);
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

	public Map<String, Field> getSubFields() {
		return subFields;
	}

	public void setSubFields(Map<String, Field> sorthashmap) {
		subFields = sorthashmap;
	}
}
