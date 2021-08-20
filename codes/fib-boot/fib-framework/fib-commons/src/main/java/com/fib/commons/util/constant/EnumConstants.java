package com.fib.commons.util.constant;

import lombok.Getter;

public interface EnumConstants {
	/**
	 * 
	 * @author fangyh
	 * @version 1.0.0
	 * @date 2021-08-18
	 */
	public enum SerializeType {
		/**  */
		JDK("jdk"),
		/**  */
		XML("xml"),
		/**  */
		PROTOSTUFF("protoStuff"),
		/** */
		JSON("json");

		@Getter
		private String name;

		SerializeType(String name) {
			this.name = name;
		}
	}

	public enum LogicType {
		TRUE(true), FALSE(false);

		@Getter
		private boolean logicFlag;

		LogicType(boolean logicFlag) {
			this.logicFlag = logicFlag;
		}
	}

	/**
	 * 
	 * @author fangyh
	 * @version 1.0.0
	 * @date 2021-08-18
	 */
	public enum SeasonEnum {
		SPRING(1), SUMMER(2), AUTUMN(3), WINTER(4);

		private int seq;

		SeasonEnum(int seq) {
			this.seq = seq;
		}

		public int getSeq() {
			return seq;
		}
	}
}
