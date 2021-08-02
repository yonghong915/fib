package com.fib.upp.service.gateway.message.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fib.upp.service.gateway.constant.EnumConstants;

import lombok.Data;

@Data
public class MessageMetaData {
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
}
