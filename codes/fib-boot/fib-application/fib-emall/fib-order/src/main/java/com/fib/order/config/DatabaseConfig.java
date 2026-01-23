package com.fib.order.config;

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
//	@Primary
//	@Bean
//	public DataSourceProxy dataSource() {
//		DruidDataSource druidDataSource = new DruidDataSource();
//		druidDataSource.setDriverClassName(driver);
//		druidDataSource.setUrl(url);
//		druidDataSource.setUsername(username);
//		druidDataSource.setPassword(password);
//		DataSourceProxy dsp = new DataSourceProxy(druidDataSource);
//		return dsp;
//	}

//	@Bean
//	public SqlSessionFactory sqlSessionFactory(DataSourceProxy dataSourceProxy) throws Exception {
//		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//		factoryBean.setDataSource(dataSourceProxy); // 必须传入代理数据源
//		return factoryBean.getObject();
//	}

	@Value("${mybatis-plus.mapper-locations}")
	private String mapperLocations;

	@Bean
	// 读取配置文件中的配置。
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return new DruidDataSource();
	}

	@Bean
	public DataSourceProxy dataSourceProxy(DataSource dataSource) {
		return new DataSourceProxy(dataSource);
	}

	@Bean
	public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSourceProxy) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSourceProxy);
		sqlSessionFactoryBean
				.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
		sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
		return sqlSessionFactoryBean.getObject();
	}
}
