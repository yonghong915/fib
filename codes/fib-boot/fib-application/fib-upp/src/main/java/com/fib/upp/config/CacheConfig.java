package com.fib.upp.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching // 开启缓存
public class CacheConfig {
	public enum CacheEnum {
	}
}
