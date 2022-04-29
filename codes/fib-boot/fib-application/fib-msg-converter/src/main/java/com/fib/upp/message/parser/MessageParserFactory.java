package com.fib.upp.message.parser;

import com.fib.upp.message.metadata.ConstantMB;
import com.giantstone.message.metadata.Message;

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
		ConstantMB.MessageType messageType = ConstantMB.MessageType.getMessageTypeByCode(message.getType());
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