package com.fib.msgconverter.commgateway.channel.longconnection.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import com.fib.msgconverter.commgateway.channel.longconnection.config.HeartbeatConfig;
import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 长连接通道抽象Writer。实现了长连接独有的心跳包识别逻辑
 * 
 * @author 刘恭亮
 * 
 */
public class AbstractWriter extends com.fib.msgconverter.commgateway.channel.net.writer.AbstractWriter {
	/**
	 * 心跳包应答配置
	 */
	protected HeartbeatConfig responseHeartbeatConfig = null;
	/**
	 * 心跳包配置
	 */
	protected HeartbeatConfig heartbeatConfig = null;

	public void write(OutputStream out, byte[] message) {
		// 1.既不是心跳包也不心跳应答包才进行过滤
		if (!isHeartbeatMessage(message) && !isResponseHeartbeatConfig(message)) {
			for (int i = 0; i < filterList.size(); i++) {
				AbstractMessageFilter filter = (AbstractMessageFilter) filterList.get(i);
				String filterClassName = filter.getClass().getName();

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
		}
		try {
			out.write(message);
		} catch (IOException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		}
	}

	public boolean isHeartbeatMessage(byte[] message) {
		if (null == heartbeatConfig) {
			return false;
		}
		return Arrays.equals(message, heartbeatConfig.getMessageSymbol().determineMessage());
	}

	public boolean isResponseHeartbeatConfig(byte[] message) {
		if (null == responseHeartbeatConfig || responseHeartbeatConfig.getResponseMessageSymbol() == null) {
			return false;
		}
		return Arrays.equals(message, responseHeartbeatConfig.getResponseMessageSymbol().determineMessage());
	}

	public HeartbeatConfig getResponseHeartbeatConfig() {
		return responseHeartbeatConfig;
	}

	public void setResponseHeartbeatConfig(HeartbeatConfig responseHeartbeatConfig) {
		this.responseHeartbeatConfig = responseHeartbeatConfig;
	}

	public HeartbeatConfig getHeartbeatConfig() {
		return heartbeatConfig;
	}

	public void setHeartbeatConfig(HeartbeatConfig heartbeatConfig) {
		this.heartbeatConfig = heartbeatConfig;
	}
}