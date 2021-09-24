package com.fib.upp.util;

public interface EnumConstants extends com.fib.commons.util.constant.EnumConstants {
	String CLEAR_BANK_CODE = "333333333";
	String TX_ID_SEQ_NAME = "";
	String MESSAGE_ID_SEQ_NAME = "";

	final String DATASOURCE_UPP = "uppdb";

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
}
