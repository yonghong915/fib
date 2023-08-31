package com.fib.msgconverter.message.packer;

import com.fib.msgconverter.message.metadata.Message;
import com.fib.msgconverter.util.ConstantMB;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-28 16:36:38
 */
public class MessagePackerFactory {

	private MessagePackerFactory() {
		// noting to do
	}

	public static AbstractMessagePacker getMessagePacker(Message message) {
		AbstractMessagePacker obj = null;
		ConstantMB.MessageType messageType = message.getType();
		switch (messageType) {
		case XML:
			obj = new XmlMessagePacker();
			break;

		case TAG:
			obj = new TagMessagePacker();
			break;

		case SWIFT:
			obj = new SwiftMessagePacker();
			break;

		default:
			obj = new DefaultMessagePacker();
			break;
		}
		return obj;
	}
}
