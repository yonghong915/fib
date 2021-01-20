package com.fib.gateway.channel.nio.reader.impl;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.fib.gateway.channel.nio.reader.AbstractNioReader;

/**
 * 默认的消息读取者(nio) : 仅读取一次，读多少算多少
 * @author 刘恭亮
 *
 */
public class DefaultNioReader extends AbstractNioReader {

	/* (non-Javadoc)
	 * @see com.giantstone.commgateway.channel.nio.reader.AbstractNioReader#checkMessageComplete()
	 */
	public boolean checkMessageComplete() {
		return onceRead > 0;
	}

	@Override
	public boolean read(SocketChannel channel, ByteBuffer commBuffer) {
		// TODO Auto-generated method stub
		return false;
	}

}
