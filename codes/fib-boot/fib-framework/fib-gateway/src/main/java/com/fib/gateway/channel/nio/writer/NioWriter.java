
package com.fib.gateway.channel.nio.writer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 消息发送者(nio)
 * @author 刘恭亮
 *
 */
public interface NioWriter{
	
	/**
	 * 设置待发送的消息数据
	 * @param message
	 */
	public void setMessage(byte[] message);
	
	/**
	 * 发送一次消息数据
	 * @param channel
	 * @param commBuffer
	 * @return 消息是否已发送完毕
	 */
	public boolean write(SocketChannel channel, ByteBuffer commBuffer);

}
