package com.fib.gateway.channel.nio.filter;

public interface AbstractMessageFilter {
	public byte[] doFilter(byte[] message);
}
