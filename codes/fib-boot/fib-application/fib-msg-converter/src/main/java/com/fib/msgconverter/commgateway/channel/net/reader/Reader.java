/**
 * 北京长信通信息技术有限公司
 * 2008-9-25 下午05:00:51
 */
package com.fib.msgconverter.commgateway.channel.net.reader;

import java.io.InputStream;

/**
 * 消息读取者(普通socket)
 * @author 刘恭亮
 *
 */
public interface Reader {
	
	/**
	 * 读取报文
	 * @param in
	 * @return
	 */
	public byte[] read(InputStream in);

}
