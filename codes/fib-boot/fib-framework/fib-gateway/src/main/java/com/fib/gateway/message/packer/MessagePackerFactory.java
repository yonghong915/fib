package com.fib.gateway.message.packer;

import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.util.EnumConstants;

/**
 * 消息组包器工厂
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-24
 */
public class MessagePackerFactory {
	private MessagePackerFactory() {
	}

	public static AbstractMessagePacker getMessagePacker(Message message) {
		AbstractMessagePacker msgPacker = null;
		int type = message.getType();
		if (EnumConstants.MsgType.XML.getCode() == type) {
			msgPacker = new XmlMessagePacker();
		} else if (EnumConstants.MsgType.TAG.getCode() == type) {
			msgPacker = new TagMessagePacker();
		} else if (EnumConstants.MsgType.SWIFT.getCode() == type) {
			msgPacker = new SwiftMessagePacker();
		} else {
			msgPacker = new DefaultMessagePacker();
		}
		return msgPacker;
	}
}
