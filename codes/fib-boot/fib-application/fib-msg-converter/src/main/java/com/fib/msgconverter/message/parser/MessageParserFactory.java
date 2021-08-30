// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.fib.msgconverter.message.parser;

import com.fib.msgconverter.message.metadata.Message;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public class MessageParserFactory {

	public MessageParserFactory() {
	}

	public static AbstractMessageParser A(Message message) {
		AbstractMessageParser obj = null;
		switch (message.getType()) {
		case 1001:
			obj = new XmlMessageParser();
			break;

		case 1002:
			obj = new TagMessageParser();
			break;

		case 1003:
			obj = new SwiftMessageParser();
			break;

		default:
			obj = new DefaultMessageParser();
			break;
		}
		return obj;
	}
}
