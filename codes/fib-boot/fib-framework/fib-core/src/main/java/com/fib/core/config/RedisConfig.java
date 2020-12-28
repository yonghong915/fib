package com.fib.core.config;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.fib.core.base.service.impl.RedissonDistributeLocker;
import com.fib.core.util.RedissonUtils;

@Configuration
@EnableCaching
public class RedisConfig {
	@Bean
	public RedissonDistributeLocker redissonLocker(RedissonClient redissonClient) {
		RedissonDistributeLocker locker = new RedissonDistributeLocker(redissonClient);
		RedissonUtils.setLocker(locker);
		return locker;
	}

	@Bean
	@ConditionalOnMissingBean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}
}
