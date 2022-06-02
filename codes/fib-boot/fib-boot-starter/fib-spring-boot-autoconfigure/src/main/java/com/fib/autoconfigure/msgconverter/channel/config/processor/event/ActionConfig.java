package com.fib.autoconfigure.msgconverter.channel.config.processor.event;

public class ActionConfig {
	private int type;
	private String clazz;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
}
