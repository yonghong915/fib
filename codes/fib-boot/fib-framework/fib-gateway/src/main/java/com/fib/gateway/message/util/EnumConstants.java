package com.fib.gateway.message.util;

import lombok.Getter;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
public final class EnumConstants {
	/***/
	public static final String MESSAGE_BEAN_NAME = "message-bean";

	/***/
	public static final String FIELD_BEAN_NAME = "field";

	public static final String VALUE_RANGE_NAME = "value-range";

	public static final String VALUE_NAME = "value";

	public static final String EVENT = "event";

	public static final String VARIABLE = "variable";
	public static final int DTA_ENC_TYP_ASC = 4000;
	public static final int DTA_ENC_TYP_BCD = 4001;
	public static final int PADDING_NONE = 5000;
	public static final int PADDING_LEFT = 5001;
	public static final int PADDING_RIGHT = 5002;

	public enum MsgType {
		COMMON(1000, "common"), XML(1001, "xml"), TAG(1002, "tag"), SWIFT(1003, "swift");

		@Getter
		private int code;
		@Getter
		private String name;

		MsgType(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public static int getMessageTypeCode(String messageName) {
			MsgType[] msgTypes = MsgType.values();
			for (MsgType msgType : msgTypes) {
				if (msgType.name.equalsIgnoreCase(messageName)) {
					return msgType.code;
				}
			}
			return 0;
		}

		public static String getMessageTypeName(int messageCode) {
			MsgType[] msgTypes = MsgType.values();
			for (MsgType msgType : msgTypes) {
				if (msgType.code == messageCode) {
					return msgType.name;
				}
			}
			return "";
		}
	}

	public enum FieldType {
		FIXED_FIELD(2000, "fixed-field"), VAR_FIELD(2001, "var-field"), COMBINE_FIELD(2002, "combine-field"),
		VAR_COMBINE_FIELD(2003, "var-combine-field"), TABLE(2004, "table"),
		TABLE_ROW_NUM_FIELD(2005, "table-row-num-field"), BITMAP_FIELD(2006, "bitmap-field"),
		LENGTH_FIELD(2007, "length-field"), REFERENCE_FIELD(2008, "reference-field"),
		VAR_REFERENCE_FIELD(2009, "var-reference-field"), MAC_FIELD(2010, "mac-field"), VAR_TABLE(2011, "var-table"),
		PBOC_LENGTH_FIELD(2012, "pboc-length-field");

		@Getter
		private int code;
		@Getter
		private String name;

		FieldType(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public static int getFieldTypeCode(String messageName) {
			FieldType[] fieldTypes = FieldType.values();
			for (FieldType fieldType : fieldTypes) {
				if (fieldType.name.equalsIgnoreCase(messageName)) {
					return fieldType.code;
				}
			}
			return 0;
		}

		public static String getFieldTypeName(int messageCode) {
			MsgType[] msgTypes = MsgType.values();
			for (MsgType msgType : msgTypes) {
				if (msgType.code == messageCode) {
					return msgType.name;
				}
			}
			return "";
		}
	}

	public enum DataType {
		STR(3000, "str"), NUM(3001, "num"), BIN(3002, "bin"), INT(3003, "int"), BYTE(3004, "byte"),
		SHORT(3005, "short"), DATETIME(3006, "datetime"), NETINT(3007, "net-int"), NETSHORT(3008, "net-short"),
		LONG(3009, "long"), DECIMAL(3010, "decimal"), DOUBLE(3011, "double");

		@Getter
		private int code;
		@Getter
		private String name;

		DataType(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public static int getDataTypeCode(String messageName) {
			DataType[] dataTypes = DataType.values();
			for (DataType dataType : dataTypes) {
				if (dataType.name.equalsIgnoreCase(messageName)) {
					return dataType.code;
				}
			}
			return 0;
		}

		public static String getDataTypeName(int messageCode) {
			DataType[] dataTypes = DataType.values();
			for (DataType dataType : dataTypes) {
				if (dataType.code == messageCode) {
					return dataType.name;
				}
			}
			return "";
		}
	}

	public enum MessageAttr {
		ID("id"), TYPE("type"), SHORT_TEXT("short-text"), CLASS("class"), XPATH("xpath"), SCHEMA_VALID("schema-valid"),
		SCHEMA_VALID_TYPE("schema-valid-type"), SCHEMA_VALID_PATH("schema-valid-path"),
		MESSAGE_CHARSET("message-charset"), PREFIX("prefix"), SUFFIX("suffix"),
		EXTENDED_ATTRIBUTES("extended-attributes"), NAME_SPACE("name-space"), REMOVE_BLANK_NODE("remove_blank_node");

		private String attrCode;

		MessageAttr(String attrCode) {
			this.attrCode = attrCode;
		}

		public String code() {
			return this.attrCode;
		}
	}

	public enum FieldAttr {
		NAME("name"), FIELD_TYPE("field-type"), REQUIRED("required"), XPATH("xpath"), SHORT_TEXT("short-text"),
		MAX_LENGTH("max-length"), MIN_LENGTH("min-length"), DATA_TYPE("data-type"), VALUE("value"),
		REFERENCE("reference"), PADDING_DIRECTION("padding-direction"), PREFIX("prefix"), EDITABLE("editable"),
		IS_REMOVE_UNWATCHABLE("isRemoveUnwatchable"), ISO8583_NO("iso8583-no"), SUFFIX("suffix"),
		LAST_ROW_SUFFIX("last-row-suffix"), EXTENDED_ATTRIBUTES("extended-attributes"), DATA_CHARSET("data-charset"),
		SHIELD("shield"), ROW_CUT("row-cut"), REF_LENGTH_FIELD("ref-length-field"),
		REF_LENGTH_FIELD_OFFSET("ref-length-field-offset"), LENGTH_FIELD_DATA_TYPE("length-field-data-type"),
		FIR_ROW_PREFIX("fir-row-prefix"), LENGTH_FIELD_DATA_ENCODING("length-field-data-encoding"),
		LENGTH_FIELD_LENGTH("length-field-length"), TAB_PREFIX("tab-prefix"), TAB_SUFFIX("tab-suffix"), TAG("tag"),
		START_FIELD("start-field"), END_FIELD("end-field"), DATA("data"), TABLE("table"),
		DATA_ENCODING("data-encoding"), PATTERN("pattern"), LENGTH_SCRIPT("length-script"), LENGTH("length");

		private String attrCode;

		FieldAttr(String attrCode) {
			this.attrCode = attrCode;
		}

		public String code() {
			return this.attrCode;
		}
	}

	public static void main(String[] args) {
		System.out.println(EnumConstants.DataType.getDataTypeCode("str"));
	}
}
