package com.fib.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 发布事件，对外提供事件发布机制
 * 
 * @author fangyh
 * @version 1.0
 * @date 2023-03-05 12:17:24
 */
@Component
public final class PublisherCustomsEvent {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public void publisherEvent(ApplicationEvent applicationEvent) {
		applicationEventPublisher.publishEvent(applicationEvent);
	}
}
