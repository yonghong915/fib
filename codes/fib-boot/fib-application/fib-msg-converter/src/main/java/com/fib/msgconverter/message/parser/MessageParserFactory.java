package com.fib.msgconverter.message.parser;

import com.fib.msgconverter.message.metadata.Message;
import com.fib.msgconverter.util.ConstantMB;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public class MessageParserFactory {
	private MessageParserFactory() {
	}

	public static AbstractMessageParser getMessageParser(Message message) {
		AbstractMessageParser obj = null;
		ConstantMB.MessageType messageType = message.getType();
		switch (messageType) {
		case XML:
			obj = new XmlMessageParser();
			break;

		case TAG:
			obj = new TagMessageParser();
			break;

		case SWIFT:
			obj = new SwiftMessageParser();
			break;

		default:
			obj = new DefaultMessageParser();
			break;
		}
		return obj;
	}
}