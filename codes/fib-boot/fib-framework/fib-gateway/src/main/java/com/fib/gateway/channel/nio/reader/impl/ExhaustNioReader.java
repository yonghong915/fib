
package com.fib.gateway.channel.nio.reader.impl;



import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.fib.gateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.gateway.channel.nio.reader.AbstractNioReader;
import com.fib.gateway.message.util.CodeUtil;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

/**
 * 穷尽读的消息读取者(nio) : 直到读到返回-1，即the channel has reached end-of-stream
 * 
 * @author 刘恭亮
 * 
 */
public class ExhaustNioReader extends AbstractNioReader {

	public boolean checkMessageComplete() {
		return onceRead == -1;
	}

	public boolean read(SocketChannel channel, ByteBuffer commBuffer) {
		commBuffer.clear();
		onceRead = 0;
		// 1. 读取部分报文数据
		try {
			onceRead = channel.read(commBuffer);
			System.out.println("一次读取的报文长度：onceRead: " + onceRead);
		} catch (IOException e) {
			ExceptionUtil.throwActualException(e);
		}
		if (onceRead > 0) {
			// 从通讯缓冲中拷贝出读到的数据
			byte[] bytes = new byte[onceRead];
			commBuffer.flip();
			commBuffer.get(bytes);
			// 加到报文缓存中
			messageBuffer.append(bytes);
		} else if (onceRead == -1 && 0 == messageBuffer.size()) {
			// throw new RuntimeException("onceRead=" + onceRead
			// + " ,remote client closed.");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("onceRead.-1"));
		}
		// else if (onceRead == 0) {
		// throw new RuntimeException(MultiLanguageResourceBundle
		// .getInstance().getString("onceRead.0"));
		// }
		// 2. 判断是否已经读完
		boolean complete = checkMessageComplete();
		System.out.println("判断是否已经读完标志: complete: " + complete);
		if (complete) {
			message = messageBuffer.toBytes();
			System.out.println("判断是否已经读完标志: complete: " + complete);
			for (int i = 0; i < filterList.size(); i++) {
				AbstractMessageFilter filter = (AbstractMessageFilter) filterList
						.get(i);
				String filterClassName = filter.getClass().getName().trim();

				if (logger.isDebugEnabled()) {
					// logger.debug("message before filter[" + filterClassName
					// + "]:\n" + CodeUtil.Bytes2FormattedText(message));
					logger
							.debug(MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"message.beforeFilter",
											new String[] {
													filterClassName,
													CodeUtil
															.Bytes2FormattedText(message) }));
				}

				message = filter.doFilter(message);

				if (logger.isDebugEnabled()) {
					// logger.debug("message after filter[" + filterClassName
					// + "]:\n" + CodeUtil.Bytes2FormattedText(message));
					logger
							.debug(MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"message.afterFilter",
											new String[] {
													filterClassName,
													CodeUtil
															.Bytes2FormattedText(message) }));
				}
			}
			return true;
		} else {
			if (logger.isInfoEnabled()) {
				// logger
				// .info("read partial message : onceRead = "
				// + onceRead
				// + " hasRead="
				// + messageBuffer.size()
				// + " hasReadMessage:\n"
				// + CodeUtil.Bytes2FormattedText(messageBuffer
				// .toBytes()));
				byte[] tmp = new byte[onceRead];
				messageBuffer.getBytesAt(messageBuffer.size() - onceRead, tmp);
				logger.info(MultiLanguageResourceBundle.getInstance()
						.getString(
								"AbstractNioReader.read.partialMessage.read",
								new String[] { "" + onceRead,
										"" + messageBuffer.size(),
										CodeUtil.Bytes2FormattedText(tmp) }));
			}
			return false;
		}
	}

}
