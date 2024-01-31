package com.fib.uqcp.config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fib.uqcp.MybatisInterceptor;

@Configuration
public class MyBatisConfig {
//	@Autowired
//	private MybatisInterceptor myBatisIntercept;
//
//	@Autowired
//	private DataSource dataSource;
//
//	@Bean
//	public SqlSessionFactoryBean sqlSessionFactoryBean() {
//		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//		// 将自定义拦截器装入mybatis
//		bean.setDataSource(dataSource);
//		bean.setPlugins(myBatisIntercept);
//		return bean;
//	}
}
