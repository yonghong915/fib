package com.fib.midbiz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.alicp.jetcache.anno.config.EnableMethodCache;

/**
 * 中间业务应用
 *
 */
@SpringBootApplication
@MapperScan("com.fib.midbiz.**.mapper")
//@EnableMethodCache(basePackages = "com.fib")
public class MidbizApplication {
//	@Bean
//	ExitCodeGenerator exitCodeGenerator() {
//		return () -> 42;
//	}

	public static void main(String[] args) {
		SpringApplication.run(MidbizApplication.class, args);
		// System.exit(SpringApplication.exit(SpringApplication.run(MidbizApplication.class,
		// args)));
//		BeanFactory a;
//		DefaultListableBeanFactory a;
//		BeanPostProcessor a;
//		AutowiredAnnotationBeanPostProcessor a;
//		AbstractApplicationContext a;
	}
}
