
package com.fib.gateway.channel.nio.writer.impl;

import com.fib.gateway.channel.nio.writer.AbstractNioWriter;

/**
 * 默认的消息发送者(nio) : 按消息长度发送，直到全部发送完毕
 * @author 刘恭亮
 *
 */
public class DefaultNioWriter extends AbstractNioWriter {

	public boolean checkMessageComplete() {
		//当已发送长度等于消息长度时为真
		if(this.message.length == this.hasWrite){
			return true;
		}
		return false;
	}

}
