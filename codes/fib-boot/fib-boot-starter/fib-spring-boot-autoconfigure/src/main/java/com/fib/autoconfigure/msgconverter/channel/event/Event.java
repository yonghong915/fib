package com.fib.autoconfigure.msgconverter.channel.event;

import com.fib.autoconfigure.msgconverter.channel.Channel;

public class Event {
	/** 事件类型 */
	private EventType eventType;

	/** 事件产生的渠道 */
	private Channel sourceChannel;

	/** 事件源 */
	private Object source;

	/** 事件相关请求消息 */
	private byte[] requestMessage;

	/** 事件相关应答消息 */
	private byte[] responseMessage;

	/** 事件产生时间 */
	private long createTime;

	/** 可能的异常 */
	private Exception excp;

	public Event(EventType eventType, Channel sourceChannel, Object source, byte[] requestMessage) {
		super();
		this.eventType = eventType;
		this.sourceChannel = sourceChannel;
		this.source = source;
		this.requestMessage = requestMessage;
		this.createTime = System.currentTimeMillis();
	}

	public Event(EventType eventType, Channel sourceChannel, Object source, byte[] requestMessage, Exception excp) {
		super();
		this.eventType = eventType;
		this.sourceChannel = sourceChannel;
		this.source = source;
		this.requestMessage = requestMessage;
		this.excp = excp;
		createTime = System.currentTimeMillis();
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public Channel getSourceChannel() {
		return sourceChannel;
	}

	public void setSourceChannel(Channel sourceChannel) {
		this.sourceChannel = sourceChannel;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public byte[] getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(byte[] requestMessage) {
		this.requestMessage = requestMessage;
	}

	public byte[] getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(byte[] responseMessage) {
		this.responseMessage = responseMessage;
	}

	public Exception getExcp() {
		return excp;
	}

	public void setExcp(Exception excp) {
		this.excp = excp;
	}

	public long getCreateTime() {
		return createTime;
	}
}