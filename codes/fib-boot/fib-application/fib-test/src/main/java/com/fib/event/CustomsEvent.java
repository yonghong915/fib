package com.fib.event;

import org.springframework.context.ApplicationEvent;

public class CustomsEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomsEvent(CustomEntity source) {
		super(source);
	}

}
