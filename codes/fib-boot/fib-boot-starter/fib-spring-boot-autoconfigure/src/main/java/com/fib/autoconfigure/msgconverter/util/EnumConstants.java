package com.fib.autoconfigure.msgconverter.util;

import com.fib.autoconfigure.msgconverter.message.metadata.ConstantMB.FieldType;
import com.fib.commons.exception.CommonException;

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

		public static MessageObjectType getMessageObjectTypeByCode(String name) {
			for (MessageObjectType thisObj : values()) {
				if (thisObj.name.equals(name)) {
					return thisObj;
				}
			}
			throw new IllegalArgumentException("Unsupport Type " + name);
		}

		public static MessageObjectType getMessageObjectTypeByCode(int code) {
			for (MessageObjectType thisObj : values()) {
				if (thisObj.code == code) {
					return thisObj;
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

		public static ProcessorType getProcessorTypeByCode(int code) {
			for (ProcessorType mt : values()) {
				if (code == mt.getCode()) {
					return mt;
				}
			}
			throw new CommonException("not support FieldType " + code);
		}

		public static ProcessorType getProcessorTypeByName(String name) {
			for (ProcessorType mt : values()) {
				if (mt.getName().equals(name)) {
					return mt;
				}
			}
			throw new CommonException("FieldType.notExist ");
		}
	}
}
