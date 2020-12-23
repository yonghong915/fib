package com.fib.core.base.datasource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.ClassUtils;

import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.fib.commons.util.ClasspathUtil;
import com.fib.commons.util.security.JasyptUtil;
import com.fib.core.util.ConstantUtil;

@Configuration
@RefreshScope
public class DataSourceConfig {
	private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

	@Autowired
	private com.fib.core.base.datasource.DataSourceProperties dataSourceProperties;

	@Bean(name = "masterDataSource")
	@Qualifier("masterDataSource")
	@Primary
	public DataSource masterDataSource() throws ClassNotFoundException, LinkageError {
		logger.info("默认数据库连接池创建中.......");
		// DataSourceProperties dsProps = dataSourceProperties4Master();
		return getDataSource();
	}

	@SuppressWarnings("unchecked")
	private Class<? extends DataSource> findType(String name, ClassLoader classLoader)
			throws ClassNotFoundException, LinkageError {
		return (Class<? extends DataSource>) ClassUtils.forName(name, classLoader);
	}

	private DataSource getDataSource() throws ClassNotFoundException, LinkageError {
		DataSourceBuilder<?> dsBuilder = DataSourceBuilder.create();
		/* 密码加密解密 */
		String password = dataSourceProperties.getPassword();
		try {
			password = JasyptUtil.decrypt(password, "");
		} catch (Exception e) {
		}
		dsBuilder.password(password);
		dsBuilder.username(dataSourceProperties.getUsername());
		dsBuilder.driverClassName(dataSourceProperties.getDriverClassName());
		dsBuilder.url(dataSourceProperties.getUrl());
		Class<? extends DataSource> ds = findType(dataSourceProperties.getType(), ClasspathUtil.getClassLoader());
		dsBuilder.type(ds);
		return dsBuilder.build();
	}

	@Bean(name = "dynamicDataSource")
	@Qualifier("dynamicDataSource")
	public DynamicDataSource dynamicDataSource() throws SQLException, ClassNotFoundException, LinkageError {
		Map<Object, Object> targetDataSources = new HashMap<>();
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		targetDataSources.put(ConstantUtil.DSType.DS_TYPE_SYSDB, getDataSource());
		//targetDataSources.put(ConstantUtil.DSType.DS_TYPE_APPDB, slaveDataSource);

		dynamicDataSource.setTargetDataSources(targetDataSources);
		dynamicDataSource.setDefaultTargetDataSource(getDataSource());
		return dynamicDataSource;
	}
}
