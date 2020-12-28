package com.fib.gateway.message.xml.channel.mq;

import com.fib.gateway.message.xml.channel.Channel;
import com.fib.gateway.message.xml.event.DefaultEventHandler;
import com.fib.gateway.message.xml.event.Event;
import com.fib.gateway.message.xml.event.EventHandler;
import com.fib.gateway.message.xml.event.EventQueue;

public class MQChannel extends Channel {
	public static void main(String[] args) {
		MQChannel test = new MQChannel();
		test.run();
	}

	public void run() {
		String msg = "<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n" + 
				"<MSG>\r\n" + 
				"	<HEAD>\r\n" + 
				"		<MSGNO>1006</MSGNO>\r\n" + 
				"	</HEAD>\r\n" + 
				"	<CFX>\r\n" + 
				"		<ZH>acct12345678</ZH>\r\n" + 
				"		<ACSDATE>20201226</ACSDATE>\r\n" + 
				"	</CFX>\r\n" + 
				"</MSG>";
		byte[] receiveMessage = msg.getBytes();
		byte[] messageId = "abcdeg".getBytes();
		EventQueue eventQueue = new EventQueue();
		setEventQueue(eventQueue);
		onRequestMessageArrived(messageId, receiveMessage);

		System.out.println(eventQueue.getQueue());

		Event event = eventQueue.selectEvent();
		System.out.println(event);
		EventHandler evtHandler = new DefaultEventHandler();
		evtHandler.handleRequestArrived(event);
	}
}
