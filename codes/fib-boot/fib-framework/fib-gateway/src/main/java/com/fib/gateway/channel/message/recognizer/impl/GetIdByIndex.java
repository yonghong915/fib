/**
 * 北京长信通信息技术有限公司
 * 2008-8-29 上午11:33:37
 */
package com.fib.gateway.channel.message.recognizer.impl;

import java.util.HashMap;
import java.util.Map;

import com.fib.gateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.gateway.message.util.CodeUtil;
import com.fib.gateway.message.xml.message.Constant;

/**
 * 按索引号取消息Id的识别器
 * 
 * @deprecated replaced by IndexRecognizer
 * @author 刘恭亮
 * 
 */
public class GetIdByIndex extends AbstractMessageRecognizer {
	public static final String FLD_INX = "index";
	public static final String FLD_DTA_TYP = "data-type";
	public static final String FLD_DTA_ENC = "data-encoding";
	public static final String FLD_LEN = "length";
	public static final String FLD_NUM = "field-num";
	public static final String FLD = "field";

	private int index;
	private int length;
	private int dataType = Constant.DTA_TYP_STR;
	private int dataEncoding = Constant.DTA_ENC_TYP_ASC;
	private int fieldNum = 1;
	private Map fields = null;

	public String recognize(byte[] message) {
		if (1 == fieldNum) {
			if (index + length > message.length) {
				throw new RuntimeException("message to short! key field index="
						+ index + ", length=" + length);
			}
			byte[] id = new byte[length];
			System.arraycopy(message, index, id, 0, length);
			if (Constant.DTA_ENC_TYP_BCD == dataEncoding) {
				id = CodeUtil.BCDtoASC(id);
			}
			return new String(id);
		} else {
			StringBuffer id = new StringBuffer(fieldNum);
			FieldParameter field;
			for (int i = 1; i < fieldNum + 1; i++) {
				field = (FieldParameter) fields.get(i);
				if (field.index + field.length > message.length) {
					throw new RuntimeException(
							"message to short! key field index=" + field.index
									+ ", length=" + field.length);
				}
				byte[] idByte = new byte[field.length];
				System.arraycopy(message, field.index, idByte, 0, field.length);
				if (Constant.DTA_ENC_TYP_BCD == field.dataEncoding) {
					idByte = CodeUtil.BCDtoASC(idByte);
				}
				id.append(idByte);
			}
			return id.toString();
		}
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);

		parseParameters();
	}

	private void parseParameters() {
		String tmp = (String) this.parameters.get(FLD_NUM);
		if (null != tmp) {
			int num = Integer.parseInt(tmp.trim());
			if (num > 0) {
				fieldNum = num;
			}
		}
		if (1 == fieldNum) {
			// index
			tmp = (String) this.parameters.get(FLD_INX);
			if (null == tmp) {
				throw new RuntimeException("parameter " + FLD_INX + " is null!");
			} else {
				tmp = tmp.trim();
				if (0 == tmp.length()) {
					throw new RuntimeException("parameter " + FLD_INX
							+ " is blank!");
				}
			}
			this.index = Integer.parseInt(tmp);
			if (index < 0) {
				throw new RuntimeException("parameter " + FLD_INX
						+ " must >= 0!");
			}

			// data-type
			tmp = (String) this.parameters.get(FLD_DTA_TYP);
			if (null != tmp) {
				tmp = tmp.trim();
				if (0 == tmp.length()) {
					throw new RuntimeException("parameter " + FLD_DTA_TYP
							+ " is blank!");
				}
				this.dataType = Constant.getDataTypeByText(tmp);
				if (!(this.dataType == Constant.DTA_TYP_STR || this.dataType == Constant.DTA_TYP_NUM)) {
					throw new RuntimeException("parameter " + FLD_DTA_TYP
							+ " is wrong! Unsupport Data Type[" + tmp
							+ "]! data type must be num/str");
				}
			}

			// data-encoding
			tmp = (String) this.parameters.get(FLD_DTA_ENC);
			if (tmp != null) {
				tmp = tmp.trim();
				if (0 == tmp.length()) {
					throw new RuntimeException("parameter " + FLD_DTA_TYP
							+ " is blank!");
				}
				if ("bcd".equalsIgnoreCase(tmp)) {
					this.dataEncoding = Constant.DTA_ENC_TYP_BCD;
				}
			}

			// length
			tmp = (String) this.parameters.get(FLD_LEN);
			if (null == tmp) {
				throw new RuntimeException("parameter " + FLD_LEN + " is null!");
			} else {
				tmp = tmp.trim();
				if (0 == tmp.length()) {
					throw new RuntimeException("parameter " + FLD_LEN
							+ " is blank!");
				}
			}
			this.length = Integer.parseInt(tmp);
			if (length < 0) {
				throw new RuntimeException("parameter " + FLD_LEN
						+ " must > 0!");
			}
		} else {
			fields = new HashMap(5);
			FieldParameter fieldParam = new FieldParameter();
			for (int i = 1; i < fieldNum + 1; i++) {
				// index
				tmp = (String) this.parameters.get(FLD + i + "." + FLD_INX);
				if (null == tmp) {
					throw new RuntimeException("parameter " + FLD + i + "."
							+ FLD_INX + " is null!");
				} else {
					tmp = tmp.trim();
					if (0 == tmp.length()) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ FLD_INX + " is blank!");
					}
				}
				fieldParam.index = Integer.parseInt(tmp);
				if (fieldParam.index < 0) {
					throw new RuntimeException("parameter " + FLD + i + "."
							+ FLD_INX + " must >= 0!");
				}

				// data-type
				tmp = (String) this.parameters.get(FLD + i + "." + FLD_DTA_TYP);
				if (null != tmp) {
					tmp = tmp.trim();
					if (0 == tmp.length()) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ FLD_DTA_TYP + " is blank!");
					}
					fieldParam.dataType = Constant.getDataTypeByText(tmp);
					if (!(fieldParam.dataType == Constant.DTA_TYP_STR || fieldParam.dataType == Constant.DTA_TYP_NUM)) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ FLD_DTA_TYP
								+ " is wrong! Unsupport Data Type[" + tmp
								+ "]! data type must be num/str");
					}
				}

				// data-encoding
				tmp = (String) this.parameters.get(FLD + i + "." + FLD_DTA_ENC);
				if (tmp != null) {
					tmp = tmp.trim();
					if (0 == tmp.length()) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ FLD_DTA_ENC + " is blank!");
					}
					if ("bcd".equalsIgnoreCase(tmp)) {
						fieldParam.dataEncoding = Constant.DTA_ENC_TYP_BCD;
					}
				}

				// length
				tmp = (String) this.parameters.get(FLD + i + "." + FLD_LEN);
				if (null == tmp) {
					throw new RuntimeException("parameter " + FLD + i + "."
							+ FLD_LEN + " is null!");
				} else {
					tmp = tmp.trim();
					if (0 == tmp.length()) {
						throw new RuntimeException("parameter " + FLD + i + "."
								+ FLD_LEN + " is blank!");
					}
				}
				fieldParam.length = Integer.parseInt(tmp);
				if (fieldParam.length < 0) {
					throw new RuntimeException("parameter " + FLD + i + "."
							+ FLD_LEN + " must > 0!");
				}

				fields.put(i, fieldParam);
			}
		}

	}

	private class FieldParameter {
		public int index;
		public int length;
		public int dataType = Constant.DTA_TYP_STR;
		public int dataEncoding = Constant.DTA_ENC_TYP_ASC;
	}

}
