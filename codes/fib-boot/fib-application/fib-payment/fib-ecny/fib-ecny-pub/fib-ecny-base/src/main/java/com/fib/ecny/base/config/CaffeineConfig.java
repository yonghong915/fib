package com.fib.ecny.base.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfig {
    /**
     * 字典缓存：TTL3分钟，最大5000条
     */
    @Bean("dictLocalCache")
    public Cache<String, String> dictLocalCache() {
        return Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    /**
     * 黑名单缓存：TTL1分钟，最大20000条
     */
    @Bean("blackListLocalCache")
    public Cache<String, Boolean> blackListLocalCache() {
        return Caffeine.newBuilder()
                .maximumSize(20000)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();
    }

    /**
     * 黑名单缓存：TTL1分钟，最大20000条
     */
    @Bean("beanInstanceCache")
    public Cache<Class<?>, Object> beanInstanceCache() {
        return Caffeine.newBuilder()
                .maximumSize(20000)
                .expireAfterWrite(Duration.ofMillis(1000))
                .build();
    }
}
