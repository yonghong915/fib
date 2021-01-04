package com.fib.gateway.message;

import java.util.Objects;

import com.fib.gateway.message.packer.AbstractMessagePacker;
import com.fib.gateway.message.packer.MessagePackerFactory;

/**
 * 报文组包器
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-30
 */
public class MessagePacker extends AbstractMessagePacker {

	@Override
	public byte[] pack() {
		AbstractMessagePacker delegate = null;
		if (Objects.isNull(this.message)) {
			throw new IllegalArgumentException("");
		}
		if (Objects.isNull(this.messageBean)) {
			throw new IllegalArgumentException("");
		}
		delegate = MessagePackerFactory.getMessagePacker(this.message);
		delegate.setMessage(this.message);
		delegate.setMessageBean(this.messageBean);
		return delegate.pack();
	}
}
