package com.fib.gateway.message.xml.message;

import com.fib.commons.exception.CommonException;

public class Constant {
	private Constant() {
	}

	public static final String REMOVE_OVER_LENGTH = "remove_over_length";
	public static final String REMOVE_START = "remove_start";
	public static final String REMOVE_PREFIX = "remove_prefix";
	public static final String REMOVE_PREFIX_NO = "no";
	public static final String SWIFT_REMOVECRLF = "remove_crlf";
	public static final String SWIFT_NEWLINE_BASEICLEN = "basic_len";
	public static final String SWIFT_NEWLINE_EXTLEN = "ext_len";
	public static final String BCD_PADDING = "padding";
	public static final String BCD_PADDING_DIRECTION = "padding-direction";
	public static final String REPLACE_ALL = "replace_all";
	public static final String NAME_SPACE = "name_space";
	public static final String VALUE_RANGE_PATH = "value-range";
	public static final String CALC_TYP_JAVA = "java";
	public static final String DEFAULT_REF = "default-ref";
	public static final String REF_TYP_DYNAMIC = "dynamic";
	public static final String REF_TYP_EXPRESSION = "expression";
	public static final String MSG_TYP_COMMON_TXT = "common";
	public static final int MSG_TYP_COMMON = 1000;
	public static final String MSG_TYP_XML_TXT = "xml";
	public static final int MSG_TYP_XML = 1001;
	public static final String MSG_TYP_TAG_TXT = "tag";
	public static final int MSG_TYP_TAG = 1002;
	public static final String MSG_TYP_SWIFT_TXT = "swift";
	public static final int MSG_TYP_SWIFT = 1003;
	public static final String SCHEMA_TYP_W3C_TXT = "W3C";
	public static final int SCHEMA_TYP_W3C = 7000;
	public static final String SCHEMA_TYP_RELAXNG_TXT = "RELAXNG";
	public static final int SCHEMA_TYP_RELAXNG = 7001;
	public static final int MSG_PACK_MODE_NORMAL = 6000;
	public static final String MSG_PACK_MODE_NORMAL_TXT = "normal";
	public static final int MSG_PACK_MODE_TEMPLATE = 6001;
	public static final String MSG_PACK_MODE_TEMPLATE_TXT = "template";
	public static final String BEAN = "b";
	public static final String CLASS = "c";
	public static final String ROWCLASS = "rc";
	public static final String ATTRIBUTE = "a";
	public static final String NAME = "n";
	public static final String TYPE = "t";
	public static final String FLD_TYP_FIXED_FLD_TXT = "fixed-field";
	public static final int FLD_TYP_FIXED_FLD = 2000;
	public static final String FLD_TYP_VAR_FLD_TXT = "var-field";
	public static final int FLD_TYP_VAR_FLD = 2001;
	public static final String FLD_TYP_COMBINE_FLD_TXT = "combine-field";
	public static final int FLD_TYP_COMBINE_FLD = 2002;
	public static final String FLD_TYP_VAR_COMBINE_FLD_TXT = "var-combine-field";
	public static final int FLD_TYP_VAR_COMBINE_FLD = 2003;
	public static final String FLD_TYP_TABLE_FLD_TXT = "table";
	public static final int FLD_TYP_TABLE_FLD = 2004;
	public static final String FLD_TYP_TABLE_ROW_NUM_FLD_TXT = "table-row-num-field";
	public static final int FLD_TYP_TABLE_ROW_NUM_FLD = 2005;
	public static final String FLD_TYP_BITMAP_FLD_TXT = "bitmap-field";
	public static final int FLD_TYP_BITMAP = 2006;
	public static final String FLD_TYP_LEN_FLD_TXT = "length-field";
	public static final int FLD_TYP_LEN_FLD = 2007;
	public static final String FLD_TYP_REF_FLD_TXT = "reference-field";
	public static final int FLD_TYP_REF_FLD = 2008;
	public static final String FLD_TYP_VAR_REF_FLD_TXT = "var-reference-field";
	public static final int FLD_TYP_VAR_REF_FLD = 2009;
	public static final String FLD_TYP_MAC_FLD_TXT = "mac-field";
	public static final int FLD_TYP_MAC_FLD = 2010;
	public static final String FLD_TYP_VAR_TABLE_FLD_TXT = "var-table";
	public static final int FLD_TYP_VAR_TABLE_FLD = 2011;
	public static final String FLD_TYP_PBOC_LENGTH_FLD_TXT = "pboc-length-field";
	public static final int FLD_TYP_PBOC_LENGTH_FLD = 2012;
	public static final String DTA_TYP_STR_TXT = "str";
	public static final int DTA_TYP_STR = 3000;
	public static final String DTA_TYP_NUM_TXT = "num";
	public static final int DTA_TYP_NUM = 3001;
	public static final String DTA_TYP_BIN_TXT = "bin";
	public static final int DTA_TYP_BIN = 3002;
	public static final String DTA_TYP_INT_TXT = "int";
	public static final int DTA_TYP_INT = 3003;
	public static final String DTA_TYP_BYTE_TXT = "byte";
	public static final int DTA_TYP_BYTE = 3004;
	public static final String DTA_TYP_SHORT_TXT = "short";
	public static final int DTA_TYP_SHORT = 3005;
	public static final String DTA_TYP_DATETIME_TXT = "datetime";
	public static final int DTA_TYP_DATETIME = 3006;
	public static final String DTA_TYP_NET_INT_TXT = "net-int";
	public static final int DTA_TYP_NET_INT = 3007;
	public static final String DTA_TYP_NET_SHORT_TXT = "net-short";
	public static final int DTA_TYP_NET_SHORT = 3008;
	public static final String DTA_TYP_LONG_TXT = "long";
	public static final int DTA_TYP_LONG = 3009;
	public static final String DTA_TYP_DECIMAL_TXT = "decimal";
	public static final int DTA_TYP_DECIMAL = 3010;
	public static final int DTA_TYP_DOUBLE = 3011;
	public static final String DTA_TYP_DOUBLE_TXT = "double";
	public static final int DTA_ENC_TYP_ASC = 4000;
	public static final int DTA_ENC_TYP_BCD = 4001;
	public static final int NONE = 5000;
	public static final int LEFT = 5001;
	public static final int RIGHT = 5002;
	public static final String EVENT_PRE_PACK = "pre-pack";
	public static final String EVENT_POST_PACK = "post-pack";
	public static final String EVENT_PRE_PARSE = "pre-parse";
	public static final String EVENT_POST_PARSE = "post-parse";
	public static final String EVENT_ROW_PRE_PACK = "row-pre-pack";
	public static final String EVENT_ROW_POST_PACK = "row-post-pack";
	public static final String EVENT_ROW_PRE_PARSE = "row-pre-parse";
	public static final String EVENT_ROW_POST_PARSE = "row-post-parse";
	public static final String OPER_BLANK_NOT = "0";
	public static final String OPER_BLANK = "1";
	public static final String REQUIRED_TYP_N = "0";
	public static final String REQUIRED_TYP_Y = "1";
	public static final String CUT_ACCODRDING_TO_LENGTH_N = "0";
	public static final String CUT_ACCODRDING_TO_LENGTH_Y = "1";
	public static final String DB_TRUE = "0";
	public static final String DB_FALSE = "1";
	public static final String USE_BEAN_Y = "true";
	public static final String USE_BEAN_N = "false";

	public static int getSchemaTypeByTxt(String var0) {
		if ("W3C".equalsIgnoreCase(var0)) {
			return 7000;
		} else if ("RELAXNG".equals(var0)) {
			return 7001;
		} else {
			throw new CommonException("");
		}
	}

	public static String getSchemaTypeTxt(int var0) {
		if (7000 == var0) {
			return "W3C";
		} else if (7001 == var0) {
			return "RELAXNG";
		} else {
			throw new CommonException("");
		}
	}

	public static String getSchemaType(int var0) {
		if (7000 == var0) {
			return "http://www.w3.org/2001/XMLSchema";
		} else if (7001 == var0) {
			return "http://relaxng.org/ns/structure/1.0";
		} else {
			throw new CommonException("");
		}
	}

	public static int getMessageTypeByText(String var0) {
		if ("common".equalsIgnoreCase(var0)) {
			return 1000;
		} else if ("xml".equalsIgnoreCase(var0)) {
			return 1001;
		} else if ("tag".equalsIgnoreCase(var0)) {
			return 1002;
		} else if ("swift".equalsIgnoreCase(var0)) {
			return 1003;
		} else {
			throw new CommonException("");
		}
	}

	public static String getMessageTypeText(int var0) {
		switch (var0) {
		case 1000:
			return "common";
		case 1001:
			return "xml";
		case 1002:
			return "tag";
		case 1003:
			return "swift";
		default:
			throw new CommonException("");
		}
	}

	public static int getMessagePackModeByText(String var0) {
		if ("normal".equalsIgnoreCase(var0)) {
			return 6000;
		} else if ("template".equalsIgnoreCase(var0)) {
			return 6001;
		} else {
			throw new CommonException("");
		}
	}

	public static String getMessagePackModeText(int var0) {
		switch (var0) {
		case 6000:
			return "normal";
		case 6001:
			return "template";
		default:
			throw new CommonException("");
		}
	}

	public static String getFieldTypeText(int var0) {
		switch (var0) {
		case 2000:
			return "fixed-field";
		case 2001:
			return "var-field";
		case 2002:
			return "combine-field";
		case 2003:
			return "var-combine-field";
		case 2004:
			return "table";
		case 2005:
			return "table-row-num-field";
		case 2006:
			return "bitmap-field";
		case 2007:
			return "length-field";
		case 2008:
			return "reference-field";
		case 2009:
			return "var-reference-field";
		case 2010:
			return "mac-field";
		case 2011:
			return "var-table";
		case 2012:
			return "pboc-length-field";
		default:
			throw new CommonException("");
		}
	}

	public static int getFieldTypeByText(String var0) {
		if ("fixed-field".equalsIgnoreCase(var0)) {
			return 2000;
		} else if ("var-field".equalsIgnoreCase(var0)) {
			return 2001;
		} else if ("combine-field".equalsIgnoreCase(var0)) {
			return 2002;
		} else if ("var-combine-field".equalsIgnoreCase(var0)) {
			return 2003;
		} else if ("table".equalsIgnoreCase(var0)) {
			return 2004;
		} else if ("var-table".equalsIgnoreCase(var0)) {
			return 2011;
		} else if ("bitmap-field".equalsIgnoreCase(var0)) {
			return 2006;
		} else if ("length-field".equalsIgnoreCase(var0)) {
			return 2007;
		} else if ("table-row-num-field".equalsIgnoreCase(var0)) {
			return 2005;
		} else if ("reference-field".equalsIgnoreCase(var0)) {
			return 2008;
		} else if ("var-reference-field".equalsIgnoreCase(var0)) {
			return 2009;
		} else if ("mac-field".equalsIgnoreCase(var0)) {
			return 2010;
		} else if ("pboc-length-field".equalsIgnoreCase(var0)) {
			return 2012;
		} else {
			throw new CommonException("");
		}
	}

	public static int getDataTypeByText(String var0) {
		if ("str".equalsIgnoreCase(var0)) {
			return 3000;
		} else if ("num".equalsIgnoreCase(var0)) {
			return 3001;
		} else if ("bin".equalsIgnoreCase(var0)) {
			return 3002;
		} else if ("int".equalsIgnoreCase(var0)) {
			return 3003;
		} else if ("byte".equalsIgnoreCase(var0)) {
			return 3004;
		} else if ("short".equalsIgnoreCase(var0)) {
			return 3005;
		} else if ("datetime".equalsIgnoreCase(var0)) {
			return 3006;
		} else if ("net-int".equalsIgnoreCase(var0)) {
			return 3007;
		} else if ("net-short".equalsIgnoreCase(var0)) {
			return 3008;
		} else if ("long".equalsIgnoreCase(var0)) {
			return 3009;
		} else if ("decimal".equalsIgnoreCase(var0)) {
			return 3010;
		} else if ("double".equalsIgnoreCase(var0)) {
			return 3011;
		} else {
			throw new CommonException("");
		}
	}

	public static String getDataTypeText(int var0) {
		switch (var0) {
		case 3000:
			return "str";
		case 3001:
			return "num";
		case 3002:
			return "bin";
		case 3003:
			return "int";
		case 3004:
			return "byte";
		case 3005:
			return "short";
		case 3006:
			return "datetime";
		case 3007:
			return "net-int";
		case 3008:
			return "net-short";
		case 3009:
			return "long";
		case 3010:
			return "decimal";
		case 3011:
			return "double";
		default:
			throw new CommonException("");
		}
	}

	public static String getJavaTypeByDataType(int var0) {
		switch (var0) {
		case 3000:
		case 3001:
		case 3006:
		case 3010:
		case 3011:
			return "String";
		case 3002:
			return "byte[]";
		case 3003:
		case 3007:
			return "int";
		case 3004:
			return "byte";
		case 3005:
		case 3008:
			return "short";
		case 3009:
			return "long";
		default:
			throw new CommonException("");
		}
	}
}
