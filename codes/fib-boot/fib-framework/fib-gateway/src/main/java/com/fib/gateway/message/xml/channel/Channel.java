package com.fib.gateway.message.xml.channel;

import com.fib.gateway.channel.config.ChannelConfig;
import com.fib.gateway.message.xml.event.Event;
import com.fib.gateway.message.xml.event.EventQueue;

public abstract class Channel {
	// 通道类型-SOCKET短连接客户端
	public static final String CHANNEL_TYP_SOCKET_CLIENT = "SOCKET_CLIENT";
	// 通道类型-SOCKET短连接服务端
	public static final String CHANNEL_TYP_SOCKET_SERVER = "SOCKET_SERVER";
	// 通道类型-长连接
	public static final String CHANNEL_TYP_LONG_CONN = "LONG_SOCKET";
	// 通道类型-MQ
	public static final String CHANNEL_TYP_MQ = "MQ";
	// 通道类型-HTTP客户端
	public static final String CHANNEL_TYP_HTTP_CLIENT = "HTTP_CLIENT";
	// 通道类型-HTTP服务端
	public static final String CHANNEL_TYP_HTTP_SERVER = "HTTP_SERVER";
	
	protected ChannelConfig channelConfig;
	public ChannelConfig getChannelConfig() {
		return channelConfig;
	}

	/**
	 * @param channelConfig
	 *            the channelConfig to set
	 */
	public void setChannelConfig(ChannelConfig channelConfig) {
		this.channelConfig = channelConfig;
	}

	protected EventQueue eventQueue;

	public EventQueue getEventQueue() {
		return eventQueue;
	}

	/**
	 * @param eventQueue the eventQueue to set
	 */
	public void setEventQueue(EventQueue eventQueue) {
		this.eventQueue = eventQueue;
	}

	protected void onRequestMessageArrived(Object source, byte[] requestMessage) {
		// 产生请求到达事件
		Event e = new Event(Event.EVENT_REQUEST_ARRIVED, this, source, requestMessage, (byte[]) null);
		eventQueue.postEvent(e);
		System.out.println(eventQueue);
	}
}
