package com.fib.msgconverter.message;

import com.fib.msgconverter.message.packer.AbstractMessagePacker;
import com.fib.msgconverter.message.packer.MessagePackerFactory;
import com.giantstone.common.multilang.MultiLanguageResourceBundle;

/**
 * 报文组报器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public class MessagePacker extends AbstractMessagePacker {

	private AbstractMessagePacker delegate;

	public MessagePacker() {
		delegate = null;
	}

	public byte[] pack() {
		if (null == message)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "message" }));
		if (null == messageBean) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "messageBean" }));
		} else {
			delegate = MessagePackerFactory.getMessagePacker(message);
			delegate.setMessage(message);
			delegate.setMessageBean(messageBean);
			delegate.setDefaultCharset(getDefaultCharset());
			return delegate.pack();
		}
	}
}