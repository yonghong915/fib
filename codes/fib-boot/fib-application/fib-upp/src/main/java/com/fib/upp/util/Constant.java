package com.fib.upp.util;

import com.fib.commons.exception.CommonException;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-02-24
 */
public class Constant {
	public enum SystemCode {
		HVPS("HVPS", "大额"), BEPS("BEPS", "小额");

		private String code;

		private String name;

		SystemCode(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String code() {
			return code;
		}

		public String value() {
			return name;
		}
	}

	public enum BatchType {
		/** 普通借记-来账 */
		COMM_TID_IN("commTIDIn", "普通借记-来账", ""),
		/** 普通贷记-来账 */
		COMM_TIC_IN("commTICIn", "普通贷记-来账", ""),
		/** 实时借记-来账 */
		IMM_TID_IN("immTIDIn", "实时借记-来账", ""),
		/** 实时贷记-来账 */
		IMM_TIC_IN("immTICIn", "实时贷记-来账", ""),
		/** 定期借记-来账 */
		SUE_BAG_IN("sueBAGIn", "定期借记-来账", ""),
		/** 定期贷记-往账 */
		SUE_BAP_OUT("sueBAPOut", "定期贷记-往账", "sueBapOutService"),
		/** 定期贷记-来账 */
		SUE_BAP_IN("sueBAPIn", "定期贷记-来账", ""),
		/** 批量代付-一方来账 */
		BATCH_BAP_ONE_IN("batchBAPOneIn", "批量代付-一方来账", ""),
		/** 批量代付-多方来账 */
		BATCH_BAP_MANY_IN("batchBAPManyIn", "批量代付-多方来账", ""),
		/** 批量代收-一方来账 */
		BATCH_BAG_ONE_IN("batchBAGOneIn", "批量代收-一方来账", ""),
		/** 批量代收-多方来账 */
		BATCH_BAG_MANY_IN("batchBAGManyIn", "批量代收-多方来账", "");

		private String code;

		private String name;

		private String serviceName;

		BatchType(String code, String name, String serviceName) {
			this.code = code;
			this.name = name;
			this.serviceName = serviceName;
		}

		public String code() {
			return code;
		}

		public String value() {
			return name;
		}

		public String getServiceName() {
			return serviceName;
		}

		public static String getServiceName(String code) {
			BatchType[] batchTypes = BatchType.values();
			for (BatchType batchType : batchTypes) {
				if (batchType.code.equals(code)) {
					return batchType.serviceName;
				}
			}
			return "";
		}
	}

	public enum ServiceName {
		/** 普通借记-来账 */
		PROCESS_PAYMENT_ORDER("processPaymentOrder", "普通借记-来账")
		/** 普通贷记-来账 */
		;

		private String code;

		private String name;


		ServiceName(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String code() {
			return code;
		}

		public String value() {
			return name;
		}
	}

	public enum BatchStatus {
		REGISTER("00", "已登记"), INPROCESS("03", "处理中"), DUE("01", "已到期"), COMPLETE("02", "已完成"), CANCEL("04", "取消"),
		ERROR("05", "异常待处理");

		private String code;

		private String name;

		BatchStatus(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String code() {
			return code;
		}

		public String value() {
			return name;
		}
	}

	public static String getJavaTypeByDataType(int i) {
		switch (i) {
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
			throw new CommonException("Constant.dataType.notExist");
		}

	}

	public static final int NONE = 5000;
	public static final int LEFT = 5001;
	public static final int RIGHT = 5002;

	public static int getMessageTypeByText(String type) {
		if ("common".equalsIgnoreCase(type))
			return 1000;
		if ("xml".equalsIgnoreCase(type))
			return 1001;
		if ("tag".equalsIgnoreCase(type))
			return 1002;
		if ("swift".equalsIgnoreCase(type))
			return 1003;
		else
			throw new CommonException("Constant.messageTypeText.notExist");
	}

	public static int getFieldTypeByText(String s) {
		if ("fixed-field".equalsIgnoreCase(s))
			return 2000;
		if ("var-field".equalsIgnoreCase(s))
			return 2001;
		if ("combine-field".equalsIgnoreCase(s))
			return 2002;
		if ("var-combine-field".equalsIgnoreCase(s))
			return 2003;
		if ("table".equalsIgnoreCase(s))
			return 2004;
		if ("var-table".equalsIgnoreCase(s))
			return 2011;
		if ("bitmap-field".equalsIgnoreCase(s))
			return 2006;
		if ("length-field".equalsIgnoreCase(s))
			return 2007;
		if ("table-row-num-field".equalsIgnoreCase(s))
			return 2005;
		if ("reference-field".equalsIgnoreCase(s))
			return 2008;
		if ("var-reference-field".equalsIgnoreCase(s))
			return 2009;
		if ("mac-field".equalsIgnoreCase(s))
			return 2010;
		else
			throw new CommonException("Constant.fieldTypeText.notExist");
	}

	public static int getDataTypeByText(String s) {
		if ("str".equalsIgnoreCase(s))
			return 3000;
		if ("num".equalsIgnoreCase(s))
			return 3001;
		if ("bin".equalsIgnoreCase(s))
			return 3002;
		if ("int".equalsIgnoreCase(s))
			return 3003;
		if ("byte".equalsIgnoreCase(s))
			return 3004;
		if ("short".equalsIgnoreCase(s))
			return 3005;
		if ("datetime".equalsIgnoreCase(s))
			return 3006;
		if ("net-int".equalsIgnoreCase(s))
			return 3007;
		if ("net-short".equalsIgnoreCase(s))
			return 3008;
		if ("long".equalsIgnoreCase(s))
			return 3009;
		if ("decimal".equalsIgnoreCase(s))
			return 3010;
		if ("double".equalsIgnoreCase(s))
			return 3011;
		else
			throw new CommonException("Constant.dataTypeText.notExist");
	}

	public static String getDataTypeText(int i) {
		switch (i) {
		case 3000:
			return "str";

		case 3001:
			return "num";

		case 3011:
			return "double";

		case 3006:
			return "datetime";

		case 3002:
			return "bin";

		case 3003:
			return "int";

		case 3007:
			return "net-int";

		case 3004:
			return "byte";

		case 3005:
			return "short";

		case 3008:
			return "net-short";

		case 3009:
			return "long";

		case 3010:
			return "decimal";
		default:
			throw new CommonException("Constant.dataType.notExist");
		}

	}

	public static final class MessageSourceKey {
		private MessageSourceKey() {
			// nothing to do
		}

		public static final String CONFIG_EMPTY_KEY = "config.null";
	}

}
