package com.fib.autoconfigure.msgconverter.channel;

import org.springframework.beans.factory.annotation.Autowired;

import com.fib.autoconfigure.msgconverter.channel.config.ChannelConfig;
import com.fib.autoconfigure.msgconverter.channel.config.ChannelMainConfig;
import com.fib.autoconfigure.msgconverter.channel.event.Event;
import com.fib.autoconfigure.msgconverter.channel.event.EventHandler;
import com.fib.autoconfigure.msgconverter.channel.event.EventQueue;
import com.fib.autoconfigure.msgconverter.channel.event.EventType;
import com.fib.autoconfigure.msgconverter.channel.message.recognizer.MessageRecognizer;

public abstract class AbstractChannel implements Channel {
	/** 通道类型 */
	protected String channelType;

	/** 通道主配置 */
	protected ChannelMainConfig mainConfig;

	/** 通道具体配置 */
	protected ChannelConfig channelConfig;

	/** 事件队列 */
	@Autowired
	protected EventQueue eventQueue;

	/** 事件处理器 */
	protected EventHandler eventHandler;

	/**
	 * 报文类型识别器
	 */
	protected MessageRecognizer messageTypeRecognizer;

	/**
	 * 应答报文返回码识别器
	 */
	protected MessageRecognizer returnCodeRecognizer;

	/**
	 * 重启通道
	 */
	public void restart() {
		shutdown();
		start();
	}

	/**
	 * 发送请求报文
	 * 
	 * @param requestMessage
	 */
	public abstract void sendRequestMessage(byte[] requestMessage, boolean isASync, int timeout);

	/**
	 * 发送应答报文
	 * 
	 * @param responseMessage
	 */
	public abstract void sendResponseMessage(byte[] responseMessage);

	/**
	 * 当请求消息到达。通道为服务器端
	 * 
	 * @param source
	 * @param requestMessage
	 */
	protected void onRequestMessageArrived(Object source, byte[] requestMessage) {
		// 产生请求到达事件
		Event e = new Event(EventType.REQUEST_ARRIVED, this, source, requestMessage, null);
		eventQueue.postEvent(e);
	}

	public String getId() {
		return mainConfig.getId();
	}

	public ChannelMainConfig getMainConfig() {
		return mainConfig;
	}

	public void setMainConfig(ChannelMainConfig mainConfig) {
		this.mainConfig = mainConfig;
	}

	public EventQueue getEventQueue() {
		return eventQueue;
	}

	public void setEventQueue(EventQueue eventQueue) {
		this.eventQueue = eventQueue;
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(EventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public MessageRecognizer getMessageTypeRecognizer() {
		return messageTypeRecognizer;
	}

	public void setMessageTypeRecognizer(MessageRecognizer messageTypeRecognizer) {
		this.messageTypeRecognizer = messageTypeRecognizer;
	}

	public ChannelConfig getChannelConfig() {
		return channelConfig;
	}

	public void setChannelConfig(ChannelConfig channelConfig) {
		this.channelConfig = channelConfig;
	}

	public MessageRecognizer getReturnCodeRecognizer() {
		return returnCodeRecognizer;
	}

	public void setReturnCodeRecognizer(MessageRecognizer returnCodeRecognizer) {
		this.returnCodeRecognizer = returnCodeRecognizer;
	}
}