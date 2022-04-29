package com.fib.upp.message.metadata;

import com.fib.commons.exception.CommonException;

public interface ConstantMB {
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
			throw new CommonException("not support message type " + code);
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
	}
}