package com.fib.core.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 事件发布
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-06-04
 */
@Component
public class EventPublisher {
	ApplicationContext applicationContext;

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void publish(Object event) {
		applicationContext.publishEvent(event);
	}
}
