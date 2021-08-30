package com.fib.msgconverter.commgateway.channel.message.recognizer.impl;

import java.util.HashMap;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.message.metadata.Constant;

@Deprecated
// 可由索引识别器和分隔符识别器组合实现
/*
 * Tag报文识别器
 * 
 * @author 白帆
 */
public class TagMessageRecognizer extends AbstractMessageRecognizer {
	public static final String FLD_INX = "index";
	public static final String FLD_DTA_TYP = "data-type";
	public static final String FLD_DTA_ENC = "data-encoding";
	public static final String FLD_LEN = "length";
	public static final String FLD_NUM = "field-num";
	public static final String FLD = "field";
	public static final String TAG = "tag";

	private String tag;
	private int index;
	private int length;
	private int dataType = Constant.DTA_TYP_STR;
	private int dataEncoding = Constant.DTA_ENC_TYP_ASC;
	private int fieldNum = 1;
	private Map fields = null;

	public String recognize(byte[] message) {
		if (1 == fieldNum) {
			if (null == tag) {
				if (index + length > message.length) {
					throw new RuntimeException(
							"message to short! key field index=" + index
									+ ", length=" + length);
				}
				byte[] id = new byte[length];
				System.arraycopy(message, index, id, 0, length);
				if (Constant.DTA_ENC_TYP_BCD == dataEncoding) {
					id = CodeUtil.BCDtoASC(id);
				}
				return new String(id);
			} else {
				int from = CodeUtil.byteArrayIndexOf(message, (tag + ":")
						.getBytes(), 0);
				if (-1 == from) {
					throw new RuntimeException("Can't find tag[" + tag
							+ "] in message");
				}
				from += tag.getBytes().length + 1;
				int to = CodeUtil.byteArrayIndexOf(message, ":".getBytes(),
						from);
				byte[] id = new byte[to - from];
				System.arraycopy(message, from, id, 0, to - from);
				return new String(id);
			}
		} else {
			String id = "";
			FieldParameter field;
			for (int i = 1; i < fieldNum + 1; i++) {
				field = (FieldParameter) fields.get(i);
				if (null == field.tag) {
					if (field.index + field.length > message.length) {
						throw new RuntimeException(
								"message to short! key field index="
										+ field.index + ", length="
										+ field.length);
					}
					byte[] subId = new byte[field.length];
					System.arraycopy(message, field.index, subId, 0,
							field.length);
					if (Constant.DTA_ENC_TYP_BCD == field.dataEncoding) {
						subId = CodeUtil.BCDtoASC(subId);
					}
					id += new String(subId);
				} else {
					int from = CodeUtil.byteArrayIndexOf(message,
							(field.tag + ":").getBytes(), 0);
					if (-1 == from) {
						throw new RuntimeException("Can't find field" + i
								+ "'s tag[" + field.tag + "] in message");
					}
					from += field.tag.getBytes().length + 1;
					int to = CodeUtil.byteArrayIndexOf(message, ":".getBytes(),
							from);
					byte[] subId = new byte[to - from];
					System.arraycopy(message, from, subId, 0, to - from);
					id += new String(subId);
				}
			}
			return id;
		}
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);

		parseParameters();
	}

	private void parseParameters() {
		// field-num
		String tmp = (String) parameters.get(FLD_NUM);
		if (null != tmp) {
			int num = Integer.parseInt(tmp);
			if (0 < num) {
				fieldNum = num;
			}
		}

		if (1 == fieldNum) {
			tmp = (String) parameters.get(TAG);
			if (null == tmp) {
				// index
				tmp = (String) parameters.get(FLD_INX);
				if (null == tmp) {
					throw new RuntimeException("parameter " + FLD_INX
							+ " is null!");
				} else {
					tmp = tmp.trim();
					if (0 == tmp.length()) {
						throw new RuntimeException("parameter " + FLD_INX
								+ " is blank!");
					}
				}
				index = Integer.parseInt(tmp);
				if (index < 0) {
					throw new RuntimeException("parameter " + FLD_INX
							+ " must >= 0!");
				}

				// data-type
				tmp = (String) parameters.get(FLD_DTA_TYP);
				if (null != tmp) {
					tmp = tmp.trim();
					if (0 == tmp.length()) {
						throw new RuntimeException("parameter " + FLD_DTA_TYP
								+ " is blank!");
					}
					dataType = Constant.getDataTypeByText(tmp);
					if (!(dataType == Constant.DTA_TYP_STR || dataType == Constant.DTA_TYP_NUM)) {
						throw new RuntimeException("parameter " + FLD_DTA_TYP
								+ " is wrong! Unsupport Data Type[" + tmp
								+ "]! data type must be num/str");
					}
				}

				// data-encoding
				tmp = (String) parameters.get(FLD_DTA_ENC);
				if (tmp != null) {
					tmp = tmp.trim();
					if (0 == tmp.length()) {
						throw new RuntimeException("parameter " + FLD_DTA_TYP
								+ " is blank!");
					}
					if ("bcd".equalsIgnoreCase(tmp)) {
						dataEncoding = Constant.DTA_ENC_TYP_BCD;
					}
				}

				// length
				tmp = (String) parameters.get(FLD_LEN);
				if (null == tmp) {
					throw new RuntimeException("parameter " + FLD_LEN
							+ " is null!");
				} else {
					tmp = tmp.trim();
					if (0 == tmp.length()) {
						throw new RuntimeException("parameter " + FLD_LEN
								+ " is blank!");
					}
				}
				length = Integer.parseInt(tmp);
				if (length <= 0) {
					throw new RuntimeException("parameter " + FLD_LEN
							+ " must > 0!");
				}
			} else {
				// tag
				if (null != parameters.get(FLD_INX)) {
					throw new RuntimeException("parameter " + TAG + " and "
							+ FLD_INX + " only need one!");
				}
				tmp = tmp.trim();
				if (0 == tmp.length()) {
					throw new RuntimeException("parameter " + TAG + " is Blank");
				}
				tag = tmp;
			}
		} else {
			fields = new HashMap(5);
			for (int i = 1; i < fieldNum + 1; i++) {
				FieldParameter field = new FieldParameter();
				// tag
				tmp = (String) parameters.get(FLD + i + "." + TAG);
				if (null == tmp) {
					// index
					tmp = (String) parameters.get(FLD + i + "." + FLD_INX);
					if (null == tmp) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ FLD_INX + " is null!");
					} else {
						tmp = tmp.trim();
						if (0 == tmp.length()) {
							throw new RuntimeException("parameter " + FLD + i
									+ "." + FLD_INX + " is blank!");
						}
					}
					field.index = Integer.parseInt(tmp);
					if (field.index < 0) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ FLD_INX + " must >= 0!");
					}

					// data-type
					tmp = (String) parameters.get(FLD + i + "." + FLD_DTA_TYP);
					if (null != tmp) {
						tmp = tmp.trim();
						if (0 == tmp.length()) {
							throw new RuntimeException("parameter " + FLD + i
									+ "." + FLD_DTA_TYP + " is blank!");
						}
						field.dataType = Constant.getDataTypeByText(tmp);
						if (!(field.dataType == Constant.DTA_TYP_STR || field.dataType == Constant.DTA_TYP_NUM)) {
							throw new RuntimeException("parameter " + FLD + i
									+ "." + FLD_DTA_TYP
									+ " is wrong! Unsupport Data Type[" + tmp
									+ "]! data type must be num/str");
						}
					}

					// data-encoding
					tmp = (String) parameters.get(FLD + i + "." + FLD_DTA_ENC);
					if (tmp != null) {
						tmp = tmp.trim();
						if (0 == tmp.length()) {
							throw new RuntimeException("parameter " + FLD + i
									+ "." + FLD_DTA_TYP + " is blank!");
						}
						if ("bcd".equalsIgnoreCase(tmp)) {
							field.dataEncoding = Constant.DTA_ENC_TYP_BCD;
						}
					}

					// length
					tmp = (String) parameters.get(FLD + i + "." + FLD_LEN);
					if (null == tmp) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ FLD_LEN + " is null!");
					} else {
						tmp = tmp.trim();
						if (0 == tmp.length()) {
							throw new RuntimeException("parameter " + FLD + i
									+ "." + FLD_LEN + " is blank!");
						}
					}
					field.length = Integer.parseInt(tmp);
					if (field.length <= 0) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ FLD_LEN + " must > 0!");
					}
				} else {
					// tag
					if (null != parameters.get(FLD + i + "." + FLD_INX)) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ TAG + " and " + FLD_INX + " only need one!");
					}
					tmp = tmp.trim();
					if (0 == tmp.length()) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ TAG + " is Blank");
					}
					field.tag = tmp;
				}
				fields.put(i, field);
			}
		}
	}

	private class FieldParameter {
		public int index;
		public int length;
		public int dataType = Constant.DTA_TYP_STR;
		public int dataEncoding = Constant.DTA_ENC_TYP_ASC;
		public String tag;
	}
}
