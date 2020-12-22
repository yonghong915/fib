package com.fib.core.config;

import javax.annotation.PostConstruct;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BloomFilterConfig {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedissonClient redisson;

	private RBloomFilter<String> bloomFilter;

	@Value("${dict.expectAmt:10000}")
	private long expectAmt;

	@Value("${dict.falseProbability:0.01}")
	private double falseProbability;

	@PostConstruct
	private void initBloomFilter() {
		logger.info("init bloomFilter...");
		this.bloomFilter = redisson.getBloomFilter("dict~BloomFilter");
		// 初始化布隆过滤器：预计元素为10000L,误差率为3%
		this.bloomFilter.tryInit(expectAmt, falseProbability);
	}

	public boolean isContains(String key) {
		return this.bloomFilter.contains(key);
	}

	public void put(String key) {
		this.bloomFilter.add(key);
	}
}
