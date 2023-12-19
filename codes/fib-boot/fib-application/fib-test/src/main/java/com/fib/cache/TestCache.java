package com.fib.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class TestCache {

	public static void main(String[] args) {
		Cache<String, String> cache = Caffeine.newBuilder().build();
		cache.put("name", "岳飞");
		String name = cache.getIfPresent("name");
		System.out.println("name = " + name);

		String hero = cache.get("hero", key -> {
			return "秦桧";
		});
		System.out.println("hero = " + hero);
	}
}