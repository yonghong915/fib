package com.fib.core.util;

public class ConstantUtil {

	public static class DSType {
		public static final String DS_TYPE_SYSDB = "SYSDB";
		public static final String DS_TYPE_APPDB = "UMPDB";
	}

	/**
	 * 日志类型 0:操作日志;1:登录日志;2:定时任务
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @since 1.0
	 */
	public static class LogType {
		/**
		 * 系统日志类型： 操作
		 */
		public static final int OPERATE_TYPE = 1;

		public static final int LOGIN_TYPE = 2;
	}

	public static class JWTConstants {

		/**
		 * 上线需要变更
		 */
		public static final String JWT_SECRET = "JYJ5Qv2WF4lA6jPl5GKuAG";

		public static final String AUTHORIZATION = "X-AUTH-TOKEN";
		public static final String CURRENT_USER_NAME = "CURRENT_TOKEN_USER_NAME";
		public static final String CURRENT_TOKEN_CLAIMS = "CURRENT_TOKEN_CLAIMS";
		public static final long TOKEN_EXPIRES_HOUR = 2;

	}

	public static enum MenuType {
		MENU(1), BUTTON(2);

		private int value;

		MenuType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public static enum UserStatus {
		NORMAL("01"), EXPIRE("01"), LOCKED("02");

		private String value;

		UserStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}