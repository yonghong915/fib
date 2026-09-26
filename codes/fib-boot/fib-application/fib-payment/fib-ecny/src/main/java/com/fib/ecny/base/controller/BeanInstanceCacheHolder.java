package com.fib.ecny.base.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BeanInstanceCacheHolder {

    public BeanInstanceCacheHolder(){}

    @Resource(name = "beanInstanceCache")
    private Cache<Class<?>, Object> beanInstanceCache;

    /**
     * key:目标Class，value：spring bean实例
     */
    private final Map<Class<?>, Object> beanCache = new ConcurrentHashMap<>();

    /**
     * 获取Bean，优先走缓存，缓存没有则从BeanFactory获取再存入缓存
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        //1.缓存命中直接返回
        return (T) beanInstanceCache.get(clazz, k -> {
            return SpringUtil.getBean(k);
        });
//        if (beanInstanceCache.containsKey(clazz)) {
//            return (T) beanInstanceCache.getIfPresent(clazz);
//        }
//        beanInstanceCache.
//        //2.缓存未命中，从BeanFactory获取
//        T bean = SpringUtil.getBean(clazz);
//        //3.放入缓存
//        beanInstanceCache.put(clazz, bean);
//        return bean;
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        beanCache.clear();
    }

    /**
     * 获取全部缓存
     */
    public Map<Class<?>, Object> getCacheMap() {
        return new ConcurrentHashMap<>(beanCache);
    }
}
