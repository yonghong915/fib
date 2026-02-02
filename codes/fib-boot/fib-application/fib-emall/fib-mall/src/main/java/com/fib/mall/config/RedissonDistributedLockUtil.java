package com.fib.mall.config;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redisson 分布式锁工具类
 */
@Component
public class RedissonDistributedLockUtil {

	@Resource
	private RedissonClient redissonClient;

	/**
	 * 获取可重入锁（默认不设置过期时间，Redisson 会自动续期（看门狗机制））
	 * 
	 * @param lockKey 锁的 key
	 * @return RLock 锁对象
	 */
	public RLock getLock(String lockKey) {
		return redissonClient.getLock(lockKey);
	}

	/**
	 * 尝试获取锁（带超时时间）
	 * 
	 * @param lockKey   锁的 key
	 * @param waitTime  最大等待时间（超出该时间则放弃获取）
	 * @param leaseTime 锁的持有时间（过期后自动释放，不设置则启用看门狗）
	 * @param timeUnit  时间单位
	 * @return 是否获取成功
	 * @throws InterruptedException 中断异常
	 */
	public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit)
			throws InterruptedException {
		RLock lock = getLock(lockKey);
		return lock.tryLock(waitTime, leaseTime, timeUnit);
	}

	/**
	 * 释放锁
	 * 
	 * @param lockKey 锁的 key
	 */
	public void releaseLock(String lockKey) {
		RLock lock = getLock(lockKey);
		// 只有当前线程持有该锁时，才释放（避免误删）
		if (lock.isHeldByCurrentThread()) {
			lock.unlock();
		}
	}

	/**
	 * 释放锁（直接传入锁对象，更高效）
	 * 
	 * @param lock RLock 锁对象
	 */
	public void releaseLock(RLock lock) {
		if (lock != null && lock.isHeldByCurrentThread()) {
			lock.unlock();
		}
	}
}
