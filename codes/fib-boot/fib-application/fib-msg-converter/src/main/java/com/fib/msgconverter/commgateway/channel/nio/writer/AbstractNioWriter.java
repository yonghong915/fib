package com.fib.msgconverter.commgateway.channel.nio.writer;

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
 * NioWriter抽象实现类
 * 
 * @author 刘恭亮
 */
public abstract class AbstractNioWriter implements NioWriter, Cloneable {
	public static final String FILTER = "message-filter";
	/**
	 * 原始的消息数据
	 */
	protected byte[] originalMessage;

	/**
	 * 发送的消息数据
	 */
	protected byte[] message;

	/**
	 * 已发送数据的长度
	 */
	protected int hasWrite = 0;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.giantstone.commgateway.channel.nio.writer.NioWriter#setMessage(byte
	 * [])
	 */
	public void setMessage(byte[] message) {
		if (null == message) {
			// throw new IllegalArgumentException("message is null");
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "message" }));
		}

		originalMessage = message;
		hasWrite = 0;
		this.message = message;

		for (int i = 0; i < filterList.size(); i++) {
			AbstractMessageFilter filter = (AbstractMessageFilter) filterList.get(i);
			String filterClassName = filter.getClass().getName().trim();

			if (logger.isDebugEnabled()) {
				// logger.debug("message before filter[" + filterClassName
				// + "]:\n" + CodeUtil.Bytes2FormattedText(this.message));
				logger.debug(MultiLanguageResourceBundle.getInstance().getString("message.beforeFilter",
						new String[] { filterClassName, CodeUtil.Bytes2FormattedText(this.message) }));
			}

			this.message = filter.doFilter(this.message.clone());

			if (logger.isDebugEnabled()) {
				// logger.debug("message after filter[" + filterClassName +
				// "]:\n"
				// + CodeUtil.Bytes2FormattedText(this.message));
				logger.debug(MultiLanguageResourceBundle.getInstance().getString("message.afterFilter",
						new String[] { filterClassName, CodeUtil.Bytes2FormattedText(this.message) }));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.giantstone.commgateway.channel.nio.writer.NioWriter#write(java.nio
	 * .channels.SocketChannel, java.nio.ByteBuffer)
	 */
	public boolean write(SocketChannel channel, ByteBuffer commBuffer) {
		commBuffer.clear();
		commBuffer.put(message, hasWrite, (message.length - hasWrite) > commBuffer.capacity() ? commBuffer.capacity()
				: (message.length - hasWrite));
		int onceWrite = 0;
		try {
			commBuffer.flip();
			onceWrite = channel.write(commBuffer);
		} catch (IOException e) {
			ExceptionUtil.throwActualException(e);
		}
		hasWrite += onceWrite;
		// 检查消息是否已发送完毕
		boolean complete = checkMessageComplete();
		if (!complete) {
			if (logger.isInfoEnabled()) {
				// logger.info("onceWrite=" + onceWrite + " hasWrite=" +
				// hasWrite);
				logger.info(MultiLanguageResourceBundle.getInstance().getString("AbstractNioWriter.write.hasWrite",
						new String[] { "" + onceWrite, "" + hasWrite }));
			}
		}
		return complete;
	}

	/**
	 * 检查消息是否已发送完毕
	 * 
	 * @return
	 */
	public abstract boolean checkMessageComplete();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		AbstractNioWriter o = null;
		try {
			o = (AbstractNioWriter) super.clone();
		} catch (CloneNotSupportedException e) {
			// e.printStackTrace();
		}
		o.originalMessage = null;
		o.message = null;
		o.hasWrite = 0;
		return o;
	}

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

	/**
	 * @return the message
	 */
	public byte[] getMessage() {
		return message;
	}

	public byte[] getOriginalMessage() {
		return originalMessage;
	}

	/**
	 * @return the hasWrite
	 */
	public int getHasWrite() {
		return hasWrite;
	}

	public List<AbstractMessageFilter> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<AbstractMessageFilter> filterList) {
		this.filterList = filterList;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}