package com.fib.uias;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-16
 */
@SpringBootApplication
//@EnableCaching
public class UiasApplication {
	public static void main(String[] args) {
		SpringApplication.run(UiasApplication.class, args);
	}
}
