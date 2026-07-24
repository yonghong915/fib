package com.fib.ecny.trans.comp;

import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBucket;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Duration;
import java.util.Collections;

@Aspect
@Component
public class IdempotentAspect {
    private static final String IDEM_TOKEN_PREFIX = "idem:token:";
    private static final String IDEM_BIZ_PREFIX = "idem:biz:";

    private final ExpressionParser parser = new SpelExpressionParser();
// ===================== Lua脚本定义 =====================
    /**
     * Token模式Lua：存在则删除返回1，不存在返回0（原子）
     * 返回 1 = 校验通过；0 = 重复提交
     */
    private static final String LUA_TOKEN_SCRIPT =
            """
                    if redis.call('EXISTS', KEYS[1]) == 1 then
                        redis.call('DEL', KEYS[1])
                        return 1
                    else
                        return 0
                    end
                    """;

    /**
     * 业务KEY模式Lua：SETNX设置过期时间
     * 返回1=首次请求；0=重复请求
     */
    private static final String LUA_BIZ_SCRIPT =
            """
            local exist = redis.call('EXISTS', KEYS[1])
            if exist == 1 then
                return 0
            end
            redis.call('SET', KEYS[1], 'processed', 'EX', ARGV[1])
            return 1
            """;
    @Resource
    private RedissonClient redissonClient;

    @Around("@annotation(idempotent)")
    public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        // 1.解析SpEL拿到key值
        String rawKey = parseSpelKey(joinPoint, idempotent.key());
        if (rawKey == null || rawKey.isBlank()) {
            throw new RuntimeException("幂等key不能为空");
        }

        Idempotent.IdemType type = idempotent.type();
        String redisKey;
        Long result;
        if (type == Idempotent.IdemType.TOKEN) {
            redisKey = IDEM_TOKEN_PREFIX + rawKey;
            // 执行Lua脚本
            result = redissonClient.getScript().eval(
                    RScript.Mode.READ_WRITE,
                    LUA_TOKEN_SCRIPT,
                    RScript.ReturnType.LONG,
                    Collections.singletonList(redisKey)
            );
            // 0=token不存在，重复提交
            if (result != 1) {
                throw new IdempotentException(idempotent.msg());
            }
            return joinPoint.proceed();

        } else {
            redisKey = IDEM_BIZ_PREFIX + rawKey;
            long expireSec = idempotent.expire();
            result = redissonClient.getScript().eval(
                    RScript.Mode.READ_WRITE,
                    LUA_BIZ_SCRIPT,
                    RScript.ReturnType.LONG,
                    Collections.singletonList(redisKey),
                    expireSec
            );
            if (result != 1) {
                throw new IdempotentException(idempotent.msg());
            }
            try {
                return joinPoint.proceed();
            } catch (Exception e) {
                // =========【重要开关】=========
                // 场景1：业务异常希望允许重试 → 打开下面删除
                // redissonClient.getBucket(redisKey).delete();
                // 场景2：无论成功失败都禁止重复执行 → 注释删除
                throw e;
            }
        }
    }

    /**
     * SpEL表达式解析
     */
    private String parseSpelKey(ProceedingJoinPoint joinPoint, String spelStr) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameters.length; i++) {
            context.setVariable(parameters[i].getName(), args[i]);
        }
        Expression expression = parser.parseExpression(spelStr);
        return expression.getValue(context, String.class);
    }
}