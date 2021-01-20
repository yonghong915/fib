package com.fib.gateway.message.xml.channel.mq;

import java.io.InputStream;
import java.util.LinkedList;

import com.fib.gateway.message.xml.channel.Channel;
import com.fib.gateway.message.xml.event.DefaultEventHandler;
import com.fib.gateway.message.xml.event.Event;
import com.fib.gateway.message.xml.event.EventHandler;
import com.fib.gateway.message.xml.event.EventQueue;

public class MQChannel extends Channel {
	private MQChannelConfig config;
	private LinkedList sendList = new LinkedList();

	public static void main(String[] args) {
		MQChannel test = new MQChannel();
		test.run();
	}

	public void run() {
		String msg = "<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n" + "<MSG>\r\n" + "	<HEAD>\r\n"
				+ "		<MSGNO>1006</MSGNO>\r\n" + "	</HEAD>\r\n" + "	<CFX>\r\n"
				+ "		<ZH>acct12345678</ZH>\r\n" + "		<ACSDATE>20201226</ACSDATE>\r\n" + "	</CFX>\r\n"
				+ "</MSG>";
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

	@Override
	public void loadConfig(InputStream in) {
		MQChannelConfigParser parser = new MQChannelConfigParser();
		parser.setChannelConfig(channelConfig);
		parser.setMainConfig(mainConfig);
		config = parser.parse(in);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendRequestMessage(byte[] requestMessage, boolean async, int timeout) {
		Message request = new Message();
		request.message = requestMessage;
		request.isAsync = async;
		if (-1 == config.getTimeout()) {
			request.timeout = timeout;
		} else {
			request.timeout = config.getTimeout();
		}
		synchronized (sendList) {
			sendList.add(request);
			sendList.notify();
		}
		byte[] receiveMessage = "abcdedd".getBytes();
		onResponseMessageArrived("aabdd", request.message, receiveMessage);

	}

	@Override
	public void sendResponseMessage(byte[] responseMessage) {

	}

	@Override
	public void closeSource(Object source) {
		// TODO Auto-generated method stub

	}

	private class Message {
		public static final int TYPE_REQ = 0x90;
		public static final int TYPE_RES = 0x91;
		public byte[] message;
		public byte[] messageId;
		public long start;
		public long timeout;
		public int type = 0x90;
		public boolean isAsync = false;
	}

	public MQChannelConfig getConnectorConfig() {
		return config;
	}

	public void setConnectorConfig(MQChannelConfig config) {
		this.config = config;
	}
}
