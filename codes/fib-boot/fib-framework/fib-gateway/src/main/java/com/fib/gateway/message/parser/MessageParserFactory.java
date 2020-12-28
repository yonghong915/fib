package com.fib.gateway.message.parser;

import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.util.EnumConstants;

/**
 * 消息解包器工厂
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-24
 */
public class MessageParserFactory {
	private MessageParserFactory() {
	}

	public static AbstractMessageParser getMessageParser(Message message) {
		AbstractMessageParser msgParser = null;
		int type = message.getType();
		if (EnumConstants.MsgType.XML.getCode() == type) {
			msgParser = new XmlMessageParser();
		} else if (EnumConstants.MsgType.TAG.getCode() == type) {
			msgParser = new TagMessageParser();
		} else if (EnumConstants.MsgType.SWIFT.getCode() == type) {
			msgParser = new SwiftMessageParser();
		} else {
			msgParser = new DefaultMessageParser();
		}
		return msgParser;
	}
}
