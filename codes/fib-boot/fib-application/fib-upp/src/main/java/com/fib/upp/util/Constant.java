package com.fib.upp.util;

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
}
