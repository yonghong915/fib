package com.fib.upp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.commons.security.SecurityEncryptor;
import com.fib.commons.security.support.sm.SM2Encryptor;
import com.fib.upp.event.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSpringEvent {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	UserService userService;

	@Test
	public void testSpringEvent() {
		logger.info("userservieTest....");
		userService.register("hello");
		
		System.out.println(SecurityEncryptor.class.isAssignableFrom(SM2Encryptor.class));
	}
}
