package com.fib.uqcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fib.autoconfigure.crypto.annotation.EnableSecurity;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @date 2023-11-06 09:45:27
 */
@SpringBootApplication
@EnableSecurity
public class UqcpApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(UqcpApplication.class);

	public static void main(String[] args) {
		LOGGER.info("Hello World!");
		SpringApplication.run(UqcpApplication.class, args);
	}
}
