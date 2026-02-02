package com.fib.midbiz.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 中间业务应用
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.fib.midbiz")
@MapperScan(basePackages = { "com.fib.midbiz.efs.mapper", "com.fib.midbiz.bss.mapper" })
public class MidbizApplication {
	public static void main(String[] args) {
		SpringApplication.run(MidbizApplication.class, args);
	}
}