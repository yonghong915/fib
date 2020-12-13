package com.fib.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config Center
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2020-12-12
 * @since 1.0.0
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
		LOGGER.info("Config Center Service started.");
	}
}