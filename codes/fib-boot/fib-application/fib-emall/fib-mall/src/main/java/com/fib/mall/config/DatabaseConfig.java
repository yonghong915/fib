package com.fib.mall.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;

import io.seata.rm.datasource.DataSourceProxy;

@Configuration
public class DatabaseConfig {
	@Value("${mybatis-plus.mapper-locations}")
	private String mapperLocations;

	@Bean
	// 读取配置文件中的配置。
	@ConfigurationProperties(prefix = "spring.datasource")
	DataSource dataSource() {
		return new DruidDataSource();
	}

	@Bean
	DataSourceProxy dataSourceProxy(DataSource dataSource) {
		return new DataSourceProxy(dataSource);
	}

	@Bean
	SqlSessionFactory sqlSessionFactoryBean(DataSource dataSourceProxy) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSourceProxy);
		sqlSessionFactoryBean
				.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
		sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
		return sqlSessionFactoryBean.getObject();
	}
}