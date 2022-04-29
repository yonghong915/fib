// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.fib.upp.message.packer;

import com.fib.upp.message.metadata.Message;

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

	public static AbstractMessagePacker A(Message message) {
		AbstractMessagePacker obj = null;
		switch (message.getType()) {
		case 1001:
			obj = new XmlMessagePacker();
			break;

		case 1002:
			obj = new TagMessagePacker();
			break;

		case 1003:
			obj = new SwiftMessagePacker();
			break;

		default:
			obj = new DefaultMessagePacker();
			break;
		}
		return obj;
	}
}
