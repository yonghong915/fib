package com.fib.upp.service.gateway.channel.reader;

import java.io.InputStream;

public interface Reader {
	/**
	 * 读取报文
	 * @param in
	 * @return
	 */
	public byte[] read(InputStream in);
}
