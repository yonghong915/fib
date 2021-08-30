package com.fib.msgconverter.commgateway.channel.longconnection.reader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.longconnection.reader.AbstractReader;
import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.message.metadata.Constant;

/**
 * 检测长度的消息读取器 : 消息中内含长度域，读取者根据长度域的值计算消息长度
 * 
 * @author 刘恭亮
 */
public class LengthCheckReader extends AbstractReader {
	public static final String LEN_FLD_INX = "length-field-index";
	public static final String LEN_FLD_DTA_TYP = "length-field-data-type";
	public static final String LEN_FLD_DTA_ENC = "length-field-data-encoding";
	public static final String LEN_FLD_LEN = "length-field-length";
	public static final String LEN_OFFSET = "message-length-offset";

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

	private byte[] appendData;

	public byte[] read(InputStream in) {
		byte[] readBuf = null; // 读取缓冲
		byte[] messageBuf = null; // 报文缓冲
		int onceRead = 0; // 一次读取的长度
		int realLengthFieldLength = 0;
		if (Constant.DTA_TYP_NUM == lengthFieldDataType && Constant.DTA_ENC_TYP_BCD == lengthFieldDataEncoding) {
			realLengthFieldLength = lengthFieldLength % 2 == 0 ? lengthFieldLength / 2 : lengthFieldLength / 2 + 1;
		} else {
			realLengthFieldLength = lengthFieldLength;
		}
		// 1. 尝试读心跳包
		if (heartbeatConfig != null) {
			byte[] heartbeatMsg = heartbeatConfig.getMessageSymbol().determineMessage();
			readBuf = new byte[heartbeatMsg.length];
			if (appendData != null && appendData.length > 0) {
				// 处理上次多读的部分数据
				System.arraycopy(appendData, 0, readBuf, 0, appendData.length);
				onceRead = appendData.length;
				appendData = null;
			} else {
				// 读取报文一次
				try {
					onceRead = in.read(readBuf, 0, readBuf.length);
				} catch (IOException e) {
					ExceptionUtil.throwActualException(e);
				}

				if (-1 == onceRead) {
					// throw new RuntimeException(
					// "onceRead = -1, remote socket maybe be closed!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("onceRead.-1"));
				}
				if (0 == onceRead) {
					// throw new RuntimeException(
					// "onceRead = 0, remote socket maybe be closed!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("onceRead.0"));
				}
			}

			int readLength = onceRead;

			// 判断接收到报文是否等于心跳包
			while (readLength < heartbeatMsg.length) {
				if (isPartOfHeartbeatMessage(readBuf, heartbeatMsg, readLength)) {
					try {
						onceRead = in.read(readBuf, readLength, readBuf.length - readLength);
					} catch (IOException e) {
						ExceptionUtil.throwActualException(e);
					}
					readLength += onceRead;
				} else {
					break;
				}
			}

			if (readLength == readBuf.length && isHeartbeatMessage(readBuf, heartbeatMsg)) {
				// 是心跳包则直接返回
				return readBuf;
			} else {
				// 非心跳包
				if (lengthFieldIndex + realLengthFieldLength <= readLength) {
					// 处理心跳包长度大于某个报文，且完整报文后有连续的报文
					byte[] lengthFieldBytes = new byte[lengthFieldLength];
					System.arraycopy(readBuf, lengthFieldIndex, lengthFieldBytes, 0, realLengthFieldLength);
					int messageLength = calculateMessageLength(lengthFieldBytes) - messageLengthOffset;
					if (messageLength < readLength) {
						int temp = readLength;
						readLength = messageLength;
						appendData = new byte[temp - messageLength];
						System.arraycopy(readBuf, messageLength, appendData, 0, appendData.length);
					}
				}
				// 已读部分加入报文缓冲
				messageBuf = appendByteBuf(readBuf, readLength, messageBuf);
				if (logger.isInfoEnabled()) {
					// logger.info("read partial message : onceRead = " +
					// onceRead
					// + " hasRead=" + messageBuf.length
					// + " hasReadMessage:\n"
					// + CodeUtil.Bytes2FormattedText(messageBuf));
					logger.info(MultiLanguageResourceBundle.getInstance().getString("AbstractReader.read.partMessage",
							new String[] { "" + readLength, "" + messageBuf.length,
									CodeUtil.Bytes2FormattedText(messageBuf) }));
				}

				if (lengthFieldIndex + realLengthFieldLength <= readLength) {
					byte[] lengthFieldBytes = new byte[lengthFieldLength];
					System.arraycopy(messageBuf, lengthFieldIndex, lengthFieldBytes, 0, realLengthFieldLength);
					readBuf = new byte[calculateMessageLength(lengthFieldBytes) - messageLengthOffset - readLength];
				} else {
					readBuf = new byte[lengthFieldIndex + realLengthFieldLength - readLength];
				}

			}
		} else {
			readBuf = new byte[lengthFieldIndex + lengthFieldLength];
		}
		// 2. 读剩余报文
		while (true) {
			try {
				onceRead = in.read(readBuf, 0, readBuf.length);
			} catch (IOException e) {
				// e.printStackTrace();
				ExceptionUtil.throwActualException(e);
			}
			if (-1 == onceRead) {
				// throw new RuntimeException(
				// "onceRead = -1, remote socket maybe be closed!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("onceRead.-1"));
			}
			if (onceRead > 0) {
				messageBuf = appendByteBuf(readBuf, onceRead, messageBuf);

			}

			boolean complete = checkMessageComplete(messageBuf);

			if (complete) {
				for (int i = 0; i < filterList.size(); i++) {
					AbstractMessageFilter filter = (AbstractMessageFilter) filterList.get(i);
					String filterClassName = filter.getClass().getName();

					if (logger.isDebugEnabled()) {
						// logger.debug("message before filter[" +
						// filterClassName
						// + "]:\n"
						// + CodeUtil.Bytes2FormattedText(messageBuf));
						logger.debug(MultiLanguageResourceBundle.getInstance().getString("message.beforeFilter",
								new String[] { filterClassName, CodeUtil.Bytes2FormattedText(messageBuf) }));
					}

					messageBuf = filter.doFilter(messageBuf);

					if (logger.isDebugEnabled()) {
						// logger.debug("message after filter[" +
						// filterClassName
						// + "]:\n"
						// + CodeUtil.Bytes2FormattedText(messageBuf));
						logger.debug(MultiLanguageResourceBundle.getInstance().getString("message.afterFilter",
								new String[] { filterClassName, CodeUtil.Bytes2FormattedText(messageBuf) }));
					}

				}
				break;
			} else {
				if (logger.isInfoEnabled()) {
					// logger.info("read partial message : onceRead = " +
					// onceRead
					// + " hasRead=" + messageBuf.length
					// + " hasReadMessage:\n"
					// + CodeUtil.Bytes2FormattedText(messageBuf));
					logger.info(MultiLanguageResourceBundle.getInstance().getString("AbstractReader.read.partMessage",
							new String[] { "" + onceRead, "" + messageBuf.length,
									CodeUtil.Bytes2FormattedText(messageBuf) }));
				}

				if (messageBuf.length >= lengthFieldIndex + realLengthFieldLength) {
					// 长度域接收完，计算消息长度
					byte[] lengthFieldData = new byte[realLengthFieldLength];
					System.arraycopy(messageBuf, lengthFieldIndex, lengthFieldData, 0, lengthFieldData.length);
					readBuf = new byte[calculateMessageLength(lengthFieldData) - messageLengthOffset
							- messageBuf.length];
				} else {
					// 长度域还未接收完
					readBuf = new byte[lengthFieldIndex + realLengthFieldLength - messageBuf.length];
				}
			}
		}

		return messageBuf;
	}

	public boolean checkMessageComplete(byte[] message) {
		int messageLength = 0;

		// 从长度域计算报文长度
		int fieldLength = lengthFieldLength;
		if (Constant.DTA_TYP_NUM == lengthFieldDataType && Constant.DTA_ENC_TYP_BCD == lengthFieldDataEncoding) {
			fieldLength = lengthFieldLength % 2 == 0 ? lengthFieldLength / 2 : lengthFieldLength / 2 + 1;
		}
		if (message.length >= lengthFieldIndex + fieldLength) {
			// 长度域接收完，计算消息长度
			byte[] lengthFieldData = new byte[fieldLength];
			System.arraycopy(message, lengthFieldIndex, lengthFieldData, 0, lengthFieldData.length);
			messageLength = calculateMessageLength(lengthFieldData);
		} else {
			// 长度域还未接收完
			return false;
		}

		// 判断是否接收完
		if (messageLength - messageLengthOffset <= message.length) {
			return true;
		}

		return false;
	}

	private int calculateMessageLength(byte[] lengthField) {
		switch (lengthFieldDataType) {
		case Constant.DTA_TYP_INT:
			return CodeUtil.BytesToInt(lengthField);
		case Constant.DTA_TYP_NET_INT:
			return CodeUtil.ntohi(lengthField);
		case Constant.DTA_TYP_BYTE:
			return lengthField[0];
		case Constant.DTA_TYP_SHORT:
			return CodeUtil.BytesToShort(lengthField);
		case Constant.DTA_TYP_NET_SHORT:
			return CodeUtil.ntohs(lengthField);
		case Constant.DTA_TYP_NUM:
			if (Constant.DTA_ENC_TYP_BCD == lengthFieldDataEncoding) {
				lengthField = CodeUtil.BCDtoASC(lengthField);
			}
			return Integer.parseInt(new String(lengthField).trim());
		default:
			// throw new RuntimeException(
			// "Unsupport Message Length Field Data Type:"
			// + lengthFieldDataType);
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"calculateMessageLength.lengthField.dataType.unsupport",
					new String[] { "" + lengthFieldDataType }));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fib.msgconverter.commgateway.channel.nio.reader.AbstractNioReader#
	 * setParameters (java.util.Map)
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
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { LEN_FLD_INX }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " + LEN_FLD_INX
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("parameter.blank",
						new String[] { LEN_FLD_INX }));
			}
		}
		lengthFieldIndex = Integer.parseInt(tmp);

		// length-field-data-type
		tmp = (String) parameters.get(LEN_FLD_DTA_TYP);
		if (null == tmp) {
			// throw new RuntimeException("parameter " + LEN_FLD_DTA_TYP
			// + " is null!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { LEN_FLD_DTA_TYP }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " + LEN_FLD_DTA_TYP
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("parameter.blank",
						new String[] { LEN_FLD_DTA_TYP }));
			}
		}
		lengthFieldDataType = Constant.getDataTypeByText(tmp);
		if (!(lengthFieldDataType == Constant.DTA_TYP_BYTE || lengthFieldDataType == Constant.DTA_TYP_INT
				|| lengthFieldDataType == Constant.DTA_TYP_NET_INT || lengthFieldDataType == Constant.DTA_TYP_SHORT
				|| lengthFieldDataType == Constant.DTA_TYP_NET_SHORT || lengthFieldDataType == Constant.DTA_TYP_NUM)) {
			// throw new RuntimeException(
			// "parameter "
			// + LEN_FLD_DTA_TYP
			// + " is wrong! Unsupport Data Type["
			// + tmp
			// + "]! data type must be num/int/net-int/short/net-short/byte");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"LengthCheckReader.parseParameters.dataType.wrong", new String[] { LEN_FLD_DTA_TYP, tmp }));
		}

		// length-field-data-encoding
		tmp = (String) parameters.get(LEN_FLD_DTA_ENC);
		if (tmp != null) {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " + LEN_FLD_DTA_TYP
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("parameter.blank",
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
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { LEN_FLD_LEN }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " + LEN_FLD_LEN
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("parameter.blank",
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LengthCheckReader.parseParameters.lengthEqual4",
						new String[] { LEN_FLD_DTA_TYP, LEN_FLD_LEN }));
			}
			break;
		case Constant.DTA_TYP_BYTE:
			if (lengthFieldLength != 1) {
				// throw new RuntimeException(LEN_FLD_DTA_TYP + " is byte, "
				// + LEN_FLD_LEN + " must be 1!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LengthCheckReader.parseParameters.lengthEqual1",
						new String[] { LEN_FLD_DTA_TYP, LEN_FLD_LEN }));
			}
			break;
		case Constant.DTA_TYP_SHORT:
		case Constant.DTA_TYP_NET_SHORT:
			if (lengthFieldLength != 2) {
				// throw new RuntimeException(LEN_FLD_DTA_TYP + " is short, "
				// + LEN_FLD_LEN + " must be 2!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LengthCheckReader.parseParameters.lengthEqual2",
						new String[] { LEN_FLD_DTA_TYP, LEN_FLD_LEN }));
			}
			break;
		case Constant.DTA_TYP_NUM:
		default:
			break;
		}

		// length-offset
		tmp = (String) parameters.get(LEN_OFFSET);
		if (null != tmp) {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " + LEN_OFFSET
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
						new String[] { LEN_OFFSET }));
			}
			messageLengthOffset = Integer.parseInt(tmp);
		}
	}
}