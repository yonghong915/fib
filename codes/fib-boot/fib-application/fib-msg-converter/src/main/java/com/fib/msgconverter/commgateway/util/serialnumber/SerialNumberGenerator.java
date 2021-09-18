package com.fib.msgconverter.commgateway.util.serialnumber;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fib.msgconverter.commgateway.dao.serialnumber.dao.SerialNumber;
import com.fib.msgconverter.commgateway.dao.serialnumber.dao.SerialNumberDAO;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 流水号（生成器）
 * 
 * @author 刘恭亮
 * 
 */
public class SerialNumberGenerator {

	private static SerialNumberGenerator instance = null;

	// private Connection conn = null;

	private SerialNumberGenerator() {
		// conn = ConnectionManager.getInstance().getConnection();
	}

	public static synchronized SerialNumberGenerator getInstance() {
		if (null == instance) {
			instance = new SerialNumberGenerator();
		}
		return instance;
	}

	private Map<String,SN> snCache = new HashMap<>();

	/**
	 * 生成下一个序号
	 * 
	 * @param snId
	 *            序号ID
	 * @return
	 */
	public synchronized String next(String snId) {
		String serialNumber = null;
		//String currentSN = null;

		if (null == snId) {
			// throw new IllegalArgumentException("snId is null");
			throw new IllegalArgumentException(MultiLanguageResourceBundle
					.getInstance().getString("inputParameter.null",
							new String[] { "snId" }));
		}

		SN sn = (SN) snCache.get(snId);
		if (null == sn) {
			sn = new SN();
			SerialNumber s = readDB(snId);
			sn.sn = s;
			sn.index = 0;
			sn.end = s.getBatchSize();
			snCache.put(snId, sn);
		} else if (sn.index >= sn.end) {
			// 读数据库
			SerialNumber s = readDB(snId);
			sn.sn = s;
			sn.index = 0;
			sn.end = s.getBatchSize();
		}

		// 获得下一个流水号
		serialNumber = calculateNext(sn.sn);
		sn.index++;

		return serialNumber;
	}

	private String calculateNext(SerialNumber sn) {
		String nextNum = null;
		if (sn.getMaxNum().length() < 18) {
			// 长整型可表示的范围用long处理
			long curNum = Long.parseLong(sn.getCurNum());
			long maxNum = Long.parseLong(sn.getMaxNum());
			curNum += sn.getIncrement();
			if (curNum > maxNum) {
				curNum = 0;
			}
			nextNum = Long.toString(curNum);
		} else {
			// 超过长整型范围
			BigInteger curNum = new BigInteger(sn.getCurNum());
			BigInteger maxNum = new BigInteger(sn.getMaxNum());
			BigInteger increment = new BigInteger(Integer.toString(sn
					.getIncrement()));
			curNum = curNum.add(increment);
			if (1 == curNum.compareTo(maxNum)) {
				curNum = new BigInteger("0");
			}
			nextNum = curNum.toString();
		}
		sn.setCurNum(nextNum);
		return nextNum;
	}

	private class SN {
		public SerialNumber sn;
		public int index;
		public int end;
	}

	// private static final int BATCH_NUM = 20;
	/**
	 * 去数据库取到流水号放到流水号池中
	 * 
	 * @param snId
	 */
	private SerialNumber readDB(String snId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		SerialNumberDAO dao = new SerialNumberDAO(false, conn);

		SerialNumber sn = null;
		try {
			// 以更新方式查询一条记录，实际上加数据库级的锁
			List<SerialNumber> result = dao.getSerialNumberForUpdate(snId);
			if (0 == result.size()) {
				// throw new
				// IllegalArgumentException("no this Serial Number ID["
				// + snId + "]");
				try {
					// 释放锁
					conn.rollback();
				} catch (SQLException e) {
					// e.printStackTrace();
				}
				throw new IllegalArgumentException(MultiLanguageResourceBundle
						.getInstance().getString(
								"SerialNumberGenerator.no.serialNumber",
								new String[] { snId }));
			}

			String nextNum = null;

			// 当前数增加
			// try {
			sn = (SerialNumber) result.get(0);
			if (sn.getMaxNum().length() < 18) {
				// 长整型可表示的范围用long处理
				long curNum = Long.parseLong(sn.getCurNum());
				long maxNum = Long.parseLong(sn.getMaxNum());
				curNum += sn.getBatchSize() * sn.getIncrement();
				if (curNum > maxNum) {
					curNum = sn.getBatchSize();
					sn.setCurNum("0");
				}
				nextNum = Long.toString(curNum);
			} else {
				// 超过长整型范围
				BigInteger curNum = new BigInteger(sn.getCurNum());
				BigInteger maxNum = new BigInteger(sn.getMaxNum());
				BigInteger increment = new BigInteger(Integer.toString(sn
						.getBatchSize()
						* sn.getIncrement()));
				curNum = curNum.add(increment);
				if (1 == curNum.compareTo(maxNum)) {
					curNum = new BigInteger("" + sn.getBatchSize());
					sn.setCurNum("0");
				}
				nextNum = curNum.toString();
			}
			// sn.setCurNum(nextNum);

			// } catch (Exception e) {
			// // 回滚，保证数据库锁释放
			// try {
			// conn.rollback();
			// } catch (SQLException e1) {
			// e1.printStackTrace();
			// }
			// ExceptionUtil.throwActualException(e);
			// } finally {
			// if (null != conn) {
			// try {
			// conn.close();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
			// }

			// 更新当前数

			dao.updateSerialNumber(nextNum, sn.getSnId());

			conn.commit();

		} catch (Exception e) {
			// 回滚，保证数据库锁释放
			try {
				if (null != conn) {
					conn.rollback();
				}

			} catch (SQLException e1) {
				// e1.printStackTrace();
			}
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

		return sn;
	}
}
