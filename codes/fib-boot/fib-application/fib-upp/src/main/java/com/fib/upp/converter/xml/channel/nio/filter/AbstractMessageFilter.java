package com.fib.upp.converter.xml.channel.nio.filter;

public interface AbstractMessageFilter {
	public byte[] doFilter(byte[] message);
}
