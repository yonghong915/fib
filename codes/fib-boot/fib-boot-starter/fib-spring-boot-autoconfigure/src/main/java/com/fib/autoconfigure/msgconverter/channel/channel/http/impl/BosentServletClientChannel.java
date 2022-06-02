package com.fib.autoconfigure.msgconverter.channel.channel.http.impl;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.autoconfigure.msgconverter.channel.channel.http.HTTPClientChannel;

public class BosentServletClientChannel extends HTTPClientChannel {
	private static final Logger LOGGER = LoggerFactory.getLogger(BosentServletClientChannel.class);

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
		// TODO Auto-generated method stub

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
