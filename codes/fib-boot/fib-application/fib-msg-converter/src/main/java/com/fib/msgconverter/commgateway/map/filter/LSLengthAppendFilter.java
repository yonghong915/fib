package com.fib.msgconverter.commgateway.map.filter;

import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;

public class LSLengthAppendFilter implements AbstractMessageFilter {
	private static int t = 6;
	public byte[] doFilter(byte[] message) {
		byte[] map = new byte[t + message.length];
		System.arraycopy(intToAsc(message.length), 0, map, 0, t);
		System.arraycopy(message, 0, map, t, message.length);
		return map;
	}

	private static byte[] intToAsc(int intValue) {
		/*
		 * StringBuffer buffer = new StringBuffer(t); int len = buffer.length();
		 * int leaveLen = t - String.valueOf(intValue).length(); if (len <
		 * leaveLen) { while (true) { buffer.append('0'); len = buffer.length();
		 * if (len == leaveLen) { break; } } } buffer.append(intValue); return
		 * buffer.toString().getBytes();
		 */

		String strInt = String.valueOf(intValue);
		int leaveLen = t - strInt.length();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < leaveLen; i++) {
			buf.append('0');
		}
		buf.append(strInt);
		return buf.toString().getBytes();
	}

}