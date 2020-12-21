package com.fib.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class RedisTest {
	@Autowired
	StringRedisTemplate stringRedisTemplate;// 操作k-v都是字符串

	@Autowired
	RedisTemplate redisTemplate;// 操作k-v对象的

	@Test
	public void test01() {
//        stringRedisTemplate.opsForValue().append("key","helloword");
//        String msg = stringRedisTemplate.opsForValue().get("key");
//        System.out.println("msg:"+msg);
		//redisTemplate.opsfor
	}
}
