package com.fib.upp.message.parser;

import com.fib.upp.message.metadata.Message;

public class MessageParserFactory {
	private MessageParserFactory() {
	}

	public static AbstractMessageParser getMessageParser(Message message) {
		AbstractMessageParser parser = null;
		switch (message.getType()) {
		case 1001:
			parser = new XmlMessageParser();
			break;
		case 1002:
			parser = new TagMessageParser();
			break;
		case 1003:
			parser = new SwiftMessageParser();
			break;
		default:
			parser = new DefaultMessageParser();
			break;
		}
		return parser;
	}
}