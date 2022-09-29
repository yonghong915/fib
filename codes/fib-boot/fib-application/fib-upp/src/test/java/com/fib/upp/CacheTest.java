package com.fib.upp;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Cache;

public class CacheTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheTest.class);
	private String ORDER_CACHE_PRE = "order_";
	static Cache<String, Object> cache = Caffeine.newBuilder().softValues().initialCapacity(128).maximumSize(1024)
			.expireAfterWrite(60, TimeUnit.SECONDS).build();

	public static void main(String[] args) {
		CacheTest test = new CacheTest();
		long orderId = 123345;
		test.getOrderId(orderId);
	}

	public Order getOrderId(long orderId) {
		String key = ORDER_CACHE_PRE + orderId;
		Order order = (Order) cache.get(key, k -> {

			RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
			Object obj = redisTemplate.opsForValue().get(key);
			if (Objects.nonNull(obj)) {
				LOGGER.info("Obatin data from redis.");
				return obj;
			}

			LOGGER.info("Obatin data from database.");
			Order myOrder = getOrderfromDb(orderId);
			redisTemplate.opsForValue().set(k, myOrder, 120, TimeUnit.SECONDS);
			return myOrder;
		});
		return order;
	}

	private Order getOrderfromDb(long orderId) {
		return null;
	}

	class Order {
	}
}
