package com.fib.upp.message.parser;

import com.fib.upp.message.bean.MessageBean;

public class MessageParser extends AbstractMessageParser {
	private AbstractMessageParser delegate;

	public MessageParser() {
	}

	public MessageBean parse() {
		if (null == message)
			throw new IllegalArgumentException("message null");
		if (null == messageData) {
			throw new IllegalArgumentException("messageData null");
		} else {
			delegate = MessageParserFactory.getMessageParser(message);
			delegate.setMessage(message);
			delegate.setMessageData(messageData);
			delegate.setMessageBean(messageBean);
			delegate.setDefaultCharset(getDefaultCharset());
			return delegate.parse();
		}
	}
}
