package com.fib.upp.message.metadata;

import com.fib.commons.exception.CommonException;

public interface ConstantMB {

	public enum MessageBeanAttr {

		/** 2000 id */
		ID(2000, "id", ""),

		/** 2001 short-text */
		SHORT_TEXT(2001, "short-text", ""),

		/** 2002 message-charset */
		MESSAGE_CHARSET(2002, "message-charset", ""),

		/** 2003 class */
		CLASS(2003, "class", ""),

		/** 2004 type */
		TYPE(2004, "type", ""),

		/** 2005 xpath */
		XPATH(2005, "xpath", ""),

		/** 2006 pack-mode */
		PACK_MODE(2006, "pack-mode", ""),

		/** 2006 prefix */
		PREFIX(2006, "prefix", ""),
		/** 2006 suffix */
		SUFFIX(2006, "suffix", "");

		private int code;
		private String name;
		private String text;

		MessageBeanAttr(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}
	}

	public enum FieldType {

		/** 2000 fixed-field */
		FIXED_FIELD(2000, "fixed-field", ""),

		/** 2001 var-field */
		VAR_FIELD(2001, "var-field", ""),

		/** 2002 combine-field */
		COMBINE_FIELD(2002, "combine-field", ""),

		/** 2003 var-combine-field */
		VAR_COMBINE_FIELD(2003, "var-combine-field", ""),

		/** 2004 table */
		TABLE(2004, "table", ""),

		/** 2005 table-row-num-field */
		TABLE_ROW_NUM_FIELD(2005, "table-row-num-field", ""),

		/** 2006 bitmap-field */
		BITMAP_FIELD(2006, "bitmap-field", ""),

		/** 2007 length-field */
		LENGTH_FIELD(2007, "length-field", ""),

		/** 2008 reference-field */
		REFERENCE_FIELD(2008, "reference-field", ""),

		/** 2009 var-reference-field */
		VAR_REFERENCE_FIELD(2009, "var-reference-field", ""),

		/** 2010 mac-field */
		MAC_FIELD(2010, "mac-field", ""),

		/** 2011 var-table */
		VAR_TABLE(2011, "var-table", "");

		private int code;
		private String name;
		private String text;

		FieldType(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}

		public static FieldType getFieldTypeByCode(int code) {
			for (FieldType mt : values()) {
				if (code == mt.getCode()) {
					return mt;
				}
			}
			throw new CommonException("not support FieldType " + code);
		}

		public static FieldType getFieldTypeByName(String name) {
			for (FieldType mt : values()) {
				if (mt.getName().equals(name)) {
					return mt;
				}
			}
			throw new CommonException("FieldType.notExist ");
		}
	}

	public enum EventType {

		/** 2000 pre-pack */
		PRE_PACK(2000, "pre-pack", ""),

		/** 2001 post-pack */
		POST_PACK(2001, "post-pack", ""),

		/** 2002 pre-parse */
		PRE_PARSE(2002, "pre-parse", ""),

		/** 2003 post-parse */
		POST_PARSE(2003, "post-parse", ""),

		/** 2004 row-pre-pack */
		ROW_PRE_PACK(2004, "row-pre-pack", ""),

		/** 2005 row-post-pack */
		ROW_POST_PACK(2005, "row-post-pack", ""),

		/** 2006 row-pre-parse */
		ROW_PRE_PARSE(2007, "row-pre-parse", ""),

		/** 2007 row-post-parse */
		ROW_POST_PARSE(2008, "row-post-parse", "");

		private int code;
		private String name;
		private String text;

		EventType(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}

		public static EventType getFieldTypeByCode(int code) {
			for (EventType mt : values()) {
				if (code == mt.getCode()) {
					return mt;
				}
			}
			throw new CommonException("not support messagePackModeText " + code);
		}

		public static EventType getFieldTypeByName(String name) {
			for (EventType mt : values()) {
				if (mt.getName().equals(name)) {
					return mt;
				}
			}
			throw new CommonException("messagePackModeText.notExist ");
		}
	}

	public enum FieldAttr {

		/** 2000 name */
		NAME(2000, "name", ""),

		/** 2001 short-text */
		SHORT_TEXT(2001, "short-text", ""),

		/** 2002 field-type */
		FIELD_TYPE(2002, "field-type", ""),

		/** 2003 reference */
		REFERENCE(2003, "reference", ""),

		/** 2004 required */
		REQUIRED(2004, "required", ""),

		/** 2005 shield */
		SHIELD(2005, "shield", ""),

		/** 2006 bitmap-field */
		BITMAP_FIELD(2006, "bitmap-field", ""),

		/** 2007 xpath */
		XPATH(2007, "xpath", ""),

		/** 2008 data-type */
		DATA_TYPE(2008, "data-type", ""),

		/** 2009 length */
		LENGTH(2009, "length", ""),

		/** 2010 editable */
		EDITABLE(2010, "editable", ""),

		/** 2011 padding */
		PADDING(2011, "padding", ""),

		/** 2011 padding-direction */
		PADDING_DIRECTION(2011, "padding-direction", ""),

		/** 2011 padding */
		VALUE(2011, "value", "");

		private int code;
		private String name;
		private String text;

		FieldAttr(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}
	}

	public enum DataType {

		/** 3000 str */
		STR(3000, "str", ""),

		/** 3001 num */
		NUM(3001, "num", ""),

		/** 3002 bin */
		BIN(3002, "bin", ""),

		/** 3003 int */
		INT(3003, "int", ""),

		/** 3004 byte */
		BYTE(3004, "byte", ""),

		/** 3005 short */
		SHORT(3005, "short", ""),

		/** 3006 datetime */
		DATETIME(3006, "datetime", ""),

		/** 3007 net-int */
		NET_INT(3007, "net-int", ""),

		/** 3008 net-short */
		NET_SHORT(3008, "net-short", ""),

		/** 3009 long */
		LONG(3009, "long", ""),

		/** 3010 decimal */
		DECIMAL(3010, "decimal", ""),

		/** 3011 double */
		DOUBLE(3011, "double", "");

		private int code;
		private String name;
		private String text;

		DataType(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}

		public static DataType getDataTypeByCode(int code) {
			for (DataType mt : values()) {
				if (code == mt.getCode()) {
					return mt;
				}
			}
			throw new CommonException("Constant.dataTypeText.notExist" + code);
		}

		public static DataType getDataTypeByName(String name) {
			for (DataType mt : values()) {
				if (mt.getName().equals(name)) {
					return mt;
				}
			}
			throw new CommonException("Constant.dataTypeText.notExist" + name);
		}
	}

	public enum MessageTag {

		/** 3000 message-bean */
		MESSAGE_BEAN(3000, "message-bean", ""),

		/** 3001 num */
		FIELD(3001, "field", ""),

		/** 3002 bin */
		VALUE_RANGE(3002, "value-range", ""),

		/** 3003 int */
		VALUE(3003, "value", "");

		private int code;
		private String name;
		private String text;

		MessageTag(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}

		public static MessageTag getMessageTagByCode(int code) {
			for (MessageTag mt : values()) {
				if (code == mt.getCode()) {
					return mt;
				}
			}
			throw new CommonException("not support Message Tag " + code);
		}
	}

	public enum ReferenceType {
		/** 1000 dynamic */
		DYNAMIC("dynamic", ""),
		/** 1001 expression */
		EXPRESSION("expression", "");

		private String code;
		private String name;

		ReferenceType(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public static ReferenceType getReferenceTypeByCode(String code) {
			for (ReferenceType mt : values()) {
				if (code.equals(mt.getCode())) {
					return mt;
				}
			}
			throw new CommonException("not support message type " + code);
		}
	}

	public enum MessageType {
		/** 1000 common */
		COMMON(1000, "common", ""),
		/** 1001 xml */
		XML(1001, "xml", ""),
		/** 1002 tag */
		TAG(1002, "tag", ""),
		/** 1003 swift */
		SWIFT(1003, "swift", "");

		private int code;
		private String name;
		private String text;

		MessageType(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}

		public static MessageType getMessageTypeByCode(int code) {
			for (MessageType mt : values()) {
				if (code == mt.getCode()) {
					return mt;
				}
			}
			throw new CommonException("not support message type " + code);
		}

		public static MessageType getMessageTypeByName(String name) {
			for (MessageType mt : values()) {
				if (mt.getName().equals(name)) {
					return mt;
				}
			}
			throw new CommonException("messageTypeText.notExist ");
		}
	}

	public enum MessagePackMode {
		/** 6000 normal */
		NORMAL(6000, "normal", ""),
		/** 6001 template */
		TEMPLATE(6001, "template", "");

		private int code;
		private String name;
		private String text;

		MessagePackMode(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}

		public static MessagePackMode getMessageTypeByCode(int code) {
			for (MessagePackMode mt : values()) {
				if (code == mt.getCode()) {
					return mt;
				}
			}
			throw new CommonException("not support messagePackModeText " + code);
		}

		public static MessagePackMode getMessageTypeByName(String name) {
			for (MessagePackMode mt : values()) {
				if (mt.getName().equals(name)) {
					return mt;
				}
			}
			throw new CommonException("messagePackModeText.notExist ");
		}
	}

	public enum PaddingDirection {

		/** 3000 none */
		NONE(5000, "none", ""),

		/** 3001 left */
		LEFT(5001, "left", ""),

		/** 3002 right */
		RIGHT(5002, "right", "");

		private int code;
		private String name;
		private String text;

		PaddingDirection(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}

		public static PaddingDirection getDataTypeByCode(int code) {
			for (PaddingDirection mt : values()) {
				if (code == mt.getCode()) {
					return mt;
				}
			}
			throw new CommonException("Constant.PaddingDirection.notExist" + code);
		}

		public static PaddingDirection getDataTypeByName(String name) {
			for (PaddingDirection mt : values()) {
				if (mt.getName().equals(name)) {
					return mt;
				}
			}
			throw new CommonException("Constant.PaddingDirection.notExist" + name);
		}
	}

	public enum DataEncTypc {

		/** 4000 none */
		ASC(4000, "asc", ""),

		/** 4001 left */
		BCD(4001, "bcd", "");

		private int code;
		private String name;
		private String text;

		DataEncTypc(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}

		public static DataEncTypc getDataTypeByCode(int code) {
			for (DataEncTypc mt : values()) {
				if (code == mt.getCode()) {
					return mt;
				}
			}
			throw new CommonException("Constant.DataEncTypc.notExist" + code);
		}

		public static DataEncTypc getDataTypeByName(String name) {
			for (DataEncTypc mt : values()) {
				if (mt.getName().equals(name)) {
					return mt;
				}
			}
			throw new CommonException("Constant.DataEncTypc.notExist" + name);
		}
	}

	public enum ChannelType {

		/** 4000 MQ */
		MQ(4000, "MQ", "MQ"),

		/** 4001 HTTP_CLIENT */
		HTTP_CLIENT(4001, "HTTP_CLIENT", "HTTP客户端"),

		/** 4001 HTTP_SERVER */
		HTTP_SERVER(4001, "HTTP_SERVER", "HTTP服务端"),

		/** 4001 SOCKET_CLIENT */
		SOCKET_CLIENT(4001, "SOCKET_CLIENT", "SOCKET短连接客户端"),

		/** 4001 SOCKET_SERVER */
		SOCKET_SERVER(4001, "SOCKET_SERVER", "SOCKET短连接服务端"),

		/** 4001 LONG_SOCKET */
		LONG_SOCKET(4001, "LONG_SOCKET", "长连接");

		private int code;
		private String name;
		private String text;

		ChannelType(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}

		public static ChannelType getChannelTypeByCode(int code) {
			for (ChannelType mt : values()) {
				if (code == mt.getCode()) {
					return mt;
				}
			}
			throw new CommonException("Constant.DataEncTypc.notExist" + code);
		}

		public static ChannelType getChannelTypeByName(String name) {
			for (ChannelType mt : values()) {
				if (mt.getName().equals(name)) {
					return mt;
				}
			}
			throw new CommonException("Constant.DataEncTypc.notExist" + name);
		}
	}

	/**
	 * 服务模式
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @date 2022-05-19 16:23:03
	 */
	public enum ChannelMode {

		/** 1000 SERVER 服务端 */
		SERVER(1000, "SERVER", "服务端"),

		/** 1001 CLIENT 客户端 */
		CLIENT(1001, "CLIENT", "客户端"),

		/** 1002 DOUBLE 双向 */
		DOUBLE(1002, "DOUBLE", "双向");

		private int code;
		private String name;
		private String text;

		ChannelMode(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}

		public static ChannelMode getChannelModeByCode(int code) {
			for (ChannelMode mt : values()) {
				if (code == mt.getCode()) {
					return mt;
				}
			}
			throw new CommonException("Constant.DataEncTypc.notExist" + code);
		}

		public static ChannelMode getChannelModeByName(String name) {
			for (ChannelMode mt : values()) {
				if (mt.getName().equals(name)) {
					return mt;
				}
			}
			throw new CommonException("Constant.DataEncTypc.notExist" + name);
		}
	}

	

	public static String getJavaTypeByDataType(int dataType) {
		switch (dataType) {
		case 3000, 3001, 3006, 3010, 3011:
			return "String";

		case 3002:
			return "byte[]";

		case 3003, 3007:
			return "int";

		case 3004:
			return "byte";

		case 3005, 3008:
			return "short";

		case 3009:
			return "long";

		default:
			throw new CommonException("Constant.dataType.notExist");
		}
	}
}