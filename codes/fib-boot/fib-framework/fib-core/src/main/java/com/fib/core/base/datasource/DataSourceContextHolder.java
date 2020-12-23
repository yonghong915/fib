package com.fib.core.base.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 构建一个DatabaseType容器，并提供了向其中设置和获取DatabaseType的方法
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-23
 */
public class DataSourceContextHolder {
	private DataSourceContextHolder() {
	}

	private static Logger logger = LoggerFactory.getLogger(DataSourceContextHolder.class);
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

	public static void setDataSourceType(String dsType) {
		if (logger.isInfoEnabled()) {
			logger.info("switch datasource to {}", dsType);
		}
		contextHolder.set(dsType);
	}

	public static String getDataSourceType() {
		return contextHolder.get();
	}

	public static void clearDataSource() {
		contextHolder.remove();
	}
}
