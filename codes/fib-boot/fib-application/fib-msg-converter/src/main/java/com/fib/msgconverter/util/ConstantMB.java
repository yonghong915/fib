package com.fib.msgconverter.util;

import com.fib.commons.exception.CommonException;

public interface ConstantMB {
	public enum FieldType {
		/** 2002 combine-field */
		COMBINE_FIELD(2002, "combine-field", ""),
		/** 2008 reference-field */
		REFERENCE_FIELD(2008, "reference-field", "");

		private int code;
		private String name;
		private String text;

		FieldType(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}
	}

	public enum MessageType {
		/** 1000 common */
		COMMON(1000, "common", ""),
		/** 1001 xml */
		XML(1001, "xml", ""),
		/** 1002 tag */
		TAG(1002, "tag", ""),
		/** 1003 swift */
		SWIFT(1003, "swift", "");

		private int code;
		private String name;
		private String text;

		MessageType(int code, String name, String text) {
			this.code = code;
			this.name = name;
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}

		public static MessageType getMessageTypeByCode(int code) {
			for (MessageType mt : values()) {
				if (code == mt.getCode()) {
					return mt;
				}
			}
			throw new CommonException("not support message type " + code);
		}

		public static MessageType getMessageTypeByName(String name) {
			for (MessageType mt : values()) {
				if (name.equals(mt.getName())) {
					return mt;
				}
			}
			throw new CommonException("not support message type " + name);
		}
	}
}