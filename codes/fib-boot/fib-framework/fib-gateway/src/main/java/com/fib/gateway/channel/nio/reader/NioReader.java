
package com.fib.gateway.channel.nio.reader;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 消息读取者(nio)
 * @author 刘恭亮
 *
 */
public interface NioReader{
	
	/**
	 * 取得读到的消息数据
	 * @return
	 */
	public byte[] getMessage();
	
	/**
	 * 从SocketChannel读取一次消息数据
	 * @param channel
	 * @param commBuffer
	 * @return 消息是否已读取完毕
	 */
	public boolean read(SocketChannel channel, ByteBuffer commBuffer);

	
}
