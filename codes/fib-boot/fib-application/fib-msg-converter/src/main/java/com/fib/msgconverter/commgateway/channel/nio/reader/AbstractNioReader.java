package com.fib.msgconverter.commgateway.channel.nio.reader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;

/**
 * NioReader抽象实现类
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-30
 */
public abstract class AbstractNioReader implements NioReader, Cloneable {
	public static final int INIT_BUFFER_SIZE = 8192;
	public static final String FILTER = "message-filter";

	/**
	 * 报文缓存
	 */
	protected com.giantstone.common.util.ByteBuffer messageBuffer;

	/**
	 * 报文
	 */
	protected byte[] message;

	/**
	 * 一次读取的数据长度
	 */
	protected int onceRead = 0;

	/**
	 * 参数
	 */
	protected Map parameters = null;

	/**
	 * 调试器
	 */
	protected Logger logger = null;

	/**
	 * 过滤器链
	 */
	protected List<AbstractMessageFilter> filterList = new ArrayList<>();

	public AbstractNioReader() {
		messageBuffer = new com.giantstone.common.util.ByteBuffer(INIT_BUFFER_SIZE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.giantstone.commgateway.channel.nio.NioReader#getMessage()
	 */
	public byte[] getMessage() {
		return message;
	}

	public byte[] getOriginalMessage() {
		return messageBuffer.toBytes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.giantstone.commgateway.channel.nio.NioReader#read(java.nio.channels
	 * .SocketChannel, java.nio.ByteBuffer)
	 */
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
		} else if (onceRead == -1) {
			// throw new RuntimeException("onceRead=" + onceRead
			// + " ,remote client closed.");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("onceRead.-1"));
		}
		// else if (onceRead == 0) {
		// throw new RuntimeException(MultiLanguageResourceBundle
		// .getInstance().getString("onceRead.0"));
		// }
		// 2. 判断是否已经读完
		boolean complete = checkMessageComplete();

		if (complete) {
			message = messageBuffer.toBytes();

			System.out.println("aaaaaaa:" + CodeUtil.Bytes2FormattedText(message));

			for (int i = 0; i < filterList.size(); i++) {
				AbstractMessageFilter filter = (AbstractMessageFilter) filterList.get(i);
				String filterClassName = filter.getClass().getName().trim();

				if (logger.isDebugEnabled()) {
					// logger.debug("message before filter[" + filterClassName
					// + "]:\n" + CodeUtil.Bytes2FormattedText(message));
					logger.debug(MultiLanguageResourceBundle.getInstance().getString("message.beforeFilter",
							new String[] { filterClassName, CodeUtil.Bytes2FormattedText(message) }));
				}

				message = filter.doFilter(message);

				if (logger.isDebugEnabled()) {
					// logger.debug("message after filter[" + filterClassName
					// + "]:\n" + CodeUtil.Bytes2FormattedText(message));
					logger.debug(MultiLanguageResourceBundle.getInstance().getString("message.afterFilter",
							new String[] { filterClassName, CodeUtil.Bytes2FormattedText(message) }));
				}
			}
			return true;
		} else {
			byte[] tmp = new byte[onceRead];
			System.out.println("bbbbbb:" + CodeUtil.Bytes2FormattedText(tmp));
			if (logger.isInfoEnabled()) {
				// logger
				// .info("read partial message : onceRead = "
				// + onceRead
				// + " hasRead="
				// + messageBuffer.size()
				// + " hasReadMessage:\n"
				// + CodeUtil.Bytes2FormattedText(messageBuffer
				// .toBytes()));
				messageBuffer.getBytesAt(messageBuffer.size() - onceRead, tmp);
				logger.info(MultiLanguageResourceBundle.getInstance().getString(
						"AbstractNioReader.read.partialMessage.read",
						new String[] { "" + onceRead, "" + messageBuffer.size(), CodeUtil.Bytes2FormattedText(tmp) }));
			}
			return false;
		}
	}

	/**
	 * 检查消息是否已读取完毕
	 * 
	 * @return
	 */
	public abstract boolean checkMessageComplete();

	/**
	 * @return the parameters
	 */
	public Map getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	public List<AbstractMessageFilter> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<AbstractMessageFilter> filterList) {
		this.filterList = filterList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		AbstractNioReader o = null;
		try {
			o = (AbstractNioReader) super.clone();
		} catch (CloneNotSupportedException e) {
			// e.printStackTrace();
		}
		o.messageBuffer = new com.giantstone.common.util.ByteBuffer(INIT_BUFFER_SIZE);
		o.onceRead = 0;
		o.message = null;
		return o;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}