/**
 * 北京长信通信息技术有限公司
 * 2015年4月8日20:40:54
 */
package com.fib.msgconverter.commgateway.channel.nio.reader.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.msgconverter.commgateway.channel.nio.reader.AbstractNioReader;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 
 * 网关测试
 * @author 王海生
 * 
 */
public class LSGateWayTestReader extends AbstractNioReader {
	
	public boolean read(SocketChannel channel, ByteBuffer commBuffer) {
		commBuffer.clear();
		onceRead = 0;
		// 1. 读取部分报文数据
		try {
			onceRead = channel.read(commBuffer);
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
			return true;
		}
		// 2. 判断是否已经读完
		boolean complete = checkMessageComplete();
			
		if (complete) {
			message = messageBuffer.toBytes();
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

	@Override
	public boolean checkMessageComplete() {
		return true;
	}
    
}
