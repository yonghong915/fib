package com.fib.upp.parse;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public class Message {
	private String groupId;
	private String id;

	/** 报文类型，默认common */
	private int type;
	private String shortText;
	private String className;
	private Map<String, Field> fields = null;
	private String prePackEvent;
	private String postPackEvent;
	private String preParseEvent;
	private String postParseEvent;
	private String xpath;
	private String template;
	private String prefixString;
	private byte[] prefix;
	private String suffixString;
	private byte[] suffix;
	private String msgCharset;

	public Message() {
		this.fields = new LinkedHashMap<>(32);
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, Field> getFields() {
		return fields;
	}

	public void setFields(Map<String, Field> fields) {
		this.fields = fields;
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

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
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

	public String getMsgCharset() {
		return msgCharset;
	}

	public void setMsgCharset(String msgCharset) {
		this.msgCharset = msgCharset;
	}

	public void setField(String s, Field field) {
		fields.put(s, field);
	}

	@Override
	public String toString() {
		return "Message [groupId=" + groupId + ", id=" + id + ", type=" + type + ", shortText=" + shortText + ", className=" + className + ", fields="
				+ fields + ", prePackEvent=" + prePackEvent + ", postPackEvent=" + postPackEvent + ", preParseEvent=" + preParseEvent
				+ ", postParseEvent=" + postParseEvent + ", xpath=" + xpath + ", template=" + template + ", prefixString=" + prefixString
				+ ", prefix=" + Arrays.toString(prefix) + ", suffixString=" + suffixString + ", suffix=" + Arrays.toString(suffix) + ", msgCharset="
				+ msgCharset + "]";
	}
}