package com.fib.msgconverter.commgateway.channel.config.processor.event;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

public class ProcessorRule {
	// dest-request-message
	public static final String TYP_DEST_REQ_TXT = "dest-request-message";
	public static final int TYP_DEST_REQ = 9900;
	// source-request-message
	public static final String TYP_SRC_REQ_TXT = "source-request-message";
	public static final int TYP_SRC_REQ = 9901;
	// dest-response-message
	public static final String TYP_DEST_RES_TXT = "dest-response-message";
	public static final int TYP_DEST_RES = 9902;
	// source-response-message
	public static final String TYP_SRC_RES_TXT = "source-response-message";
	public static final int TYP_SRC_RES = 9903;

	private String processorId;
	private int requestMessageFrom;

	// private String notifyProcessorId;
	// private int notifyRequestMessageFrom;
	public String getProcessorId() {
		return processorId;
	}

	public void setProcessorId(String processorId) {
		this.processorId = processorId;
	}

	public int getRequestMessageFrom() {
		return requestMessageFrom;
	}

	public void setRequestMessageFrom(int requestMessageFrom) {
		this.requestMessageFrom = requestMessageFrom;
	}

	// public String getNotifyProcessorId() {
	// return notifyProcessorId;
	// }
	// public void setNotifyProcessorId(String notifyProcessorId) {
	// this.notifyProcessorId = notifyProcessorId;
	// }
	// public int getNotifyRequestMessageFrom() {
	// return notifyRequestMessageFrom;
	// }
	// public void setNotifyRequestMessageFrom(int notifyRequestMessageFrom) {
	// this.notifyRequestMessageFrom = notifyRequestMessageFrom;
	// }
	public static String getRequestMessageFromText(int requestMessageFrom) {
		switch (requestMessageFrom) {
		case TYP_DEST_REQ:
			return TYP_DEST_REQ_TXT;
		case TYP_SRC_REQ:
			return TYP_SRC_REQ_TXT;
		case TYP_DEST_RES:
			return TYP_DEST_RES_TXT;
		case TYP_SRC_RES:
			return TYP_SRC_RES_TXT;
		default:
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"ProcessorRule.getRequestMessageFromByText.requestMessageFrom.unkown",
									new String[] { requestMessageFrom + "" }));
		}
	}

	public static int getRequestMessageFromByText(String text) {
		if (TYP_DEST_REQ_TXT.equals(text)) {
			return TYP_DEST_REQ;
		} else if (TYP_SRC_REQ_TXT.equals(text)) {
			return TYP_SRC_REQ;
		} else if (TYP_DEST_RES_TXT.equals(text)) {
			return TYP_DEST_RES;
		} else if (TYP_SRC_RES_TXT.equals(text)) {
			return TYP_SRC_RES;
		} else {
			// throw new
			// IllegalArgumentException("Unkown request-message-from :" + text);
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"ProcessorRule.getRequestMessageFromByText.requestMessageFrom.unkown",
									new String[] { text }));
		}
	}
}
