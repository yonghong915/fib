package com.fib.uqcp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@Configuration
@MapperScan("com.fib.uqcp.mapper")
public class MybatisPlusConfig {
//	@Bean
//	public PaginationInterceptor paginationInterceptor() {
//		return new PaginationInterceptor();
//	}
}
