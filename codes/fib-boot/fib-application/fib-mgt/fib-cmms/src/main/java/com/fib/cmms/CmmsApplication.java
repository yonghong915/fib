package com.fib.cmms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @date 2023-11-06 09:45:27
 */
@SpringBootApplication
public class CmmsApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(CmmsApplication.class);

	public static void main(String[] args) {
		LOGGER.info("Hello World!");
		SpringApplication.run(CmmsApplication.class, args);
		
	}
}
