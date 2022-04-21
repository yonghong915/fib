package com.fib.upp.util;

public interface UppEnumConstants extends com.fib.commons.util.constant.EnumConstants {
	String CLEAR_BANK_CODE = "333333333";
	String TX_ID_SEQ_NAME = "";
	String MESSAGE_ID_SEQ_NAME = "";

	final String DATASOURCE_UPP = "uppdb";

	/**
	 * 系统码
	 * 
	 * @author fangyh
	 * @version 1.0.0
	 * @date 2021-11-15
	 */
	enum SystemCode {
		/** 大额实时支付系统 */
		TRAFFIC_HVPS("HVPS"),
		/** 小额批量支付系统 */
		TRAFFIC_BEPS("BEPS"),
		/** 网上支付跨行清算系统 */
		TRAFFIC_IBPS("IBPS");

		private String code;

		SystemCode(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	/**
	 * 支付模式
	 * 
	 * @author fangyh
	 * @version 1.0.0
	 * @date 2021-11-15
	 */
	enum PaymentModel {
		/** 批量 */
		BATCH("BATCH"),
		/** 定期 */
		TERMLY("TERMLY"),
		/** 实时 */
		REALTIME("REALTIME"),
		/** 普通 */
		NORMAL("NORMAL"),
		/** 第三方 */
		THIRDPARTY("THIRDPARTY"),
		/** 客户发起 */
		CUSTOMER("CUSTOMER"),
		/** 金融机构发起 */
		FINANCE("FINANCE");

		private String code;

		PaymentModel(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

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
			throw new IllegalArgumentException("Unsupport MessageObjectType " + name);
		}

		public static String getNameByCode(int code) {
			for (MessageObjectType thisObj : values()) {
				if (thisObj.code == code) {
					return thisObj.name;
				}
			}
			throw new IllegalArgumentException("Unsupport MessageObjectType " + code);
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
			throw new IllegalArgumentException("Unsupport ProcessorType " + name);
		}

		public static String getNameByCode(int code) {
			for (ProcessorType thisObj : values()) {
				if (thisObj.code == code) {
					return thisObj.name;
				}
			}
			throw new IllegalArgumentException("Unsupport ProcessorType " + code);
		}
	}
}
