package com.fib.upp.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.core.common.entity.Email;
import com.fib.core.event.CustomEvent;
import com.fib.core.event.EventPublisher;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	private EventPublisher eventPublisher;

	@Autowired
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void register(String username) {
		log.info("username={}", username);

		String serviceName = "emailService";
		Email email = new Email();
		email.setAddress("yonghong913@1122.com");
		CustomEvent customEvent = new CustomEvent(email, serviceName);
		eventPublisher.publish(customEvent);

		serviceName = "smsService";
		customEvent = new CustomEvent("smsService execute", serviceName);
		eventPublisher.publish(customEvent);

		serviceName = "userService";
		customEvent = new CustomEvent("mqService execute", serviceName);
		eventPublisher.publish(customEvent);

		log.info("UserService register={} end", username);
	}
}
