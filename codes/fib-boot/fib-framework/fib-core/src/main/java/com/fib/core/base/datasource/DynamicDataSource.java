package com.fib.core.base.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源（需要继承AbstractRoutingDataSource）
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	private Logger log = LoggerFactory.getLogger(DynamicDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		String dataSourceType = DataSourceContextHolder.getDataSourceType();
		log.info("Current datasource type is {}.", dataSourceType);
		return dataSourceType;
	}
}
