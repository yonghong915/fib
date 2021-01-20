package com.fib.gateway.channel.nio.reader.impl;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

import com.fib.gateway.channel.nio.reader.AbstractNioReader;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

/**
 * 固定长度读取器,仅能读取固定长度的数据,当读取的数据超过该长度时会抛出异常
 * 
 * @author 白帆
 * 
 */
public class FixedLengthNioReader extends AbstractNioReader {
	public static final String FIX_LENGTH = "fixed-length";

	private int length;

	public boolean checkMessageComplete() {
		if (length == messageBuffer.size()) {
			return true;
		} else if (length > messageBuffer.size()) {
			return false;
		} else {
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"FixedLengthNioReader.read.more",
							new String[] { length + "",
									messageBuffer.size() + "" }));
		}
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);

		parseParameters();
	}

	private void parseParameters() {
		String tmp = (String) parameters.get(FIX_LENGTH);
		if (null == tmp) {
			// throw new RuntimeException("parameter " + LEN_FLD_INX +
			// " is null!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("parameter.null",
							new String[] { FIX_LENGTH }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter " + LEN_FLD_INX
				// + " is blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { FIX_LENGTH }));
			}
		}

		length = Integer.parseInt(tmp);
	}

	@Override
	public boolean read(SocketChannel channel, ByteBuffer commBuffer) {
		// TODO Auto-generated method stub
		return false;
	}
}
