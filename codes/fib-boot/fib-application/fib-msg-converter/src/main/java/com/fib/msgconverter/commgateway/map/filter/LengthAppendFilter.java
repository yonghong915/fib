package com.fib.msgconverter.commgateway.map.filter;

import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;

public class LengthAppendFilter implements AbstractMessageFilter {

	public byte[] doFilter(byte[] message) {
		byte[] map = new byte[8 + message.length];
		System.arraycopy(intToAsc(message.length), 0, map, 0, 8);
		System.arraycopy(message, 0, map, 8, message.length);
		return map;
	}

	private static byte[] intToAsc(int intValue) {
		/*
		 * StringBuffer buffer = new StringBuffer(8); int len = buffer.length();
		 * int leaveLen = 8 - String.valueOf(intValue).length(); if (len <
		 * leaveLen) { while (true) { buffer.append('0'); len = buffer.length();
		 * if (len == leaveLen) { break; } } } buffer.append(intValue); return
		 * buffer.toString().getBytes();
		 */

		String strInt = String.valueOf(intValue);
		int leaveLen = 8 - strInt.length();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < leaveLen; i++) {
			buf.append('0');
		}
		buf.append(strInt);
		return buf.toString().getBytes();
	}

}