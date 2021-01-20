
package com.fib.gateway.channel.nio.reader.impl;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

import com.fib.gateway.channel.nio.reader.AbstractNioReader;
import com.fib.gateway.message.util.CodeUtil;
import com.fib.gateway.message.xml.message.Constant;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;


/**
 * 检测尾部的nio消息读取者 : 当读取到消息尾部标识符号时认为读取完毕
 * 
 * @author 刘恭亮
 * 
 */
public class TailCheckNioReader extends AbstractNioReader {
	public static final String TAIL_SYMBOL = "tail-symbol";
	public static final String TAIL_SYMBOL_DATA_TYPE = "tail-symbol-data-type";

	/**
	 * 消息尾部标识符号
	 */
	protected byte[] tail;

	public boolean checkMessageComplete() {
		return messageBuffer.lastIndexOf(tail) != -1;
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
		// tail-symbol-data-type
		int tailDataType = Constant.DTA_TYP_STR;
		String tmp = (String) parameters.get(TAIL_SYMBOL_DATA_TYPE);
		if (tmp != null) {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " +
				// TAIL_SYMBOL_DATA_TYPE
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { TAIL_SYMBOL_DATA_TYPE }));
			}
			if ("bin".equalsIgnoreCase(tmp)) {
				tailDataType = Constant.DTA_TYP_BIN;
			}
		}

		// tail-symbol
		tmp = (String) parameters.get(TAIL_SYMBOL);
		if (null == tmp) {
			// throw new RuntimeException("parameter " + TAIL_SYMBOL +
			// " is null!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("parameter.null"));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " + TAIL_SYMBOL
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.null",
								new String[] { TAIL_SYMBOL }));
			}
		}
		if (Constant.DTA_TYP_BIN == tailDataType) {
			tail = CodeUtil.HextoByte(tmp);
		} else {
			tail = tmp.getBytes();
		}

	}

	@Override
	public boolean read(SocketChannel channel, ByteBuffer commBuffer) {
		// TODO Auto-generated method stub
		return false;
	}

}
