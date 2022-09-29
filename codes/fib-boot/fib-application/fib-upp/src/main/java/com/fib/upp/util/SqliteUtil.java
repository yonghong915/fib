package com.fib.upp.util;

import java.sql.SQLException;
import java.util.List;

import com.fib.commons.exception.CommonException;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

/**
 * Sqlite公共类
 * 
 * @author fangyh
 *
 */
public final class SqliteUtil {
	private SqliteUtil() {
	}

	static Db db = Db.use("sqlitedb");

	public static boolean existsTable(String tableName) {
		String extSql = "select count(*) cnt from sqlite_master where type='table' AND name = ? ";
		Entity entity = SqliteUtil.queryOne(extSql, tableName);
		int cnt = entity.getInt("cnt");
		return cnt > 0;
	}

	public static int createTable(String sql) {
		try {
			return db.execute(sql);
		} catch (SQLException e) {
			throw new CommonException(e);
		}
	}

	public static int execute(String sql, Object... params) {
		try {
			return db.execute(sql, params);
		} catch (SQLException e) {
			throw new CommonException(e);
		}
	}

	public static int[] exeteBatch(String sql, Iterable<Object[]> paramsBatch) {
		try {
			return db.executeBatch(sql, paramsBatch);
		} catch (SQLException e) {
			throw new CommonException(e);
		}
	}

	public static List<Entity> queryList(String qrySql, Object... params) {
		try {
			return db.query(qrySql, params);
		} catch (SQLException e) {
			throw new CommonException(e);
		}
	}

	public static Entity queryOne(String qrySql, Object... params) {
		try {
			return db.queryOne(qrySql, params);
		} catch (SQLException e) {
			throw new CommonException(e);
		}
	}

	public static int createIndex(String sql) {
		try {
			return db.execute(sql);
		} catch (SQLException e) {
			throw new CommonException(e);
		}
	}

	public static int drop(String tableName) {
		try {
			return db.execute("drop table " + tableName);
		} catch (SQLException e) {
			throw new CommonException(e);
		}
	}
}
