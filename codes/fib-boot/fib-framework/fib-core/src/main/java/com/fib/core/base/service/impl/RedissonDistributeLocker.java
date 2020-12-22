package com.fib.core.base.service.impl;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import com.fib.core.base.service.DistributeLocker;

public class RedissonDistributeLocker implements DistributeLocker {

	private final RedissonClient redissonClient;

	public RedissonDistributeLocker(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public void lock(String lockKey) {
		RLock lock = redissonClient.getLock(lockKey);
		lock.lock();
	}

	@Override
	public void unlock(String lockKey) {
		RLock lock = redissonClient.getLock(lockKey);
		lock.unlock();
	}

	@Override
	public void lock(String lockKey, int leaseTime) {
		RLock lock = redissonClient.getLock(lockKey);
		lock.lock(leaseTime, TimeUnit.MILLISECONDS);
	}

	@Override
	public void lock(String lockKey, int timeout, TimeUnit unit) {
		RLock lock = redissonClient.getLock(lockKey);
		lock.lock(timeout, unit);
	}

	@Override
	public boolean tryLock(String lockKey) {
		RLock lock = redissonClient.getLock(lockKey);
		return lock.tryLock();
	}

	@Override
	public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
		RLock lock = redissonClient.getLock(lockKey);
		try {
			return lock.tryLock(waitTime, leaseTime, unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isLocked(String lockKey) {
		RLock lock = redissonClient.getLock(lockKey);
		return lock.isLocked();
	}

	@Override
	public boolean isHeldByCurrentThread(String lockKey) {
		RLock lock = redissonClient.getLock(lockKey);
		return lock.isHeldByCurrentThread();
	}

	@Override
	public void bfAdd(String bloomFilterName, String value) {
		RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);
		bloomFilter.add(value);
	}

	@Override
	public RBloomFilter<String> init(String bloomFilterName) {
		long expectedInsertions = 1000000L;
		double falseProbability = 0.0001;
		RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);
		bloomFilter.tryInit(expectedInsertions, falseProbability);
		return bloomFilter;
	}

	@Override
	public RBloomFilter<String> init(String bloomFilterName, Long expectedInsertions, Double falseProbability) {
		RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);
		bloomFilter.tryInit(expectedInsertions, falseProbability);
		return bloomFilter;
	}

	@Override
	public void bfDelete(String bloomFilterName) {
		RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);
		bloomFilter.delete();
	}

	@Override
	public boolean bfContains(String bloomFilterName, String value) {
		RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);
		return bloomFilter.contains(value);
	}

}
