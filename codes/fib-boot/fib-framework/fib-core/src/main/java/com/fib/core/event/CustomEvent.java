package com.fib.core.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * 通用事件
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-06-04
 */
public class CustomEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6296105952100903704L;

	@Getter
	private String serviceName;

	public CustomEvent(Object source, String serviceName) {
		super(source);
		this.serviceName = serviceName;
	}
}