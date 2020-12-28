package com.fib.gateway.message;

import java.util.Objects;

import com.fib.gateway.message.packer.AbstractMessagePacker;
import com.fib.gateway.message.packer.MessagePackerFactory;


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
