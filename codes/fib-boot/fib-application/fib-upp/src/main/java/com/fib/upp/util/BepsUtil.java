package com.fib.upp.util;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-25
 */
public class BepsUtil {
	/**
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @since 1.0
	 * @date 2021-01-25
	 */
	public enum QueueStatus {
		VLD("VLD");

		private String code;

		QueueStatus(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}
	}

	/**
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @since 1.0
	 * @date 2021-01-25
	 */
	public enum QueueHeaderStatus {
		/***/
		OPN("OPN"),
		/***/
		FPC("FPC"),
		/***/
		CLS("CLS"),
		/***/
		FAILED("FAILED");

		private String code;

		QueueHeaderStatus(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}
	}

	/**
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @since 1.0
	 * @date 2021-01-25
	 */
	public enum QueueType {
		/***/
		ISA001("ISA001"),
		/***/
		MMA001("MMA001"),
		/***/
		RMD001("RMD001"),
		/***/
		TMA001("TMA001");

		private String code;

		QueueType(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}
	}

	public enum QueueItemStatus {
		/***/
		START("START"),
		/***/
		END("END"),
		/***/
		FAILED("FAILED");

		private String code;

		QueueItemStatus(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}
	}
}
