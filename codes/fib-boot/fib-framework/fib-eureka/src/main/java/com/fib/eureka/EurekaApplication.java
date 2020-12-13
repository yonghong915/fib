package com.fib.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Register Center Server
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2020-12-12
 * @since 1.0.0
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(EurekaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EurekaApplication.class, args);
		LOGGER.info("Eureka Register Center Server started.");
	}
}