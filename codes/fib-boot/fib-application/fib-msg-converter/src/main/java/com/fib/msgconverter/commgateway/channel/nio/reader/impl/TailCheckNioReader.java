/**
 * 北京长信通信息技术有限公司
 * 2008-8-26 下午02:20:09
 */
package com.fib.msgconverter.commgateway.channel.nio.reader.impl;

import java.util.Map;

import com.fib.msgconverter.commgateway.channel.nio.reader.AbstractNioReader;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.message.metadata.Constant;

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

}
