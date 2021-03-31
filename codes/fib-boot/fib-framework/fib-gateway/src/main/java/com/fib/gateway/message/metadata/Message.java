package com.fib.gateway.message.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fib.gateway.message.util.EnumConstants;

import lombok.Data;

/**
 * 消息信息
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
@Data
public class Message {
	private String databaseMessageId;
	private String groupId;

	/** 报文id */
	private String id;

	/** 报文类型，默认common */
	private int type = EnumConstants.MsgType.COMMON.getCode();

	/** 描述信息 */
	private String shortText;

	/** 报文对应的 MessageBean类名 */
	private String className;
	private Map<String, Field> fields = new LinkedHashMap<>();

	/** XPath表达式 */
	private String xpath;

	/***/
	private String template;
	private Map<String, String> nameSpaces = new HashMap<>();
	private String prePackEvent;
	private String postPackEvent;
	private String preParseEvent;
	private String postParseEvent;

	private boolean schemaValid = false;
	private int schemaValidType;
	private String schemaValidPath;

	private String prefixString;

	/** 前缀 */
	private byte[] prefix;
	private String suffixString;

	/** 后缀 */
	private byte[] suffix;
	private Map<String, Variable> variable = new HashMap<>();
	private String msgCharset;

	private String extendedAttributeText;
	private Map<String, String> extendedAttributes = new HashMap<>();
	private boolean removeBlankNode = false;

	public List<Field> getFieldList() {
		List<Field> var1 = new ArrayList<>(this.fields.size());
		var1.addAll(this.fields.values());
		return var1;
	}

	public Field getField(String var1) {
		return this.fields.get(var1);
	}

	public void setField(String var1, Field var2) {
		this.fields.put(var1, var2);
	}

	public String getExtendAttribute(String var1) {
		return null == this.extendedAttributes ? null : this.extendedAttributes.get(var1);
	}

	public void setExtendAttribute(String var1, String var2) {
		if (null == this.extendedAttributes) {
			this.extendedAttributes = new HashMap<>(3);
		}

		this.extendedAttributes.put(var1, var2);
	}

	public boolean equalTo(Message var1) {
		if (null == var1) {
			return false;
		} else if (!this.id.equalsIgnoreCase(var1.getId())) {
			return false;
		} else if (this.type != var1.getType()) {
			return false;
		} else if (!this.className.equalsIgnoreCase(var1.getClassName())) {
			return false;
		} else if (null == this.xpath ^ null == var1.getXpath()) {
			return false;
		} else if (null != this.xpath && null != var1.getXpath() && !this.xpath.equalsIgnoreCase(var1.getXpath())) {
			return false;
		} else if (this.schemaValidType != var1.getSchemaValidType()) {
			return false;
		} else if (null != this.schemaValidPath && !this.schemaValidPath.equalsIgnoreCase(var1.getSchemaValidPath())) {
			return false;
		} else {
			if (1002 == var1.getType() || 1003 == var1.getType()) {
				if (null == this.prefix ^ null == var1.getPrefix()) {
					return false;
				}

				if (null != this.prefix && null != var1.getPrefix() && !Arrays.equals(this.prefix, var1.getPrefix())) {
					return false;
				}

				if (null == this.suffix ^ null == var1.getSuffix()) {
					return false;
				}

				if (null != this.suffix && null != var1.getSuffix() && !Arrays.equals(this.suffix, var1.getSuffix())) {
					return false;
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
			} else if (null == this.template ^ null == var1.getTemplate()) {
				return false;
			} else if (null != this.template && null != var1.getTemplate()
					&& !this.template.equalsIgnoreCase(var1.getTemplate())) {
				return false;
			} else if (null == this.variable ^ null == var1.getVariable()) {
				return false;
			} else {
				Iterator<?> var2;
				Variable var3;
				if (null != this.variable && null != var1.getVariable()) {
					if (this.variable.size() != var1.getVariable().size()) {
						return false;
					}

					var2 = var1.getVariable().values().iterator();

					while (var2.hasNext()) {
						var3 = (Variable) var2.next();
						if (!var3.equalsTo(this.variable.get(var3.getName()))) {
							return false;
						}
					}
				}

				var2 = this.fields.values().iterator();

				Field var4;
				do {
					if (!var2.hasNext()) {
						return true;
					}

					var4 = (Field) var2.next();
				} while (var4.equalTo(var1.getField(var4.getName())));

				return false;
			}
		}
	}

	public Message copy() {
		Message var1 = new Message();
		var1.setGroupId(this.getGroupId());
		var1.setId(this.getId());
		var1.setType(this.type);
		var1.setClassName(this.getClassName());
		var1.setFields(this.getFields());
		var1.setPrePackEvent(this.getPrePackEvent());
		var1.setPostPackEvent(this.getPostPackEvent());
		var1.setPreParseEvent(this.getPreParseEvent());
		var1.setPostParseEvent(this.getPostParseEvent());
		var1.setXpath(this.getXpath());
		var1.setTemplate(this.getTemplate());
		var1.setPrefix(this.getPrefix());
		var1.setSuffix(this.getSuffix());
		var1.setVariable(this.getVariable());
		var1.setMsgCharset(this.getMsgCharset());
		var1.setSchemaValid(this.isSchemaValid());
		var1.setSchemaValidType(this.getSchemaValidType());
		var1.setSchemaValidPath(this.getSchemaValidPath());
		return var1;
	}
}
