package com.fib.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.db.Db;

public class TestSyncLock {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestSyncLock.class);

	public static void main(String[] args) {
		TestSyncLock t = new TestSyncLock();
		try (ExecutorService es = new ThreadPoolExecutor(10, 10, 20, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());) {
			for (int i = 0; i < 10; i++) {
				es.submit(t::doBusiness);
			}
			es.shutdown();
		}
	}

	public void insertData() {
		String sql = "insert into test_data(id,name) values (?,?)";
		List<Object[]> paramsBatch = new ArrayList<>();
		for (int i = 0; i < 1; i++) {
			int id = ThreadLocalRandom.current().nextInt(0, 1000);
			Object[] params = { id, id + "name" };
			paramsBatch.add(params);
			if (paramsBatch.size() == 100) {
				try {
					Db.use().executeBatch(sql, paramsBatch);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				paramsBatch = new ArrayList<>();
			}
		}
		try {
			Db.use().executeBatch(sql, paramsBatch);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void doBusiness() {
		LOGGER.info("进入业务处理doBusiness");
		boolean lockFlag = obtainLock();
		if (!lockFlag) {
			LOGGER.info("一直没获取到锁");
		} else {
			LOGGER.info("我获取到锁了");
//			try {
//				TimeUnit.MILLISECONDS.sleep(2000);
//			} catch (InterruptedException e) {
//				Thread.currentThread().interrupt();
//			}
			insertData();

			int row = 0;
			try {
				row = Db.use().execute("delete from resource_lock where resource=?", "aaa");
			} catch (SQLException e) {
				LOGGER.info("删除数据库异常", e);
			}
			LOGGER.info("删除row={}", row);
		}
	}

	public boolean obtainLock() {
		// 处理锁
		int sleepTimes = 0;
		boolean lockFlag = false;
		do {
			try {
				int row = Db.use().execute("insert into resource_lock(resource) values (?)", "aaa");
				LOGGER.info("row={}", row);
				if (row == 1) {
					lockFlag = true;
				}
			} catch (SQLException e) {
				LOGGER.info("插入数据库异常", e);
			}
			if (lockFlag) {
				break;
			}
			try {
				TimeUnit.MILLISECONDS.sleep(2000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			sleepTimes++;
		} while (sleepTimes <= 10);
		return lockFlag;
	}
}
