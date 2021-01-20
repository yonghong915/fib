package com.fib.gateway.map.filter;

import com.fib.gateway.channel.nio.filter.AbstractMessageFilter;

public class LSLengthTrimFilter implements AbstractMessageFilter{
	private static int t = 6;
	
	public byte[] doFilter(byte[] message) {
		byte[] map = new byte[message.length - t];
		System.arraycopy(message, t, map, 0, map.length);
		return map;
	}
}
