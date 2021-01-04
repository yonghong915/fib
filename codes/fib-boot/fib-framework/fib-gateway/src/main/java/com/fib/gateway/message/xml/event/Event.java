package com.fib.gateway.message.xml.event;

import com.fib.gateway.message.xml.channel.Channel;

import lombok.Data;

@Data
public class Event {

	/**
	 * 事件类型：接收连接失败
	 */
	public static final int EVENT_ACCEPT_ERROR = 7980;

	/**
	 * 事件类型：接收请求报文失败
	 */
	public static final int EVENT_REQUEST_RECEIVE_ERROR = 7981;

	/**
	 * 事件类型：接收应答报文失败
	 */
	public static final int EVENT_RESPONSE_RECEIVE_ERROR = 7982;

	/**
	 * 事件类型：发送请求报文失败
	 */
	public static final int EVENT_REQUEST_SEND_ERROR = 7983;

	/**
	 * 事件类型：发送应答报文失败
	 */
	public static final int EVENT_RESPONSE_SEND_ERROR = 7984;

	/**
	 * 事件类型：连接失败
	 */
	public static final int EVENT_CONNECT_ERROR = 7985;

	/**
	 * 事件类型：内部系统MessageBean请求到达
	 */
	public static final int EVENT_MB_REQUEST_ARRIVED = 9980;

	/**
	 * 事件类型：内部系统MessageBean应答到达
	 */
	public static final int EVENT_MB_RESPONSE_ARRIVED = 9981;

	/**
	 * 事件类型：内部系统MessageBean应答已回送
	 */
	public static final int EVENT_MB_RESPONSE_SENT = 9982;

	/**
	 * 事件类型：内部系统MessageBean请求已发送
	 */
	public static final int EVENT_MB_REQUEST_SENT = 9983;

	/**
	 * 事件类型：普通版：请求报文到达；代理版：外系统请求报文到达
	 */
	public static final int EVENT_REQUEST_ARRIVED = 9990;

	/**
	 * 事件类型：普通版：应答报文到达；代理版：外系统应答报文到达
	 */
	public static final int EVENT_RESPONSE_ARRIVED = 9991;

	/**
	 * 事件类型：普通版：应答报文已回送；代理版：外系统应答报文已回送
	 */
	public static final int EVENT_RESPONSE_SENT = 9992;

	/**
	 * 事件类型：普通版：请求报文已发送；代理版：外系统请求报文已发送
	 */
	public static final int EVENT_REQUEST_SENT = 9993;

	/**
	 * 事件类型：异常
	 */
	public static final int EVENT_EXCEPTION = 9999;

	/**
	 * 事件类型：严重异常，引起通道重启
	 */
	public static final int EVENT_FATAL_EXCEPTION = 10000;

	/**
	 * 事件类型
	 */
	private int eventType;

	/**
	 * 事件产生的渠道
	 */
	private Channel sourceChannel;

	/**
	 * 事件源
	 */
	private Object source;

	/**
	 * 事件相关请求消息
	 */
	private byte[] requestMessage;

	/**
	 * 事件相关应答消息
	 */
	private byte[] responseMessage;

	private long createTime;

	public byte[] getRequestMessage() {
		return requestMessage;
	}

	/**
	 * @param requestMessage the requestMessage to set
	 */
	public void setRequestMessage(byte[] requestMessage) {
		this.requestMessage = requestMessage;
	}

	public Channel getSourceChannel() {
		return sourceChannel;
	}

	/**
	 * @param sourceChannel the sourceChannel to set
	 */
	public void setSourceChannel(Channel sourceChannel) {
		this.sourceChannel = sourceChannel;
	}

	public Event(int eventType, Channel sourceChannel, Object source, byte[] requestMessage, byte[] responseMessage) {
		super();
		this.eventType = eventType;
		this.sourceChannel = sourceChannel;
		this.source = source;
		this.requestMessage = requestMessage;
		this.responseMessage = responseMessage;
		this.createTime = System.currentTimeMillis();
	}
}
