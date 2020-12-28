package com.fib.core.base.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ClassUtils;

import com.fib.commons.util.ClasspathUtil;
import com.fib.commons.util.security.JasyptUtil;
import com.fib.core.util.ConstantUtil;
import com.zaxxer.hikari.HikariDataSource;

import cn.hutool.core.util.StrUtil;

@Configuration
public class DataSourceConfig {
	private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

	@Autowired
	private com.fib.core.base.datasource.DataSourceProperties dataSourceProperties;

	@Bean(name = "masterDataSource")
	@Qualifier("masterDataSource")
	@Primary
	public DataSource masterDataSource() throws ClassNotFoundException, LinkageError {
		logger.info("default database connection pool is building.......");
		return getDataSource();
	}

	private DataSource getDataSource() throws ClassNotFoundException, LinkageError {
		DataSourceBuilder<?> dsBuilder = DataSourceBuilder.create();
		/* 密码加密解密 */
		String password = dataSourceProperties.getPassword();
		try {
			password = JasyptUtil.decrypt(password, "");
		} catch (Exception e) {
			//
		}
		dsBuilder.password(password);
		dsBuilder.username(dataSourceProperties.getUsername());
		dsBuilder.driverClassName(dataSourceProperties.getDriverClassName());
		dsBuilder.url(dataSourceProperties.getUrl());
		Class<? extends DataSource> ds = getDataSourceType(dataSourceProperties.getType(),
				ClasspathUtil.getClassLoader());
		dsBuilder.type(ds);
		return dsBuilder.build();
	}

	@SuppressWarnings("unchecked")
	private Class<? extends DataSource> getDataSourceType(String name, ClassLoader classLoader)
			throws ClassNotFoundException, LinkageError {
		Class<? extends DataSource> type;
		if (StrUtil.isNotEmpty(name)) {
			type = HikariDataSource.class;
		} else {
			type = (Class<? extends DataSource>) ClassUtils.forName(name, classLoader);
		}
		return type;
	}

	@Bean(name = "dynamicDataSource")
	@Qualifier("dynamicDataSource")
	public DynamicDataSource dynamicDataSource() throws ClassNotFoundException, LinkageError {
		Map<Object, Object> targetDataSources = new HashMap<>();
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		targetDataSources.put(ConstantUtil.DSType.DS_TYPE_SYSDB, getDataSource());
		targetDataSources.put(ConstantUtil.DSType.DS_TYPE_APPDB, getDataSource());

		dynamicDataSource.setTargetDataSources(targetDataSources);
		dynamicDataSource.setDefaultTargetDataSource(getDataSource());
		return dynamicDataSource;
	}
}
