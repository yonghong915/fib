/**
 * 北京长信通信息技术有限公司
 * 2008-8-25 下午08:55:41
 */
package com.fib.msgconverter.commgateway.channel.nio.reader.impl;

import java.util.Map;

import org.apache.log4j.Level;

import com.fib.msgconverter.commgateway.channel.nio.reader.AbstractNioReader;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.message.metadata.Constant;

/**
 * 检测长度的nio消息读取者 : 消息中内含长度域，读取者根据长度域的值计算消息长度
 * 
 * @author 刘恭亮
 * 
 */
public class LengthCheckNioReader extends AbstractNioReader {
	public static final String LEN_FLD_INX = "length-field-index";
	public static final String LEN_FLD_DTA_TYP = "length-field-data-type";
	public static final String LEN_FLD_DTA_ENC = "length-field-data-encoding";
	public static final String LEN_FLD_LEN = "length-field-length";
	public static final String LEN_OFFSET = "length-offset";

	public static final String LEN_OFFSET_OLD = "message-length-offset";
	/**
	 * 从消息的长度域中计算出的消息长度
	 */
	protected int messageLength = -1;

	/**
	 * 长度域在消息中的起始位置
	 */
	protected int lengthFieldIndex;

	/**
	 * 长度域数据类型: num int short byte net-int net-short
	 */
	protected int lengthFieldDataType;

	/**
	 * 长度域数据编码类型：BCD/ASC
	 */
	protected int lengthFieldDataEncoding = Constant.DTA_ENC_TYP_ASC;

	/**
	 * 长度域数据长度：LL为2，LLL为3
	 */
	protected int lengthFieldLength;

	/**
	 * 消息长度偏移量
	 */
	protected int messageLengthOffset = 0;

	public boolean checkMessageComplete() {
		if (-1 == messageLength) {
			int fieldLength = lengthFieldLength;
			if (Constant.DTA_TYP_NUM == lengthFieldDataType
					&& Constant.DTA_ENC_TYP_BCD == lengthFieldDataEncoding) {
				fieldLength = lengthFieldLength % 2 == 0 ? lengthFieldLength / 2
						: lengthFieldLength / 2 + 1;
			}
			if (messageBuffer.size() >= lengthFieldIndex + fieldLength) {
				// 长度域接收完，计算消息长度
				calculateMessageLength(messageBuffer.getBytesAt(
						lengthFieldIndex, fieldLength));
			} else {
				// 长度域还未接收完
				return false;
			}
		}

		// 判断是否接收完
		if (messageLength - messageLengthOffset == messageBuffer.size()) {
			return true;
		} else if (messageLength - messageLengthOffset < messageBuffer.size()) {
			messageBuffer.remove(messageLength - messageLengthOffset,
					messageBuffer.size() - messageLength + messageLengthOffset);
			return true;
		} else {
			if (Level.INFO.isGreaterOrEqual(logger.getLevel())) {
				// logger.info("messageLength=" + messageLength + " offset="
				// + messageLengthOffset + " hasRead=" + messageBuffer.size());
				logger
						.info(MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"LengthCheckNioReader.checkMessageComplete.info",
										new String[] { "" + messageLength,
												"" + messageLengthOffset,
												"" + messageBuffer.size() }));
			}
			return false;
		}
	}

	private void calculateMessageLength(byte[] lengthField) {
		switch (lengthFieldDataType) {
		case Constant.DTA_TYP_INT:
			messageLength = CodeUtil.BytesToInt(lengthField);
			break;
		case Constant.DTA_TYP_NET_INT:
			messageLength = CodeUtil.ntohi(lengthField);
			break;
		case Constant.DTA_TYP_BYTE:
			messageLength = lengthField[0];
			break;
		case Constant.DTA_TYP_SHORT:
			messageLength = CodeUtil.BytesToShort(lengthField);
			break;
		case Constant.DTA_TYP_NET_SHORT:
			messageLength = CodeUtil.ntohs(lengthField);
			break;
		case Constant.DTA_TYP_NUM:
			if (Constant.DTA_ENC_TYP_BCD == lengthFieldDataEncoding) {
				lengthField = CodeUtil.BCDtoASC(lengthField);
			}
			messageLength = Integer.parseInt(new String(lengthField).trim());
			break;
		default:
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.giantstone.commgateway.channel.nio.reader.AbstractNioReader#setParameters
	 * (java.util.Map)
	 */
	public void setParameters(Map parameters) {
		super.setParameters(parameters);

		parseParameters();
	}

	private void parseParameters() {
		// length-field-index
		String tmp = (String) parameters.get(LEN_FLD_INX);
		if (null == tmp) {
			// throw new RuntimeException("parameter " + LEN_FLD_INX +
			// " is null!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("parameter.null",
							new String[] { LEN_FLD_INX }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " + LEN_FLD_INX
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { LEN_FLD_INX }));
			}
		}
		lengthFieldIndex = Integer.parseInt(tmp);

		// length-field-data-type
		tmp = (String) parameters.get(LEN_FLD_DTA_TYP);
		if (null == tmp) {
			// throw new RuntimeException("parameter " + LEN_FLD_DTA_TYP
			// + " is null!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("parameter.null",
							new String[] { LEN_FLD_DTA_TYP }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " + LEN_FLD_DTA_TYP
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { LEN_FLD_DTA_TYP }));
			}
		}
		lengthFieldDataType = Constant.getDataTypeByText(tmp);
		if (!(lengthFieldDataType == Constant.DTA_TYP_BYTE
				|| lengthFieldDataType == Constant.DTA_TYP_INT
				|| lengthFieldDataType == Constant.DTA_TYP_NET_INT
				|| lengthFieldDataType == Constant.DTA_TYP_SHORT
				|| lengthFieldDataType == Constant.DTA_TYP_NET_SHORT || lengthFieldDataType == Constant.DTA_TYP_NUM)) {
			// throw new RuntimeException(
			// "parameter "
			// + LEN_FLD_DTA_TYP
			// + " is wrong! Unsupport Data Type["
			// + tmp
			// + "]! data type must be num/int/net-int/short/net-short/byte");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("Reader.dataType.unsupport",
							new String[] { LEN_FLD_DTA_TYP, tmp }));
		}

		// length-field-data-encoding
		tmp = (String) parameters.get(LEN_FLD_DTA_ENC);
		if (tmp != null) {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " + LEN_FLD_DTA_TYP
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { LEN_FLD_DTA_TYP }));
			}
			if ("bcd".equalsIgnoreCase(tmp)) {
				lengthFieldDataEncoding = Constant.DTA_ENC_TYP_BCD;
			}
		}

		// length-field-length
		tmp = (String) parameters.get(LEN_FLD_LEN);
		if (null == tmp) {
			// throw new RuntimeException("parameter " + LEN_FLD_LEN +
			// " is null!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("parameter.null",
							new String[] { LEN_FLD_LEN }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " + LEN_FLD_LEN
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { LEN_FLD_LEN }));
			}
		}
		lengthFieldLength = Integer.parseInt(tmp);
		switch (lengthFieldDataType) {
		case Constant.DTA_TYP_INT:
		case Constant.DTA_TYP_NET_INT:
			if (lengthFieldLength != 4) {
				// throw new RuntimeException(LEN_FLD_DTA_TYP + " is int, "
				// + LEN_FLD_LEN + " must be 4!");
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"LengthCheckReader.parseParameters.lengthEqual4",
										new String[] { LEN_FLD_DTA_TYP,
												LEN_FLD_LEN }));
			}
			break;
		case Constant.DTA_TYP_BYTE:
			if (lengthFieldLength != 1) {
				// throw new RuntimeException(LEN_FLD_DTA_TYP + " is byte, "
				// + LEN_FLD_LEN + " must be 1!");
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"LengthCheckReader.parseParameters.lengthEqual1",
										new String[] { LEN_FLD_DTA_TYP,
												LEN_FLD_LEN }));
			}
			break;
		case Constant.DTA_TYP_SHORT:
		case Constant.DTA_TYP_NET_SHORT:
			if (lengthFieldLength != 2) {
				// throw new RuntimeException(LEN_FLD_DTA_TYP + " is short, "
				// + LEN_FLD_LEN + " must be 2!");
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"LengthCheckReader.parseParameters.lengthEqual2",
										new String[] { LEN_FLD_DTA_TYP,
												LEN_FLD_LEN }));
			}
			break;
		case Constant.DTA_TYP_NUM:
		default:
			break;
		}

		// length-offset
		tmp = (String) parameters.get(LEN_OFFSET);
		if (null == tmp) {
			tmp = (String) parameters.get(LEN_OFFSET_OLD);
		}
		if (null != tmp) {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " + LEN_OFFSET
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { LEN_OFFSET }));
			}
			messageLengthOffset = Integer.parseInt(tmp);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.giantstone.commgateway.channel.nio.reader.AbstractNioReader#clone()
	 */
	public Object clone() {
		LengthCheckNioReader o = (LengthCheckNioReader) super.clone();
		o.messageLength = -1;
		return o;
	}

}
