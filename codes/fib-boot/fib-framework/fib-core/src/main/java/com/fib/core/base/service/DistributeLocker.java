package com.fib.core.base.service;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RBloomFilter;

public interface DistributeLocker {
	/**
	 * 加锁
	 *
	 * @param lockKey key
	 */
	void lock(String lockKey);

	/**
	 * 释放锁
	 *
	 * @param lockKey key
	 */
	void unlock(String lockKey);

	/**
	 * 加锁锁,设置有效期
	 *
	 * @param lockKey key
	 * @param timeout 有效时间，默认时间单位在实现类传入
	 */
	void lock(String lockKey, int timeout);

	/**
	 * 加锁，设置有效期并指定时间单位
	 *
	 * @param lockKey key
	 * @param timeout 有效时间
	 * @param unit    时间单位
	 */
	void lock(String lockKey, int timeout, TimeUnit unit);

	/**
	 * 尝试获取锁，获取到则持有该锁返回true,未获取到立即返回false
	 *
	 * @param lockKey
	 * @return true-获取锁成功 false-获取锁失败
	 */
	boolean tryLock(String lockKey);

	/**
	 * 尝试获取锁，获取到则持有该锁leaseTime时间. 若未获取到，在waitTime时间内一直尝试获取，超过waitTime还未获取到则返回false
	 *
	 * @param lockKey   key
	 * @param waitTime  尝试获取时间
	 * @param leaseTime 锁持有时间
	 * @param unit      时间单位
	 * @return true-获取锁成功 false-获取锁失败
	 */
	boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit);

	/**
	 * 锁是否被任意一个线程锁持有
	 *
	 * @param lockKey
	 * @return true-被锁 false-未被锁
	 */
	boolean isLocked(String lockKey);

	/**
	 * lock.isHeldByCurrentThread()的作用是查询当前线程是否保持此锁定
	 * 
	 * @param lockKey
	 * @return
	 */
	boolean isHeldByCurrentThread(String lockKey);

	/**
	 * 添加元素到布隆过滤器中
	 * 
	 * @param bloomFilterName
	 * @param value
	 */
	void bfAdd(String bloomFilterName, String value);

	/**
	 * 初始化布隆过滤器
	 * 
	 * @param bloomFilterName 默认给了expectedInsertions = 1000000L falseProbability =
	 *                        0.0001
	 * @return 布隆过滤器名称
	 */
	RBloomFilter<String> init(String bloomFilterName);

	/**
	 * 初始化布隆过滤器
	 * 
	 * @param bloomFilterName    名称
	 * @param expectedInsertions 预计插入元素总量
	 * @param falseProbability   精度（误判率）
	 * @return 布隆过滤器名称
	 */
	RBloomFilter<String> init(String bloomFilterName, Long expectedInsertions, Double falseProbability);

	/**
	 * 删除布隆过滤器（注意不是删除元素）
	 * 
	 * @param bloomFilterName 名称
	 */
	void bfDelete(String bloomFilterName);

	/**
	 * 判断
	 * 
	 * @param bloomFilterName 名称
	 * @param value           false表示则一定不存在，true 则可能存在
	 * @return
	 */
	boolean bfContains(String bloomFilterName, String value);
}
