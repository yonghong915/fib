package com.fib.pay.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 * 中间业务应用
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.fib.pay")
@MapperScan(basePackages = { "com.fib.pay.hvps.mapper", "com.fib.pay.beps.mapper" })
public class PayApplication {
	public static void main(String[] args) {
		SpringApplication.run(PayApplication.class, args);
	}
}