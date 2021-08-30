// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.fib.msgconverter.message.packer;

import com.fib.msgconverter.message.metadata.Message;

// Referenced classes of package com.giantstone.message.packer:
//			B, A, C, E, 
//			AbstractMessagePacker

public class MessagePackerFactory {

	public MessagePackerFactory() {
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
