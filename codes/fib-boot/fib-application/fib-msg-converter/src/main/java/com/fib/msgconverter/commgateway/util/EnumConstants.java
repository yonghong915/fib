package com.fib.msgconverter.commgateway.util;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-31
 */
public interface EnumConstants extends com.fib.commons.util.constant.EnumConstants {
	final static String EMPTY_NULL = null;
	final static String EMPTY_STR = "";

	/**
	 * 数据类型
	 * 
	 * @author fangyh
	 * @version 1.0.0
	 * @date 2021-08-31
	 */
	public enum DataType {

	}

	/**
	 * 报文传输类型
	 * 
	 * @author fangyh
	 * @version 1.0.0
	 * @date 2021-08-31
	 */
	public enum MessageObjectType {
		/***/
		JSON_PLUS(134, "JSON+"),
		/***/
		JSON(135, "JSON"),
		/***/
		MAP(136, "MAP"),
		/***/
		MESSAGE_BEAN(137, "MESSAGE-BEAN");

		private int code;
		private String name;

		MessageObjectType(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public static int getCodeByName(String name) {
			for (MessageObjectType thisObj : values()) {
				if (thisObj.name.equals(name)) {
					return thisObj.code;
				}
			}
			throw new IllegalArgumentException("Unsupport Type " + name);
		}

		public static String getNameByCode(int code) {
			for (MessageObjectType thisObj : values()) {
				if (thisObj.code == code) {
					return thisObj.name;
				}
			}
			throw new IllegalArgumentException("Unsupport Type " + code);
		}
	}

	/**
	 * 报文处理器类型
	 * 
	 * @author fangyh
	 * @version 1.0.0
	 * @date 2021-08-31
	 */
	public enum ProcessorType {
		/***/
		SAVE_AND_TRANSMIT(149, "SAVE_AND_TRANSMIT"),
		/***/
		SAVE_AND_TRANSFORM(150, "SAVE_AND_TRANSFORM"),
		/** 本地处理 */
		LOCAL(151, "LOCAL"),
		/** 转发处理 */
		TRANSMIT(152, "TRANSMIT"),
		/** 转换处理 */
		TRANSFORM(153, "TRANSFORM");

		private int code;
		private String name;

		ProcessorType(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public static int getCodeByName(String name) {
			for (ProcessorType thisObj : values()) {
				if (thisObj.name.equals(name)) {
					return thisObj.code;
				}
			}
			throw new IllegalArgumentException("Unsupport Type " + name);
		}

		public static String getNameByCode(int code) {
			for (ProcessorType thisObj : values()) {
				if (thisObj.code == code) {
					return thisObj.name;
				}
			}
			throw new IllegalArgumentException("Unsupport Type " + code);
		}
	}
}
