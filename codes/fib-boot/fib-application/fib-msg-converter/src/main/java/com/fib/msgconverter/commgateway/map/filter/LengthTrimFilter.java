package com.fib.msgconverter.commgateway.map.filter;

import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;

public class LengthTrimFilter implements AbstractMessageFilter{

	public byte[] doFilter(byte[] message) {
		byte[] map = new byte[message.length - 8];
		System.arraycopy(message, 8, map, 0, map.length);
		return map;
	}
}
