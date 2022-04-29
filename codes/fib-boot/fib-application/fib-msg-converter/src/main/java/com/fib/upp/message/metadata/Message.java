package com.fib.upp.message.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.giantstone.common.util.SortHashMap;

public class Message {

	private String databaseMessageId;
	private String groupId;
	private String id;

	/** 报文类型，默认common */
	private int type;
	private String shortText;
	private String className;
	private SortHashMap fields;
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
	private Map<String, Variable> variable;
	private String msgCharset;

	public Message() {
		type = ConstantMB.MessageType.COMMON.getCode();
		fields = new SortHashMap(32);
		variable = new HashMap<>();
	}

	public Field getField(String s) {
		return (Field) fields.get(s);
	}

	public void setField(String s, Field field) {
		fields.put(s, field);
	}

	public String getId() {
		return id;
	}

	public void setId(String s) {
		id = s;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String s) {
		shortText = s;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String s) {
		className = s;
	}

	public List<Field> getFieldList() {
		List<Field> arraylist = new ArrayList<>(fields.size());
		arraylist.addAll(fields.values());
		return arraylist;
	}

	public SortHashMap getFields() {
		return fields;
	}

	public void setFields(SortHashMap sorthashmap) {
		fields = sorthashmap;
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

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String s) {
		xpath = s;
	}

	public int getType() {
		return type;
	}

	public void setType(int i) {
		type = i;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String s) {
		template = s;
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

	public Map<String, Variable> getVariable() {
		return variable;
	}

	public void setVariable(Map<String, Variable> map) {
		variable = map;
	}

	public boolean equalTo(Message message) {
		label0: {
			if (null == message)
				return false;
			if (!id.equalsIgnoreCase(message.getId()))
				return false;
			if (type != message.getType())
				return false;
			if (!className.equalsIgnoreCase(message.getClassName()))
				return false;
			if ((null == xpath) ^ (null == message.getXpath()))
				return false;
			if (null != xpath && null != message.getXpath() && !xpath.equalsIgnoreCase(message.getXpath()))
				return false;
			if (1002 == message.getType() || 1003 == message.getType()) {
				if ((null == prefix) ^ (null == message.getPrefix()))
					return false;
				if (null != prefix && null != message.getPrefix() && !Arrays.equals(prefix, message.getPrefix()))
					return false;
				if ((null == suffix) ^ (null == message.getSuffix()))
					return false;
				if (null != suffix && null != message.getSuffix() && !Arrays.equals(suffix, message.getSuffix()))
					return false;
			}
			if ((null == prePackEvent) ^ (null == message.getPrePackEvent()))
				return false;
			if (null != prePackEvent && null != message.getPrePackEvent() && !prePackEvent.equalsIgnoreCase(message.getPrePackEvent()))
				return false;
			if ((null == postPackEvent) ^ (null == message.getPostPackEvent()))
				return false;
			if (null != postPackEvent && null != message.getPostPackEvent() && !postPackEvent.equalsIgnoreCase(message.getPostPackEvent()))
				return false;
			if ((null == preParseEvent) ^ (null == message.getPreParseEvent()))
				return false;
			if (null != preParseEvent && null != message.getPreParseEvent() && !preParseEvent.equalsIgnoreCase(message.getPreParseEvent()))
				return false;
			if ((null == postParseEvent) ^ (null == message.getPostParseEvent()))
				return false;
			if (null != postParseEvent && null != message.getPostParseEvent() && !postParseEvent.equalsIgnoreCase(message.getPostParseEvent()))
				return false;
			if ((null == template) ^ (null == message.getTemplate()))
				return false;
			if (null != template && null != message.getTemplate() && !template.equalsIgnoreCase(message.getTemplate()))
				return false;
			if ((null == variable) ^ (null == message.getVariable()))
				return false;
			if (null == variable || null == message.getVariable())
				break label0;
			if (variable.size() != message.getVariable().size())
				return false;
			Iterator<Variable> iterator = message.getVariable().values().iterator();
			Variable a = null;
			do {
				if (!iterator.hasNext())
					break label0;
				a = iterator.next();
			} while (a.equals(variable.get(a.getName())));
			return false;
		}
		Iterator<Field> iterator1 = fields.values().iterator();
		while (iterator1.hasNext()) {
			Field field = iterator1.next();
			if (!field.equalTo(message.getField(field.getName())))
				return false;
		}
		return true;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String s) {
		groupId = s;
	}

	public String getMsgCharset() {
		return msgCharset;
	}

	public void setMsgCharset(String s) {
		msgCharset = s;
	}

	public String getPrefixString() {
		return prefixString;
	}

	public void setPrefixString(String s) {
		prefixString = s;
	}

	public String getSuffixString() {
		return suffixString;
	}

	public void setSuffixString(String s) {
		suffixString = s;
	}

	public String getDatabaseMessageId() {
		return databaseMessageId;
	}

	public void setDatabaseMessageId(String s) {
		databaseMessageId = s;
	}

	public Message copy() {
		Message message = new Message();
		message.setGroupId(getGroupId());
		message.setId(getId());
		message.setType(type);
		message.setClassName(getClassName());
		message.setFields(getFields());
		message.setPrePackEvent(getPrePackEvent());
		message.setPostPackEvent(getPostPackEvent());
		message.setPreParseEvent(getPreParseEvent());
		message.setPostParseEvent(getPostParseEvent());
		message.setXpath(getXpath());
		message.setTemplate(getTemplate());
		message.setPrefix(getPrefix());
		message.setSuffix(getSuffix());
		message.setVariable(getVariable());
		message.setMsgCharset(getMsgCharset());
		return message;
	}
}
