//package com.fib.core;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.redisson.api.RBucket;
//import org.redisson.api.RList;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.redisson.client.codec.StringCodec;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CoreApplication.class)
//public class RedisTest {
//	@Autowired
//	StringRedisTemplate stringRedisTemplate;// 操作k-v都是字符串
//
//	@Autowired
//	RedissonClient redissonClient;// 操作k-v对象的
//
//	@Test
//	public void testBucket() {
//		RBucket<Object> bucket = redissonClient.getBucket("name", StringCodec.INSTANCE);
//		Object o = bucket.get();
//		System.out.println("name=" + o);
//		String key = "";
//		RLock lock = redissonClient.getLock(key);
//		try {
//			// 加锁 操作很类似Java的ReentrantLock机制
//			lock.lock();
//			//ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
//			// 如果库存为空
//			//if (productInfo.getProductStock() == 0) {
//			//	return false;
//			//}
//			// 简单减库存操作 没有重新写其他接口了
//			//productInfo.setProductStock(productInfo.getProductStock() - 1);
//			//productInfoMapper.updateByPrimaryKey(productInfo);
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		} finally {
//			// 解锁
//			lock.unlock();
//		}
//	}
//
//	@Test
//	public void testList() {
//		RList<String> interests = redissonClient.getList("interests");
//		interests.add("篮球");
//		interests.add("爬山");
//		interests.add("编程");
//	}
//}
