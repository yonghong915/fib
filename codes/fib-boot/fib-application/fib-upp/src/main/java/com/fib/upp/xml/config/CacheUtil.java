package com.fib.upp.xml.config;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class CacheUtil {
	private CacheUtil() {
		//
	}

	public static Cache<String, Object> modualCache = cacheManager();

	private static Cache<String, Object> cacheManager() {
		// Caffeine配置
		Cache<String, Object> caffeine = Caffeine.newBuilder().initialCapacity(100).maximumSize(1000)
				// 最后一次写入后经过固定时间过期
				.expireAfterWrite(10, TimeUnit.DAYS).build();
		return caffeine;
	}
}
