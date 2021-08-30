package com.fib.msgconverter.commgateway.channel.longconnection.config;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.script.BeanShellEngine;
import com.giantstone.common.util.CodeUtil;

public class MessageSymbol {
	// type = "value"
	public static final String TYP_VALUE_TXT = "value";
	public static final int TYP_VALUE = 0x60;
	// type = "script"
	public static final String TYP_SCRIPT_TXT = "script";
	public static final int TYP_SCRIPT = 0x61;
	// data-type = "bin"
	public static final String DTA_TYP_BIN_TXT = "bin";
	public static final int DTA_TYP_BIN = 0x70;
	// data-type = "str"
	public static final String DTA_TYP_STR_TXT = "str";
	public static final int DTA_TYP_STR = 0x71;
	/**
	 * 报文符号ID
	 */
	private String id;
	/**
	 * 报文符号取值类型
	 */
	private int type = TYP_VALUE;
	/**
	 * 报文数据类型
	 */
	private int dataType = DTA_TYP_STR;
	/**
	 * 报文数据
	 */
	private String value;

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

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static String getTypeText(int type) {
		switch (type) {
		case TYP_VALUE:
			return TYP_VALUE_TXT;
		case TYP_SCRIPT:
			return TYP_SCRIPT_TXT;
		default:
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("type.unsupport",
							new String[] { type + "" }));
		}
	}

	public static int getTypeByText(String text) {
		if (TYP_VALUE_TXT.equals(text)) {
			return TYP_VALUE;
		} else if (TYP_SCRIPT_TXT.equals(text)) {
			return TYP_SCRIPT;
		} else {
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("type.unsupport",
							new String[] { text }));
		}
	}

	public static String getDataTypeText(int dataType) {
		switch (dataType) {
		case DTA_TYP_BIN:
			return DTA_TYP_BIN_TXT;
		case DTA_TYP_STR:
			return DTA_TYP_STR_TXT;
		default:
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("type.unsupport",
							new String[] { dataType + "" }));
		}
	}

	public static int getDataTypeByText(String text) {
		if (DTA_TYP_BIN_TXT.equals(text)) {
			return DTA_TYP_BIN;
		} else if (DTA_TYP_STR_TXT.equals(text)) {
			return DTA_TYP_STR;
		} else {
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("type.unsupport",
							new String[] { text }));
		}
	}

	public byte[] determineMessage() {
		return determineMessage(null);
	}

	public byte[] determineMessage(byte[] message) {
		byte[] ret = null;
		if (TYP_VALUE == type) {
			if (DTA_TYP_STR == dataType) {
				ret = value.getBytes();
			} else {
				hexStrCheck(value);
				ret = CodeUtil.HextoByte(value);
			}
		} else {
			BeanShellEngine engine = new BeanShellEngine();
			if (null != message) {
				engine.set("message", message);
			}
			ret = (byte[]) engine.eval(value);
		}
		return ret;
	}

	private void hexStrCheck(String hexStr) {
		byte[] bValue = hexStr.trim().getBytes();
		if (bValue.length % 2 != 0) {
			// throw new RuntimeException("string[" + hexStr
			// + "]'s length is odd number, it isn't a hex string!");
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"LongConnectionSocketChannelConfigParser.hexStrCheck.notHexString.1",
									new String[] { hexStr }));
		}
		for (int i = 0; i < bValue.length; i++) {
			if (bValue[i] >= '0' && bValue[i] <= '9') {
				continue;
			} else if (bValue[i] >= 'a' && bValue[i] <= 'f') {
				continue;
			} else if (bValue[i] >= 'A' && bValue[i] <= 'F') {
				continue;
			} else {
				// throw new RuntimeException("string[" + hexStr
				// + "] contains invalid char[" + bValue[i]
				// + "], it isn't a hex string!");
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"LongConnectionSocketChannelConfigParser.hexStrCheck.notHexString.2",
										new String[] { hexStr,
												String.valueOf(bValue[i]) }));
			}
		}
	}
}
