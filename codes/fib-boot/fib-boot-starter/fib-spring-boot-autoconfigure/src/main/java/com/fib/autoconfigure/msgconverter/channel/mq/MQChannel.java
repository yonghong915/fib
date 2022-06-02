package com.fib.autoconfigure.msgconverter.channel.mq;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.msgconverter.channel.AbstractChannel;

import cn.hutool.core.io.FileUtil;

@Component("mqChannel")
public class MQChannel extends AbstractChannel {
	private static final Logger LOGGER = LoggerFactory.getLogger(MQChannel.class);

	private MQReceiveThread receiver;
	// 启动标志
	private boolean runFlag;

	@Autowired
	private IMQMessageService mqMessageService;

	@Override
	public void loadConfig(InputStream is) {
		//
	}

	@Override
	public void start() {
		runFlag = Boolean.TRUE.booleanValue();
		receiver = new MQReceiveThread();
		receiver.start();
	}

	@Override
	public void shutdown() {
		runFlag = Boolean.FALSE.booleanValue();
		if (null != receiver) {
			try {
				receiver.interrupt();
				receiver.join(5000);
			} catch (InterruptedException e) {
				LOGGER.warn("MQReceiveThread Interrupted", e);
				Thread.currentThread().interrupt();
			}
			receiver = null;
		}
	}

	@Override
	public void sendRequestMessage(byte[] requestMessage, boolean isASync, int timeout) {
		//
	}

	@Override
	public void sendResponseMessage(byte[] responseMessage) {
		//
	}

	private class MQReceiveThread extends Thread {
		@Override
		public void run() {
			while (runFlag) {
				try {
					TimeUnit.MILLISECONDS.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// FileUtil.readString("data/cnaps/ccms.801.001.02.txt",
				// StandardCharsets.UTF_8).getBytes();//
				byte[] receiveMessage = mqMessageService.receiveMessage();
				onRequestMessageArrived("mq.messageId", receiveMessage);
			}
		}
	}
}