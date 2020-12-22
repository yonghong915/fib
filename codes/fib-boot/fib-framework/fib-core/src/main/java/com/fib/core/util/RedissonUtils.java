package com.fib.core.util;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RBloomFilter;

import com.fib.core.base.service.DistributeLocker;

public class RedissonUtils {
	private static DistributeLocker locker;

	public static void setLocker(DistributeLocker locker) {
		RedissonUtils.locker = locker;
	}

	public static void lock(String lockKey) {
		locker.lock(lockKey);
	}

	public static void unlock(String lockKey) {
		locker.unlock(lockKey);
	}

	public static void lock(String lockKey, int timeout) {
		locker.lock(lockKey, timeout);
	}

	public static void lock(String lockKey, int timeout, TimeUnit unit) {
		locker.lock(lockKey, timeout, unit);
	}

	public static boolean tryLock(String lockKey) {
		return locker.tryLock(lockKey);
	}

	public static boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
		return locker.tryLock(lockKey, waitTime, leaseTime, unit);
	}

	public static boolean isLocked(String lockKey) {
		return locker.isLocked(lockKey);
	}

	public static boolean isHeldByCurrentThread(String lockKey) {
		return locker.isHeldByCurrentThread(lockKey);
	}

	public static void bfAdd(String bloomFilterName, String value) {
		locker.bfAdd(bloomFilterName, value);
	}

	public static void bfDelete(String bloomFilterName) {
		locker.bfDelete(bloomFilterName);
	}

	public static RBloomFilter<String> init(String bloomFilterName) {
		return locker.init(bloomFilterName);
	}

	public static RBloomFilter<String> init(String bloomFilterName, Long expectedInsertions, Double falseProbability) {
		return locker.init(bloomFilterName, expectedInsertions, falseProbability);
	}

	public static boolean bfContains(String bloomFilterName, String value) {
		return locker.bfContains(bloomFilterName, value);
	}
}
