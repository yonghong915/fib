package com.fib.ecny.base.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.fib.ecny.base.enums.EcnyErrorCode;
import com.fib.ecny.base.exception.BizException;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Spring Bean本地缓存Holder
 * <p>仅用于【Spring单例Bean】缓存，原型Bean不要使用本类获取！</p>
 */
@Component
public class BeanInstanceCacheHolder {

    public BeanInstanceCacheHolder() {
    }

    @Resource(name = "beanInstanceCache")
    private Cache<Class<?>, Object> beanInstanceCache;

    /**
     * 获取Spring Bean，优先Caffeine本地缓存，缓存缺失从Spring容器加载并缓存
     *
     * @param clazz Bean类型Class
     * @return 单例Bean实例
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        if (Objects.isNull(clazz)) {
            throw new BizException(EcnyErrorCode.UWAP_R_9999, "clazz must not be null");
        }
        return (T) beanInstanceCache.get(clazz, SpringUtil::getBean);
    }

    /**
     * 手动清除指定Class缓存
     */
    public void evict(Class<?> clazz) {
        if (Objects.nonNull(clazz)) {
            beanInstanceCache.invalidate(clazz);
        }
    }

    /**
     * 清空全部Bean缓存，容器关闭时调用
     */
    public void evictAll() {
        beanInstanceCache.invalidateAll();
    }

    /**
     * Spring容器销毁时自动清理缓存，释放Bean引用，防止内存泄漏
     */
    @PreDestroy
    public void preDestroy() {
        evictAll();
    }
}