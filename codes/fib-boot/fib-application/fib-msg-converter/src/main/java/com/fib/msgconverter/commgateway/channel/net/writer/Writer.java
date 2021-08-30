/**
 * 北京长信通信息技术有限公司
 * 2008-9-25 下午05:22:31
 */
package com.fib.msgconverter.commgateway.channel.net.writer;

import java.io.OutputStream;

/**
 * 消息发送器（普通Socket）
 * @author 刘恭亮
 *
 */
public interface Writer {

	/**
	 * 发送报文
	 * @param out
	 * @param message
	 */
	public void write(OutputStream out, byte[] message);
}
