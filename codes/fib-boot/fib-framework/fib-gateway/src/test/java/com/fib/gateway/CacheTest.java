package com.fib.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewayApplication.class)
public class CacheTest {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CacheManager cacheManager;

	@Test
	public void test() {
		Cache cache = cacheManager.getCache("GoodsType");
		Element element = new Element("key1", "value1");
		cache.put(element);

		Element value = cache.get("key1");
		logger.info("value:{} ", value);
		
		element = new Element("key1", "value2");
		cache.put(element);
		value = cache.get("key1");
		logger.info(value.getObjectValue().toString());

	}

}
