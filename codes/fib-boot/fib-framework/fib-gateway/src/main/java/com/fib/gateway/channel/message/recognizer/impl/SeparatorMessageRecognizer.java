package com.fib.gateway.channel.message.recognizer.impl;

import java.util.Map;

import com.fib.gateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.gateway.message.util.CodeUtil;
import com.fib.gateway.message.xml.message.Constant;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

/**
 * 分隔符报文识别器
 * 
 * @author 白帆
 * 
 */
public class SeparatorMessageRecognizer extends AbstractMessageRecognizer {
	public static final String SEPARATOR = "separator";
	public static final String INDEX = "field-index";
	public static final String SEPARATOR_TYPE = "separator-type";

	private byte[] separator;

	private int index;

	public String recognize(byte[] message) {
		int startAt = 0;
		int tmp = index;
		while (1 < tmp) {
			startAt = CodeUtil.byteArrayIndexOf(message, separator, startAt
					+ separator.length);
			tmp--;
		}
		if (0 > startAt) {
			// throw new RuntimeException("Can not find separator[" + separator
			// + "], index[" + tmp + "]");
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"SeparatorMessageRecognizer.recognize.separator.notFound",
									new String[] {
											CodeUtil
													.Bytes2FormattedText(separator),
											"" + index }));
		}
		int endAt = 0;
		if (0 == startAt) {
			endAt = CodeUtil.byteArrayIndexOf(message, separator, startAt);
		} else {
			endAt = CodeUtil.byteArrayIndexOf(message, separator, startAt
					+ separator.length);
		}
		if (-1 == endAt) {
			endAt = message.length;
		}
		byte[] messageId = null;
		if (0 == startAt) {
			messageId = new byte[endAt - startAt];
			System.arraycopy(message, 0, messageId, 0, endAt - startAt);
		} else {
			messageId = new byte[endAt - startAt - separator.length];
			System.arraycopy(message, startAt + separator.length, messageId, 0,
					endAt - startAt - separator.length);
		}

		return new String(messageId);
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);
		parseParameters();
	}

	private void parseParameters() {
		// separator
		String separatorStr = (String) parameters.get(SEPARATOR);
		if (null == separatorStr) {
			// throw new RuntimeException("paramter[" + SEPARATOR +
			// "] is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("parameter.null",
							new String[] { SEPARATOR }));
		}

		// separator-type
		String tmp = (String) parameters.get(SEPARATOR_TYPE);

		if (Constant.DTA_TYP_BIN_TXT.equalsIgnoreCase(tmp)) {
			if (separatorStr.startsWith("0x")) {
				separatorStr = separatorStr.substring(2);
			}
			separator = CodeUtil.HextoByte(separatorStr);
		} else {
			separator = separatorStr.getBytes();
		}

		// field-index
		tmp = (String) parameters.get(INDEX);
		if (null == tmp) {
			// throw new RuntimeException("paramter[" + INDEX + "] is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("parameter.null",
							new String[] { INDEX }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter[" + INDEX +
				// "] is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { INDEX }));
			}
		}
		index = Integer.parseInt(tmp);
		if (1 > index) {
			// throw new RuntimeException(
			// "field-index start from 1!Must NOT less than 1");
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"SeparatorMessageRecognizer.recognize.fieldIndex.lessThan1",
									new String[] { index + "" }));
		}
	}
}
