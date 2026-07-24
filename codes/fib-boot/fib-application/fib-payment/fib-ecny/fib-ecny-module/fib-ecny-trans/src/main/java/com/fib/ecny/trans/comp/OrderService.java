package com.fib.ecny.trans.comp;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@Slf4j
public class OrderService {
    @Autowired
    private RedissonClient redissonClient;

    public String prepareOrder() {
        String token = IdUtil.getSnowflakeNextIdStr() + RandomUtil.randomString(6);

        //redis.opsForValue().set("order:token" + token, "1", Duration.ofMinutes(15));
        return token;
    }


    public void createOrder() {
        RLock lock = redissonClient.getLock("order_lock");
        try {
            lock.lock(10, TimeUnit.SECONDS);  // 10秒锁超时
            // 业务逻辑处理
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


}