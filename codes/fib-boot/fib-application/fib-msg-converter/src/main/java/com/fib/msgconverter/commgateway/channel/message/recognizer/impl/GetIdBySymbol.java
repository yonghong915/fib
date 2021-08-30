/**
 * 北京长信通信息技术有限公司
 * 2008-8-29 下午12:03:29
 */
package com.fib.msgconverter.commgateway.channel.message.recognizer.impl;

import java.util.HashMap;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.giantstone.common.util.CodeUtil;

/**
 * 按前后符号取消息ID的识别器
 * 
 * @deprecated replaced by SeparatorRecognizer
 * @author 刘恭亮
 * 
 */
public class GetIdBySymbol extends AbstractMessageRecognizer {
	private static final String FROM = "from-symbol";
	private static final String TO = "to-symbol";
	public static final String FLD_NUM = "field-num";
	public static final String FLD = "field";

	private String fromSymbol;
	private String toSymbol;

	private int fieldNum = 1;
	private Map fields = null;

	public String recognize(byte[] message) {
		if (1 == fieldNum) {
			int from = CodeUtil.byteArrayIndexOf(message,
					fromSymbol.getBytes(), 0);
			if (-1 == from) {
				throw new RuntimeException("Can't find from symbol["
						+ fromSymbol + "] in message");
			}
			from += fromSymbol.getBytes().length;
			int to = CodeUtil.byteArrayIndexOf(message, toSymbol.getBytes(),
					from);
			if (-1 == to) {
				throw new RuntimeException("Can't find to symbol[" + toSymbol
						+ "] in message");
			}
			byte[] id = new byte[to - from];
			System.arraycopy(message, from, id, 0, to - from);
			return new String(id);
		} else {
			StringBuffer id = new StringBuffer(fieldNum);
			FieldParameter field;
			for (int i = 1; i < fieldNum + 1; i++) {
				field = (FieldParameter) fields.get(i);
				int from = CodeUtil.byteArrayIndexOf(message, field.fromSymbol
						.getBytes(), 0);
				if (-1 == from) {
					throw new RuntimeException("Can't find field" + i
							+ "'s from symbol[" + field.fromSymbol
							+ "] in message");
				}
				from += field.fromSymbol.getBytes().length;
				int to = CodeUtil.byteArrayIndexOf(message, field.toSymbol
						.getBytes(), from);
				if (-1 == to) {
					throw new RuntimeException("Can't find field" + i
							+ "'s to symbol[" + field.toSymbol + "] in message");
				}
				byte[] idBytes = new byte[to - from];
				System.arraycopy(message, from, idBytes, 0, to - from);
				id.append(idBytes);
			}
			return id.toString();
		}
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);

		parseParameters();
	}

	public void parseParameters() {
		String tmp = (String) this.parameters.get(FLD_NUM);
		if (null != tmp) {
			int num = Integer.parseInt(tmp.trim());
			if (num > 0) {
				fieldNum = num;
			}
		}

		if (1 == fieldNum) {
			// from-symbol
			tmp = (String) this.parameters.get(FROM);
			if (null == tmp) {
				throw new RuntimeException("parameter " + FROM + " is NULL");
			} else {
				tmp = tmp.trim();
				if (0 == tmp.length()) {
					throw new RuntimeException("parameter " + FROM
							+ " is Blank");
				}
			}
			fromSymbol = tmp;

			// to-symbol
			tmp = (String) this.parameters.get(TO);
			if (null == tmp) {
				throw new RuntimeException("parameter " + TO + " is NULL");
			} else {
				tmp = tmp.trim();
				if (0 == tmp.length()) {
					throw new RuntimeException("parameter " + TO + " is Blank");
				}
			}
			toSymbol = tmp;
		} else {
			fields = new HashMap(5);
			FieldParameter field;
			for (int i = 1; i < fieldNum + 1; i++) {
				field = new FieldParameter();
				// from-symbol
				tmp = (String) this.parameters.get(FLD + i + "." + FROM);
				if (null == tmp) {
					throw new RuntimeException("parameter " + FLD + i + "."
							+ FROM + " is NULL");
				} else {
					tmp = tmp.trim();
					if (0 == tmp.length()) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ FROM + " is Blank");
					}
				}
				field.fromSymbol = tmp;

				// to-symbol
				tmp = (String) this.parameters.get(FLD + i + "." + TO);
				if (null == tmp) {
					throw new RuntimeException("parameter " + FLD + i + "."
							+ TO + " is NULL");
				} else {
					tmp = tmp.trim();
					if (0 == tmp.length()) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ TO + " is Blank");
					}
				}
				field.toSymbol = tmp;

				fields.put(i, field);
			}
		}
	}

	private class FieldParameter {
		public String fromSymbol;
		public String toSymbol;
	}
}
