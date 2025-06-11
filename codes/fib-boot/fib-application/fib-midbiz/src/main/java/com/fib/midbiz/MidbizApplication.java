package com.fib.midbiz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 中间业务应用
 *
 */
@SpringBootApplication
@MapperScan("com.fib.midbiz.**.mapper")
public class MidbizApplication {
	public static void main(String[] args) {
		SpringApplication.run(MidbizApplication.class, args);
	}
}
