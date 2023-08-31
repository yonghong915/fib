package com.fib.event;

import org.springframework.context.ApplicationEvent;

public class BusinessTrackEvent extends ApplicationEvent {
	public BusinessTrackEvent(Object businessTrackEventEntity) {
		super(businessTrackEventEntity);
	}
}
