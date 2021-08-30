package com.fib.msgconverter.commgateway.channel.nio.filter;

public interface AbstractMessageFilter {
	public byte[] doFilter(byte[] message);
}
