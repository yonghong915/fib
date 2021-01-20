package com.fib.gateway.channel.config.processor.event;

import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class ScheduleRule {
	private int loop;
	private long interval;
	private String intervalString;
	private String endFlag;

	public int getLoop() {
		return loop;
	}

	public void setLoop(int loop) {
		this.loop = loop;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public String getEndFlag() {
		return endFlag;
	}

	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

	public String getIntervalString() {
		return intervalString;
	}

	public void setIntervalString(String intervalString) {
		this.intervalString = intervalString;
	}

	public static long getTimesByText(String text) {
		if (3 >= text.length()) {
			return Long.parseLong(text);
		} else {
			String timeunit = text.substring(text.length() - 3);
			String quantityStr = text.substring(0, text.length() - 3);
			int quantity = Integer.parseInt(quantityStr);
			if ("sec".equals(timeunit)) {
				return quantity * 1000;
			} else if ("min".equals(timeunit)) {
				return quantity * 1000 * 60;
			} else if ("hou".equals(text)) {
				return quantity * 1000 * 60 * 60;
			} else if ("day".equals(text)) {
				return quantity * 1000 * 60 * 60 * 24;
			} else {
				// throw new IllegalArgumentException("Unsupported Time[" + text
				// + "]!");
				throw new IllegalArgumentException(
						MultiLanguageResourceBundle.getInstance().getString("time.unsupport", new String[] { text }));
			}
		}
	}
}
