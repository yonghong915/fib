/**
 * 北京长信通信息技术有限公司
 * 2008-8-25 下午08:40:07
 */
package com.fib.msgconverter.commgateway.channel.nio.reader.impl;

import com.fib.msgconverter.commgateway.channel.nio.reader.AbstractNioReader;

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

}
