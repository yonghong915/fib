package com.fib.upp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */

@SpringBootApplication
//@EnableAsync
//@EnableCaching
@ComponentScan({ "com.fib.core", "com.fib.upp", "com.fib.commons" })
//@ImportResource("classpath:/spring-drools.xml")
@EnableAutoConfiguration
public class UppApplication {
	public static void main(String[] args) {
		SpringApplication.run(UppApplication.class, args);
	}
}
