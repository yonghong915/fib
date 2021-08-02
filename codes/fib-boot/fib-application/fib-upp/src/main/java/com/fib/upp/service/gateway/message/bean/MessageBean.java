package com.fib.upp.service.gateway.message.bean;

import com.fib.upp.service.gateway.message.metadata.MessageMetaData;

import lombok.Getter;
import lombok.Setter;

public abstract class MessageBean {
	public static final String NEW_LINE = System.getProperty("line.separator");

	public abstract void validate();

	@Getter
	@Setter
	private MessageMetaData metadata;

}
