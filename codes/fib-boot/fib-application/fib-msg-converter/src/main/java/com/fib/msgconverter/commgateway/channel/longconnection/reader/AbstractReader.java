package com.fib.msgconverter.commgateway.channel.longconnection.reader;

import java.io.InputStream;
import java.util.Arrays;

import com.fib.msgconverter.commgateway.channel.longconnection.config.HeartbeatConfig;

/**
 * 长连接通道抽象Reader。实现了长连接独有的心跳包识别逻辑
 * 
 * @author 刘恭亮
 * 
 */
public abstract class AbstractReader extends com.fib.msgconverter.commgateway.channel.net.reader.AbstractReader {
	/**
	 * 心跳包配置
	 */
	protected HeartbeatConfig heartbeatConfig = null;

	public abstract byte[] read(InputStream in);

	protected boolean isPartOfHeartbeatMessage(byte[] readBuf, byte[] heartbeatMsg, int readLength) {
		for (int i = 0; i < readLength; i++) {
			if (readBuf[i] != heartbeatMsg[i]) {
				return false;
			}
		}
		return true;
	}

	public boolean isHeartbeatMessage(byte[] message, byte[] heartbeatMsg) {
		return Arrays.equals(message, heartbeatMsg);
	}

	public boolean isHeartbeatMessage(byte[] message) {
		if (null == heartbeatConfig) {
			return false;
		}

		return isHeartbeatMessage(message, heartbeatConfig.getMessageSymbol().determineMessage());
	}

	public HeartbeatConfig getHeartbeatConfig() {
		return heartbeatConfig;
	}

	public void setHeartbeatConfig(HeartbeatConfig heartbeatConfig) {
		this.heartbeatConfig = heartbeatConfig;
	}
}