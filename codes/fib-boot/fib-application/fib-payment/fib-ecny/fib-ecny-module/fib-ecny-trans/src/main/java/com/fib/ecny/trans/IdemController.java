package com.fib.ecny.trans;

import jakarta.annotation.Resource;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 生成幂等 Token 接口
 */
@RestController
@RequestMapping("/idem")
public class IdemController {
    @Resource
    private RedissonClient redissonClient;
    private static final String IDEM_TOKEN_PREFIX = "idem:token:";

    @GetMapping("/getToken")
    public String getIdemToken() {
        String token = java.util.UUID.randomUUID().toString().replace("-", "");
        String key = IDEM_TOKEN_PREFIX + token;
        // 有效期5分钟
        redissonClient.getBucket(key).set("", Duration.ofMinutes(5));
        return token;
    }
}
