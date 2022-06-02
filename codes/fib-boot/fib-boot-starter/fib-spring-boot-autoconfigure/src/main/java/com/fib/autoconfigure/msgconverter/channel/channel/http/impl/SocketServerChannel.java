package com.fib.autoconfigure.msgconverter.channel.channel.http.impl;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.autoconfigure.msgconverter.channel.AbstractChannel;

public class SocketServerChannel extends AbstractChannel {
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketServerChannel.class);

	@Override
	public void loadConfig(InputStream is) {
		LOGGER.info("loadConfig");
	}

	@Override
	public void start() {
		LOGGER.info("start");

	}

	@Override
	public void shutdown() {
		//

	}

	@Override
	public void sendRequestMessage(byte[] requestMessage, boolean isASync, int timeout) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendResponseMessage(byte[] responseMessage) {
		// TODO Auto-generated method stub

	}

}
