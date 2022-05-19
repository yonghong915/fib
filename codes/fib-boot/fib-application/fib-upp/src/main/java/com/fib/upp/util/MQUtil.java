package com.fib.upp.util;

public interface MQUtil {

	/**
	 * 主题
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @date 2022-05-19 11:11:17
	 */
	public enum Topic {
		/** 订单主题 */
		TOPIC_ORDER("Topic_Order", "订单主题"),

		/** 支付主题 */
		TOPIC_PAY("Topic_Pay", "支付主题");

		private String code;

		private String name;

		Topic(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}
	}

	/**
	 * 标签
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @date 2022-05-19 11:11:23
	 */
	public enum Tag {
		/** 图书标签 */
		TAG_BOOK("Tag_Book", "图书标签");

		private String code;

		private String name;

		Tag(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}
	}
}
