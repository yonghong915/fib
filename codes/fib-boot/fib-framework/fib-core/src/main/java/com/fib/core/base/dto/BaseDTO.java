package com.fib.core.base.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public abstract class BaseDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6060535681706403250L;
	protected String channelSerialNo;
	protected Date channelDate;
	protected String channel;
}