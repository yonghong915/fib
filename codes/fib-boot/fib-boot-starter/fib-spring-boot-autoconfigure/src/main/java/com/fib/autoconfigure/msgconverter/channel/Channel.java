package com.fib.autoconfigure.msgconverter.channel;

import java.io.InputStream;

import org.apache.dubbo.common.extension.SPI;

import com.fib.autoconfigure.msgconverter.channel.config.ChannelConfig;
import com.fib.autoconfigure.msgconverter.channel.config.ChannelMainConfig;
import com.fib.autoconfigure.msgconverter.channel.event.EventHandler;
import com.fib.autoconfigure.msgconverter.channel.event.EventQueue;
import com.fib.autoconfigure.msgconverter.channel.message.recognizer.MessageRecognizer;

/**
 * 通讯通道
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-19 16:13:28
 */
@SPI
public interface Channel {

	/**
	 * 加载配置
	 * 
	 * @param configFileName
	 */
	void loadConfig(InputStream is);

	/**
	 * 启动通道
	 */
	void start();

	/**
	 * 停止通道
	 */
	public abstract void shutdown();

	String getId();

	MessageRecognizer getMessageTypeRecognizer();

	ChannelConfig getChannelConfig();

	EventHandler getEventHandler();

	void setMainConfig(ChannelMainConfig channelMainConfig);

	void setChannelConfig(ChannelConfig channelConfig);

	void setEventHandler(EventHandler newInstance);

	void setMessageTypeRecognizer(MessageRecognizer createRecognizer);

	void setReturnCodeRecognizer(MessageRecognizer createRecognizer);

	void setEventQueue(EventQueue eventQueue);
}