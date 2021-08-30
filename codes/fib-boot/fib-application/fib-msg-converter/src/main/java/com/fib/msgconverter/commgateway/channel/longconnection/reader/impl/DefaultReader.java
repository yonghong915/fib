package com.fib.msgconverter.commgateway.channel.longconnection.reader.impl;

import java.io.IOException;
import java.io.InputStream;

import com.fib.msgconverter.commgateway.channel.longconnection.reader.AbstractReader;
import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 默认的消息读取器。只读一次，读多少算多少
 * 
 * @author 刘恭亮
 * 
 */
public class DefaultReader extends AbstractReader {

	public boolean checkMessageComplete(byte[] message) {
		return true;
	}

	public byte[] read(InputStream in) {
		byte[] readBuf = null; // 读取缓冲
		byte[] messageBuf = null; // 报文缓冲
		int onceRead = 0; // 一次读取的长度

		// 1. 心跳包判定
		if (heartbeatConfig != null) {
			byte[] heartbeatMsg = heartbeatConfig.getMessageSymbol().determineMessage();
			// 读取报文一次
			readBuf = new byte[heartbeatMsg.length];
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

			if (isHeartbeatMessage(readBuf, heartbeatMsg)) {
				// 是心跳包则直接返回
				return readBuf;
			} else {
				// 非心跳包，已读部分加入报文缓冲
				messageBuf = appendByteBuf(readBuf, readBuf.length, messageBuf);
				if (logger.isInfoEnabled()) {
					// logger.info("read partial message : onceRead = " +
					// onceRead
					// + " hasRead=" + messageBuf.length
					// + " hasReadMessage:\n"
					// + CodeUtil.Bytes2FormattedText(messageBuf));
					logger.info(MultiLanguageResourceBundle.getInstance().getString("AbstractReader.read.partMessage",
							new String[] { "" + readBuf.length, "" + messageBuf.length,
									CodeUtil.Bytes2FormattedText(messageBuf) }));
				}
			}
		}

		readBuf = new byte[DEFAULT_BUFFER_SIZE];
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
			if (0 == onceRead) {
				// throw new RuntimeException(
				// "onceRead = 0, remote socket maybe be closed!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("onceRead.0"));
			}
			messageBuf = appendByteBuf(readBuf, onceRead, messageBuf);

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
			}
		}

		return messageBuf;
	}

}
