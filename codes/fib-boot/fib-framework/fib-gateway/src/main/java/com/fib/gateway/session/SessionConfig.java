package com.fib.gateway.session;

import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class SessionConfig {

	public final static String SESSION_INTO_DB_LEVEL_SUCCESS_TXT = "SUCCESS";
	public final static String SESSION_INTO_DB_LEVEL_ERROR_TXT = "ERROR";

	private long timeout = 5 * 60 * 1000;
	private String timeText;
	private String levelIntoDBText = SESSION_INTO_DB_LEVEL_ERROR_TXT;
	private boolean shieldSensitiveField = false;
	private String sensitiveFieldsInMap;
	private char sensitiveFieldsRelpace = '*';

	public static long getTimeByText(String text) {
		if (3 >= text.length()) {
			return Integer.parseInt(text);
		} else {
			String timeunit = text.substring(text.length() - 3);
			long quantity = Long
					.parseLong(text.substring(0, text.length() - 3));

			if ("sec".equals(timeunit)) {
				return quantity * 1000;
			} else if ("min".equals(timeunit)) {
				return quantity * 1000 * 60;
			} else if ("hou".equals(timeunit)) {
				return quantity * 1000 * 60 * 60;
			} else if ("day".equals(timeunit)) {
				return quantity * 1000 * 60 * 60 * 24;
			} else {
				// throw new
				// RuntimeException("Unsupported time expression end :"
				// + timeunit + ", must be sec/min/hou/day");
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"SessionConfig.getTimeByText.timeExpressionEnd.unsupport",
										new String[] { timeunit }));
			}
		}
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getTimeText() {
		return timeText;
	}

	public void setTimeText(String timeText) {
		this.timeText = timeText;
	}

	public String getLevelIntoDBText() {
		return levelIntoDBText;
	}

	public void setLevelIntoDBText(String levelIntoDBText) {
		this.levelIntoDBText = levelIntoDBText;
	}

	public boolean isShieldSensitiveField() {
		return shieldSensitiveField;
	}

	public void setShieldSensitiveField(boolean shieldSensitiveField) {
		this.shieldSensitiveField = shieldSensitiveField;
	}

	public String getSensitiveFieldsInMap() {
		return sensitiveFieldsInMap;
	}

	public void setSensitiveFieldsInMap(String sensitiveFieldsInMap) {
		this.sensitiveFieldsInMap = sensitiveFieldsInMap;
	}

	public char getSensitiveFieldsRelpace() {
		return sensitiveFieldsRelpace;
	}

	public void setSensitiveFieldsRelpace(char sensitiveFieldsRelpace) {
		this.sensitiveFieldsRelpace = sensitiveFieldsRelpace;
	}

}
