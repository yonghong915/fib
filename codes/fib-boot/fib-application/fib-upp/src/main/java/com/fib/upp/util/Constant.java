package com.fib.upp.util;

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
}
