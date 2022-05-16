package com.fib.autoconfigure.disruptor.event;

import java.util.EventObject;

public abstract class DisruptorEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** System time when the event happened */
	private final long timestamp;
	/** Event Name */
	private String event;
	/** Event Tag */
	private String tag;
	/** Event Keys */
	private String key;
	/** Event body */
	private transient Object body;

	protected DisruptorEvent(Object source) {
		super(source);
		this.timestamp = System.currentTimeMillis();
	}

	public final long getTimestamp() {
		return this.timestamp;
	}

	public String getRouteExpression() {
		return new StringBuilder("/").append(getEvent()).append("/").append(getTag()).append("/").append(getKey()).toString();

	}

	public void setSource(Object source) {
		this.source = source;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return new StringBuilder("DisruptorEvent [event :").append(getEvent()).append(",tag :").append(getTag()).append(", key :").append(getKey())
				.append("]").toString();
	}
}