package com.fib.cmms.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@MapperScan("com.fib.cmms.mapper")
public class MybatisPlusConfig {
//	@Bean
//	public PaginationInterceptor paginationInterceptor() {
//		return new PaginationInterceptor();
//	}
}
