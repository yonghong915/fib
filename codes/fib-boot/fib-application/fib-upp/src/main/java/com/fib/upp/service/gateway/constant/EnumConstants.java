package com.fib.upp.service.gateway.constant;

import lombok.Getter;

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

	/**
	 * 报文类型
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @since 1.0
	 * @date 2020-12-30
	 */
	public enum MsgType {
		/** 通用 */
		COMMON(1000, "common"),
		/** 专用于xml报文 */
		XML(1001, "xml"),
		/** 专用于tag报文 */
		TAG(1002, "tag"),
		/** 专用于swift报文 */
		SWIFT(1003, "swift");

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

	/**
	 * 组报方式
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @since 1.0
	 * @date 2020-12-30
	 */
	public enum PackMode {
		/** 正常组报方式 */
		NORMAL(6000, "normal"),
		/** 模板组报方式 */
		TEMPLATE(6001, "template");

		@Getter
		private int code;
		@Getter
		private String name;

		PackMode(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public static int getPackModeCode(String messageName) {
			PackMode[] packModes = PackMode.values();
			for (PackMode packMode : packModes) {
				if (packMode.name.equalsIgnoreCase(messageName)) {
					return packMode.code;
				}
			}
			return 0;
		}

		public static String getPackModeName(int messageCode) {
			PackMode[] packModes = PackMode.values();
			for (PackMode packMode : packModes) {
				if (packMode.code == messageCode) {
					return packMode.name;
				}
			}
			return "";
		}
	}

	/**
	 * 域类型
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @since 1.0
	 * @date 2020-12-30
	 */
	public enum FieldType {
		/** 定长域 */
		FIXED_FIELD(2000, "fixed-field"),
		/** 变长域 */
		VAR_FIELD(2001, "var-field"),
		/** 组合域 */
		COMBINE_FIELD(2002, "combine-field"),
		/** 变长组合域 */
		VAR_COMBINE_FIELD(2003, "var-combine-field"),
		/** 表格域 */
		TABLE(2004, "table"),
		/** 表格行数域 */
		TABLE_ROW_NUM_FIELD(2005, "table-row-num-field"),
		/** 位图域 */
		BITMAP_FIELD(2006, "bitmap-field"),
		/** 长度域 */
		LENGTH_FIELD(2007, "length-field"),
		/** 引用域 */
		REFERENCE_FIELD(2008, "reference-field"),
		/** 变长引用域 */
		VAR_REFERENCE_FIELD(2009, "var-reference-field"),
		/** mac校验位 */
		MAC_FIELD(2010, "mac-field"), VAR_TABLE(2011, "var-table"), PBOC_LENGTH_FIELD(2012, "pboc-length-field");

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

	/**
	 * 域值的数据类型
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @since 1.0
	 * @date 2020-12-30
	 */
	public enum DataType {
		/** 任意字符串 */
		STR(3000, "str"),
		/** 数字字符串,字符必须是 0～9数字字 */
		NUM(3001, "num"),
		/** 二进制数据 */
		BIN(3002, "bin"),
		/** 整数,4 字节 */
		INT(3003, "int"),
		/** byte类型整数,1 字节,取值范围0～255 */
		BYTE(3004, "byte"),
		/** 短整数,2 字节 */
		SHORT(3005, "short"),
		/** 日期时间类型 */
		DATETIME(3006, "datetime"),
		/** 网络字节顺序的整数,4 字节 */
		NETINT(3007, "net-int"),
		/** 网络字节顺序的短整数,2 字节 */
		NETSHORT(3008, "net-short"),
		/** 长整型,8字节 */
		LONG(3009, "long"),
		/** 小数 */
		DECIMAL(3010, "decimal"), DOUBLE(3011, "double");

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

	/**
	 * 域值的编码
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @since 1.0
	 * @date 2020-12-30
	 */
	public enum DataEncoding {
		/** asc字符 */
		ASC("asc"),
		/** bcd码 */
		BCD("bcd");

		@Getter
		private String code;

		DataEncoding(String code) {
			this.code = code;
		}

		public String code() {
			return this.code;
		}
	}

	/**
	 * 填充字符填充方向
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @since 1.0
	 * @date 2020-12-30
	 */
	public enum PaddingDirection {
		/** 不填充 */
		NONE(5000, "none"),
		/** 左填充 */
		LEFT(5001, "left"),
		/** 右填充 */
		RIGHT(5002, "right");

		@Getter
		private int code;
		@Getter
		private String name;

		PaddingDirection(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public static int getPaddingDirectionCode(String messageName) {
			PaddingDirection[] paddingDirections = PaddingDirection.values();
			for (PaddingDirection paddingDirection : paddingDirections) {
				if (paddingDirection.name.equalsIgnoreCase(messageName)) {
					return paddingDirection.code;
				}
			}
			return 0;
		}

		public static String getPaddingDirectionName(int messageCode) {
			PaddingDirection[] paddingDirections = PaddingDirection.values();
			for (PaddingDirection paddingDirection : paddingDirections) {
				if (paddingDirection.code == messageCode) {
					return paddingDirection.name;
				}
			}
			return "";
		}
	}

	public enum ReferenceType {
		DYNAMIC("dynamic"), EXPRESSION("expression");

		private String attrCode;

		ReferenceType(String attrCode) {
			this.attrCode = attrCode;
		}

		public String code() {
			return this.attrCode;
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
		DATA_ENCODING("data-encoding"), PATTERN("pattern"), LENGTH_SCRIPT("length-script"), LENGTH("length"),
		REF_VALUE_RANGE("ref-value-range"), REFERENCE_TYPE("reference-type"), ROW_NUM_FIELD("row-num-field"),
		ROW_XPATH("row-xpath"), CLASS("class"), CALC_TYPE("calc-type"), DEFAULT_REF("default-ref"), TYPE("type");

		private String attrCode;

		FieldAttr(String attrCode) {
			this.attrCode = attrCode;
		}

		public String code() {
			return this.attrCode;
		}
	}

	public enum ValueRangeAttr {
		DEFAULT_REF("default-ref");

		private String attrCode;

		ValueRangeAttr(String attrCode) {
			this.attrCode = attrCode;
		}

		public String code() {
			return this.attrCode;
		}
	}

	public enum LoginFlag {
		TRUE(true, "true"), FALSE(false, "false");

		@Getter
		private boolean code;
		@Getter
		private String name;

		LoginFlag(boolean code, String name) {
			this.code = code;
			this.name = name;
		}

		public static boolean getLoginFlagCode(String messageName) {
			LoginFlag[] loginFlags = LoginFlag.values();
			for (LoginFlag loginFlag : loginFlags) {
				if (loginFlag.name.equalsIgnoreCase(messageName)) {
					return loginFlag.code;
				}
			}
			return false;
		}

		public static String getLoginFlagName(boolean messageCode) {
			LoginFlag[] loginFlags = LoginFlag.values();
			for (LoginFlag loginFlag : loginFlags) {
				if (loginFlag.code == messageCode) {
					return loginFlag.name;
				}
			}
			return "";
		}
	}
}
