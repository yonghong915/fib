package com.fib.uqcp;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })

public class MybatisInterceptor implements Interceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		LOGGER.info("MybatisInterceptor-----intercept----");

		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
				SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		// id为执行的mapper方法的全路径名，如com.uv.dao.UserDao.selectPageVo
		String id = mappedStatement.getId();
		// sql语句类型 select、delete、insert、update
		SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
		BoundSql boundSql = statementHandler.getBoundSql();
		// 获取到原始sql语句
		String sql = boundSql.getSql();

		String mSql = sql;
		if (SqlCommandType.INSERT == sqlCommandType) {
			mSql = sql + ",";
		} else if (SqlCommandType.UPDATE == sqlCommandType) {

		}

		Field field = boundSql.getClass().getDeclaredField("sql");
		field.setAccessible(true);
		field.set(boundSql, mSql);

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
	}
}
